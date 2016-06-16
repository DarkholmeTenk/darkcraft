package io.darkcraft.mod.common.magic.caster;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import net.minecraftforge.common.util.ForgeDirection;

public interface IBlockCasterHandler
{
	/**
	 * @return a location to aim projectile spells at
	 */
	public SimpleDoubleCoordStore getProjectileTarget();

	/**
	 * @return a direction to face for touch spells
	 */
	public ForgeDirection getFacing();
}
