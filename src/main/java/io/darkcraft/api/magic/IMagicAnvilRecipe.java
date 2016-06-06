package io.darkcraft.api.magic;

import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.magic.component.IComponent;
import io.darkcraft.mod.common.magic.spell.Spell;
import io.darkcraft.mod.common.magic.tileent.MagicAnvil;
import net.minecraft.item.ItemStack;

/**
 * Implement to create anvil recipes. All ItemStack[]s are of size 3.
 * Can be registered by calling {@link io.darkcraft.mod.common.registries.MagicAnvilRecipeRegistry#addRecipe MagicAnvilRecipeRegistry.addRecipe} before postInit
 * @author dark
 */
public interface IMagicAnvilRecipe
{
	/**
	 * Used to sort recipes for the guide
	 */
	public String id();

	/**
	 * Used to determine which recipe to use
	 * @param anvil the anvil where the crafting is occurring.
	 * @param input the items which have been input.
	 * @param spell the spell which has been cast.
	 * @return true if valid, false otherwise.
	 */
	public boolean isValid(MagicAnvil anvil, ItemStack[] input, Spell spell);

	/**
	 * Attempt to perform the crafting. This will only be performed if isValid has returned true.
	 * @param caster the caster who cast the spell
	 * @param items the items which are in the anvil
	 * @param spell the spell which was cast on the anvil
	 * @return the ItemStack[] to use. No crafting will be done if input is exactly == to output or output == null.
	 */
	public ItemStack[] craft(MagicAnvil anvil, ICaster caster, ItemStack[] items, Spell spell);

	/**
	 * Called when the crafting is done, so you can give some xp to the crafter or something.
	 * Will not be called if anvil is unload and reloaded mid craft. Entity may no longer exist.
	 * @param anvil the anvil where the crafting took place
	 * @param caster the ICaster which performed the crafting
	 */
	public void craftingDone(MagicAnvil anvil, ICaster caster);

	/**
	 * Used for displaying all recipes.
	 * @return an array containing the items you expect to be part of the recipe.
	 */
	public ItemStack[] getDesiredItems();

	/**
	 * Used for displaying all recipes.
	 * @return an array containing the spell effects expected to be part of the recipe.
	 */
	public IComponent[] getDesiredComponent();

	/**
	 * Used for displaying all recipes.
	 * @return an array containing all the items you expect to get out of the recipe.
	 */
	public ItemStack[] getExpectedOutput();

	/**
	 * Used to hide the recipe from recipe displays. If true, none of the expected/desired functions should be called.
	 * @return true if the recipe should be hidden, false otherwise.
	 */
	public boolean isHidden();
}
