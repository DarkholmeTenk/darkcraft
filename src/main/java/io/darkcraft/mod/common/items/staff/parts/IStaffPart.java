package io.darkcraft.mod.common.items.staff.parts;

import io.darkcraft.mod.common.items.staff.ItemStaffHelper;

public interface IStaffPart
{
	/**
	 * @return a unique identifier which is used to save/load the part
	 */
	public String id();

	public void render();

	/**
	 *
	 * @param helper a helper representing the staff to calculate the power multiplier of
	 * @return a double representing how much the power of a spell going through this component is multiplied by. 1 is normal, 2 is double strength, etc.
	 */
	public double getPowerMult(ItemStaffHelper helper);

	public boolean isDefault();
}
