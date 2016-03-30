package io.darkcraft.mod;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.config.ConfigHandler;
import io.darkcraft.darkcore.mod.config.ConfigHandlerFactory;
import io.darkcraft.darkcore.mod.interfaces.IConfigHandlerMod;
import io.darkcraft.mod.common.CommonProxy;
import io.darkcraft.mod.common.magic.field.MagicFieldFactory;
import io.darkcraft.mod.common.magic.items.staff.StaffHelperFactory;
import io.darkcraft.mod.common.network.PlayerCasterPacketHandler;
import io.darkcraft.mod.common.network.SpellCreationPacketHandler;
import io.darkcraft.mod.common.registries.CommandRegistry;
import io.darkcraft.mod.common.registries.EntRegistry;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import io.darkcraft.mod.common.registries.MagicConfig;
import io.darkcraft.mod.common.registries.MagicalRegistry;

import java.util.Random;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid = "darkcraft", name = "Darkcraft", version = "0.1", dependencies = "required-after:darkcore@[0.4]; required-after:SkillAPI")
public class DarkcraftMod implements IConfigHandlerMod
{
	public static DarkcraftMod				i;
	public static final String				modName					= "darkcraft";
	public static Random					modRand					= new Random();
	public static ConfigHandler				configHandler			= null;

	@SidedProxy(clientSide = "io.darkcraft.mod.client.ClientProxy",
			serverSide = "io.darkcraft.mod.common.CommonProxy")
	public static CommonProxy				proxy;

	{
		i = this;
	}

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
		ItemBlockRegistry.registerRecipes();
		MagicalRegistry.registerMagic();
		EntRegistry.registerEntities();
		FMLInterModComms.sendMessage("SkillAPI", "register", "io.darkcraft.mod.common.registries.SkillRegistry.requestAPI");
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(i, proxy);
		DarkcoreMod.packetHandler.registerHandler(SpellCreationPacketHandler.disc, new SpellCreationPacketHandler());
		DarkcoreMod.packetHandler.registerHandler(PlayerCasterPacketHandler.disc, new PlayerCasterPacketHandler());
		proxy.init();
		FMLCommonHandler.instance().bus().register(MagicFieldFactory.factory);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		MagicConfig.refreshConfigs();
	}

	@EventHandler
	public void serverStarting(FMLServerAboutToStartEvent event)
	{
		registerRegistries();
		StaffHelperFactory.clear();
	}

	@EventHandler
	public void serverStartEvent(FMLServerStartingEvent event)
	{
		CommandRegistry.registerCommands(event);
		MagicFieldFactory.clear();
	}

	private void registerRegistries()
	{
	}
}
