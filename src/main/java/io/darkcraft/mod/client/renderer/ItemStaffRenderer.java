package io.darkcraft.mod.client.renderer;

import io.darkcraft.mod.common.items.staff.ItemStaffHelper;
import io.darkcraft.mod.common.items.staff.ItemStaffHelperFactory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

public class ItemStaffRenderer implements IItemRenderer
{

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		ItemStaffHelper helper;
		if(item == null)
			helper = ItemStaffHelperFactory.getDefaultHelper();
		else
			helper = ItemStaffHelperFactory.getHelper(item);
		helper.render();

	}

}
