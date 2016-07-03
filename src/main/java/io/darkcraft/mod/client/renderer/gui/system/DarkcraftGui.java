package io.darkcraft.mod.client.renderer.gui.system;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import io.darkcraft.darkcore.mod.datastore.GuiTexture;
import io.darkcraft.darkcore.mod.datastore.WindowSpaceStore;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IDraggable;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IGuiContainer;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.ITypable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;

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
		super(cont);
		background = _background;
		guiW = background.w;
		guiH = background.h;
		xSize = guiW;
		ySize = guiH;
	}

	private int oldW;
	private int oldH;
	private float guiScale = 1;
	private void rescale()
	{
		//if(parentGui != null)
		//{
		//	guiScale = 1;
		//	return;
		//}
		if((oldW == width) && (oldH == height)) return;
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
		GL11.glPushMatrix();
		GL11.glScalef(guiScale, guiScale, 1);
		GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
		GL11.glEnable(GL11.GL_BLEND);
		pticks = pTicks;
		guiX = (int) (((width / guiScale) - background.w) / 2);
		guiY = (int) (((height/ guiScale) - background.h) / 2);
		background.render(guiX, guiY, zLevel, true);
		GL11.glPushMatrix();
		GL11.glTranslated(guiX, guiY, zLevel);
		int mx = mouseX - guiX;
		int my = mouseY - guiY;
		for (AbstractGuiElement e : elements)
		{
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

	@Override
	public void clickableClicked(IClickable c, String id)
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
			mc.thePlayer.closeScreen();
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
		WindowSpaceStore wss = new WindowSpaceStore(0,0,sr.getScaleFactor());
		wss = wss.transform(guiX, guiY);
		wss = wss.scale(guiScale);
		wss = wss.transform(e.x, e.y);
		return wss;
	}

}
