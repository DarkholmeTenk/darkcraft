package io.darkcraft.mod.common.magic.blocks.tileent;

import io.darkcraft.darkcore.mod.abstracts.AbstractTileEntity;
import io.darkcraft.darkcore.mod.datastore.Pair;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.helpers.MathHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.helpers.WorldHelper;
import io.darkcraft.darkcore.mod.interfaces.IActivatable;
import io.darkcraft.darkcore.mod.interfaces.IBlockUpdateDetector;
import io.darkcraft.mod.client.renderer.LetterRenderer;
import io.darkcraft.mod.common.magic.systems.symbolic.ISymbolicSpell;
import io.darkcraft.mod.common.magic.systems.symbolic.SymbolicRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class MagicSymbol extends AbstractTileEntity implements IActivatable, IBlockUpdateDetector
{
	private char myChar = 's';
	private boolean isActive = false;
	private boolean isRoot = false;
	private SimpleCoordStore root;

	private Pair<SimpleCoordStore,Character>[] existing = null;
	private String glyphs = null;
	private String activeSpellID = null;
	private ISymbolicSpell activeSpell = null;
	private int radius = 0;
	private int charge = 0;

	private void cancel()
	{
		boolean shouldRoot = isRoot;
		isActive = false;
		isRoot = false;
		if(shouldRoot)
		{
			System.out.println("Cancelling " + glyphs);
			if(existing != null)
				for(Pair<SimpleCoordStore,Character> pos : existing)
				{
					TileEntity te = pos.a.getTileEntity();
					if(te instanceof MagicSymbol)
						((MagicSymbol)te).cancel();
				}
			existing = null;
			activeSpellID = null;
			if(activeSpell != null)
				activeSpell.cancel();
			activeSpell = null;
			charge = 0;
		}
		else
		{
			root = null;
		}
		sendUpdate();
	}

	public void clearCircle()
	{
		if(existing == null) return;
		if(activeSpell != null) activeSpell.cancel();
		for(Pair<SimpleCoordStore,Character> d : existing)
		{
			TileEntity te = d.a.getTileEntity();
			if(te instanceof MagicSymbol)
				d.a.setToAir();
		}
	}

	private char[] shiftCharArray(char[] arr, int r)
	{
		char[] newArr = new char[arr.length];
		for(int i = 0; i < arr.length; i++)
		{
			int nx = ((arr.length + i) - r) % arr.length;
			newArr[nx] = arr[i];
		}
		return newArr;
	}

	private void scanForNew()
	{
		if(ServerHelper.isClient()) return;
		mainLoop:
		for(int horI = 0; horI < 4; horI++)
		{
			ForgeDirection d = MathHelper.horizontal[horI];
			int r = 0;
			{
				SimpleCoordStore lastSCS = coords();
				SimpleCoordStore scs = lastSCS.getNearby(d);
				while(scs.getTileEntity() instanceof MagicSymbol)
				{
					MagicSymbol ms = (MagicSymbol)scs.getTileEntity();
					if(ms.isRoot() || (ms.isActive() != isActive()))
					{
						cancel();
						ms.cancel();
						return;
					}
					r++;
					lastSCS = scs;
					scs = lastSCS.getNearby(d);
				}
			}
			if(r == 0) continue;
			int tr = (r << 1);

			char[] data = new char[r << 3];
			MagicSymbol[] msArr = new MagicSymbol[r << 3];
			SimpleCoordStore curCoords = coords();
			curCoords = curCoords.getNearby(d.getOpposite(),r);
			for(int di = 0; di < 4; di ++)
			{
				ForgeDirection x = MathHelper.horizontal[(horI+di) % 4];
				int o = (tr * di);
				for(int i = 0; i < tr; i++)
				{
					if(curCoords.getTileEntity() instanceof MagicSymbol)
					{
						MagicSymbol ms = ((MagicSymbol)curCoords.getTileEntity());
						if(ms != this)
						{
							if(ms.isRoot() || (ms.isActive() != isActive()))
							{
								cancel();
								ms.cancel();
								return;
							}
						}
						data[o+i] = ms.getCharacter();
						msArr[o+i] = ms;
						curCoords = curCoords.getNearby(x);
					}
					else
						continue mainLoop;
				}
			}
			data = shiftCharArray(data,r);
			MathHelper.shiftObjArray(msArr, r);
			glyphs = String.copyValueOf(data);
			activeSpellID = SymbolicRegistry.match(glyphs);
			System.out.println("Glyphs found: "+ glyphs +"="+activeSpellID);
			if(activeSpellID == null)
			{
				cancel();
				return;
			}
			isRoot = true;
			radius = r;
			existing = new Pair[r << 3];
			for(int i = 0; i < existing.length; i++)
				existing[i] = new Pair<SimpleCoordStore,Character>(msArr[i].coords(),data[i]);
			root = coords().getNearby(MathHelper.horizontal[(horI+1)%4],radius);
			return;
		}
	}

	private boolean isExistingBroken()
	{
		int l = 0;
		if(isRunning())
			l = existing.length;
		else if(isRoot())
			l = charge;
		else
			return false;
		for(int i = 0; i < l; i++)
		{
			TileEntity te = existing[i].a.getTileEntity();
			if(!(te instanceof MagicSymbol)) return false;
			Character ch = ((MagicSymbol)te).getCharacter();
			if(!existing[i].b.equals(ch)) return false;
		}
		return true;
	}

	public void scanExisting()
	{
		if(ServerHelper.isClient()) return;
		if(!isExistingBroken())
		{
			cancel();
		}
	}

	private void activate()
	{
		activeSpell = SymbolicRegistry.createSpell(activeSpellID, glyphs, coords(), root);
		if(activeSpell == null)
			cancel();
		sendUpdate();
	}

	@Override
	public void tick()
	{
		if(isRoot())
		{
			if(isRunning())
			{
				if((tt % 10) == 0)
					scanExisting();
				if(isRunning())
					activeSpell.tick();
			}
			else
			{
				if(((tt % 20) == 0) && ServerHelper.isServer())
				{
					scanExisting();
					if(!isRoot) return;
					if(charge >= existing.length) return;
					Pair<SimpleCoordStore,Character> nextBlock = existing[charge++];
					TileEntity te = nextBlock.a.getTileEntity();
					if(!(te instanceof MagicSymbol) || (((MagicSymbol)te).getCharacter() != nextBlock.b))
						cancel();
					else
					{
						((MagicSymbol)te).isActive = true;
						((MagicSymbol)te).sendUpdate();
						if(charge == existing.length)
							activate();
					}
				}
			}

		}
	}

	public char getCharacter()
	{
		if(LetterRenderer.validChar(myChar))
			return myChar;
		return 's';
	}

	public void setCharacter(Character c)
	{
		myChar = c;
	}

	public boolean isRunning()
	{
		if(!isActive()) return false;
		if(!isRoot()) return false;
		if((activeSpell != null) && ServerHelper.isClient()) return true;
		if(isRoot && (existing == null))
		{
			if(ServerHelper.isServer())
				cancel();
			return false;
		}
		if(charge < existing.length) return false;
		if(activeSpell == null) return false;
		return true;
	}

	public boolean isActive(){ return isActive; }

	public boolean isRoot(){ return isRoot && ((existing != null) || ServerHelper.isClient()); }

	@Override
	public boolean activate(EntityPlayer ent, int side)
	{

		if(isRoot)
			cancel();
		else
			scanForNew();
		System.out.println("C:"+myChar);
		return true;
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		if(isRoot() && isRunning())
		{
			if(activeSpell != null)
			{
				NBTTagCompound subNBT = new NBTTagCompound();
				activeSpell.write(subNBT);
				nbt.setTag("spellData", subNBT);
			}
		}
		if(existing != null)
		{
			nbt.setInteger("existingL", existing.length);
			for(int i = 0; i < existing.length; i++)
			{
				existing[i].a.writeToNBT(nbt, "existingSCS"+i);
				nbt.setInteger("existingC"+i, existing[i].b);
			}
		}
		super.writeToNBT(nbt);
	}

	@Override
	public void writeTransmittable(NBTTagCompound nbt)
	{
		nbt.setInteger("c", myChar);
		nbt.setBoolean("active", isActive);
		nbt.setBoolean("root", isRoot);
		if(isRoot)
		{
			nbt.setString("activeSpellID", activeSpellID);
			nbt.setInteger("charge", charge);
			nbt.setString("glyphs", glyphs);
			nbt.setInteger("radius", radius);
			if(activeSpell != null)
			{
				NBTTagCompound subNBT = nbt.hasKey("spellData") ? nbt.getCompoundTag("spellData") : new NBTTagCompound();
				activeSpell.writeTrans(subNBT);
				nbt.setTag("spellData", subNBT);
			}
		}
		if(root != null)
			root.writeToNBT(nbt, "rootPos");
		coords().writeToNBT(nbt, "coords");
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		if(nbt.hasKey("spellData"))
		{
			if(activeSpell != null)
				activeSpell.read(nbt.getCompoundTag("spellData"));
			else
			{
				String id = nbt.getString("activeSpellID");
				String tx = nbt.getString("glyphs");
				NBTTagCompound data = nbt.getCompoundTag("spellData");
				SimpleCoordStore me = SimpleCoordStore.readFromNBT(nbt, "coords");
				SimpleCoordStore o = SimpleCoordStore.readFromNBT(nbt, "rootPos");
				if((me != null) && (o != null))
				{
					activeSpell = SymbolicRegistry.createSpell(id, tx, me, o);
					if(activeSpell != null)
						activeSpell.read(data);
				}
				else
					activeSpell = SymbolicRegistry.loadSpell(id, tx, data);
			}
		}
		if(nbt.hasKey("existingL"))
		{
			int l = nbt.getInteger("existingL");
			if((existing == null) || (existing.length != l))
			{
				existing = new Pair[l];
				for(int i = 0; i < l; i++)
					existing[i] = new Pair<SimpleCoordStore,Character>(SimpleCoordStore.readFromNBT(nbt, "existingSCS"+i), (char) nbt.getInteger("existingC"+i));
			}
		}
	}

	@Override
	public void readTransmittable(NBTTagCompound nbt)
	{
		myChar = (char) nbt.getInteger("c");
		isActive = nbt.getBoolean("active");
		isRoot = nbt.getBoolean("root");
		if(isRoot)
		{
			activeSpellID = nbt.getString("activeSpellID");
			glyphs = nbt.getString("glyphs");
			charge = nbt.getInteger("charge");
			radius = nbt.getInteger("radius");
			if(nbt.hasKey("spellData"))
			{
				if(activeSpell == null)
					activeSpell = SymbolicRegistry.loadSpell(activeSpellID, glyphs, nbt.getCompoundTag("spellData"));
				else
					activeSpell.readTrans(nbt);
			}
		}
		root = SimpleCoordStore.readFromNBT(nbt, "rootPos");
	}

	@Override
	public void blockUpdated(Block updatedBlock)
	{
		if(WorldHelper.softBlock(worldObj, xCoord, yCoord-1, zCoord))
			coords().setToAir();
	}
}
