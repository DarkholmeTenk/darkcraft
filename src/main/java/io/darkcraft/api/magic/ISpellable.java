package io.darkcraft.api.magic;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;

/**
 * An interface to be implemented by blocks or tile entities to allow them to react to getting hit by spells
 * @author dark
 *
 */
public interface ISpellable
{
	/**
	 * Called when the block/te is hit by a spell
	 * @param block the coordinates of the block that was hit (can't be null)
	 * @param spell the spell that hit the block (can't be null)
	 * @param caster the caster that cast the spell (may be null)
	 * @return true if the block absorbs the spell (blocks any further spell processing) or false if not
	 */
	public boolean spellHit(SimpleCoordStore block, Spell spell, ICaster caster);
}
