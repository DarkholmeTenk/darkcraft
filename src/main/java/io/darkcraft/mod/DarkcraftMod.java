package io.darkcraft.mod;

import io.darkcraft.darkcore.mod.config.ConfigHandler;
import io.darkcraft.darkcore.mod.config.ConfigHandlerFactory;
import io.darkcraft.darkcore.mod.interfaces.IConfigHandlerMod;
import io.darkcraft.mod.common.CommonProxy;
import io.darkcraft.mod.common.registries.CommandRegistry;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;

import java.util.Random;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

@Mod(modid = "darkcraft", version = "0.1", dependencies = "required-after:darkcore; required-after:SkillAPI")
public class DarkcraftMod implements IConfigHandlerMod
{
	public static Random					modRand					= new Random();
	public static ConfigHandler				configHandler			= null;

	@SidedProxy(clientSide = "io.darkcraft.mod.client.ClientProxy",
			serverSide = "io.darkcraft.mod.common.CommonProxy")
	public static CommonProxy				proxy;

	@Override
	public String getModID()
	{
		return "darkcraft";
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		configHandler = ConfigHandlerFactory.getConfigHandler(this);
		ItemBlockRegistry.registerBlocks();
		ItemBlockRegistry.registerItems();
		FMLInterModComms.sendMessage("SkillAPI", "register", "io.darkcraft.mod.common.registries.SkillRegistry.requestAPI");
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init();
	}

	@EventHandler
	public void serverStarting(FMLServerAboutToStartEvent event)
	{
		registerRegistries();
	}

	@EventHandler
	public void serverStartEvent(FMLServerStartingEvent event)
	{
		CommandRegistry.registerCommands(event);
	}

	private void registerRegistries()
	{
	}
}
