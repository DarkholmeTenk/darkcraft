package io.darkcraft.mod.client.renderer.gui.system;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IDraggable;
import io.darkcraft.mod.client.renderer.gui.textures.ScalableInternal;
import io.darkcraft.mod.client.renderer.gui.textures.Scrollbar;

public class DarkcraftGuiList extends AbstractGuiElement implements IDraggable
{
	private List<AbstractGuiElement> elements = new ArrayList();
	private ScalableInternal bg;
	private Scrollbar scroll;
	private int scrollPos;
	private int totalHeight;

	public DarkcraftGuiList(int _x, int _y, int width, int height)
	{
		super(_x, _y, width, height);
		bg = new ScalableInternal(width-16, height);
		scroll = new Scrollbar(height);
	}

	private boolean renderBar() { return totalHeight > (h-2); }

	private void reclamp()
	{
		if(!renderBar())
			scrollPos = 0;
		else
			scrollPos = MathHelper.clamp(scrollPos, 0, totalHeight - (h-2));
	}

	private void recalc()
	{
		int th = 0;
		for(AbstractGuiElement e : elements)
			th += e.h;
		totalHeight = th;
		reclamp();
	}

	public void addElement(AbstractGuiElement e)
	{
		elements.add(e);
		e.parent = parent;
		recalc();
	}

	@Override
	public boolean click(int button, int x, int y)
	{
		if(x > (w - 16))
		{
			if(y < 16)
				scrollPos -= 32;
			else if(y > (h-16))
				scrollPos += 32;
			reclamp();
		}
		else
		{
			int cy = y - scrollPos;
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
		if(x >  (w-16))
		{
			if((y > 16) || (y < (h-16)))
			{
				float p = (y-16) / (float)(h - 32);
				p *= (totalHeight - (h-2));
				scrollPos = (int) p;
				reclamp();
				return true;
			}
		}
		return false;
	}

	private void renderScrollbar()
	{
		float sbp = scrollPos/(float)(totalHeight - (h-2));
		scroll.render(w-16, 0, 0, sbp, renderBar());
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		bg.render(0, 0, 0, true);
		renderScrollbar();
		GL11.glTranslatef(1, 1, 0);
		int y = -scrollPos;
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

}
