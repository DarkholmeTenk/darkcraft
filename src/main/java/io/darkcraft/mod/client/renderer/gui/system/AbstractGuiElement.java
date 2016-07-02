package io.darkcraft.mod.client.renderer.gui.system;

import io.darkcraft.mod.client.renderer.gui.system.interfaces.IGuiContainer;

public abstract class AbstractGuiElement
{
	public boolean enabled = true;
	public boolean visible = true;
	public IGuiContainer parent;

	public int w;
	public int h;
	public int x;
	public int y;

	public AbstractGuiElement(int _x, int _y, int width, int height)
	{
		x = _x;
		y = _y;
		w = width;
		h = height;
	}

	public void setSize(int _w, int _h)
	{
		w = _w;
		h = _h;
		if(parent != null)
			parent.recalc();
	}

	public void setPos(int _x, int _y)
	{
		x = _x;
		y = _y;
		if(parent != null)
			parent.recalc();
	}

	/**
	 * Mouse is considered to be over the element if it is visible and 0&lt;=mouseX&lt;w and 0&lt;=mouseY&lt;h.<br>
	 * This is because the x and y parameters are translated to this elements frame of reference.
	 * @param mouseX
	 * @param mouseY
	 * @return
	 */
	public boolean withinBounds(int mouseX, int mouseY)
	{
		if((mouseX < 0) || (mouseY < 0)) return false;
		if((mouseX > w) || (mouseY > h)) return false;
		return true;
	}

	/**
	 * Will be translated to correct x/y coordinates before this is called.<br>
	 * Assume that zLevel is 0
	 * @param pticks
	 * @param mouseX
	 * @param mouseY
	 */
	public abstract void render(float pticks, int mouseX, int mouseY);
}
