package io.darkcraft.interop.thaumcraft.items;

import java.util.List;

import io.darkcraft.darkcore.mod.abstracts.AbstractItem;
import io.darkcraft.interop.thaumcraft.DarkcraftTC;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.wands.IWandRodOnUpdate;

public class WandRodItem extends AbstractItem implements IWandRodOnUpdate
{
	public WandRodItem()
	{
		super(DarkcraftMod.modName);
		setUnlocalizedName("TC.WandRod");
	}

	@Override
	public void initRecipes()
	{
		// TODO Auto-generated method stub

	}

	private boolean shouldUpdate(ItemStack is)
	{
		if(is.stackTagCompound == null) return false;
		int timer = (is.stackTagCompound.getInteger("tickTimer") + 1) % 10;
		is.stackTagCompound.setInteger("tickTimer", timer);
		return timer == 0;
	}

	@Override
	public void onUpdate(ItemStack is, EntityPlayer player)
	{
		if((is == null) || (player == null)) return;
		PlayerCaster pc = Helper.getPlayerCaster(player);
		if(pc == null) return;
		if(!shouldUpdate(is)) return;
		int max = DarkcraftTC.maxRecharge * 100;
		int absMax = DarkcraftTC.wr.getCapacity() * 100;
		if(is.stackTagCompound.getBoolean("sceptre"))
		{
			max *= 1.5;
			absMax *= 1.5;
		}
		List<Aspect> primals = Aspect.getPrimalAspects();
		for(Aspect a : primals)
		{
			int amount = is.stackTagCompound.getInteger(a.getTag());
			if(amount < max)
			{
				if(pc.useMana(1, false))
					is.stackTagCompound.setInteger(a.getTag(), Math.min(amount+150, max));
			}
			else if(amount > absMax)
				is.stackTagCompound.setInteger(a.getTag(), absMax);
		}
	}

}
