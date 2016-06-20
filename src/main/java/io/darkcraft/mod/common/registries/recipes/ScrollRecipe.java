package io.darkcraft.mod.common.registries.recipes;

import io.darkcraft.api.magic.IMagicAnvilRecipe;
import io.darkcraft.darkcore.mod.helpers.MessageHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.mod.common.magic.blocks.tileent.MagicAnvil;
import io.darkcraft.mod.common.magic.items.MagicScroll;
import io.darkcraft.mod.common.magic.items.SoulGem;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class ScrollRecipe implements IMagicAnvilRecipe
{
	@Override
	public String id()
	{
		return "dc.rec.scroll";
	}

	@Override
	public boolean isValid(MagicAnvil anvil, ItemStack[] input, Spell spell)
	{
		boolean empty,soul,paper;
		empty=soul=paper=false;
		for(ItemStack is : input)
		{
			if(is == null){ empty = true; continue; }
			if(is.stackSize > 1) return false;
			if(is.getItem() == Items.paper) paper = true;
			if(SoulGem.getGemSize(is) != null) soul = true;
		}
		return empty && soul && paper;
	}

	@Override
	public ItemStack[] craft(MagicAnvil anvil, ICaster caster, ItemStack[] items, Spell spell)
	{
		SoulGem.Size s = null;
		for(int i = 0; i < items.length; i++)
			if(SoulGem.getSoulSize(items[i]) != null)
				s = SoulGem.getSoulSize(items[i]);
		if(s == null)
		{
			if((caster instanceof PlayerCaster) && ServerHelper.isServer())
			{
				EntityPlayer pl = ((PlayerCaster)caster).getCaster();
				if(pl != null)
					MessageHelper.sendMessage(pl, MagicAnvil.emptySoulMessage);
			}
			return null;
		}
		int uses = s.powerLevel();
		ItemStack scroll = new ItemStack(ItemBlockRegistry.scroll,1);
		MagicScroll.setSpell(scroll, spell, uses);
		return new ItemStack[]{null,scroll,null};
	}

	@Override
	public void craftingDone(MagicAnvil anvil, ICaster caster)
	{

	}

	private static ItemStack[] desired = new ItemStack[]{new ItemStack(Items.paper),null,new ItemStack(ItemBlockRegistry.soulGem)};
	@Override
	public ItemStack[] getDesiredItems(){ return desired; }

	@Override
	public IComponent[] getDesiredComponent(){ return null; }

	private static ItemStack[] out = new ItemStack[]{null,new ItemStack(ItemBlockRegistry.scroll,1), null};
	@Override
	public ItemStack[] getExpectedOutput(){ return out; }

	@Override
	public boolean isHidden()
	{
		return false;
	}

}
