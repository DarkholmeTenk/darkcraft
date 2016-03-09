package io.darkcraft.mod.common.magic.spell;

public enum CastType
{
	SELF,
	TOUCH,
	PROJECTILE;

	public static CastType getType(String s)
	{
		for(CastType ct : values())
			if(ct.name().equalsIgnoreCase(s))
				return ct;
		return PROJECTILE;
	}
}
