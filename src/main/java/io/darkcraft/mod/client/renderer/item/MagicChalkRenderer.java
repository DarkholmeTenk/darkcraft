package io.darkcraft.mod.client.renderer.item;

import org.lwjgl.opengl.GL11;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import io.darkcraft.darkcore.mod.datastore.UVStore;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.common.magic.items.MagicChalk;
import io.darkcraft.mod.common.magic.systems.symbolic.ChalkType;

public class MagicChalkRenderer implements IItemRenderer
{


	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return true;
	}
	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		GL11.glPushMatrix();
		if(type.equals(ItemRenderType.EQUIPPED))
		{
			GL11.glRotated(45, 0, 1, 0);
			GL11.glRotated(-65, 1, 0, 0);
			GL11.glTranslated(0, -0.8, 0.75);
			GL11.glScaled(1.65, 1.65, 1.65);
			GL11.glRotated(90, 0, 1, 0);
		}
		else if(type.equals(ItemRenderType.EQUIPPED_FIRST_PERSON))
		{
			GL11.glRotated(40, 0, 1, 0);
			GL11.glTranslated(0, 0.6, 0);
			GL11.glScaled(1.65, 1.65, 1.65);
		}
		else if(type.equals(ItemRenderType.INVENTORY))
		{
			GL11.glRotated(45, -1, 0, 0);
			GL11.glTranslated(0,-0.1,0);
			GL11.glScaled(0.8, 0.8, 0.8);
		}
		renderChalk(MagicChalk.getISDamage(item), ChalkType.values()[item.getItemDamage()]);
		GL11.glPopMatrix();
	}

	private static final UVStore[] uvs = new UVStore[]{
		new UVStore(0.25,0.5,0,0.25),
		new UVStore(0.25,0.5,0,0.25),
		new UVStore(0,0.25,0,1),
		new UVStore(0,0.25,0,1),
		new UVStore(0,0.25,0,1),
		new UVStore(0,0.25,0,1)
	};
	private static final ResourceLocation tex = new ResourceLocation(DarkcraftMod.modName, "textures/items/chalk.png");
	private void renderChalk(double damage, ChalkType chalkType)
	{
		GL11.glDisable(GL11.GL_LIGHTING);
		RenderHelper.colour(chalkType.renderColour);
		RenderHelper.bindTexture(tex);
		RenderHelper.cube(-0.05f, -0.1f, -0.05f, 0.05f, (float)(0.4-(damage/2)), 0.05f, true, uvs);
		GL11.glEnable(GL11.GL_LIGHTING);
	}
}
