package io.darkcraft.mod.common.magic.systems.soulspell.impl;

import io.darkcraft.mod.common.magic.systems.soulspell.ISoulSpell;
import io.darkcraft.mod.common.magic.systems.soulspell.ISoulSpellFactory;
import net.minecraft.nbt.NBTTagCompound;

public class DarkcraftSoulSpellFactory implements ISoulSpellFactory
{

	@Override
	public ISoulSpell createSoulSpell(String id, NBTTagCompound nbt)
	{
		if(id.equals("dc.targetmobs"))
			return new TargetMobsSoulSpell(nbt);
		if(id.equals("dc.manaregen"))
			return new ManaRegenSS();
		return null;
	}

}
