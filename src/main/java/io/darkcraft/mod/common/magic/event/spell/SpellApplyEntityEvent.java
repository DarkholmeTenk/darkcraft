package io.darkcraft.mod.common.magic.event.spell;

import io.darkcraft.mod.common.magic.caster.ICaster;
import io.darkcraft.mod.common.magic.spell.Spell;
import net.minecraft.entity.Entity;

public class SpellApplyEntityEvent extends SpellApplyEvent
{
	public final Entity entity;
	public SpellApplyEntityEvent(ICaster caster, Spell spell, Entity ent)
	{
		super(caster, spell);
		entity = ent;
	}

}
