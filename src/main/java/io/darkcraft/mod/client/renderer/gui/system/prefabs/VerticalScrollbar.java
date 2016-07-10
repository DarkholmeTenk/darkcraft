package io.darkcraft.mod.client.renderer.gui.system.prefabs;

import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.mod.client.renderer.gui.system.AbstractGuiElement;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IDraggable;
import io.darkcraft.mod.client.renderer.gui.textures.VerticalScrollbarTexture;

public class VerticalScrollbar extends AbstractGuiElement implements IClickable, IDraggable
{
	public float value;
	private float clickAmount;
	private float min;
	private float max;
	private boolean scrollable;
	private VerticalScrollbarTexture scrollTex;

	public VerticalScrollbar(int x, int y, int h)
	{
		this(x,y,h,32);
	}

	public VerticalScrollbar(int x, int y, int h, float scrollAmount)
	{
		super(x,y,16,h);
		clickAmount = scrollAmount;
		scrollTex = new VerticalScrollbarTexture(h);
	}

	public void setScrollable(boolean _scrollable)
	{
		scrollable = _scrollable;
		reclamp();
	}

	public void setMinMax(float _min, float _max)
	{
		min = _min;
		max = _max;
		reclamp();
	}

	public int asInt()
	{
		return Math.round(value);
	}

	private void reclamp()
	{
		if(!scrollable)
			value = 0;
		else
			value = (float) MathHelper.clamp(value, min, max);
	}

	@Override
	public boolean click(int button, int x, int y)
	{
		if(y < 16)
			value -= clickAmount;
		else if(y > (h-16))
			value += clickAmount;
		else
			drag(button,x,y);
		reclamp();
		parent.clickableClicked(this, "scroll", button);
		return true;
	}

	@Override
	public boolean drag(int button, int x, int y)
	{
		if((y >= 16) && (y <= (h-16)))
		{
			float p = (y-16) / (float)(h - 32);
			p *= (max-min);
			value = min + p;
			reclamp();
			parent.clickableClicked(this, "scroll", button);
			return true;
		}
		return false;
	}

	@Override
	public void render(float pticks, int mouseX, int mouseY)
	{
		float x = (value - min) / (max - min);
		scrollTex.render(0, 0, 0, x, scrollable);
	}

}
