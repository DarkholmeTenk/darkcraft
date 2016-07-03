package io.darkcraft.mod.client.renderer.gui.system;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import io.darkcraft.darkcore.mod.datastore.WindowSpaceStore;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IDraggable;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IGuiContainer;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.ITypable;
import io.darkcraft.mod.client.renderer.gui.system.prefabs.VerticalScrollbar;
import io.darkcraft.mod.client.renderer.gui.textures.ScalableInternal;

public class DarkcraftGuiList extends AbstractGuiElement implements IDraggable, IGuiContainer
{
	private List<AbstractGuiElement> elements = new ArrayList();
	private ScalableInternal bg;
	private VerticalScrollbar scroll;

	public DarkcraftGuiList(int _x, int _y, int width, int height)
	{
		super(_x, _y, width, height);
		bg = new ScalableInternal(width-16, height);
		scroll = new VerticalScrollbar(width-16, 0, height);
	}

	@Override
	public void recalc()
	{
		int th = 0;
		for(AbstractGuiElement e : elements)
			th += e.h;
		scroll.setMinMax(0, th-(h-2));
		scroll.setScrollable(th > (h - 2));
	}

	@Override
	public void addElement(AbstractGuiElement e)
	{
		elements.add(e);
		e.parent = this;
		recalc();
	}

	@Override
	public void removeElement(AbstractGuiElement e)
	{
		elements.remove(e);
		e.parent = null;
	}

	@Override
	public boolean click(int button, int x, int y)
	{
		if(x > (w - 16))
		{
			scroll.click(button, x-scroll.x, y-scroll.y);
		}
		else
		{
			int cy = y - scroll.asInt();
			for(AbstractGuiElement e : elements)
			{
				if(e.withinBounds(x, cy))
				{
					if(e instanceof IClickable)
						((IClickable) e).click(button, x, cy);
					break;
				}
				else
					cy += e.h;
			}
		}
		return true;
	}

	@Override
	public boolean drag(int button, int x, int y)
	{
		if(button != 0) return false;
		if(x > (w-16))
		{
			return scroll.drag(button, x-scroll.x, y-scroll.y);
		}
		return false;
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		bg.render(0, 0, 0, true);
		GL11.glPushMatrix();
		GL11.glTranslatef(scroll.x, scroll.y, 0);
		scroll.render(pticks, mouseX, mouseY);
		GL11.glPopMatrix();
		GL11.glTranslatef(1, 1, 0);
		int y = -scroll.asInt();
		for(AbstractGuiElement e : elements)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(0, y, 0);
			if(((y >= 0) && ((y+e.h) < h)))
				e.render(pticks, mouseX, mouseY);
			y += e.h;
			GL11.glPopMatrix();
			if(y > h) break;
		}
	}

	@Override
	public void clickableClicked(IClickable c, String id)
	{
		if(parent != null)
			parent.clickableClicked(c, id);
	}

	@Override
	public void typableChanged(ITypable t)
	{
		if(parent != null)
			parent.typableChanged(t);
	}

	@Override
	public WindowSpaceStore getWindowSpace(AbstractGuiElement e)
	{
		if((parent != null) && (wss == null))
			wss = parent.getWindowSpace(this);
		if(e == this) return wss;
		int y = -scroll.asInt();
		for(AbstractGuiElement _e : elements)
		{
			if(e == _e)
				break;
			y += _e.h;
		}
		return wss.transform(1, y);
	}

}
