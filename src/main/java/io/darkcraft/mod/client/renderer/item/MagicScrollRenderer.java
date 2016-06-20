package io.darkcraft.mod.client.renderer.item;

import org.lwjgl.opengl.GL11;

import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.items.MagicScroll;
import io.darkcraft.mod.common.magic.systems.component.IComponent;
import io.darkcraft.mod.common.magic.systems.spell.CastType;
import io.darkcraft.mod.common.magic.systems.spell.Spell;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class MagicScrollRenderer implements IItemRenderer
{
	private ResourceLocation rl = new ResourceLocation(DarkcraftMod.modName,"textures/items/scroll.png");
	private IModelCustom model;

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
		if(model == null) model = AdvancedModelLoader.loadModel(new ResourceLocation(DarkcraftMod.modName,"models/item/scroll.obj"));
		RenderHelper.bindTexture(rl);
		GL11.glColor3f(1, 1, 1);
		if(type == ItemRenderType.EQUIPPED_FIRST_PERSON)
		{
			GL11.glRotated(0, 0, 1, 0);
			GL11.glRotated(-15, 0, 0, 1);
			GL11.glTranslated(0.4, 1, 0);
		}
		else if(type == ItemRenderType.EQUIPPED)
		{
			GL11.glRotated(-30, 0, 1, 0);
			GL11.glRotated(228, 0, 0, 1);
			GL11.glRotated(185, 1, 0, 0);
			GL11.glTranslated(-0.9, -0.1, -0.3);
		}
		else if(type == ItemRenderType.INVENTORY)
			GL11.glRotated(90, 0, 1, 0);
		else
		{
			GL11.glRotated(180, 0, 1, 0);
			GL11.glScaled(0.7, 0.7, 0.7);
			GL11.glTranslated(-0.2, 0, 0);
		}
		GL11.glScalef(0.5f, 0.5f, 0.5f);
		model.renderAll();
		Spell s = MagicScroll.getSpell(item);
		if((item == null) || (s == null)) return;
		{
			GL11.glPushMatrix();
			GL11.glRotated(90, 0, 1, 0);
			GL11.glRotated(180, 0, 0, 1);
			CastType t = s.type;
			RenderHelper.bindTexture(CastType.icon);
			RenderHelper.uiFace(-0.6f, -0.45f, 1f, 1f, -0.01f, t.uv, true);
			IComponent mc = s.mostExpensiveComponent.component;
			RenderHelper.bindTexture(mc.getIcon());
			RenderHelper.uiFace(-0.45f, -0.45f, 1f, 1f, -0.01f, mc.getIconLocation(), true);
			GL11.glPopMatrix();
		}
	}

}
