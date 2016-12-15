package io.darkcraft.mod.common.magic.blocks.tileent;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

import io.darkcraft.api.magic.IStaffable;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.helpers.MessageHelper;
import io.darkcraft.darkcore.mod.helpers.MultiBlockHelper;
import io.darkcraft.darkcore.mod.helpers.PlayerHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.interfaces.IActivatable;
import io.darkcraft.darkcore.mod.multiblock.AirBlockState;
import io.darkcraft.darkcore.mod.multiblock.BlockState;
import io.darkcraft.darkcore.mod.multiblock.IBlockState;
import io.darkcraft.darkcore.mod.multiblock.IMultiBlockStructure;
import io.darkcraft.darkcore.mod.nbt.NBTProperty;
import io.darkcraft.darkcore.mod.nbt.NBTProperty.SerialisableType;
import io.darkcraft.darkcore.mod.nbt.NBTSerialisable;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.abstracts.AbstractMFTileEntity;
import io.darkcraft.mod.client.renderer.gui.SpellCreationGui;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.items.staff.StaffHelper;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import io.darkcraft.mod.common.magic.systems.spell.caster.EntityCaster;
import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@NBTSerialisable
public class SpellCreator extends AbstractMFTileEntity implements IActivatable, IStaffable
{
	@NBTProperty
	private double currentCost = 0;
	@NBTProperty
	private double totalCost = 0;

	@NBTProperty({SerialisableType.WORLD})
	private Spell currentSpell = null;
	@NBTProperty({SerialisableType.WORLD})
	private String currentSpellOwner = null;

	public void closeGui()
	{
	}

	@Override
	public boolean activate(EntityPlayer ent, int side)
	{
		if(ServerHelper.isServer())
		{
			if(ent.getHeldItem() != null) return false;
			if(!hasSpell())
			{
				if(isValidStructure())
					ent.openGui(DarkcraftMod.i, SpellCreationGui.GUI_ID, worldObj, xCoord, yCoord, zCoord);
				else
					MessageHelper.sendMessage(ent, "dc.message.creator.invalid");
			}
		}
		return true;
	}

	@Override
	public boolean staffActivate(EntityPlayer player, StaffHelper helper)
	{
		if(!hasSpell) return false;
		EntityPlayer own = PlayerHelper.getPlayer(currentSpellOwner);
		String un = PlayerHelper.getUsername(player);
		if((own == null ) || ((currentSpellOwner != null) && currentSpellOwner.equals(un)))
		{
			setSpell(un,null);
			MessageHelper.sendMessage(player, "dc.message.creator.cleared");
			return true;
		}
		return false;
	}

	public boolean isValidStructure()
	{
		return validStructure;
	}

	public boolean isUser(EntityPlayer pl)
	{
		if(currentSpellOwner == null) return false;
		String un = PlayerHelper.getUsername(pl);
		return un.equals(currentSpellOwner);
	}

	public void setSpell(String un, Spell spell)
	{
		EntityPlayer pl = PlayerHelper.getPlayer(un);
		if(spell == null)
		{
			hasSpell = false;
			currentSpell = null;
			currentSpellOwner = null;
			currentCost = totalCost = 0;
			hasUser = new String[]{null,null,null,null};
			markDirty();
			sendUpdate();
		}
		else if(!hasSpell())
		{
			hasSpell = true;
			currentSpell = spell;
			currentSpellOwner = un;
			hasUser = new String[]{null,null,null,null};
			currentCost = totalCost = spell.getCost(null) * 10;
			markDirty();
			sendUpdate();
		}
		else
		{
			MessageHelper.sendMessage(pl, "dc.message.creator.inUse");
		}
	}

	@NBTProperty({SerialisableType.TRANSMIT})
	private boolean hasSpell = false;
	public boolean hasSpell()
	{
		if(ServerHelper.isClient())
			return hasSpell;
		return (currentSpell != null) && (currentSpellOwner != null);
	}

	@NBTProperty
	private boolean validStructure = false;

	@NBTProperty
	public String[] hasUser = new String[]{null,null,null,null};

	@Override
	public void tick()
	{
		if((tt % 100) == 1)
		{
			boolean lvi = validStructure;
			validStructure = MultiBlockHelper.isMultiblockValid(this, SpellCreatorStructure.i);
			if(lvi != validStructure)
				sendUpdate();
		}
		if(ServerHelper.isServer())
			if(isValidStructure() && hasSpell() && ((tt%10) == 0))
				checkNearbyPlayers();
		if(((tt % 20) == 0) && hasSpell && ServerHelper.isClient())
			DarkcraftMod.particle.createSpellCreateParticles(coords(), hasUser);
	}

