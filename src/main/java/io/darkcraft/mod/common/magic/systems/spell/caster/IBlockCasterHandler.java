package io.darkcraft.mod.common.magic.systems.spell.caster;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import net.minecraftforge.common.util.ForgeDirection;

public interface IBlockCasterHandler
{
	public double getMaxMana();

	public double getMana();

	/**
	 * @return a location to aim projectile spells at
	 */
	public SimpleDoubleCoordStore getProjectileTarget();

	/**
	 * @return a direction to face for touch spells
	 */
	public ForgeDirection getFacing();
}
