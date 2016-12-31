package io.darkcraft.mod.common.magic.systems.component.impl.potions;

import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.common.registries.SkillRegistry;

public class Nightvision extends AbstractPotionComponent
{

	public Nightvision()
	{
		super("nightvision", "darkcraft.component.nightvision", 600, SkillRegistry.illusion, new UVStore(0.3, 0.4, 0.5,0.6));
	}

	@Override
	public double getCost()
	{
		return 8;
	}

	@Override
	public ResourceLocation getProjectileTexture(){ return MagicalRegistry.mark.getProjectileTexture(); }

	@Override
	public UVStore getProjectileLocation(int frame){ return MagicalRegistry.mark.getProjectileLocation(frame); }

	@Override
	public PotionEffect getPotionEffect(int mag, int dur)
	{
		return new PotionEffect(16, dur * 20);
	}

}
