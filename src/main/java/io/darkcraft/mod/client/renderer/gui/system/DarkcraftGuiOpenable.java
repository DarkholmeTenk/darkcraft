package io.darkcraft.mod.client.renderer.gui.system;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import io.darkcraft.darkcore.mod.datastore.WindowSpaceStore;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IDraggable;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IGuiContainer;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.ITypable;
import io.darkcraft.mod.client.renderer.gui.system.prefabs.ButtonCloseOpen;

public class DarkcraftGuiOpenable<T extends AbstractGuiElement> extends AbstractGuiElement implements IGuiContainer<T>, IClickable, IDraggable
{
	protected ButtonCloseOpen closeOpenButton;
	public final AbstractGuiElement title;
	private List<T> hiddenElements = new ArrayList();
	private final int cH;

	public DarkcraftGuiOpenable(int _x, int _y, AbstractGuiElement titleElement)
	{
		super(_x, _y, titleElement.w+16, Math.max(titleElement.h, 16));
		title = titleElement;
		cH = h;
		closeOpenButton = new ButtonCloseOpen(titleElement.w, 0);
		closeOpenButton.parent = this;
	}

	public void open()
	{
		closeOpenButton.isOpen = true;
		recalc();
	}

	public void close()
	{
		closeOpenButton.isOpen = false;
		recalc();
	}

	private boolean handleClick(int button, int x, int y, AbstractGuiElement e)
	{
		if(!(e instanceof IClickable)) return false;
		int cx = x - e.x;
		int cy = y - e.y;
		if(e.withinBounds(cx, cy))
			return ((IClickable)e).click(button, x-e.x, y-e.y);
		return false;
	}

	private boolean handleDrag(int button, int x, int y, AbstractGuiElement e)
	{
		if(!(e instanceof IDraggable)) return false;
		int cx = x - e.x;
		int cy = y - e.y;
		if(e.withinBounds(cx, cy))
			return ((IDraggable)e).drag(button, x-e.x, y-e.y);
		return false;
	}

	@Override
	public T getHovered(int mouseX, int mouseY)
	{
		if((mouseX > (w - 16)) && (mouseY < 16) && closeOpenButton.enabled && closeOpenButton.visible)
			return null;
		else
		{
			if(closeOpenButton.isOpen)
				for(T e : hiddenElements)
				{
					int cx = mouseX - e.x;
					int cy = mouseY - e.y;
					if(e.withinBounds(cx, cy))
						return e;
				}
		}
		return null;
	}

	@Override
	public boolean click(int button, int x, int y)
	{
		if((x > (w - 16)) && (y < 16) && closeOpenButton.enabled && closeOpenButton.visible)
		{
			closeOpenButton.click(button, x-(w-16), y);
			return true;
		}
		else
		{
			if(handleClick(button, x,y, title)) return true;
			if(closeOpenButton.isOpen)
				for(AbstractGuiElement e : hiddenElements)
					if(handleClick(button, x,y, e)) return true;
		}
		return false;
	}

	@Override
	public boolean drag(int button, int x, int y)
	{
		if((x > (w - 16)) && (y < 16))
			return false;
		else
		{
			if(handleDrag(button, x,y, title)) return true;
			if(closeOpenButton.isOpen)
				for(AbstractGuiElement e : hiddenElements)
					if(handleDrag(button, x,y, e)) return true;
		}
		return false;
	}

	@Override
	public void addElement(T e)
	{
		hiddenElements.add(e);
		e.parent = this;
	}

	@Override
	public void removeElement(AbstractGuiElement e)
	{
		hiddenElements.remove(e);
		e.parent = null;
	}

	@Override
	public void clickableClicked(IClickable c, String id)
	{
		if(c == closeOpenButton)
			recalc();
		else
			parent.clickableClicked(c, id);
	}

	@Override
	public void typableChanged(ITypable t){}

	@Override
	public void recalc()
	{
		if(!closeOpenButton.isOpen)
			setSize(w, cH);
		else
		{
			int mH = cH;
			for(AbstractGuiElement e : hiddenElements)
				mH = Math.max(mH, e.y+e.h);
			setSize(w, mH);
		}
	}

	private void render(AbstractGuiElement e, float pticks, int mouseX, int mouseY)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(e.x, e.y, 0);
		e.render(pticks, mouseX-e.x, mouseY-e.y);
		GL11.glPopMatrix();
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		GL11.glPushMatrix();
		GL11.glTranslatef(0, (cH-title.h)/2, 0);
		title.render(pticks, mouseX, mouseY);
		GL11.glPopMatrix();
		render(closeOpenButton, pticks, mouseX, mouseY);
		if(closeOpenButton.isOpen)
			for(AbstractGuiElement e : hiddenElements)
				render(e, pticks, mouseX, mouseY);
	}

	@Override
	public WindowSpaceStore getWindowSpace(AbstractGuiElement e)
	{
		if((parent != null) && (wss == null))
			wss = parent.getWindowSpace(this);
		if(e == title)
			return wss.transform(0, (cH-title.h)/2);
		return wss.transform(e.x, e.y);
	}

}
