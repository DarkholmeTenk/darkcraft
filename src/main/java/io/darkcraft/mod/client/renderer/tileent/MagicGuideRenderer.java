package io.darkcraft.mod.client.renderer.tileent;

import io.darkcraft.api.magic.IMagicAnvilRecipe;
import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.darkcore.mod.abstracts.AbstractObjRenderer;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.darkcore.mod.multiblock.IMultiBlockStructure;
import io.darkcraft.darkcore.mod.multiblock.RenderBlockAccess;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.LetterRenderer;
import io.darkcraft.mod.common.magic.component.IComponent;
import io.darkcraft.mod.common.magic.tileent.MagicGuide;
import io.darkcraft.mod.common.magic.tileent.MagicGuide.Mode;
import io.darkcraft.mod.common.magic.tileent.guide.IGuidePage;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;

import java.util.WeakHashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

public class MagicGuideRenderer extends AbstractObjRenderer
{
	private IModelCustom model;
	private ResourceLocation lectTex = new ResourceLocation(DarkcraftMod.modName,"textures/tileents/lectern.png");
	private ResourceLocation bookTex = new ResourceLocation(DarkcraftMod.modName,"textures/tileents/book.png");

	@Override
	public AbstractBlock getBlock()
	{
		return ItemBlockRegistry.magicGuide;
	}

	@Override
	public boolean handleLighting()
	{
		return false;
	}

	private void rotate(TileEntity te)
	{
		if(te == null) return;
		int m = (te.getBlockMetadata() + 3) % 4;
		GL11.glRotatef(-90*m, 0, 1, 0);
	}

	private void moveForBook(TileEntity te,float d)
	{
		GuideData gd = GuideData.getGD(te);
		float h = gd.height;
		gd.height = (h+0.001f) %1;
		if(h > 0.5)
			h = 1f-h;
		GL11.glTranslated(0, h/d, 0);
	}

	@Override
	public void renderBlock(Tessellator tess, TileEntity te, int x, int y, int z)
	{
		if(model == null) model = AdvancedModelLoader.loadModel(new ResourceLocation(DarkcraftMod.modName,"models/tiles/guide.obj"));
		GL11.glPushMatrix();
		rotate(te);
		bindTexture(lectTex);
		model.renderOnly("Lectern");
		moveForBook(te,4f);
		bindTexture(bookTex);
		model.renderOnly("Book");
		GL11.glPopMatrix();
	}

	@Override
	public void renderNormal(Tessellator tess, TileEntity te, int x, int y, int z)
	{
		//rotate(te);
		GL11.glPushMatrix();
		if(!(te instanceof MagicGuide)) return;
		GuideData gd = GuideData.getGD(te);
		moveForBook(te,8);
		GL11.glTranslated(0.5, 0, 0.5);
		rotate(te);
		MagicGuide mg = (MagicGuide)te;
		mg.getMode();
		if(mg.getMode() == Mode.ANVIL)
			renderRecipe(gd, mg.getCurrentAnvilRecipe());
		if(mg.getMode() == Mode.CONTENTS)
			renderContents(gd);
		if(mg.getMode() == Mode.PAGES)
			renderPage(gd, mg.getGuidePage());
		GL11.glPopMatrix();
	}

	private void rotateToPage()
	{
		GL11.glRotated(90, 0, 1, 0);
		GL11.glRotated(-75, 1, 0, 0);
	}

	private void renderContents(GuideData gd)
	{
		GL11.glTranslated(0, 1, 0.2);
		rotateToPage();
		GL11.glScaled(0.01, 0.01, 0.01);
		LetterRenderer.render("manual",0,0, 0,0,0, 0.1f,0.2f,1);
	}

	private void renderPage(GuideData gd, IGuidePage page)
	{
		GL11.glTranslated(0, 1.1f, 0);
		page.render(gd);
	}

	private void renderRecipe(GuideData gd, IMagicAnvilRecipe currentAnvilRecipe)
	{
		if(currentAnvilRecipe == null) return;
		GL11.glTranslated(0,1.1f,0);
		GL11.glPushMatrix();
		GL11.glScaled(0.07, 0.07, 0.07);
		MagicAnvilRenderer.i.renderBlock(Tessellator.instance, null, 0, 0, 0);
		GL11.glPopMatrix();
		EntityItem[] eiArr = gd.getEIs(currentAnvilRecipe);
		GL11.glPushMatrix();
		GL11.glScaled(0.3, 0.3, 0.3);
		for(int i = 0; i < 3; i++)
			if(eiArr[i] != null)
				RenderManager.instance.renderEntityWithPosYaw(eiArr[i], 0,0,1.8-(0.6*i), 0,90);
		for(int i = 0; i < 3; i++)
			if(eiArr[3+i] != null)
				RenderManager.instance.renderEntityWithPosYaw(eiArr[3+i], 0,0,-(0.6+(0.6*i)), 0,90);
		IComponent[] c = currentAnvilRecipe.getDesiredComponent();
		if(c != null)
		{
			GL11.glPushMatrix();
			GL11.glRotated(-15, 0, 0, 1);
			GL11.glRotated(90, 0, 1, 0);
			GL11.glTranslated(0, -0.7, 0.5);
			GL11.glScaled(0.4, 0.4, 0.4);
			float xo = -((c.length -1) / 2f);
			GL11.glTranslated(xo, 0, 0);
			for(int i = 0; i < c.length; i++)
			{
				if(c[i] != null)
				{
					bindTexture(c[i].getIcon());
					RenderHelper.face(-0.5f, 1, -0.5f, 0.5f, 0.5f, c[i].getIconLocation(), true);
				}
				GL11.glTranslated(1, 0, 0);
			}
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
	}


	public static class GuideData
	{
		private IMagicAnvilRecipe lr;
		private EntityItem[] eiArr = new EntityItem[6];
		public float height = 0;

		public EntityItem[] getEIs(IMagicAnvilRecipe r)
		{
			if(r != lr)
			{
				ItemStack[] in = r.getDesiredItems();
				ItemStack[] out = r.getExpectedOutput();
				for(int i = 0; i < 3; i++)
					if(in != null)
						eiArr[i] = in[i] == null ? null : new EntityItem(Minecraft.getMinecraft().theWorld,0,0,0, in[i]);
					else
						eiArr[i] = null;
				for(int i = 0; i < 3; i++)
					if(out != null)
						eiArr[i+3] = out[i] == null ? null : new EntityItem(Minecraft.getMinecraft().theWorld,0,0,0, out[i]);
					else
						eiArr[i+3] = null;
				for(int i = 0; i < 6; i++)
				{
					if(eiArr[i] != null)
					{
						eiArr[i].hoverStart = 0.205f;
						eiArr[i].rotationYaw = 0;
					}
				}
				lr = r;
			}
			return eiArr;
		}

		private RenderBlockAccess lastRBA;
		private IMultiBlockStructure lstruct;
		public RenderBlockAccess getRBA(IMultiBlockStructure struct)
		{
			if(lstruct != struct)
			{
				lastRBA = new RenderBlockAccess(struct);
				lstruct = struct;
			}
			return lastRBA;
		}

		private static WeakHashMap<TileEntity,GuideData> map = new WeakHashMap();
		public static GuideData getGD(TileEntity te)
		{
			if(!map.containsKey(te))
				map.put(te, new GuideData());
			return map.get(te);
		}
	}
}
