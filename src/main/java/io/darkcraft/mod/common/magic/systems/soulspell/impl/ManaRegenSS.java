package io.darkcraft.mod.common.magic.systems.soulspell.impl;

import java.util.List;

import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;
import io.darkcraft.mod.common.magic.blocks.tileent.GemStand;
import io.darkcraft.mod.common.magic.systems.effects.SSEffectManaRegen;
import io.darkcraft.mod.common.magic.systems.soulspell.ISoulSpell;
import io.darkcraft.mod.common.magic.systems.spell.caster.BlockCaster;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

public class ManaRegenSS implements ISoulSpell
{
	private int tt;
	private GemStand gemstand;
	private AxisAlignedBB aabb;

	@Override
	public String id()
	{
		return "dc.manaregen";
	}

	private void applyNearby()
	{
		if(ServerHelper.isClient()) return;
		if(gemstand == null) return;
		if((gemstand != null) && (aabb == null))
			aabb = gemstand.coords().getCenter().getAABB(9.5);
		int size = gemstand.getSoulSize().ordinal();
		List list = gemstand.getWorldObj().getEntitiesWithinAABB(EntityLivingBase.class, aabb);
		for(Object o : list)
		{
			if(!(o instanceof EntityLivingBase)) continue;
			EntityLivingBase ent = (EntityLivingBase) o;
			EntityEffectStore ees = EffectHandler.getEffectStore(ent);
			if(ees == null) continue;
			ees.addEffect(new SSEffectManaRegen(null, ent, size, 100));
		}
	}

	@Override
	public void tick(BlockCaster caster)
	{
		if((tt % 20) == 0)
		{
			applyNearby();
		}
		tt++;
	}

	@Override
	public void setGemStand(GemStand stand)
	{
		gemstand = stand;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt){}

	@Override
	public void readFromNBT(NBTTagCompound nbt){}

}
