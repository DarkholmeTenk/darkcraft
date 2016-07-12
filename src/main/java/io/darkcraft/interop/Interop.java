package io.darkcraft.interop;

import io.darkcraft.darkcore.mod.interop.InteropHandler;
import io.darkcraft.interop.thaumcraft.DarkcraftTC;

public class Interop
{
	public static DarkcraftTC thaum;

	public static void preInit()
	{
		InteropHandler.register(thaum = new DarkcraftTC());
		if(thaum.installed)
			thaum.preInit();
	}

	public static void init()
	{
		if(thaum.installed)
			thaum.init();
	}

	public static void postInit()
	{
		if(thaum.installed)
			thaum.postInit();
	}
}
