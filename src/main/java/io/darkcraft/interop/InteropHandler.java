package io.darkcraft.interop;

import cpw.mods.fml.common.Loader;
import io.darkcraft.interop.thaumcraft.DarkcraftTC;

public class InteropHandler
{
	public static String tc = "Thaumcraft";
	public static boolean tcInstalled = false;
	public static void preInit()
	{
		if(Loader.isModLoaded(tc))
			tcInstalled = true;
	}

	public static void init()
	{
		if(tcInstalled)
			DarkcraftTC.init();
	}

	public static void postInit()
	{
		if(tcInstalled)
			DarkcraftTC.postInit();
	}
}
