package io.darkcraft.mod.client.renderer.gui.system.prefabs;

import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.mod.client.renderer.gui.system.AbstractGuiElement;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IDraggable;
import io.darkcraft.mod.client.renderer.gui.textures.HorizontalScrollbarTexture;

public class HorizontalScrollbar extends AbstractGuiElement implements IClickable, IDraggable
{
	public float value;
	private float clickAmount;
	private float min;
	private float max;
	private boolean scrollable;
	private HorizontalScrollbarTexture scrollTex;

	public HorizontalScrollbar(int x, int y, int w)
	{
		this(x,y,w,32);
	}

	public HorizontalScrollbar(int x, int y, int w, float scrollAmount)
	{
		super(x,y,w,16);
		clickAmount = scrollAmount;
		scrollTex = new HorizontalScrollbarTexture(w);
		scrollable = true;
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

	public void setValue(float v)
	{
		value = v;
		reclamp();
		triggerChange();
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
		if(x < 16)
			value -= clickAmount;
		else if(x > (w-16))
			value += clickAmount;
		else
			drag(button,x,y);
		reclamp();
		parent.clickableClicked(this, "scroll", button);
		triggerChange();
		return true;
	}

	@Override
	public boolean drag(int button, int x, int y)
	{
		if((x >= 16) && (x <= (w-16)))
		{
			float p = (x-16) / (float)(w - 32);
			p *= (max-min);
			value = min + p;
			reclamp();
			parent.clickableClicked(this, "scroll", button);
			triggerChange();
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
