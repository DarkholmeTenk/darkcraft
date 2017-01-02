package io.darkcraft.mod.client.renderer.gui.system;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.opengl.GL11;

import io.darkcraft.darkcore.mod.datastore.WindowSpaceStore;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IDraggable;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IGuiContainer;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.ITypable;
import io.darkcraft.mod.client.renderer.gui.system.prefabs.VerticalScrollbar;
import io.darkcraft.mod.client.renderer.gui.textures.ScalableInternal;

public class DarkcraftGuiList<T extends AbstractGuiElement> extends AbstractGuiElement implements IDraggable, IGuiContainer<T>, Iterable<T>
{
	private List<T> elements = new ArrayList();
	private ScalableInternal bg;
	private VerticalScrollbar scroll;
	public int iW;

	public DarkcraftGuiList(int _x, int _y, int width, int height)
	{
		super(_x, _y, width, height);
		bg = new ScalableInternal(width-16, height);
		iW = bg.w-3;
		scroll = new VerticalScrollbar(width-16, 0, height);
		scroll.parent = this;
	}

	@Override
	public void recalc()
	{
		int th = 0;
		for(AbstractGuiElement e : elements)
		{
			if(e.w > iW)
				e.setSize(iW, e.h);
			th += e.h;
		}
		scroll.setMinMax(0, th-(h-3));
		scroll.setScrollable(th > (h - 3));
	}

	@Override
	public void addElement(T e)
	{
		elements.add(e);
		if(e.w > iW)
			e.setSize(iW, e.h);
		e.parent = this;
		recalc();
	}

	@Override
	public void removeElement(T e)
	{
		elements.remove(e);
		e.parent = null;
		recalc();
	}

	public void clear()
	{
		elements.clear();
		recalc();
	}

	public void sort(Comparator<T> comparator)
	{
		elements.sort(comparator);
		recalc();
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
			if((x == 0) || (y == 0) || (x == (w - 16)) || (y == h))
			{
				parent.clickableClicked(this, "list", button);
				return false;
			}
			int cy = (y + scroll.asInt()) - 1;
			for(AbstractGuiElement e : elements)
			{
				if(e.withinBounds(x, cy))
				{
					if(e instanceof IClickable)
						((IClickable) e).click(button, x, cy);
					return true;
				}
				else
					cy -= e.h;
			}
			parent.clickableClicked(this, "list", button);
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
		else
		{
			if((x == 0) || (y == 0) || (x == (w - 16)) || (y == h)) return false;
			int cy = (y + scroll.asInt()) - 1;
			for(AbstractGuiElement e : elements)
			{
				if(e.withinBounds(x, cy))
				{
					if(e instanceof IDraggable)
						((IDraggable) e).drag(button, x, cy);
					break;
				}
				else
					cy -= e.h;
			}
		}
		return false;
	}

	@Override
	public T getHovered(int mouseX, int mouseY)
	{
		if((mouseX > 0) && (mouseY > 0) && (mouseX < (w - 16)) && (mouseY < h))
		{
			int cy = (mouseY + scroll.asInt()) - 1;
			for(T e : elements)
			{
				if(e.withinBounds(mouseX, cy))
					return e;
				else
					cy -= e.h;
			}
		}
		return null;
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		bg.render(0, 0, 0, true);
		GL11.glPushMatrix();
		GL11.glTranslatef(scroll.x, scroll.y, 0);
		scroll.render(pticks, mouseX, mouseY);
		GL11.glPopMatrix();
		GL11.glTranslatef(2, 2, 0);
		GL11.glPushAttrib(GL11.GL_SCISSOR_BIT);
		WindowSpaceStore wss = getWindowSpace(this);
		wss = wss.transform(2, h-1);
		GL11.glScissor((int) wss.x, (int) wss.getFromBottom(), (int)((w-17) * wss.scale), (int)((h-3) * wss.scale));
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		int y = -scroll.asInt();
		for(AbstractGuiElement e : elements)
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(0, y, 0);
			if((((y+e.h) >= 0) && ((y < h))))
				e.render(pticks, mouseX, mouseY);
			y += e.h;
			GL11.glPopMatrix();
			if(y > h) break;
		}
		GL11.glPopAttrib();
	}

	@Override
	public void clickableClicked(IClickable c, String id, int button)
	{
		if(parent != null)
			parent.clickableClicked(c, id, button);
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
		return wss.transform(2, y);
	}

	@Override
	public Iterator<T> iterator()
	{
		return elements.iterator();
	}

}