	private void checkNearbyPlayers()
	{
		boolean ownerIn = false;
		SimpleCoordStore me = coords();
		for(ForgeDirection fd : ForgeDirection.VALID_DIRECTIONS)
		{
			if((fd == ForgeDirection.UP) || (fd == ForgeDirection.DOWN)) continue;
			int i = fd.ordinal() - 2;
			SimpleCoordStore checkBlock = me.getNearby(fd).getNearby(fd);
			AxisAlignedBB aabb = checkBlock.getAABB(2);
			List<EntityPlayer> l = worldObj.getEntitiesWithinAABB(EntityPlayer.class, aabb);
			if(l.size() > 0)
			{
				String n = PlayerHelper.getUsername(l.get(0));
				hasUser[i] = n;
				if(currentSpellOwner.equals(n))
					ownerIn = true;
			}
			else
				hasUser[i] = null;
		}
		if(ownerIn)
		{
			for(String s : hasUser)
			{
				if(currentCost <= 0) break;
				if(s == null) continue;
				EntityPlayer pl = PlayerHelper.getPlayer(s);
				if(pl == null) continue;
				EntityCaster c = Helper.getCaster(pl);
				double am = Math.min(Math.max(20,currentSpell.getCost(null)/4), currentCost);
				if(pl.capabilities.isCreativeMode) am = currentCost;
				if(pl.capabilities.isCreativeMode || c.useMana(am, false))
					currentCost -= am;
			}
			if(currentCost <= 0)
			{
				NBTTagCompound nbt = new NBTTagCompound();
				currentSpell.writeToNBT(nbt);
				for(String s : hasUser)
				{
					if(s == null) continue;
					EntityPlayer pl = PlayerHelper.getPlayer(s);
					if(pl == null) continue;
					PlayerCaster c = Helper.getPlayerCaster(pl);
					Spell spell = Spell.readFromNBT(nbt);
					c.learnSpell(spell);
				}
				setSpell(null,null);
			}
		}
		else
		{
			for(int i = 0; i < 4; i++)hasUser[i]=null;
		}
		sendUpdate();
	}

	public double getProgress()
	{
		if((!hasSpell()) || (totalCost == 0))
			return -1;
		return (totalCost - currentCost) / totalCost;
	}

	/*
	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		if(currentSpell != null)
		{
			NBTTagCompound snbt = new NBTTagCompound();
			currentSpell.writeToNBT(snbt);
			nbt.setTag("cs", snbt);
			nbt.setString("cso", currentSpellOwner);
			nbt.setDouble("cc", currentCost);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		if(nbt.hasKey("cs"))
		{
			currentSpell = Spell.readFromNBT(nbt.getCompoundTag("cs"));
			currentSpellOwner = nbt.getString("cso");
			currentCost = nbt.getDouble("cc");
		}
	}

	@Override
	public void writeTransmittable(NBTTagCompound nbt)
	{
		super.writeTransmittable(nbt);
		for(int i = 0; i < 4; i++)
			if(hasUser[i] != null)
				nbt.setString("hu"+i, hasUser[i]);
	}

	@Override
	public void readTransmittable(NBTTagCompound nbt)
	{
		super.readTransmittable(nbt);
		for(int i = 0; i < 4; i++)
			hasUser[i] = nbt.hasKey("hu"+i) ? nbt.getString("hu"+i) : null;
	}

	@Override
	public void writeTransmittableOnly(NBTTagCompound nbt)
	{
		nbt.setBoolean("hasSpell", hasSpell());
	}

	@Override
	public void readTransmittableOnly(NBTTagCompound nbt)
	{
		hasSpell = nbt.getBoolean("hasSpell");
	}*/

	public static IMultiBlockStructure getStructure()
	{
		return SpellCreatorStructure.i;
	}

	private static class SpellCreatorStructure implements IMultiBlockStructure
	{
		public static final SpellCreatorStructure i = new SpellCreatorStructure();
		private static IBlockState t = new BlockState(ItemBlockRegistry.spellCreatorBlock);
		private static IBlockState b = new BlockState(Blocks.stonebrick);
		private static IBlockState c = new BlockState(Blocks.stonebrick,3);
		private static IBlockState a = AirBlockState.i;
		public static IBlockState[][] floor = new IBlockState[][]{
			{b,null,a,null,b},
			{null,null,a,null,null},
			{a,a,t,a,a},
			{null,null,a,null,null},
			{b,null,a,null,b}
		};
		public static IBlockState[][] above = new IBlockState[][]{
			{b,null,a,null,b},
			{null,null,a,null,null},
			{a,a,a,a,a},
			{null,null,a,null,null},
			{b,null,a,null,b}
		};
		public static IBlockState[][] top = new IBlockState[][]{
			{c,null,null,null,c},
			{null,null,null,null,null},
			{null,null,null,null,null},
			{null,null,null,null,null},
			{c,null,null,null,c}
		};
		public static IBlockState[][][] struct = new IBlockState[][][]{floor,above,above,top};

		@Override
		public IBlockState[][][] getStructureDefinition()
		{
			return struct;
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

	AxisAlignedBB aabb;
	@Override
	@SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
		if(aabb == null)
			aabb = AxisAlignedBB.getBoundingBox(xCoord-2, yCoord, zCoord-2, xCoord+3, yCoord+4, zCoord+4);
		return aabb;
    }

}
