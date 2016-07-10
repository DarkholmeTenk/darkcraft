package io.darkcraft.mod.client.renderer.gui.system.spells;

import io.darkcraft.mod.client.renderer.gui.system.DarkcraftGuiList;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftLabel;
import io.darkcraft.mod.client.renderer.gui.system.DarkcraftSubGui;
import io.darkcraft.mod.client.renderer.gui.system.daedric.DaedricLabel;
import io.darkcraft.mod.client.renderer.gui.system.interfaces.IClickable;
import io.darkcraft.mod.client.renderer.gui.textures.ScalableBackground;
import io.darkcraft.mod.common.magic.systems.component.IConfigurableComponent;
import io.darkcraft.mod.common.magic.systems.spell.ComponentInstance;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;
import io.darkcraft.mod.common.network.SpellSelectionPacketHandler;

public class SpellHover extends DarkcraftSubGui
{
	private Spell spell;
	private PlayerCaster pc;
	private SpellIcon icon;
	private DarkcraftLabel label;
	private DarkcraftGuiList list;
	private DarkcraftLabel cost;

	public SpellHover(int _x, int _y, int width, int height)
	{
		super(_x, _y, new ScalableBackground(width, height));
		setSpell(null, null);
		addElement(icon = new SpellIcon(18,36,16,16,null));
		addElement(label = new DarkcraftLabel(18,18,width-36,""));
		addElement(new DaedricLabel(36,38,"Cost:"));
		addElement(cost = new DarkcraftLabel(70,42,60,""));
		addElement(list = new DarkcraftGuiList(18,60,width-36,height-78));
	}

	public void setSpell(PlayerCaster _pc, Spell sp)
	{
		pc = _pc;
		if(sp == null)
		{
			spell = null;
			visible = false;
		}
		else
		{
			if(spell == sp) return;
			visible = true;
			label.text = sp.name;
			cost.text = String.format("%.1f", sp.getCost(pc));
			icon.spell = sp;
			list.clear();
			for(int i = 0; i < sp.components.length; i++)
			{
				ComponentInstance ci = sp.components[i];
				if(ci.component instanceof IConfigurableComponent)
					list.addElement(new ComponentInstanceConfig(0,0,list.iW - 16,i,ci));
				else
					list.addElement(new ComponentInstanceLabel(0,0,list.iW,ci));
			}
			spell = sp;
		}
	}

	public Spell getSpell()
	{
		return spell;
	}

	@Override
	public void clickableClicked(IClickable c, String id, int button)
	{
		if("config".equals(id))
		{
			ComponentInstanceConfig cic = (ComponentInstanceConfig) c;
			int slot = pc.getIndex(spell);
			int ciSlot = cic.slot;
			int val = cic.val;
			SpellSelectionPacketHandler.sendConfigChange(pc.getCaster(), slot, ciSlot, val);
		}
		else
			super.clickableClicked(c, id, button);
	}
}
