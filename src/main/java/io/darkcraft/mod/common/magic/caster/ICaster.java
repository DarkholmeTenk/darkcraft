package io.darkcraft.mod.common.magic.caster;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.mod.common.magic.spell.Spell;
import net.minecraft.entity.Entity;

/**
 * Useless on its own, caster will either be {@link EntityCaster} or {@link BlockCaster}
 * @author dark
 *
 */
public interface ICaster
{
	public SimpleDoubleCoordStore getCoords();

	public double getMana();

	public double getMaxMana();

	public boolean useMana(double amount, boolean sim);

	public void cast(Spell spell);

	public void cast(Spell spell, SimpleCoordStore block);

	public void cast(Spell spell, Entity ent);
}
