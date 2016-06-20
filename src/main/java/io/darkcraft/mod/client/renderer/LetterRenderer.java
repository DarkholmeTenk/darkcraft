package io.darkcraft.mod.client.renderer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.darkcore.mod.datastore.Pair;
import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.DarkcraftMod;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public class LetterRenderer
{
	private static ResourceLocation rl = new ResourceLocation(DarkcraftMod.modName,"font/daedric.png");
	private static ResourceLocation gl = new ResourceLocation(DarkcraftMod.modName, "textures/glow.png");
	private static HashMap<Character,LetterData> dataMap = new HashMap();

	private static void refreshData()
	{
		if(dataMap.isEmpty())
		{
			LetterData ld = new LetterData(new UVStore(1,6,63,73));
			ld.addLetters(0,0, 5,0, 5,1, 4,1, 4,2, 3,2, 3,4, 2,4, 2,2, 3,2, 3,1, 1,1, 1,8, 3,8, 3,10, 2,10, 2,9, 1,9, 1,8, 0,8);
			dataMap.put('a', ld);
			ld = new LetterData(new UVStore(14,20,63,73));
			ld.addLetters(0,0, 6,0, 6,3, 5,3, 5,4, 4,4, 4,3, 5,3, 5,1, 2,1, 2,3, 3,3, 3,4, 4,4, 4,7, 3,7, 3,8, 2,8, 2,9, 1,9, 1,10, 0,10, 0,4, 1,4, 1,1, 0,1);
			ld.addLetters(1,5, 2,5, 2,6, 3,6, 3,7, 2,7, 2,8, 1,8);
			dataMap.put('b', ld);
			ld = new LetterData(new UVStore(22,28,63,74));
			ld.addLetters(0,0, 6,0, 6,11, 5,11, 5,3, 4,3, 4,2, 5,2, 5,1, 1,1, 1,2, 2,2, 2,3, 1,3, 1,11, 0,11);
			dataMap.put('c', ld);
			ld = new LetterData(new UVStore(29,35, 63,73));
			ld.addLetters(1,0, 6,0, 6,10, 5,10, 5,2, 4,2, 4,1, 2,1, 2,3, 3,3, 3,4, 2,4, 2,5, 1,5, 1,6, 2,6, 2,8, 3,8, 3,9, 4,9, 4,10, 3,10, 3,9, 2,9, 2,8, 1,8, 1,6, 0,6, 0,3, 1,3);
			dataMap.put('d', ld);
			ld = new LetterData(new UVStore(37,43, 63,73));
			ld.addLetters(0,0, 5,0, 5,7, 4,7, 4,5, 1,5, 1,9, 6,9, 6,10, 0,10, 0,2, 1,2, 1,4, 4,4, 4,1, 0,1);
			dataMap.put('e', ld);
			ld = new LetterData(new UVStore(44,51, 63,74));
			ld.addLetters(0,0, 7,0, 7,1, 5,1, 5,2, 4,2, 4,3, 7,3, 7,7, 6,7, 6,5, 2,5, 2,7, 1,7, 1,11, 0,11, 0,7, 1,7, 1,5, 2,5, 2,3, 3,3, 3,2, 4,2, 4,1, 1,1, 1,3, 0,3);
			dataMap.put('f', ld);
			ld = new LetterData(new UVStore(53,58, 63,73));
			ld.addLetters(0,0, 5,0, 5,5, 4,5, 4,7, 3,7, 3,9, 2,9, 2,10, 1,10, 1,9, 2,9, 2,7, 3,7, 3,5, 4,5, 4,2, 3,2, 3,1, 1,1, 1,2, 0,2);
			dataMap.put('g', ld);
			ld = new LetterData(new UVStore(59,65, 62,73));
			ld.addLetters(0,1, 5,1, 5,0, 6,0, 6,3, 5,3, 5,2, 3,2, 3,4, 4,4, 4,5, 5,5, 5,6, 4,6, 4,8, 3,8, 3,10, 2,10, 2,11, 0,11, 0,10, 2,10, 2,8, 3,8, 3,4, 2,4, 2,2, 0,2);
			dataMap.put('h', ld);
			ld = new LetterData(new UVStore(66,70, 63,74));
			ld.addLetters(0,0, 4,0, 4,2, 3,2, 3,6, 2,6, 2,11, 1,11, 1,6, 2,6, 2,2, 1,2, 1,3, 0,3);
			dataMap.put('i', ld);
			ld = new LetterData(new UVStore(72,78, 63,73));
			ld.addLetters(0,0, 6,0, 6,1, 5,1, 5,3, 6,3, 6,10, 3,10, 3,9, 5,9, 5,3, 4,3, 4,2, 1,2, 1,6, 0,6);
			dataMap.put('j', ld);
			ld = new LetterData(new UVStore(79,85, 63,73));
			ld.addLetters(1,0, 6,0, 6,1, 5,1, 5,3, 6,3, 6,7, 5,7, 5,8, 4,8, 4,10, 2,10, 2,9, 1,9, 1,8, 0,8, 0,2, 1,2);
			ld.addLetters(2,1, 4,1, 4,3, 5,3, 5,7, 3,7, 3,9, 2,9, 2,8, 1,8, 1,2, 2,2);
			dataMap.put('k', ld);
			ld = new LetterData(new UVStore(87,94, 63,73));
			ld.addLetters(0,0, 7,0, 7,1, 6,1, 6,3, 5,3, 5,1, 3,1, 3,2, 4,2, 4,9, 5,9, 5,10, 3,10, 3,3, 2,3, 2,1, 0,1);
			dataMap.put('l', ld);
			ld = new LetterData(new UVStore(102,108, 64,74));
			ld.addLetters(0,0, 3,0, 3,1, 2,1, 2,2, 3,2, 3,3, 4,3, 4,0, 6,0, 6,9, 5,9, 5,10, 4,10, 4,9, 5,9, 5,3, 4,3, 4,4, 3,4, 3,3, 2,3, 2,2, 1,2, 1,5, 0,5);
			dataMap.put('m', ld);
			ld = new LetterData(new UVStore(110,115, 63,73));
			ld.addLetters(0,0, 3,0, 3,2, 4,2, 4,0, 5,0, 5,4, 4,4, 4,3, 3,3, 3,2, 2,2, 2,4, 3,4, 3,7, 2,7, 2,9, 1,9, 1,10, 0,10, 0,9, 1,9, 1,7, 2,7, 2,4, 1,4, 1,1, 0,1);
			dataMap.put('n', ld);
			ld = new LetterData(new UVStore(116,122, 63,73));
			ld.addLetters(0,0, 6,0, 6,1, 5,1, 5,2, 6,2, 6,9, 5,9, 5,10, 4,10, 4,9, 5,9, 5,2, 4,2, 4,1, 3,1, 3,2, 2,2, 2,9, 3,9, 3,10, 2,10, 2,9, 1,9, 1,1, 0,1);
			ld.addLetters(3,3, 4,3, 4,5, 3,5);
			dataMap.put('o', ld);
			ld = new LetterData(new UVStore(0,7, 82,92));
			ld.addLetters(0,0, 7,0, 7,1, 6,1, 6,2, 5,2, 5,5, 4,5, 4,8, 5,8, 5,10, 4,10, 4,8, 3,8, 3,5, 1,5, 1,1, 0,1);
			ld.addLetters(2,1, 5,1, 5,2, 4,2, 4,4, 2,4);
			dataMap.put('p', ld);
			ld = new LetterData(new UVStore(8,14, 82,92));
			ld.addLetters(0,0, 6,0, 6,1, 5,1, 5,2, 4,2, 4,3, 3,3, 3,6, 5,6, 5,7, 3,7, 3,9, 4,9, 4,10, 2,10, 2,7, 0,7, 0,6, 2,6, 2,3, 1,3, 1,1, 0,1);
			ld.addLetters(2,1, 4,1, 4,2, 3,2, 3,3, 2,3);
			dataMap.put('q', ld);
			ld = new LetterData(new UVStore(15,21, 82,93));
			ld.addLetters(0,1, 2,1, 2,0, 4,0, 4,1, 5,1, 5,2, 6,2, 6,3, 3,3, 3,4, 2,4, 2,11, 1,11, 1,3, 3,3, 3,2, 4,2, 4,1, 2,1, 2,2, 0,2);
			dataMap.put('r', ld);
			ld = new LetterData(new UVStore(22,28, 82,93));
			ld.addLetters(1,0, 5,0, 5,1, 6,1, 6,2, 5,2, 5,1, 3,1, 3,2, 4,2, 4,4, 3,4, 3,5, 4,5, 4,7, 5,7, 5,9, 4,9, 4,10, 3,10, 3,11, 2,11, 2,10, 3,10, 3,9, 4,9, 4,7, 3,7, 3,5, 2,5, 2,4, 1,4, 1,3, 0,3, 0,2, 1,2, 1,3, 3,3, 3,2, 2,2, 2,1, 1,1);
			dataMap.put('s', ld);
			ld = new LetterData(new UVStore(29,34, 80,92));
			ld.addLetters(1,0, 4,0, 4,1, 5,1, 5,3, 4,3, 4,4, 3,4, 3,3, 1,3, 1,4, 2,4, 2,5, 3,5, 3,9, 2,9, 2,11, 3,11, 3,10, 4,10, 4,9, 5,9, 5,10, 4,10, 4,11, 3,11, 3,12, 2,12, 2,11, 1,11, 1,10, 0,10, 0,9, 2,9, 2,5, 1,5, 1,4, 0,4, 0,2, 3,2, 3,3, 4,3, 4,1, 1,1);
			dataMap.put('t', ld);
			ld = new LetterData(new UVStore(36,42, 82,92));
			ld.addLetters(0,0, 6,0, 6,1, 5,1, 5,3, 6,3, 6,7, 5,7, 5,9, 4,9, 4,10, 1,10, 1,9, 0,9, 0,4, 1,4, 1,8, 2,8, 2,9, 4,9, 4,7, 5,7, 5,4, 4,4, 4,3, 3,3, 3,2, 2,2, 2,1, 0,1);
			ld.addLetters(2,5, 3,5, 3,7, 2,7);
			dataMap.put('u', ld);
			ld = new LetterData(new UVStore(44,49, 82,92));
			ld.addLetters(1,0, 2,0, 2,2, 1,2, 1,9, 3,9, 3,8, 4,8, 4,3, 3,3, 3,0, 4,0, 4,3, 5,3, 5,8, 4,8, 4,10, 1,10, 1,9, 0,9, 0,1, 1,1);
			dataMap.put('v', ld);
			ld = new LetterData(new UVStore(51,57, 82,92));
			ld.addLetters(0,0, 6,0, 6,2, 5,2, 5,1, 4,1, 4,3, 5,3, 5,6, 4,6, 4,9, 3,9, 3,10, 0,10, 0,9, 2,9, 2,7, 1,7, 1,6, 0,6, 0,2, 1,2, 1,1, 0,1);
			ld.addLetters(2,1, 3,1, 3,3, 4,3, 4,6, 3,6, 3,7, 2,7, 2,6, 1,6, 1,2, 2,2);
			dataMap.put('w', ld);
			ld = new LetterData(new UVStore(58,65, 80,92));
			ld.addLetters(0,3, 1,3, 1,2, 2,2, 2,1, 3,1, 3,0, 4,0, 4,1, 6,1, 6,3, 7,3, 7,9, 6,9, 6,11, 5,11, 5,12, 0,12, 0,10, 1,10, 1,9, 0,9);
			ld.addLetters(3,1, 4,1, 4,2, 5,2, 5,3, 6,3, 6,8, 5,8, 5,10, 2,10, 2,9, 1,9, 1,3, 2,3, 2,2, 3,2);
			dataMap.put('x', ld);
			ld = new LetterData(new UVStore(66,72, 82,92));
			ld.addLetters(1,0, 2,0, 2,1, 3,1, 3,2, 4,2, 4,0, 6,0, 6,1, 5,1, 5,2, 4,2, 4,5, 3,5, 3,9, 5,9, 5,10, 1,10, 1,9, 0,9, 0,6, 1,6, 1,8, 2,8, 2,5, 3,5, 3,2, 1,2);
			dataMap.put('y', ld);
			ld = new LetterData(new UVStore(73,77, 82,92));
			ld.addLetters(0,0, 4,0, 4,1, 3,1, 3,2, 2,2, 2,3, 3,3, 3,5, 2,5, 2,10, 1,10, 1,5, 2,5, 2,4, 0,4, 0,3, 1,3, 1,2, 2,2, 2,1, 0,1);
			dataMap.put('z', ld);
			ld = new LetterData(new UVStore(124,127, 127,127));
			dataMap.put(' ', ld);
		}
	}

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

	private static int renderChar(Tessellator tess, int x, LetterData ld)
	{
		tess.setNormal(0, 0, 1);
		tess.addVertexWithUV(x+ld.x, 0, 0, ld.uv.U, ld.uv.V);
		tess.addVertexWithUV(x+ld.x, ld.y, 0, ld.uv.U, ld.uv.v);
		tess.addVertexWithUV(x, ld.y, 0, ld.uv.u, ld.uv.v);
		tess.addVertexWithUV(x, 0, 0, ld.uv.u, ld.uv.V);
		return ld.x + 1;
	}

	private static int renderGlow(Tessellator tess, int x, LetterData ld)
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
		refreshData();
		LetterData ld = dataMap.get(l);
		if(ld == null) return 0;
		return ld.x;
	}

	public static int height(char l)
	{
		refreshData();
		LetterData ld = dataMap.get(l);
		if(ld == null) return 0;
		return ld.y;
	}

	public static final Colour defaultTextColour = RenderHelper.white;
	public static final Colour defaultGlowColour = new Colour(0.1f,0.2f,1);
	public static void render(char l, boolean glow, Colour textColour, Colour glowColour)
	{
		refreshData();
		LetterData ld = dataMap.get(l);
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

	public static void render(char l)
	{
		render(l,true,defaultTextColour,defaultGlowColour);
	}

	public static void render(String s)
	{
		render(s,0,0,defaultTextColour,defaultGlowColour);
	}

	public static void render(String s, int x, int y, Colour textColour, Colour glowColour)
	{
		GL11.glPushMatrix();
		boolean el = GL11.glIsEnabled(GL11.GL_LIGHTING);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glTranslatef(x, y, 0);
		refreshData();
		int size = s.length();
		LetterData[] ld = new LetterData[s.length()];
		for(int i = 0; i < size; i++)
		{
			char c = s.charAt(i);
			LetterData d = dataMap.get(c);
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
		for(LetterData d : ld)
			if(d != null)
				i+=renderChar(tess,i,d);
		tess.draw();

		RenderHelper.colour(glowColour);
		boolean bfc = GL11.glIsEnabled(GL11.GL_CULL_FACE);
		//GL11.glDisable(GL11.GL_CULL_FACE);
		RenderHelper.bindTexture(gl);
		tess.startDrawingQuads();
		i = 0;
		for(LetterData d : ld)
			if(d != null)
				i+= renderGlow(tess,i,d);
		tess.draw();
		if(bfc)
			GL11.glEnable(GL11.GL_CULL_FACE);
		if(el)
			GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

	private static class LetterData
	{
		private final UVStore uv;
		private final int x;
		private final int y;
		private Set<Pair<Integer,Integer>[]> data = new HashSet();

		private LetterData(UVStore _uv)
		{
			uv = _uv.div(128);
			x = (int) Math.round(_uv.U - _uv.u);
			y = (int) Math.round(_uv.V - _uv.v);
		}

		public void addLetters(Integer... incoming)
		{
			if((incoming.length % 2) != 0)
			{
				System.err.println("Invalid data");
				return;
			}
			int s = incoming.length / 2;
			Pair<Integer,Integer>[] pairData = new Pair[s];
			for(int i = 0; i < s; i++)
				pairData[i] = new Pair(incoming[2*i],y-incoming[(2*i)+1]);
			data.add(pairData);
		}
	}
}
