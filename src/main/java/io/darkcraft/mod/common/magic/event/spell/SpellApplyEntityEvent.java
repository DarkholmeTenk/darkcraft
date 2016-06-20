package io.darkcraft.mod.common.magic.event.spell;

import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
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
