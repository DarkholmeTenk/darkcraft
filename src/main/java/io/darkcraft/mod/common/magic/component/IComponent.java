package io.darkcraft.mod.common.magic.component;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.mod.common.magic.caster.ICaster;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import skillapi.api.implement.ISkill;

/**
 * Should implement {@link IMagnitudeComponent} if the component should have a variable magnitude.
 * Should implement {@link IDurationComponent} if the component should have a variable magnitude.
 * @author dark
 *
 */
public interface IComponent
{
	public String id();

	public ISkill getMainSkill();

	/**
	 * @return the cost of the spell. If this is a magnitude/duration component, the cost will be passed to those methods too.
	 */
	public double getCost();

	/**
	 * Apply the effect of this component to a block.
	 * @param caster the thing which cast the spell containing this component
	 * @param blockPos the position of the block to affect
	 * @param magnitude the magnitude this spell has
	 */
	public void apply(ICaster caster, SimpleCoordStore blockPos, int magnitude);

	/**
	 * Apply the effect of this component to an entity.
	 * @param caster the thing which cast the spell containing this component
	 * @param ent the entity which has been hit by this spell
	 * @param magnitude the magnitude this spell has
	 */
	public void apply(ICaster caster, Entity ent, int magnitude);

	public ResourceLocation getProjectileTexture();
}
