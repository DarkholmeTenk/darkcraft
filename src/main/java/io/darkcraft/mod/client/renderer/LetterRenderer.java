package io.darkcraft.mod.client.renderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.darkcore.mod.datastore.Pair;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.DaedricLetters;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class LetterRenderer
{
	private static ResourceLocation rl = new ResourceLocation(DarkcraftMod.modName,"font/daedric.png");
	private static ResourceLocation gl = new ResourceLocation(DarkcraftMod.modName, "textures/glow.png");
	private static double gT(double a, double c)
	{
		double m = 1.3;
		return ((a-c)*m)+c;
	}

	private static void face(Tessellator tess, int x, int y, int X, int Y, int cx, int cy, double v, double V)
	{
		double h = 1.5;
		double U = Math.max(X-x, Y-y)/16.0;
		tess.addVertexWithUV(x, y, 0, 0, V);
		tess.addVertexWithUV(X, Y, 0, U, V);
		tess.addVertexWithUV(gT(X,cx), gT(Y,cy), h, U, v);
		tess.addVertexWithUV(gT(x,cx), gT(y,cy), h, 0, v);
	}

	private static int renderChar(Tessellator tess, int x, DaedricLetters.LetterData ld)
	{
		tess.setNormal(0, 0, 1);
		tess.addVertexWithUV(x+ld.x, 0, 0, ld.uv.U, ld.uv.V);
		tess.addVertexWithUV(x+ld.x, ld.y, 0, ld.uv.U, ld.uv.v);
		tess.addVertexWithUV(x, ld.y, 0, ld.uv.u, ld.uv.v);
		tess.addVertexWithUV(x, 0, 0, ld.uv.u, ld.uv.V);
		return ld.x + 1;
	}

	private static int renderGlow(Tessellator tess, int x, DaedricLetters.LetterData ld)
	{
		int cx = (ld.x/2)+x;
		int cy = ld.y/2;
		for(Pair<Integer,Integer>[] block : ld.data)
		{
			for(int i = 0; i < block.length; i++)
			{
				int n = (i+1)%block.length;
				face(tess,x+block[i].a, block[i].b, x+block[n].a, block[n].b,cx,cy,0,1);
			}
		}
		return ld.x + 1;
	}

	public static int width(char l)
	{
		DaedricLetters.refreshData();
		DaedricLetters.LetterData ld = DaedricLetters.dataMap.get(l);
		if(ld == null) return 0;
		return ld.x;
	}

	public static int width(String s)
	{
		int w = 0;
		for(char c : s.toCharArray())
			w += width(c) + 1;
		return w;
	}

	public static int height(char l)
	{
		DaedricLetters.refreshData();
		DaedricLetters.LetterData ld = DaedricLetters.dataMap.get(l);
		if(ld == null) return 0;
		return ld.y;
	}

	public static final Colour defaultTextColour = Colour.white;
	public static final Colour defaultGlowColour = new Colour(0.1f,0.2f,1);

	@SideOnly(Side.CLIENT)
	public static void render(char l, boolean glow, Colour textColour, Colour glowColour)
	{
		DaedricLetters.refreshData();
		DaedricLetters.LetterData ld = DaedricLetters.dataMap.get(l);
		if(ld != null)
		{
			Tessellator tess = Tessellator.instance;
			RenderHelper.bindTexture(rl);
			RenderHelper.colour(textColour);
			tess.startDrawingQuads();
			renderChar(tess,0,ld);
			tess.draw();

			if(glow)
			{
				RenderHelper.bindTexture(gl);
				tess.startDrawingQuads();
				RenderHelper.colour(glowColour);
				renderGlow(tess,0,ld);
				tess.draw();
			}
		}
		RenderHelper.resetColour();
	}

	@SideOnly(Side.CLIENT)
	public static void render(char l)
	{
		render(l,true,defaultTextColour,defaultGlowColour);
	}

	@SideOnly(Side.CLIENT)
	public static void render(String s)
	{
		render(s,0,0,defaultTextColour,defaultGlowColour);
	}

	@SideOnly(Side.CLIENT)
	public static void render(String s, int x, int y, Colour textColour, Colour glowColour, boolean glow)
	{
		GL11.glPushMatrix();
		boolean el = GL11.glIsEnabled(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glTranslatef(x, y, 0);
		DaedricLetters.refreshData();
		int size = s.length();
		DaedricLetters.LetterData[] ld = new DaedricLetters.LetterData[s.length()];
		for(int i = 0; i < size; i++)
		{
			char c = s.charAt(i);
			DaedricLetters.LetterData d = DaedricLetters.dataMap.get(c);
			if(d == null)
				ld[i] = null;
			else
				ld[i] = d;
		}
		RenderHelper.colour(textColour);
		Tessellator tess = Tessellator.instance;
		RenderHelper.bindTexture(rl);
		tess.startDrawingQuads();
		int i = 0;
		for(DaedricLetters.LetterData d : ld)
			if(d != null)
				i+=renderChar(tess,i,d);
		tess.draw();

		if(glow)
		{
			RenderHelper.colour(glowColour);
			boolean bfc = GL11.glIsEnabled(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_CULL_FACE);
			RenderHelper.bindTexture(gl);
			tess.startDrawingQuads();
			i = 0;
			for(DaedricLetters.LetterData d : ld)
				if(d != null)
					i+= renderGlow(tess,i,d);
			tess.draw();
			if(bfc)
				GL11.glEnable(GL11.GL_CULL_FACE);
		}
		if(el)
			GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	@SideOnly(Side.CLIENT)
	public static void render(String s, int x, int y, Colour textColour, Colour glowColour)
	{
		render(s,x,y,textColour,glowColour,true);
	}
}
