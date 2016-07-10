package io.darkcraft.mod.client.renderer.gui.system.spells;

import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGuiOpenable;
import io.darkcraft.mod.client.renderer.gui.system.daedric.DaedricLabel;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;
import io.darkcraft.mod.client.renderer.gui.system.prefabs.HorizontalScrollbar;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.component.IDurationComponent;
import io.darkcraft.mod.common.magic.systems.component.IMagnitudeComponent;
import io.darkcraft.mod.common.magic.systems.component.INoAreaComponent;
import io.darkcraft.mod.common.magic.systems.spell.ComponentInstance;

public class ComponentInstanceMod extends DarkcraftGuiOpenable
{
	private IComponent c;
	private ComponentInstance ci;
	private ComponentInstanceLabel instanceLabel;
	private HorizontalScrollbar mag;
	private HorizontalScrollbar dur;
	private HorizontalScrollbar are;

	public ComponentInstanceMod(int _x, int _y, int w, ComponentInstance _ci)
	{
		super(_x, _y, new ComponentInstanceLabel(0,0,w,_ci));
		c = _ci.component;
		ci = _ci;
		instanceLabel = (ComponentInstanceLabel) title;
		initBars();
	}

	public ComponentInstanceMod(int _x, int _y, int w, IComponent _c)
	{
		super(_x, _y, new ComponentInstanceLabel(0,0,w,new ComponentInstance(_c,0,0)));
		c = _c;
		instanceLabel = (ComponentInstanceLabel) title;
		ci = instanceLabel.compInst;
		initBars();
	}

	private void initBars()
	{
		int x = 70;
		int y = 18;
		if(c instanceof IMagnitudeComponent)
		{
			IMagnitudeComponent _c = (IMagnitudeComponent) c;
			mag = new HorizontalScrollbar(x,y,w-x-2,1);
			mag.setMinMax(_c.getMinMagnitude(), _c.getMaxMagnitude());
			mag.setValue(ci.magnitude);
			addElement(mag);
			addElement(new DaedricLabel(2,y+2,x-4,"Magnitude:"));
			y+=18;
		}
		if(c instanceof IDurationComponent)
		{
			IDurationComponent _c = (IDurationComponent) c;
			dur = new HorizontalScrollbar(x,y,w-x-2,1);
			dur.setMinMax(_c.getMinDuration(), _c.getMaxDuration());
			dur.setValue(ci.duration);
			addElement(dur);
			addElement(new DaedricLabel(2,y+2,x-4,"Duration:"));
			y+=18;
		}
		if(!(c instanceof INoAreaComponent))
		{
			are = new HorizontalScrollbar(x,y,w-x-2,1);
			are.setMinMax(0, 5);
			are.setValue(ci.area);
			addElement(are);
			addElement(new DaedricLabel(2,y+2,x-4,"Area:"));
			y+=18;
		}
		if(y == 18)
			closeOpenButton.visible = false;
	}

	@Override
	public void clickableClicked(IClickable c, String id, int button)
	{
		if(id.equals("scroll"))
		{
			instanceLabel.setCI(new ComponentInstance(this.c,
					mag != null ? mag.asInt() : 0,
					dur != null ? dur.asInt() : 0,
					are != null ? are.asInt() : 0));
			ci = instanceLabel.compInst;
		}
		else
			super.clickableClicked(c, id, button);
	}

}
