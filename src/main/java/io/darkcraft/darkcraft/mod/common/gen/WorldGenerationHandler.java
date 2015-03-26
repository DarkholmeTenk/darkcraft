package io.darkcraft.darkcraft.mod.common.gen;

import cpw.mods.fml.common.registry.GameRegistry;
import io.darkcraft.darkcore.mod.config.CType;
import io.darkcraft.darkcore.mod.config.ConfigFile;
import io.darkcraft.darkcore.mod.config.ConfigItem;
import io.darkcraft.darkcraft.mod.DarkcraftMod;

public class WorldGenerationHandler
{
	ConfigFile file = null;
	
	public final double pylonChance;
	
	private PylonGenerator pylon = new PylonGenerator();
	
	{
		file = DarkcraftMod.configHandler.registerConfigNeeder("worldGen");
		pylonChance = file.getConfigItem(new ConfigItem("Pylon Generation Chance", CType.DOUBLE, 0.01,
				"The default chance (out of 1) for a pylon to generate in a chunk",
				"Default: 0.01")).getDouble();
	}
	
	public void registerWorldGenerators()
	{
		GameRegistry.registerWorldGenerator(pylon, 100);
	}
}
