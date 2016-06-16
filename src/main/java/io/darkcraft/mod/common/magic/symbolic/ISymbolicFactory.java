package io.darkcraft.mod.common.magic.symbolic;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import net.minecraft.nbt.NBTTagCompound;

/**
 * This interface allows you to define your own factory for creating symbolic spells.
 * How you choose to handle spells with too many glyphs is entirely up to you.
 *
 * To register your factory, see {@link SymbolicRegistry#registerFactory(ISymbolicFactory)}
 * @author dark
 *
 */
public interface ISymbolicFactory
{
	/**
	 * This method will be called to determine whether your Symbolic Factory has a spell matching the text.
	 * @param glyphs the text used in the runes
	 * @return null if your factory has no matching spells or the spell's ID if it does
	 */
	public String match(String glyphs);

	/**
	 * This method will be called when a spell matching the text has been found and the symbols have all charged up.
	 * @param id the id of the spell the match method has identified.
	 * @param glyphs the text that the runes are spelling
	 * @param rootRune the coordinates of the first rune in the sequence
	 * @param center the location of the center of the glyph
	 * @return null if creation failed (missing item, etc), or the spell to use
	 */
	public ISymbolicSpell createSpell(String id, String glyphs, SimpleCoordStore rootRune, SimpleCoordStore center);

	/**
	 * This method will be called when the root node of the glyph is loaded.
	 * Also called client side with the transmittable NBT data.
	 * @param id the id of the spell that it was using
	 * @param glyphs the text that the runes are spelling
	 * @param nbt the NBT data of the spell, on the server will have access to all data, on the client will only have the trans data
	 * @return an instance of the spell recreated using the nbt data, or null to stop the spell being loaded
	 */
	public ISymbolicSpell loadSpell(String id, String glyphs, NBTTagCompound nbt);
}
