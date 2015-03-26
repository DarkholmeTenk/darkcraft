package io.darkcraft.darkcraft.mod.common.multiblock;

import io.darkcraft.darkcore.mod.multiblock.BlockState;
import io.darkcraft.darkcore.mod.multiblock.IMultiBlockStructure;
import io.darkcraft.darkcraft.mod.common.registries.ItemBlockRegistry;

public class PylonStructure implements IMultiBlockStructure
{
	public static PylonStructure i = new PylonStructure();
	private static BlockState[][][] structure;
	
	static
	{
		BlockState core = ItemBlockRegistry.pylonCoreBlock.getBlockState();
		BlockState panel = ItemBlockRegistry.pylonPanelBlock.getBlockState();
		BlockState block = ItemBlockRegistry.pylonBlock.getBlockState();
		
		BlockState[][] pillar = new BlockState[][]{
			null,
			null,
			{null,null,block,null,null},
			null,
			null};
		BlockState[][] second = new BlockState[][]{
				{block,null,null,null,block},
				null,
				null,
				null,
				{block,null,null,null,block}};
		BlockState[][] floor = new BlockState[][]{
				{block,null,panel,null,block},
				null,
				{panel,null,core,null,panel},
				null,
				{block,null,panel,null,block}};
		structure = new BlockState[][][]{
				floor,
				second,
				pillar,
				pillar,
				pillar
		};
	}
	
	@Override
	public BlockState[][][] getStructureDefinition()
	{
		return structure;
	}

	@Override
	public int getCoreX()
	{
		return 2;
	}

	@Override
	public int getCoreY()
	{
		return 0;
	}

	@Override
	public int getCoreZ()
	{
		return 2;
	}

}
