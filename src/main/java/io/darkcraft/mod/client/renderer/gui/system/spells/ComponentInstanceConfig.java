package io.darkcraft.mod.client.renderer.gui.system.spells;

import io.darkcraft.mod.client.renderer.gui.system.AbstractGuiElement;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGuiOpenable;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftLabel;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;
import io.darkcraft.mod.client.renderer.gui.system.prefabs.HorizontalScrollbar;
import io.darkcraft.mod.common.magic.systems.component.IConfigurableComponent;
import io.darkcraft.mod.common.magic.systems.spell.ComponentInstance;
import net.minecraft.util.StatCollector;

public class ComponentInstanceConfig extends DarkcraftGuiOpenable<ComponentInstanceLabel,AbstractGuiElement>
{
	public final int slot;
	public final ComponentInstance ci;
	private final IConfigurableComponent cc;
	private HorizontalScrollbar hs;
	private DarkcraftLabel label;
	public int val;

	public ComponentInstanceConfig(int _x, int _y, int w, int _slot, ComponentInstance _ci)
	{
		super(_x, _y, new ComponentInstanceLabel(0,0,w,_ci));
		slot = _slot;
		ci = _ci;
		val = ci.config;
		cc = (IConfigurableComponent) ci.component;
		hs = new HorizontalScrollbar(2,title.h+2,this.w-4,1);
		hs.setMinMax(cc.getMinConfig(), cc.getMaxConfig());
		hs.setValue(val);
		hs.setScrollable(true);
		addElement(hs);
		label = new DarkcraftLabel(2,title.h + 20,this.w-4,StatCollector.translateToLocal(cc.getConfigDescription(val)));
		addElement(label);
	}

	@Override
	public void clickableClicked(IClickable c, String id, int button)
	{
		if("scroll".equals(id))
		{
			if(ci.config != hs.asInt())
			{
				ci.config = hs.asInt();
				val = ci.config;
				label.text = StatCollector.translateToLocal(cc.getConfigDescription(val));
				title.setCI(ci);
				parent.clickableClicked(this, "config", button);
			}
		}
		else
			super.clickableClicked(c, id, button);
	}

}
