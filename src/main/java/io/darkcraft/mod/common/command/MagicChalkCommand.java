package io.darkcraft.mod.common.command;

import java.util.List;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import io.darkcraft.darkcore.mod.abstracts.AbstractCommandNew;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;

public class MagicChalkCommand extends AbstractCommandNew
{

	@Override
	public String getCommandName()
	{
		return "chalk";
	}

	@Override
	public void getAliases(List<String> list){}

	@Override
	public boolean process(ICommandSender sen, List<String> strList)
	{
		if(sen instanceof EntityPlayer)
		{
			if(strList.size() == 0) return false;
			ItemStack is = new ItemStack(ItemBlockRegistry.magicChalk);
			//MagicChalk.setString(is,strList.get(0));
			WorldHelper.giveItemStack((EntityPlayer) sen, is);
			return true;
		}
		return false;
	}

}
