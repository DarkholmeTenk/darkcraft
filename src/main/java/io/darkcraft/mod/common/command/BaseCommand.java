package io.darkcraft.mod.common.command;

import io.darkcraft.darkcore.mod.abstracts.AbstractCommandNew;

import java.util.List;

import net.minecraft.command.ICommandSender;

public class BaseCommand extends AbstractCommandNew
{

	@Override
	public String getCommandName()
	{
		return "darkcraft";
	}

	@Override
	public void getAliases(List<String> list)
	{
		list.add("dcraft");
	}

	@Override
	public boolean process(ICommandSender sen, List<String> strList)
	{
		return false;
	}

}
