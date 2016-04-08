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

	/**
	 * Add mana to the caster's pool
	 * @param amount the amount of mana to add
	 * @param sim if true, simulates adding but does not actually add
	 * @return the amount of mana from amount that did not get added. I.e. 0 means all mana was added
	 */
	public double addMana(double amount, boolean sim);

	public void cast(Spell spell, boolean useMana);

	public void cast(Spell spell, SimpleCoordStore block, boolean useMana);

	public void cast(Spell spell, Entity ent, boolean useMana);
}
