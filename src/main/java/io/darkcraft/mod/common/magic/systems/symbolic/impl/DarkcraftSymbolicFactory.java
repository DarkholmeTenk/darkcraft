package io.darkcraft.mod.common.magic.systems.symbolic.impl;

import io.darkcraft.darkcore.mod.datastore.SimpleCoordStore;
import io.darkcraft.mod.common.magic.systems.symbolic.ISymbolicFactory;
import io.darkcraft.mod.common.magic.systems.symbolic.ISymbolicSpell;
import io.darkcraft.mod.common.magic.systems.symbolic.SymbolicRegistry;
import net.minecraft.nbt.NBTTagCompound;

public class DarkcraftSymbolicFactory implements ISymbolicFactory
{
	{
		SymbolicRegistry.registerFactory(this);
	}

	@Override
	public String match(String glyphs)
	{
		if(glyphs.startsWith("target.mobs")) return "dc.targetmobs";
		if(glyphs.startsWith("target.players")) return "dc.targetplayers";
		if(glyphs.startsWith("magicka.sila")) return "dc.manaregen";
		if(glyphs.startsWith("volatus")) return "dc.featherfall";
		if(glyphs.startsWith("cognitio.enchantia")) return "dc.scrollwrite";
		if(glyphs.startsWith("tutamen.praecantatio")) return "dc.armorenchant";
		return null;
	}

	@Override
	public ISymbolicSpell createSpell(String id, String glyphs, SimpleCoordStore rootRune, SimpleCoordStore center)
	{
		boolean empowered = glyphs.endsWith(".maxima");
		if(id.equals("dc.targetmobs"))		return new SpellSoulgemSymbolicSpell(rootRune, center, "dc.targetmobs");
		if(id.equals("dc.targetplayers"))	return new SpellSoulgemSymbolicSpell(rootRune, center, "dc.targetplayers");
		if(id.equals("dc.manaregen"))		return new ManaRegenSymbolicSpell(rootRune, center);
		if(id.equals("dc.featherfall"))		return new FeatherfallSymbolic(rootRune, center, empowered);
		if(id.equals("dc.scrollwrite"))		return new ScrollWritingSymbolic(rootRune, center);
		if(id.equals("dc.armorenchant"))	return new ArmorEnchantSymbolic(rootRune, center);
		return null;
	}

	@Override
	public ISymbolicSpell loadSpell(String id, String glyphs, NBTTagCompound nbt)
	{
		return null;
	}

}
