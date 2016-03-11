package io.darkcraft.mod.client.renderer.tileent;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.darkcore.mod.abstracts.AbstractObjRenderer;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.LetterRenderer;
import io.darkcraft.mod.common.magic.items.staff.ItemStaffHelper;
import io.darkcraft.mod.common.magic.tileent.MagicStaffChanger;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

public class MagicStaffChangerRenderer extends AbstractObjRenderer
{
	IModelCustom model;
	ResourceLocation rl = new ResourceLocation(DarkcraftMod.modName, "textures/tileents/wood.png");

	@Override
	public AbstractBlock getBlock()
	{
		return ItemBlockRegistry.magicStaffChanger;
	}

	private void renderText(String s, int angle)
	{
		GL11.glPushMatrix();{
			GL11.glRotated(angle, 0, 1, 0);
			GL11.glTranslated(-0.95, -0.75, 1.01);
			GL11.glScalef(0.02f, 0.02f, 0.05f);
			LetterRenderer.render(s);
		}GL11.glPopMatrix();
	}

	@Override
	public void renderBlock(Tessellator tess, TileEntity te, int x, int y, int z)
	{
		if(model == null) model = AdvancedModelLoader.loadModel(new ResourceLocation(DarkcraftMod.modName,"models/tiles/staffChanger.obj"));
		if(!(te instanceof MagicStaffChanger))return;
		GL11.glPushMatrix();
		MagicStaffChanger msc = (MagicStaffChanger)te;
		bindTexture(rl);
		model.renderAll();
		GL11.glTranslated(0, 4, 0);
		GL11.glRotatef(180, 1, 0, 0);
		model.renderAll();
		GL11.glPopMatrix();
		ItemStaffHelper ish = msc.getStaff();
		if(ish != null)
		{
			GL11.glPushMatrix();
			GL11.glTranslated(0, 2, 0);
			ish.render();
			renderText("p  n    p n    p  n",0);
			GL11.glPopMatrix();
		}
		renderText("dark rises over",0);
		renderText("your hopes and",90);
		renderText("crushes them",180);
		renderText("    darkcraft",270);
	}

}
