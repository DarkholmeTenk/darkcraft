package io.darkcraft.mod.client.renderer.gui.system;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import net.minecraft.util.ResourceLocation;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.datastore.WindowSpaceStore;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IDraggable;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IGuiContainer;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.ITypable;

public class DarkcraftGuiTabbed<U extends AbstractGuiElement, T extends AbstractGuiElement & IGuiContainer<U>>
			extends AbstractGuiElement implements IDraggable, IGuiContainer<U>, IClickable
{
	private Tab current;

	private Map<Tab, T> map = new LinkedHashMap<>();

	public DarkcraftGuiTabbed(int _x, int _y, int width, int height)
	{
		super(_x, _y, width, height);
	}

	@Override
	public boolean click(int button, int x, int y)
	{
		AbstractGuiElement clicked = getElement(x, y);
		if(y > 24)
			y -= 24;
		if(clicked instanceof Tab)
			current = (Tab) clicked;
		else if(clicked instanceof IClickable)
			return ((IClickable)clicked).click(button, x, y);
		return clicked != null;
	}

	@Override
	public boolean drag(int button, int x, int y)
	{
		AbstractGuiElement clicked = getElement(x, y);
		if(y > 24)
			y -= 24;
		if(clicked instanceof IDraggable)
			return ((IDraggable)clicked).drag(button, x, y);
		return false;
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		int x = 0;
		for(Tab t : map.keySet())
		{
			GL11.glPushMatrix();
			GL11.glTranslated(x, 0, 0);
			t.render(pticks, mouseX, mouseY);
			x+= 24;
			GL11.glPopMatrix();
		}
		T t = map.get(current);
		if(t != null)
		{
			GL11.glPushMatrix();
			GL11.glTranslated(0, 24, 0);
			t.render(pticks, mouseX, mouseY);
			GL11.glPopMatrix();
		}
	}

	public void addElement(Tab tab, T element)
	{
		map.put(tab, element);
		if(current == null)
			current = tab;
	}

	@Override
	public void addElement(U e)
	{
		throw new UnsupportedOperationException("You have to use the other method");
	}

	@Override
	public void removeElement(U e)
	{
		throw new UnsupportedOperationException("You have to use the other method");
	}

	@Override
	public U getHovered(int mouseX, int mouseY)
	{
		AbstractGuiElement element = getElement(mouseX, mouseY);
		if((element != null) && !(element instanceof Tab))
		{
			T igc = (T) element;
			return igc.getHovered(mouseX, mouseY - 24);
		}
		return null;
	}

	public AbstractGuiElement getElement(int mouseX, int mouseY)
	{
		if(mouseY <= 24)
		{
			int numX = mouseX / 24;
			int i = 0;
			for(Tab t : map.keySet())
				if(i++ == numX)
					return t;
			return null;
		}
		return map.get(current);
	}

	@Override
	public void clickableClicked(IClickable c, String id, int button)
	{
		if(c instanceof Tab)
			current = (Tab) c;
		else if(parent != null)
			parent.clickableClicked(c, id, button);
	}

	@Override
	public void typableChanged(ITypable t)
	{
	}

	@Override
	public void recalc()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public WindowSpaceStore getWindowSpace(U e)
	{
		return parent.getWindowSpace(this);
	}

	public Set<Tab> tabSet()
	{
		return map.keySet();
	}

	public T getElement(Tab tab)
	{
		return map.get(tab);
	}

	public static class Tab extends DarkcraftGuiButton
	{
		private final ResourceLocation rl;
		private final UVStore uv;

		public Tab(String id, ResourceLocation rl, UVStore uv)
		{
			super(id, 0,0, 24,24, "");
			this.rl = rl;
			this.uv = uv;
		}

		@Override
		public void render(float pticks, int mouseX, int mouseY)
		{
			RenderHelper.resetColour();
			RenderHelper.bindTexture(rl);
			RenderHelper.uiFace(0, 0, 24, 24, 0, uv, true);
		}

	}
}
