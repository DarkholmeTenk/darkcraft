package io.darkcraft.mod.common.registries;

import io.darkcraft.mod.common.command.BaseCommand;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommandRegistry
{
	private static BaseCommand bc = new BaseCommand();

	public static void registerCommands(FMLServerStartingEvent event)
	{
		event.registerServerCommand(bc);
	}
}
