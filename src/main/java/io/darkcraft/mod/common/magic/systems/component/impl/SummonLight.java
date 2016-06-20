package io.darkcraft.mod.common.magic.systems.component.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.component.INoAreaComponent;
import io.darkcraft.mod.common.magic.systems.spell.caster.ICaster;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import io.darkcraft.mod.common.registries.MagicalRegistry;
import io.darkcraft.mod.common.registries.SkillRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import skillapi.api.implement.ISkill;

public class SummonLight implements IComponent, INoAreaComponent
{

	@Override
	public String id(){ return "conLight"; }

	@Override
	public String getUnlocalisedName(){ return "darkcraft.component.conLight"; }

	@Override
	public ResourceLocation getIcon(){ return MagicalRegistry.componentTex; }

	private final UVStore uv = new UVStore(0.4,0.5,0.1,0.2);
	@Override
	public UVStore getIconLocation(){ return uv; }

	@Override
	public ISkill getMainSkill(){ return SkillRegistry.conjuration; }

	@Override
	public double getCost(){ return 100; }

	@Override
	public void apply(ICaster caster, SimpleCoordStore blockPos, int side, int magnitude, int duration)
	{
		ForgeDirection d = ForgeDirection.VALID_DIRECTIONS[side];
		SimpleCoordStore np = blockPos.getNearby(d);
		if((np.getBlock() == null) || np.getBlock().isAir(np.getWorldObj(), np.x, np.y, np.z))
		{
			np.setBlock(ItemBlockRegistry.magicLight, 0, 3);
		}
		else
			Helper.playFizzleNoise(blockPos.getCenter());
	}

	@Override
	public void apply(ICaster caster, Entity ent, int magnitude, int duration){}

	@Override
	public boolean applyToEnt(){ return false; }

	@Override
	public boolean applyToBlock(){ return true; }

	@Override
	public ResourceLocation getProjectileTexture(){ return MagicalRegistry.projectileTex; }

	private static final UVStore[] uvs = new UVStore[]{new UVStore(0.3,0.4,0.1,0.2)};
	@Override
	public UVStore getProjectileLocation(int f){ return uvs[f%uvs.length]; }

}
