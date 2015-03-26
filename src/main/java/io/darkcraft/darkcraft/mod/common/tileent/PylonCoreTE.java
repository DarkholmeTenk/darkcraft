package io.darkcraft.darkcraft.mod.common.tileent;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import io.darkcraft.darkcore.mod.abstracts.AbstractTileEntity;
import io.darkcraft.darkcore.mod.config.CType;
import io.darkcraft.darkcore.mod.config.ConfigFile;
import io.darkcraft.darkcore.mod.config.ConfigItem;
import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.darkcore.mod.helpers.MultiBlockHelper;
import io.darkcraft.darkcore.mod.helpers.ServerHelper;
import io.darkcraft.darkcore.mod.interfaces.IActivatable;
import io.darkcraft.darkcore.mod.interfaces.IMultiBlockCore;
import io.darkcraft.darkcraft.mod.DarkcraftMod;
import io.darkcraft.darkcraft.mod.common.multiblock.PylonStructure;

public class PylonCoreTE extends AbstractTileEntity implements IMultiBlockCore, IActivatable
{
	private static final ConfigFile	pylonConfig;
	private static final int		rechargeFreq;
	private static final int		rechargeAm;
	private static final int		maxCharge;

	static
	{
		pylonConfig = DarkcraftMod.configHandler.registerConfigNeeder("PylonCore");
		rechargeFreq = pylonConfig.getConfigItem(
				new ConfigItem("Pylon recharge frequency", CType.INT, 20,
						"How many ticks between pylon recharge pulses",
						"Default: 20")).getInt();

		rechargeAm = pylonConfig.getConfigItem(
				new ConfigItem("Pylon recharge amount", CType.INT, 20,
						"How many nanogenes pylons generate per recharge pulse",
						"Default: 20")).getInt();

		maxCharge = pylonConfig.getConfigItem(
				new ConfigItem("Pylon max charge", CType.INT, 20000,
						"How many nanogenes pylons can hold", "Default: 20000"))
				.getInt();
	}

	private boolean					validMB		= false;
	private int						chargeLevel	= 0;

	@Override
	public boolean isValid()
	{
		return validMB;
	}

	@Override
	public void recheckValidity()
	{
		validMB = MultiBlockHelper.isMultiblockValid(this, PylonStructure.i);
	}

	@Override
	public boolean keepRechecking()
	{
		return false;
	}

	@Override
	public SimpleCoordStore getCoords()
	{
		return coords;
	}

	@Override
	public boolean activate(EntityPlayer ent, int side)
	{
		if (!ServerHelper.isServer())
			return true;
		long startTime = System.nanoTime();
		recheckValidity();
		long diff = System.nanoTime() - startTime;
		System.out.println("TT:" + diff + "ns");
		System.out.println("Valid:" + isValid());
		return true;
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();
		if(!isValid())
			return;
		if (chargeLevel < maxCharge && tt % rechargeFreq == 0)
			chargeLevel = Math.min(maxCharge, chargeLevel + rechargeAm);
	}

	@Override
	public void writeTransmittable(NBTTagCompound nbt)
	{
		super.writeTransmittable(nbt);
		nbt.setInteger("charge", chargeLevel);
	}

	@Override
	public void readTransmittable(NBTTagCompound nbt)
	{
		super.readTransmittable(nbt);
		chargeLevel = nbt.getInteger("charge");
	}
}
