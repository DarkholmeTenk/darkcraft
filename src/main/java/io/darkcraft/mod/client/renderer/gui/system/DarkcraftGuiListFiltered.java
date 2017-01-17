package io.darkcraft.mod.client.renderer.gui.system;

import java.util.LinkedHashSet;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import io.darkcraft.mod.client.renderer.gui.system.interfaces.Tab;

public class DarkcraftGuiListFiltered<I extends AbstractGuiElement, T extends Tab<I>> extends DarkcraftGuiList<I>
{
	private final Set<T> tabs;
	private T current;

	private void setTab(T tab)
	{
		current = tab;
		for(I t : elements)
			t.enabled = (tab == null) || tab.filter(t);
		recalc();
	}

	public DarkcraftGuiListFiltered(int _x, int _y, int width, int height)
	{
		this(_x, _y, width, height, new LinkedHashSet<T>());
	}

	public DarkcraftGuiListFiltered(int _x, int _y, int width, int height, Set<T> tabs)
	{
		super(_x, _y, width, height - 24);
		setSize(width, height);
		this.tabs = tabs;
		current = null;
	}

	public void addTab(T tab)
	{
		tabs.add(tab);
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		GL11.glPushMatrix();
		super.render(pticks, mouseX, mouseY);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glTranslated(0, h-24, 0);
		for(Tab t : tabs)
		{
			t.down = (t == current);
			GL11.glPushMatrix();
			t.render(pticks, mouseX, mouseY);
			GL11.glPopMatrix();
			GL11.glTranslated(24, 0, 0);
		}
		GL11.glPopMatrix();
	}

	@Override
	public boolean click(int button, int x, int y)
	{
		if((y > (h-24)) && (y < h) && ((x > 0) && (x < w)))
		{
			int tabNum = x / 24;
			if(tabNum >= tabs.size())
				setTab(null);
			else
			{
				int i = 0;
				for(T t : tabs)
					if(i++ == tabNum)
						setTab(current == t ? null : t);
			}
			return true;
		}
		else
			return super.click(button, x, y);
	}
}
