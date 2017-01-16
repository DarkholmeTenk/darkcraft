package io.darkcraft.mod.common.magic.systems.symbolic.impl.effects;

import java.util.List;

import io.darkcraft.darkcore.mod.handlers.EffectHandler;
import io.darkcraft.darkcore.mod.impl.EntityEffectStore;
import io.darkcraft.mod.common.magic.blocks.tileent.MagicRune;
import io.darkcraft.mod.common.magic.systems.effects.SSEffectManaRegen;
import io.darkcraft.mod.common.magic.systems.symbolic.impl.modifiers.Potency;
import io.darkcraft.mod.common.magic.systems.symbolic.type.Modifier;
import io.darkcraft.mod.common.magic.systems.symbolic.type.SymbolicEffect.SymbolicEntityEffect;
import io.darkcraft.mod.common.registries.MagicConfig;
import net.minecraft.entity.EntityLivingBase;

public class ManaRegen extends SymbolicEntityEffect
{
	private int level = 1;

	@Override
	public void tick()
	{
		MagicRune rune = getTileEntity();
		if(rune.tt % 100 != 0)
			return;
		List<EntityLivingBase> entities = getSelector().getAffectedEntities(rune);
		for(EntityLivingBase entity : entities)
		{
			EntityEffectStore ees = EffectHandler.getEffectStore(entity);
			if(ees == null) continue;
			if(rune.useCharge(50 * level, false))
				ees.addEffect(new SSEffectManaRegen(null, entity, level, 100));
		}
	}

	@Override
	public String id()
	{
		return "magicka";
	}

	@Override
	public boolean addModifier(Modifier modifier)
	{
		if(modifier instanceof Potency)
			level = Math.min(level + ((Potency)modifier).potency(), MagicConfig.maxRegenLevel);
		else
			return false;
		return true;
	}

}
