package io.darkcraft.mod.client.renderer;

import io.darkcraft.mod.common.magic.items.staff.ItemStaffHelper;
import io.darkcraft.mod.common.magic.items.staff.ItemStaffHelperFactory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

public class ItemStaffRenderer implements IItemRenderer
{

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
		ItemStaffHelper helper;
		if(item == null)
			helper = ItemStaffHelperFactory.getDefaultHelper();
		else
			helper = ItemStaffHelperFactory.getHelper(item);
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
		else if(type == ItemRenderType.ENTITY)
		{
			GL11.glTranslated(0, 0.5, 0);
			GL11.glScaled(0.5, 0.5, 0.5);
		}
		helper.render();
		GL11.glPopMatrix();
	}

}
