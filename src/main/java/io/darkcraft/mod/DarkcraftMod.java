package io.darkcraft.mod;

import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;

import io.darkcraft.darkcore.mod.DarkcoreMod;
import io.darkcraft.darkcore.mod.config.ConfigHandler;
import io.darkcraft.darkcore.mod.config.ConfigHandlerFactory;
import io.darkcraft.darkcore.mod.interfaces.IConfigHandlerMod;
import io.darkcraft.darkcore.mod.nbt.NBTHelper;
import io.darkcraft.interop.Interop;
import io.darkcraft.mod.common.magic.field.MagicFieldFactory;
import io.darkcraft.mod.common.magic.items.staff.StaffHelperFactory;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.network.ChalkGuiPacketHandler;
import io.darkcraft.mod.common.network.PlayerCasterPacketHandler;
import io.darkcraft.mod.common.network.SpellCreationPacketHandler;
import io.darkcraft.mod.common.network.SpellSelectionPacketHandler;
import io.darkcraft.mod.common.registries.CommandRegistry;
import io.darkcraft.mod.common.registries.DarkcraftCreativeTabs;
import io.darkcraft.mod.common.registries.EntRegistry;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import io.darkcraft.mod.common.registries.MagicAnvilRecipeRegistry;
import io.darkcraft.mod.common.registries.MagicConfig;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.proxy.CommonProxy;
import io.darkcraft.mod.proxy.ParticleHandler;

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

@Mod(modid = "darkcraft", name = "Darkcraft", version = "0.15", dependencies = "required-after:darkcore@[0.42,0.499]; required-after:SkillAPI; after:Thaumcraft")
public class DarkcraftMod implements IConfigHandlerMod
{
	public static DarkcraftMod				i;
	public static final String				modName					= "darkcraft";
	public static Random					modRand					= new Random();
	public static ConfigHandler				configHandler			= null;
	public static CreativeTabs				tab						= new DarkcraftCreativeTabs();
	public static boolean					inited					= false;

	@SidedProxy(	clientSide = "io.darkcraft.mod.proxy.ClientProxy",
					serverSide = "io.darkcraft.mod.proxy.CommonProxy")
	public static CommonProxy				proxy;

	@SidedProxy(	clientSide = "io.darkcraft.mod.proxy.ParticleHandler$ClientParticleHandler",
					serverSide = "io.darkcraft.mod.proxy.ParticleHandlerImpl")
	public static ParticleHandler			particle;

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
		DarkcoreMod.registerCreativeTab(modName, tab);
		configHandler = ConfigHandlerFactory.getConfigHandler(this);
		ItemBlockRegistry.registerBlocks();
		ItemBlockRegistry.registerItems();
		ItemBlockRegistry.registerRecipes();
		MagicalRegistry.registerMagic();
		EntRegistry.registerEntities();
		FMLInterModComms.sendMessage("SkillAPI", "register", "io.darkcraft.mod.common.registries.SkillRegistry.requestAPI");
		Interop.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(i, proxy);
		DarkcoreMod.packetHandler.registerHandler(SpellCreationPacketHandler.disc, new SpellCreationPacketHandler());
		DarkcoreMod.packetHandler.registerHandler(PlayerCasterPacketHandler.disc, new PlayerCasterPacketHandler());
		DarkcoreMod.packetHandler.registerHandler(ChalkGuiPacketHandler.disc, new ChalkGuiPacketHandler());
		DarkcoreMod.packetHandler.registerHandler(SpellSelectionPacketHandler.disc, new SpellSelectionPacketHandler());
		NBTHelper.register(Spell.class, Spell.spellMapper);
		Interop.init();
		proxy.init();
		FMLCommonHandler.instance().bus().register(MagicFieldFactory.factory);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		MagicConfig.refreshConfigs();
		MagicAnvilRecipeRegistry.postInit();
		Interop.postInit();
		inited = true;
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
