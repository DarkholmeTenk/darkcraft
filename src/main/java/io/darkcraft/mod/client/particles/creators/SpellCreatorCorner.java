package io.darkcraft.mod.client.particles.creators;

import net.minecraft.util.ResourceLocation;

import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.datastore.SimpleDoubleCoordStore;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.mod.client.particles.ParticleCreator;
import io.darkcraft.mod.client.particles.movement.Orbit;
import io.darkcraft.mod.client.particles.movement.Towards;
import io.darkcraft.mod.common.magic.blocks.tileent.SpellCreator;

public class SpellCreatorCorner extends ParticleCreator
{
	private final SpellCreator spellCreator;
	private final SimpleDoubleCoordStore  spellCreatorCentre;
	private final SimpleCoordStore columnTop;
	private final SimpleDoubleCoordStore columnTopCenter;
	private final BlockCreator bc;

	public SpellCreatorCorner(int life, SpellCreator creator, int side)
	{
		super(life, creator.coords());
		spellCreator = creator;
		SimpleCoordStore top = creator.coords();
		spellCreatorCentre = top.getCenter();
		switch(side)
		{
			case 0: columnTop = top.translate( 2, 3,  2); break;
			case 1: columnTop = top.translate(-2, 3,  2); break;
			case 2: columnTop = top.translate(-2, 3, -2); break;
			case 3: columnTop = top.translate( 2, 3, -2); break;
			default: columnTop = top;
		}
		columnTopCenter = columnTop.getCenter();
		bc = new BlockCreator(life, columnTop.getWorldObj(), columnTop, columnTop);
		if(creator.getProgress() < 0.5)
			add(bc);
	}

	@Override
	public void setRenderInfo(ResourceLocation rl, UVStore uv, Colour c)
	{
		super.setRenderInfo(rl, uv, c);
		bc.setRenderInfo(rl, uv, c);
	}

	@Override
	public void tick()
	{
		if(rand.nextDouble() < 0.15)
			add(create(particleMaxAge, columnTopCenter, new Towards(spellCreatorCentre, 0.25)));
		if(rand.nextDouble() < 0.25)
		{
			double progress = spellCreator.getProgress();
			if(progress > 0.5)
			{
				double r = rand.nextDouble() * 360;
				double p = 2 - progress;
				double x = MathHelper.sin(r) * p;
				double z = MathHelper.cos(r) * p;
				add(create(particleMaxAge*2, columnTopCenter.translate(x, 0, z), new Orbit(columnTopCenter, 5 + (5 * progress))));
			}
		}
	}

}
