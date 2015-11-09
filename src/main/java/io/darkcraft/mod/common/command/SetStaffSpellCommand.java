package io.darkcraft.mod.common.command;

import io.darkcraft.darkcore.mod.abstracts.AbstractCommandNew;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.mod.common.items.staff.ItemStaffHelper;
import io.darkcraft.mod.common.items.staff.ItemStaffHelperFactory;
import io.darkcraft.mod.common.magic.SpellPartRegistry;
import io.darkcraft.mod.common.magic.component.IComponent;
import io.darkcraft.mod.common.magic.spell.ComponentInstance;
import io.darkcraft.mod.common.magic.spell.Spell;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class SetStaffSpellCommand extends AbstractCommandNew
{

	@Override
	public String getCommandName()
	{
		return "setstaffspell";
	}

	@Override
	public void getAliases(List<String> list)
	{
		list.add("sss");
		list.add("setspell");
		list.add("ss");
	}

	@Override
	public boolean process(ICommandSender sen, List<String> strList)
	{
		if(sen instanceof EntityPlayer)
		{
			EntityPlayer pl = (EntityPlayer) sen;
			ItemStaffHelper helper = ItemStaffHelperFactory.getHelper(pl.getHeldItem());
			if(helper == null)
			{
				sendString(sen, "No staff held!");
				return true;
			}

			int s = strList.size();
			if((s % 3) != 1)
			{
				sendString(sen, "Command should be in the form <spellName>, <spell>");
				sendString(sen, "Spell must be in the form of a list of <componentID> <magnitude> <duration>");
				return true;
			}
			s /= 3;
			String name = strList.get(0);
			ComponentInstance[] comps = new ComponentInstance[s];
			for(int i = 0; i<s; i++)
			{
				int b = (i * 3) + 1;
				String id = strList.get(b);
				int mag = MathHelper.toInt(strList.get(b+1), 1);
				int dur = MathHelper.toInt(strList.get(b+2), 1);
				IComponent c = SpellPartRegistry.getComponent(id);
				if(c == null)
				{
					sendString(sen, "Invalid component: " + id);
					return true;
				}
				comps[i] = new ComponentInstance(c, mag, dur);
			}
			Spell spell = new Spell(name,comps);
			helper.setSpell(spell);
		}
		return true;
	}

}
