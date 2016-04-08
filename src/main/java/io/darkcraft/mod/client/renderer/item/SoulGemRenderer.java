package io.darkcraft.mod.client.renderer.item;

import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.items.SoulGem;
import io.darkcraft.mod.common.magic.items.SoulGem.Size;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

public class SoulGemRenderer implements IItemRenderer
{
	IModelCustom[] models = new IModelCustom[SoulGem.Size.values().length];
	private ResourceLocation tex = new ResourceLocation(DarkcraftMod.modName,"textures/soul.png");
	private ResourceLocation btex = new ResourceLocation(DarkcraftMod.modName,"textures/soul.png");

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

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		int m = item.getItemDamage() % models.length;
		SoulGem.Size s = SoulGem.Size.values()[m];
		if(models[m] == null) models[m] = AdvancedModelLoader.loadModel(new ResourceLocation(DarkcraftMod.modName,"models/soulgem/"+s.name()+".obj"));
		if(s == Size.Black)
			RenderHelper.bindTexture(btex);
		else
			RenderHelper.bindTexture(tex);
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		models[m].renderAll();
	}

}
