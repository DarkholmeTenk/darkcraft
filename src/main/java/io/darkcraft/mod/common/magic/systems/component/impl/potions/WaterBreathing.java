package io.darkcraft.mod.common.magic.systems.component.impl.potions;

import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.common.registries.SkillRegistry;

public class WaterBreathing extends AbstractPotionComponent
{

	public WaterBreathing()
	{
		super("waterbreath", "darkcraft.component.waterbreath", 600, SkillRegistry.alteration, new UVStore(0.3,0.4,0.1,0.2));
	}

	@Override
	public double getCost()
	{
		return 5;
	}

	@Override
	public ResourceLocation getProjectileTexture()
	{
		return MagicalRegistry.ff.getProjectileTexture();
	}

	@Override
	public UVStore getProjectileLocation(int frame)
	{
		return MagicalRegistry.ff.getProjectileLocation(frame);
	}

	@Override
	public PotionEffect getPotionEffect(int mag, int dur)
	{
		return new PotionEffect(13, dur * 20);
	}

}
