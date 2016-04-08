package io.darkcraft.mod.common.magic.tileent;

import io.darkcraft.api.magic.IMagicAnvilRecipe;
import io.darkcraft.darkcore.mod.abstracts.AbstractTileEntity;
import io.darkcraft.darkcore.mod.interfaces.IActivatablePrecise;
import io.darkcraft.mod.common.magic.tileent.guide.IGuidePage;
import io.darkcraft.mod.common.magic.tileent.guide.PageCreator;
import io.darkcraft.mod.common.registries.MagicAnvilRecipeRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class MagicGuide extends AbstractTileEntity implements IActivatablePrecise
{
	private Mode mode = Mode.CONTENTS;
	private IGuidePage[] pages = new IGuidePage[]{new PageCreator()};
	private int page;

	@Override
	public boolean activate(EntityPlayer ent, int side, float x, float y, float z)
	{
		int meta = getBlockMetadata();
		float nx;
		float nz;
		switch(meta)
		{
			case 2: nx=x;nz=z; break;
			case 3: nx=z;nz=1-x; break;
			default: nx=1-x;nz=1-z; break;
			case 1: nx=1-z;nz=x; break;
		}
		if(nx < 0.5)
		{
			if(mode != Mode.CONTENTS)
			{
				if(page <= 0)
				{
					page = 0;
					mode = Mode.values()[mode.ordinal()-1];
				}
				else
					page--;
				sendUpdate();
			}
		}
		if(nx > 0.5)
		{
			if(mode == Mode.CONTENTS)
			{
				mode = Mode.PAGES;
				page = 0;
			}
			else if(mode == Mode.ANVIL)
			{
				if(page < (MagicAnvilRecipeRegistry.getNumRecipes() - 1))
					page++;
			}
			else if(mode == Mode.PAGES)
			{
				page++;
				if(page == pages.length)
				{
					mode = Mode.ANVIL;
					page = 0;
				}
			}
			sendUpdate();
		}
		return false;
	}

	public Mode getMode()
	{
		return mode;
	}

	public int getPage()
	{
		return page;
	}

	public IGuidePage getGuidePage()
	{
		return pages[page % pages.length];
	}

	public IMagicAnvilRecipe getCurrentAnvilRecipe()
	{
		if(mode != Mode.ANVIL) return null;
		return MagicAnvilRecipeRegistry.getRecipe(page);
	}

	@Override
	public void writeTransmittable(NBTTagCompound nbt)
	{
		nbt.setInteger("m", mode.ordinal());
		nbt.setInteger("p", page);
	}

	@Override
	public void readTransmittable(NBTTagCompound nbt)
	{
		mode = Mode.values()[nbt.getInteger("m")];
		page = nbt.getInteger("p");
	}

	public static enum Mode
	{
		CONTENTS,
		PAGES,
		ANVIL;
	}
}
