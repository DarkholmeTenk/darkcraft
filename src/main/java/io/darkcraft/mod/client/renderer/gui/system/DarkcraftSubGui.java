package io.darkcraft.mod.client.renderer.gui.system;

import java.util.Set;

import org.lwjgl.opengl.GL11;

import gnu.trove.set.hash.THashSet;
import io.darkcraft.darkcore.mod.datastore.GuiTexture;
import io.darkcraft.darkcore.mod.datastore.WindowSpaceStore;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IDraggable;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IGuiContainer;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.ITypable;

public class DarkcraftSubGui extends AbstractGuiElement implements IClickable, IGuiContainer, IDraggable
{
	private GuiTexture bg;
	private Set<AbstractGuiElement> elements = new THashSet();

	public DarkcraftSubGui(int _x, int _y, GuiTexture gt)
	{
		super(_x, _y, gt.w, gt.h);
		bg = gt;
	}

	@Override
	public boolean drag(int button, int x, int y)
	{
		AbstractGuiElement e = getHovered(x,y);
		if(e instanceof IDraggable)
			return ((IDraggable) e).drag(button, x-e.x, y-e.y);
		return false;
	}

	@Override
	public boolean click(int button, int x, int y)
	{
		AbstractGuiElement e = getHovered(x,y);
		if(e instanceof IClickable)
			return ((IClickable) e).click(button, x-e.x, y-e.y);
		return false;
	}

	@Override
	public void addElement(AbstractGuiElement e)
	{
		elements.add(e);
		e.parent = this;
	}

	@Override
	public void removeElement(AbstractGuiElement e)
	{
		elements.remove(e);
		e.parent = null;
	}

	@Override
	public AbstractGuiElement getHovered(int mouseX, int mouseY)
	{
		for(AbstractGuiElement e : elements)
		{
			if(e.withinBounds(mouseX-e.x, mouseY-e.y))
				return e;
		}
		return null;
	}

	@Override
	public void clickableClicked(IClickable c, String id, int button)
	{
		parent.clickableClicked(c, id, button);
	}

	@Override
	public void typableChanged(ITypable t)
	{
		parent.typableChanged(t);
	}

	@Override
	public void recalc(){}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		bg.render(0, 0, 0, true);
		for(AbstractGuiElement e : elements)
			if(e.visible)
			{
				GL11.glPushMatrix();
				GL11.glTranslatef(e.x, e.y, 0);
				e.render(pticks, mouseX, mouseY);
				GL11.glPopMatrix();
			}
	}

	@Override
	public WindowSpaceStore getWindowSpace(AbstractGuiElement e)
	{
		if(wss == null) wss = parent.getWindowSpace(this);
		if(e == this) return wss;
		WindowSpaceStore temp = wss.transform(e.x, e.y);
		return temp;
	}

}
