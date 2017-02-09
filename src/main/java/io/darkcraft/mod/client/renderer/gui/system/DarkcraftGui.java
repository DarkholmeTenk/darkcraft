package io.darkcraft.mod.client.renderer.gui.system;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

import io.darkcraft.darkcore.mod.datastore.GuiTexture;
import io.darkcraft.darkcore.mod.datastore.WindowSpaceStore;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IDraggable;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IGuiContainer;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.ITypable;

public class DarkcraftGui extends GuiContainer implements IGuiContainer
{
	private final GuiTexture			background;
	private DarkcraftGui				parentGui;
	private DarkcraftGui				subGui;
	private ITypable					activeTyper;
	private List<AbstractGuiElement>	elements		= new ArrayList();

	public int							guiX;
	public int							guiW;
	public int							guiY;
	public int							guiH;
	float								pticks;
	public boolean						inventoryGui	= false;

	public DarkcraftGui(Container cont, GuiTexture _background)
	{
		this(cont, _background, _background.w, _background.h);
	}

	public DarkcraftGui(Container cont, GuiTexture _background, int w, int h)
	{
		super(cont == null ? DummyContainer.i : cont);
		background = _background;
		guiW = w;
		guiH = h;
		xSize = guiW;
		ySize = guiH;

		inventoryGui = ((cont != null) && !(cont instanceof DummyContainer));
	}

	private int oldW;
	private int oldH;
	private float guiScale = 1;
	private void rescale()
	{
		if((oldW == width) && (oldH == height)) return;
		//ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft(), width, height);
		oldW = width;
		oldH = height;
		if((guiW < (width - 16)) && (guiH < (height - 16)))
			guiScale = 1;
		else
		{
			float s = Math.min(
					(float) (width-16) / guiW,
					(float) (height-16) / guiH);
			s = (float) (Math.floor(s * 32) / 32);
			guiScale = s;
		}
	}

	private int transToScale(int mouseIn)
	{
		return MathHelper.round(mouseIn / guiScale);
	}

	@Override
	public void addElement(AbstractGuiElement element)
	{
		elements.add(element);
		element.parent = this;
	}

	@Override
	public void removeElement(AbstractGuiElement e)
	{
		elements.remove(e);
		e.parent = null;
	}

