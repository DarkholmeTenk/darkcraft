package io.darkcraft.mod.common.magic.systems.spell;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.mod.DarkcraftMod;
import net.minecraft.util.ResourceLocation;

public enum CastType
{
	SELF(new UVStore(0,0.5,0,0.5)),
	TOUCH(new UVStore(0,0.5,0.5,1)),
	PROJECTILE(new UVStore(0.5,1,0,0.5));

	public static final ResourceLocation icon = new ResourceLocation(DarkcraftMod.modName,"textures/gui/casttypes.png");
	public final UVStore uv;
	private CastType(UVStore _uv)
	{
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
