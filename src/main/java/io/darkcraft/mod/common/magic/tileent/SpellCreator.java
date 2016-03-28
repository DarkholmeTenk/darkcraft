package io.darkcraft.mod.common.magic.tileent;

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
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.abstracts.AbstractMFTileEntity;
import io.darkcraft.mod.client.renderer.gui.SpellCreationGui;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.caster.EntityCaster;
import io.darkcraft.mod.common.magic.caster.PlayerCaster;
import io.darkcraft.mod.common.magic.items.staff.IStaffable;
import io.darkcraft.mod.common.magic.items.staff.StaffHelper;
import io.darkcraft.mod.common.magic.spell.Spell;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.util.ForgeDirection;

public class SpellCreator extends AbstractMFTileEntity implements IActivatable, IStaffable
{
	private double currentCost = 0;
	private Spell currentSpell = null;
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
			currentSpell = null;
			currentSpellOwner = null;
			currentCost = 0;
			hasUser = new String[]{null,null,null,null};
			markDirty();
			sendUpdate();
		}
		else if(!hasSpell())
		{
			currentSpell = spell;
			currentSpellOwner = un;
			hasUser = new String[]{null,null,null,null};
			currentCost = spell.getCost(null) * 10;
			markDirty();
			sendUpdate();
		}
		else
		{
			MessageHelper.sendMessage(pl, "dc.message.creator.inUse");
		}
	}

	private boolean hasSpell = false;
	public boolean hasSpell()
	{
		if(ServerHelper.isClient())
			return hasSpell;
		return (currentSpell != null) && (currentSpellOwner != null);
	}

	private boolean validStructure = false;
	public String[] hasUser = new String[]{null,null,null,null};

	@Override
	public void tick()
	{
		if((tt % 100) == 0)
			validStructure = MultiBlockHelper.isMultiblockValid(this, SpellCreatorStructure.i);
		if(ServerHelper.isServer())
			if(isValidStructure() && hasSpell() && ((tt%10) == 0))
				checkNearbyPlayers();
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
				if(c.useMana(am, false))
					currentCost -= am;
			}
			if(currentCost <= 0)
			{
				for(String s : hasUser)
				{
					if(s == null) continue;
					EntityPlayer pl = PlayerHelper.getPlayer(s);
					if(pl == null) continue;
					PlayerCaster c = Helper.getPlayerCaster(pl);
					c.learnSpell(currentSpell);
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
			{c,null,null,null,null},
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

}