	@Override
	public void drawDefaultBackground()
	{
		if (parentGui == null)
		{
			if (inventoryGui)
				super.drawDefaultBackground();
			else
				drawGradientRect(0, 0, width, height, -1072689136, -804253680);
		}
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y)
	{
		if (subGui != null)
		{
			GL11.glPushMatrix();
			subGui.width = width;
			subGui.height = height;
			subGui.drawScreen(x, y, pticks);
			GL11.glPopMatrix();
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float pTicks)
	{
		rescale();
		if (inventoryGui)
			super.drawScreen(mouseX, mouseY, pTicks);
		else
		{
			drawDefaultBackground();
			GL11.glPushMatrix();
			drawGuiContainerBackgroundLayer(pTicks, mouseX, mouseY);
			drawGuiContainerForegroundLayer(mouseX, mouseY);
			GL11.glPopMatrix();
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float pTicks, int mouseX, int mouseY)
	{
		handleHover(mouseX, mouseY);
		GL11.glPushMatrix();
		GL11.glScalef(guiScale, guiScale, 1);
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glEnable(GL11.GL_BLEND);
		RenderHelper.resetColour();
		pticks = pTicks;
		guiX = (int) (((width / guiScale) - guiW) / 2);
		guiY = (int) (((height/ guiScale) - guiH) / 2);
		background.render(guiX, guiY, zLevel, true);
		GL11.glPushMatrix();
		GL11.glTranslated(guiX, guiY, zLevel);
		int mx = mouseX - guiX;
		int my = mouseY - guiY;
		for (AbstractGuiElement e : elements)
		{
			if(!e.visible) continue;
			GL11.glPushMatrix();
			GL11.glTranslatef(e.x, e.y, 0);
			e.render(pTicks, mx - e.x, my - e.y);
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
		GL11.glPopAttrib();
		GL11.glPopMatrix();
	}

	private void setTyper(ITypable t)
	{
		if (activeTyper != null) activeTyper.setInFocus(false);
		activeTyper = t;
		if (t != null) t.setInFocus(true);
	}

	@Override
	public AbstractGuiElement getHovered(int mouseX, int mouseY)
	{
		if (subGui != null)
			return subGui.getHovered(mouseX, mouseY);
		int x = transToScale(mouseX) - guiX;
		int y = transToScale(mouseY) - guiY;
		for (AbstractGuiElement e : elements)
		{
			if(!e.visible) continue;
			int cx = x - e.x;
			int cy = y - e.y;
			if (e.withinBounds(cx, cy))
			{
				if(e instanceof IGuiContainer)
					return ((IGuiContainer) e).getHovered(cx, cy);
				return e;
			}
		}
		return null;
	}

	@Override
	protected void mouseClicked(int x, int y, int b)
	{
		if (subGui != null)
		{
			subGui.mouseClicked(x, y, b);
			return;
		}
		x = transToScale(x) - guiX;
		y = transToScale(y) - guiY;
		for (AbstractGuiElement e : elements)
		{
			if (!((e instanceof ITypable) || (e instanceof IClickable))) continue;
			if (!(e.enabled && e.visible)) continue;
			int cx = x - e.x;
			int cy = y - e.y;
			if (!e.withinBounds(cx, cy)) continue;
			if (e instanceof IClickable) if (((IClickable) e).click(b, cx, cy)) return;
			if (e instanceof ITypable)
			{
				setTyper((ITypable) e);
				return;
			}
		}
		setTyper(null);
		if(inventoryGui)
			super.mouseClicked(x + guiX, y + guiY, b);
	}

	@Override
	protected void mouseClickMove(int x, int y, int b, long t)
	{
		if (subGui != null)
		{
			subGui.mouseClickMove(x, y, b, t);
			return;
		}
		x = transToScale(x) - guiX;
		y = transToScale(y) - guiY;
		for (AbstractGuiElement e : elements)
		{
			if (!(e instanceof IDraggable)) continue;
			if (!(e.enabled && e.visible)) continue;
			int cx = x - e.x;
			int cy = y - e.y;
			if (!e.withinBounds(cx, cy)) continue;
			if (((IDraggable) e).drag(b, cx, cy)) return;
		}
		if(inventoryGui)
			super.mouseClickMove(x + guiX, y + guiY, b, t);
	}

	private int lastMouseX;
	private int lastMouseY;
	private void handleHover(int mouseX, int mouseY)
	{
		if((mouseX == lastMouseX) && (mouseY == lastMouseY)) return;
		if(subGui != null) return;
		lastMouseX = mouseX; lastMouseY = mouseY;
		AbstractGuiElement hover = getHovered(mouseX,mouseY);
		if(hover != lastHovered)
		{
			hoverChanged(hover);
			lastHovered = hover;
		}
	}

	protected AbstractGuiElement lastHovered = null;
	@Override
	protected void mouseMovedOrUp(int x, int y, int w)
    {
		if(inventoryGui)
			super.mouseMovedOrUp(x, y, w);
		if(subGui != null)
			subGui.mouseMovedOrUp(x, y, w);
		else
			handleHover(x,y);
    }

	public void hoverChanged(AbstractGuiElement newHover){}

	@Override
	public void clickableClicked(IClickable c, String id, int button)
	{
		if ("cross".equals(id)) close();
	}

	@Override
	public void typableChanged(ITypable t)
	{
	}

	public void openSubGui(DarkcraftGui gui)
	{
		gui.parentGui = this;
		subGui = gui;
	}

	public void close()
	{
		if (parentGui == null)
		{
			EntityClientPlayerMP pl = mc.thePlayer;
			if(inventoryGui)
				pl.closeScreen();
			else
				pl.closeScreenNoPacket();
		}
		else
		{
			parentGui.subGui = null;
			parentGui = null;
		}
	}

	@Override
	public void recalc(){}

	@Override
	protected void keyTyped(char c, int i)
	{
		if (subGui != null)
		{
			subGui.keyTyped(c, i);
			return;
		}
		if (i == 1)
		{
			if (activeTyper != null)
				setTyper(null);
			else
				close();
			return;
		}
		if (activeTyper != null)
			activeTyper.keyTyped(c, i);
		else if(inventoryGui)
			super.keyTyped(c, i);
	}

	@Override
	public WindowSpaceStore getWindowSpace(AbstractGuiElement e)
	{
		Minecraft mc = Minecraft.getMinecraft();
		ScaledResolution sr = new ScaledResolution(mc,mc.displayWidth,mc.displayHeight);
		WindowSpaceStore wss = new WindowSpaceStore(0,0,mc.displayHeight / sr.getScaledHeight_double());
		wss = wss.scale(guiScale);
		wss = wss.transform(guiX, guiY);
		wss = wss.transform(e.x, e.y);
		return wss;
	}

	public static class DummyContainer extends Container
	{
		public static final DummyContainer i = new DummyContainer();

		@Override
		public void putStackInSlot(int p_75141_1_, ItemStack p_75141_2_){}

		@Override
		public boolean canInteractWith(EntityPlayer p_75145_1_)
		{
			return false;
		}
	}
}
