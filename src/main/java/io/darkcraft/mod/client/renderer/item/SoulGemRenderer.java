package io.darkcraft.mod.client.renderer.item;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.ClientProxy;
import io.darkcraft.mod.common.magic.items.SoulGem;
import io.darkcraft.mod.common.magic.items.SoulGem.Size;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class SoulGemRenderer implements IItemRenderer
{
	public static SoulGemRenderer i;
	IModelCustom[] models = new IModelCustom[SoulGem.Size.values().length];
	private ResourceLocation tex = new ResourceLocation(DarkcraftMod.modName,"textures/soul.png");
	private ResourceLocation btex = new ResourceLocation(DarkcraftMod.modName,"textures/soul.png");
	private ResourceLocation ftex = new ResourceLocation(DarkcraftMod.modName, "textures/souloverlay.png");

	{
		i = this;
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return true;
	}

	private void transferTexture(int tf, int tt)
	{
		GL13.glClientActiveTexture(tt);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL13.GL_COMBINE);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_COMBINE_RGB, GL11.GL_REPLACE);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_COMBINE_ALPHA, GL11.GL_REPLACE);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_SOURCE0_RGB, tf);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_SOURCE0_ALPHA, tf);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_OPERAND0_RGB, GL11.GL_SRC_COLOR);
		GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL13.GL_SOURCE0_ALPHA, GL11.GL_SRC_ALPHA);
	}

	private void testTex(ResourceLocation rl, boolean x, float r)
	{
		if(x)
		{
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			ARBShaderObjects.glUseProgramObjectARB(ClientProxy.enchShader);
			int un0 = ARBShaderObjects.glGetUniformLocationARB(ClientProxy.enchShader, "texture0");
			int un1 = ARBShaderObjects.glGetUniformLocationARB(ClientProxy.enchShader, "texture1");
			GL13.glClientActiveTexture(GL13.GL_TEXTURE3);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			RenderHelper.bindTexture(ftex);
			GL13.glClientActiveTexture(GL13.GL_TEXTURE0);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			RenderHelper.bindTexture(rl);
			ARBShaderObjects.glUniform1iARB(un0, 3);
			ARBShaderObjects.glUniform1iARB(un1, 3);
		}
		else
			RenderHelper.bindTexture(rl);
	}

	private void handleTexture(int m, float r)
	{
		float s = 1.0001f;
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glPushMatrix();
		GL11.glScalef(s, s, s);
		RenderHelper.bindTexture(ftex);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glRotatef((r/1495)*360, 0, 0, 1);
		GL11.glTranslatef(r/221, r/663, 0);
		models[m].renderAll();
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPopMatrix();
	}

	public void renderSoulGem(SoulGem.Size size, boolean full)
	{
		int p = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);
		int m = size.ordinal();
		long x = 3 * 5 * 13 * 17 * 23 * 1000;
		float r = (RenderHelper.getTime() % x) / 1000f;
		if(models[m] == null) models[m] = AdvancedModelLoader.loadModel(new ResourceLocation(DarkcraftMod.modName,"models/soulgem/"+size.name()+".obj"));
		GL11.glScalef(0.3f, 0.3f, 0.3f);
		//testTex(size==Size.Black?btex:tex,full,r);
		RenderHelper.bindTexture(size==Size.Black?btex:tex);
		models[m].renderAll();
		if(full)
			handleTexture(m,r);
		ARBShaderObjects.glUseProgramObjectARB(p);
		}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		int m = item.getItemDamage() % models.length;
		SoulGem.Size s = SoulGem.Size.values()[m];
		renderSoulGem(s, SoulGem.getSoulSize(item) != null);
	}

}
