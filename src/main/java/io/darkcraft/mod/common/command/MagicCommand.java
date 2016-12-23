package io.darkcraft.mod.common.command;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

import io.darkcraft.darkcore.mod.abstracts.AbstractCommandNew;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.component.SpellPartRegistry;
import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;

public class MagicCommand extends AbstractCommandNew
{
	public MagicCommand()
	{
		super(new SetManaSC(), new MagicChalkCommand(), new DebugGuiSC(), new UnlockSC());
	}

	@Override
	public String getCommandName()
	{
		return "magic";
	}

	@Override
	public void getAliases(List<String> list)
	{
		list.add("m");
	}

	@Override
	public boolean process(ICommandSender sen, List<String> strList)
	{
		return false;
	}

	private static class SetManaSC extends AbstractCommandNew
	{

		@Override
		public String getCommandName()
		{
			return "setmana";
		}

		@Override
		public void getCommandUsage(ICommandSender sen, String tc)
		{
			sendString(sen, tc + " <player> [mana]");
		}

		@Override
		public void getAliases(List<String> list){}

		@Override
		public boolean process(ICommandSender sen, List<String> strList)
		{
			EntityPlayer pl = null;
			if(sen instanceof EntityPlayer)
				pl = (EntityPlayer) sen;
			if((strList.size() == 0) || (strList.size() > 2)) return false;
			if(strList.size() == 2)
				pl = PlayerHelper.getPlayer(strList.get(0));
			if(pl == null)
			{
				sendString(sen, "No player specified");
				return false;
			}
			try
			{
				double am = Double.parseDouble(strList.size() == 2 ? strList.get(1) : strList.get(0));
				PlayerCaster pc = Helper.getPlayerCaster(pl);
				pc.setMana(am);
				sendString(sen, "Set " + PlayerHelper.getUsername(pl)+"'s mana to " + String.format("%.1f",am));
			}
			catch(NumberFormatException e)
			{
				sendString(sen, "Not a number");
				return false;
			}
			return true;
		}

	}

	private static class DebugGuiSC extends AbstractCommandNew
	{
		@Override
		public String getCommandName()
		{
			return "debuggui";
		}

		@Override
		public void getAliases(List<String> list){}

		@Override
		public boolean process(ICommandSender sen, List<String> strList)
		{
			EntityPlayer pl = null;
			if(sen instanceof EntityPlayer)
				pl = (EntityPlayer) sen;
			if(pl != null)
				pl.openGui(DarkcraftMod.i, 1000, pl.worldObj, 0, 0, 0);
			return true;
		}
	}

	private static class UnlockSC extends AbstractCommandNew
	{

		@Override
		public String getCommandName()
		{
			return "unlockspellcomponents";
		}

		@Override
		public void getCommandUsage(ICommandSender sen, String tc)
		{
			sendString(sen, tc + " <player> [all|component]");
		}

		@Override
		public void getAliases(List<String> list)
		{
			list.add("unlock");
		}

		@Override
		public boolean process(ICommandSender sen, List<String> strList)
		{
			if((strList.size() == 0) || (strList.size() > 2))
				return false;

			String user = sen.getCommandSenderName();
			String component = "";
			if(strList.size() == 1)
				component = strList.get(0);
			else
			{
				user = strList.get(0);
				component = strList.get(1);
			}

			EntityPlayer player = PlayerHelper.getPlayer(user);
			if(player == null)
			{
				sendString(sen, "Could not find player " + user);
				return false;
			}

			Set<IComponent> components = Sets.newHashSet();
			if(component.equals("all"))
				components.addAll(SpellPartRegistry.getAllComponents());
			else
			{
				IComponent icomponent = SpellPartRegistry.getComponent(component);
				if(icomponent == null)
				{
					sendString(sen, "Could not find component " + component);
					return false;
				}
				components.add(icomponent);
			}

			PlayerCaster pc = Helper.getPlayerCaster(player);
			if(pc == null)
				return true;
			for(IComponent c : components)
				pc.learnComponent(c);
			return true;
		}
	}
}
