package io.darkcraft.mod.common.magic.systems.spell;

import net.minecraft.util.ResourceLocation;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.DarkcraftMod;

public enum CastType
{
	SELF(1, new UVStore(0,0.5,0,0.5)),
	TOUCH(1, new UVStore(0,0.5,0.5,1)),
	PROJECTILE(1.5, new UVStore(0.5,1,0,0.5)),
	BOLT(2.5, new UVStore(0.5,1,0.5,1));

	public static final ResourceLocation icon = new ResourceLocation(DarkcraftMod.modName,"textures/gui/casttypes.png");
	public final UVStore uv;
	public final double costMult;
	private CastType(double costMultiplier, UVStore _uv)
	{
		costMult = costMultiplier;
		uv = _uv;
	}

	public static CastType getType(String s)
	{
		for(CastType ct : values())
			if(ct.name().equalsIgnoreCase(s))
				return ct;
		return PROJECTILE;
	}
}
