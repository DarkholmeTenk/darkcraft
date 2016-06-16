package io.darkcraft.mod.common.command;

import java.util.List;

import io.darkcraft.darkcore.mod.abstracts.AbstractCommandNew;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.caster.PlayerCaster;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;

public class MagicCommand extends AbstractCommandNew
{
	public MagicCommand()
	{
		super(new SetManaSC(), new MagicChalkCommand());
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
}
