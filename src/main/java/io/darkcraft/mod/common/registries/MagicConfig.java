package io.darkcraft.mod.common.registries;

import io.darkcraft.darkcore.mod.config.ConfigFile;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.items.SoulGem;
import io.darkcraft.mod.common.magic.items.SoulGem.Size;
import io.darkcraft.mod.common.magic.tileent.MagicVortexCrystal;

public class MagicConfig
{
	public static ConfigFile	config;
	public static double		xpCostMult		= 0.1;
	public static double		minCostMult		= 0.5;
	public static double		maxCostMult		= 1.5;
	public static double		projectileSpeed	= .8;

	public static double		fieldDecay		= 0.95;
	public static double		fieldDecrease	= 0.1;
	public static int			fieldTicks		= 20;
	public static int			fieldSize		= 5;
	public static boolean		fieldSizeY		= false;
	public static double		fieldMax		= 100;

	public static int 			magicVortexLavaRate = 6000;
	public static int			magicVortexSpawnRate = 1200;
	public static int			magicVortexSpawnRad = 35;
	public static int			magicVortexSpawnMax = 45;
	public static int			magicVortexNumRings = 30;

	public static int			maxSkillXPFromSpell = 25;
	public static int			maxSkillsXPFromSpell = 3;

	public static int maxMana = 100000;
	public static int minMana = 100;
	public static double manaRegenRate = 0.005;
	public static boolean regenPercent = true;

	public static boolean		traceLiquids = false;
	public static int			touchCastDistance = 5;
	public static boolean		castWithHand = true;

	public static boolean		deity = true;

	//COMPONENT BALANCE
	public static ConfigFile	cbConfig;
	public static double fortuneMult = 6;
	public static int fortuneLevel = 3;
	public static double silkMult = 3;
	public static int[] soulHealthSizes = new int[]{0,5,15,30,45};
	public static boolean recallOthers = true;

	public static void refreshConfigs()
	{
		if (config == null) config = DarkcraftMod.configHandler.registerConfigNeeder("Magic");
		xpCostMult = config.getDouble("spell xp mult", 0.1, "This is the value the spell cost is multiplied by to work out how much xp to award");
		minCostMult = config.getDouble("min spell cost mult", 0.5, "The lowest amount of the original spell cost that can be reached by player being skilled in that skill");
		maxCostMult = config.getDouble("max spell cost mult", 1.5, "The maximum amount of the original spell cost that can be reached by player being unskilled in that skill");
		projectileSpeed = config.getDouble("projectile speed", .8);

		fieldDecay = MathHelper.clamp(config.getDouble("field decay multiplier", 0.95, "Every magic field tick, the current field strength is multiplied by this","This has to be between 0 and 1"),0,1);
		fieldDecrease = config.getDouble("field decay const", 0.1, "A static amount for the field to decrease by every field tick, after the multiplier is taken into account");
		fieldTicks = Math.max(1, config.getInt("field tick rate", 20, "Number of regular ticks per field tick","Must be at least 1"));
		fieldSize = MathHelper.clamp(config.getInt("field size", 5, "This value affects the size of each chunk of the magic field","The size of each chunk is 2^this value","Min: 2, Max:8"), 2, 8);
		fieldSizeY = config.getBoolean("field size y", false, "If false, the magic field will be the same no matter your y value","If true, the field will be divided into y chunks too");
		fieldMax = config.getDouble("field max", 100, "Sets the maximum value the field can take");
		MagicVortexCrystal.setSpawnData(config.getString("magic vortex - crystal spawn items", "minecraft:diamond#2#0#4,minecraft:diamond#1#0#8,minecraft:diamond#5#0#1", "Config for stuff the magic vortex should spawn","Comma separated list in the form 'itemID#count#metadata#weight'"));
		magicVortexLavaRate = config.getInt("magic vortex - lava check rate", 6000, "How many ticks between the vortex repairing lava lakes");
		magicVortexSpawnRate = config.getInt("magic vortex - crystal spawn rate", 1200, "Number of ticks between vortex spawning new crystals");
		magicVortexSpawnRad = config.getInt("magic vortex - spawn radius", 35, "radius in which crystals will spawn");
		magicVortexSpawnMax = config.getInt("magic vortex - spawn max", 45, "Maximum number of magic crystals the vortex will maintain");
		magicVortexNumRings = config.getInt("magic vortex - num rings", 30, "Number of rings to render");

		deity = config.getBoolean("Deity - Enabled", true, "Is the deity system enabled");
		manaRegenRate = config.getDouble("Mana - Regen rate", 0.005, "How much mana a player regens per second","If Regen Percent is true, this is the percent of the player's mana that gets regen (with 1 being all)","Else this is the amount of mana that regens per second");
		regenPercent = config.getBoolean("Mana - Regen percent", true, "If true, regen an amount of mana proportional to max mana","Else regen a flat amount");

		if(cbConfig == null) cbConfig = DarkcraftMod.configHandler.registerConfigNeeder("Magic Component Balance");
		fortuneMult = cbConfig.getDouble("Fortune cost multiplier", 6, "Configures how much of a cost increase fortune is on excavate");
		fortuneLevel = cbConfig.getInt("Fortune level", 3, "The level of fortune fortune spells have");
		silkMult = cbConfig.getDouble("Silk cost multiplier", 3, "Configures how much of a cost increase silk touch is on excavate");
		recallOthers = cbConfig.getBoolean("Can recall others", true, "If this is true, you can cast the recall spells on any living entity");

		setSoulSizes();
	}

	private static void setSoulSizes()
	{
		Size[] sizes = SoulGem.Size.values();
		int[] soulSizes = new int[sizes.length-1];
		for(int i = 0; i < (sizes.length-1); i++)
		{
			Size s = sizes[i];
			soulSizes[i] = config.getInt("Soul level - " + s.name(), soulHealthSizes[i], "Amount of health a creature needs to have a " + s.name() + " sized soul");
		}
		soulHealthSizes = soulSizes;
	}
}
