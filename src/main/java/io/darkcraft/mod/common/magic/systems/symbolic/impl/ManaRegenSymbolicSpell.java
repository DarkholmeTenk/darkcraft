package io.darkcraft.mod.common.magic.systems.symbolic.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.mod.common.magic.items.SoulGem;
import io.darkcraft.mod.common.magic.items.SoulGem.Size;
import net.minecraft.item.ItemStack;

public class ManaRegenSymbolicSpell extends AbstractEnchantGemSymbolic
{

	public ManaRegenSymbolicSpell(SimpleCoordStore rootRune, SimpleCoordStore _center)
	{
		super(rootRune, _center);
	}

	@Override
	public void enchant(Size soulSize, ItemStack is)
	{
		SoulGem.setSoulSpell(is, "dc.manaregen");
	}

}
