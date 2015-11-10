package io.darkcraft.mod.common.magic.caster;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.mod.common.entities.EntitySpellProjectile;

/**
 * Useless on its own, caster will either be {@link EntityCaster} or {@link BlockCaster}
 * @author dark
 *
 */
public interface ICaster
{
	public double getMana();

	public double getMaxMana();

	public boolean useMana(double amount, boolean sim);

	public SimpleDoubleCoordStore getSpellCreationPos();

	public void setVelocity(EntitySpellProjectile sp);
}
