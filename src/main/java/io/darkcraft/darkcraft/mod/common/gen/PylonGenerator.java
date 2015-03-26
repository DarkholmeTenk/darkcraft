package io.darkcraft.darkcraft.mod.common.gen;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.helpers.MultiBlockHelper;
import io.darkcraft.darkcraft.mod.DarkcraftMod;
import io.darkcraft.darkcraft.mod.common.multiblock.PylonStructure;

import java.util.Random;

import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.common.IWorldGenerator;

public class PylonGenerator implements IWorldGenerator
{
	
	private void generatePylon(Random r, World w, int x, int z)
	{
		MultiBlockHelper.generateAtFloor(PylonStructure.i, w,new SimpleCoordStore(w, x << 4,255,z<<4), ForgeDirection.NORTH);
		//MultiBlockHelper.generateStructure(PylonStructure.i, pos, dir);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkProvider chunkGenerator,
			IChunkProvider chunkProvider)
	{
		if(random.nextDouble() < DarkcraftMod.wgh.pylonChance)
			generatePylon(random,world,chunkX,chunkZ);
	}

}
