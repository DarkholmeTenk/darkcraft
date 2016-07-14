package io.darkcraft.mod.common.magic.systems.symbolic.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.helpers.MessageHelper;
import io.darkcraft.darkcore.mod.helpers.OreDictionaryHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.mod.common.magic.blocks.tileent.GemStand;
import io.darkcraft.mod.common.magic.items.MagicScroll;
import io.darkcraft.mod.common.magic.items.SoulGem.Size;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class ScrollWritingSymbolic extends AbstractSpellEnchanterSymbolic
{
	public ScrollWritingSymbolic(SimpleCoordStore rootRune, SimpleCoordStore center)
	{
		super(rootRune, center);
	}

	private boolean usePaper(EntityPlayer pl)
	{
		for(int i = 0; i < pl.inventory.getSizeInventory(); i++)
		{
			ItemStack is = pl.inventory.getStackInSlot(i);
			if(is == null) continue;
			if(OreDictionaryHelper.matches(is, "paper"))
			{
				pl.inventory.decrStackSize(i, 1);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean enchant(PlayerCaster pc, EntityPlayer pl, GemStand gs)
	{
		Size soulSize = gs.getSoulSize();
		Spell spell = pc.getCurrentSpell();
		if((soulSize == null) || (spell == null)) return false;
		double cost = spell.getCost(pc);
		if(pc.useMana(cost, true))
		{
			if(usePaper(pl))
			{
				pc.useMana(cost, false);
				ItemStack scroll = new ItemStack(ItemBlockRegistry.scroll,1);
				MagicScroll.setSpell(scroll, spell, soulSize.powerLevel());
				WorldHelper.giveItemStack(pl, scroll);
			}
		}
		else
			MessageHelper.sendMessage(pl, "dc.message.nomana");
		return false;
	}
}
