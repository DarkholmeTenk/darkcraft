package io.darkcraft.mod.client.renderer.item;

import org.lwjgl.opengl.GL11;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.items.ComponentBook;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ComponentBookRenderer implements IItemRenderer
{
	private static IModelCustom model;
	private static final ResourceLocation tex = new ResourceLocation(DarkcraftMod.modName,"textures/items/book.png");

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
		if(model == null) model = AdvancedModelLoader.loadModel(new ResourceLocation(DarkcraftMod.modName,"models/item/book.obj"));
		GL11.glScaled(0.8, 0.8, 0.8);
		if(type == ItemRenderType.EQUIPPED_FIRST_PERSON)
		{
			GL11.glTranslated(0.4, 1, 0);
			GL11.glRotated(60, 0, 0, 1);
		}
		else if(type == ItemRenderType.EQUIPPED)
		{
			GL11.glTranslated(0.5, 0.4, 0.5);
			GL11.glRotated(130, 0, 1, 0);
			GL11.glRotated(40, 0, 0, 1);
		}
		else if(type == ItemRenderType.INVENTORY)
			GL11.glRotated(-90, 0, 1, 0);
		else
		{
			GL11.glRotated(180, 1, 0, 0);
			GL11.glRotated(-74, 0, 0, 1);
			GL11.glScaled(0.7, 0.7, 0.7);
		}

		RenderHelper.bindTexture(tex);
		model.renderAll();
		IComponent c = ComponentBook.getComponent(item);
		if(c != null)
		{
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glRotated(-15, 0, 0, 1);
			GL11.glRotated(-90, 0, 1, 0);
			UVStore uv = c.getIconLocation();
			if(uv == null) uv = UVStore.defaultUV;
			RenderHelper.bindTexture(c.getIcon());
			RenderHelper.face(-0.5f, 0.15f, -0.5f, 0.5f, 0.5f, uv, true);
			GL11.glEnable(GL11.GL_LIGHTING);
		}
	}

}
