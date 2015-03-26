package io.darkcraft.darkcraft.mod;

import java.util.Random;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.config.ConfigHandler;
import io.darkcraft.darkcore.mod.config.ConfigHandlerFactory;
import io.darkcraft.darkcore.mod.interfaces.IConfigHandlerMod;
import io.darkcraft.darkcraft.mod.common.CommonProxy;
import io.darkcraft.darkcraft.mod.common.command.CreateSpellCommand;
import io.darkcraft.darkcraft.mod.common.gen.WorldGenerationHandler;
import io.darkcraft.darkcraft.mod.common.registries.ItemBlockRegistry;
import io.darkcraft.darkcraft.mod.common.registries.SpellComponentRegistry;
import io.darkcraft.darkcraft.mod.common.registries.SpellInstanceRegistry;
import io.darkcraft.darkcraft.mod.common.spellsystem.MagicDamageSource;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;

@Mod(modid = "darkcraft", version = "0.1", dependencies = "required-after:darkcore")
public class DarkcraftMod implements IConfigHandlerMod
{
	public static Random					modRand					= new Random();
	public static ConfigHandler				configHandler			= null;
	public static SpellInstanceRegistry		spellInstanceRegistry	= null;
	public static MagicDamageSource			damageSource			= new MagicDamageSource();
	public static WorldGenerationHandler	wgh;

	@SidedProxy(clientSide = "io.darkcraft.darkcraft.mod.client.ClientProxy",
			serverSide = "io.darkcraft.darkcraft.mod.common.CommonProxy")
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
		SpellComponentRegistry.registerComponents();
		registerCommands();
	}

	private void registerCommands()
	{
		DarkcoreMod.comHandler.addCommand(new CreateSpellCommand());
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		proxy.init();
		wgh = new WorldGenerationHandler();
		wgh.registerWorldGenerators();
	}

	@EventHandler
	public void serverStarting(FMLServerAboutToStartEvent event)
	{
		registerRegistries();
	}

	private void registerRegistries()
	{
		if (spellInstanceRegistry != null)
			FMLCommonHandler.instance().bus().unregister(spellInstanceRegistry);
		spellInstanceRegistry = new SpellInstanceRegistry();
		FMLCommonHandler.instance().bus().register(spellInstanceRegistry);
	}
}
