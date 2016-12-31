package io.darkcraft.mod.common.magic.systems.component.impl.potions;

import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.common.registries.SkillRegistry;

public class Invisibility extends AbstractPotionComponent
{

	public Invisibility()
	{
		super("invisibility", "darkcraft.component.invisibility", 120, SkillRegistry.illusion, new UVStore(0.3, 0.4, 0.4,0.5));
	}

	@Override
	public double getCost()
	{
		return 14;
	}

	@Override
	public ResourceLocation getProjectileTexture(){ return MagicalRegistry.mark.getProjectileTexture(); }

	@Override
	public UVStore getProjectileLocation(int frame){ return MagicalRegistry.mark.getProjectileLocation(frame); }

	@Override
	public PotionEffect getPotionEffect(int mag, int dur)
	{
		return new PotionEffect(14, dur * 20);
	}

}
