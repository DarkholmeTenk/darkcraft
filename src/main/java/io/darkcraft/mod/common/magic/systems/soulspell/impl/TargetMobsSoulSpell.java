package io.darkcraft.mod.common.magic.systems.soulspell.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.mod.common.magic.blocks.tileent.GemStand;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.magic.systems.spell.caster.BlockCaster;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.nbt.NBTTagCompound;

public class TargetMobsSoulSpell extends TargetEntsAbstractSoulSpell
{
	private Spell spell;
	private GemStand stand;

	public TargetMobsSoulSpell(NBTTagCompound nbt)
	{
		if(nbt.hasKey("spell"))
			spell = Spell.readFromNBT(nbt.getCompoundTag("spell"));
	}

	@Override
	public String id()
	{
		return "dc.targetmobs";
	}

	@Override
	public void tick(BlockCaster caster)
	{
		if(ServerHelper.isClient()) return;
		if((spell == null) || (stand == null)) return;
		if((stand.tt % 20) != 0) return;
		EntityLivingBase ent = (EntityLivingBase) getNearestEntity(caster, stand);
		if(ent == null) return;
		SimpleDoubleCoordStore t = new SimpleDoubleCoordStore(ent).translate(0, ent.getEyeHeight(), 0);
		stand.setProjectileTarget(t);
		caster.cast(spell, true);
	}

	@Override
	public boolean shouldTarget(Entity e)
	{
		if(!(e instanceof EntityLivingBase)) return false;
		if((e instanceof IMob) || (e instanceof EntityMob)) return true;
		return false;
	}

	@Override
	public void setGemStand(GemStand _stand)
	{
		stand = _stand;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		// TODO Auto-generated method stub

	}

}
