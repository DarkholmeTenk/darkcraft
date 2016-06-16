package io.darkcraft.mod.client.renderer.tileent;

import org.lwjgl.opengl.GL11;

import io.darkcraft.darkcore.mod.abstracts.AbstractBlock;
import io.darkcraft.darkcore.mod.abstracts.AbstractBlockRenderer;
import io.darkcraft.darkcore.mod.datastore.Colour;
import io.darkcraft.darkcore.mod.helpers.RenderHelper;
import io.darkcraft.mod.client.renderer.LetterRenderer;
import io.darkcraft.mod.common.magic.tileent.MagicSymbol;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;

public class MagicSymbolRenderer extends AbstractBlockRenderer
{

	@Override
	public AbstractBlock getBlock()
	{
		return ItemBlockRegistry.magicSymbol;
	}

	@Override
	public void renderBlock(Tessellator tess, TileEntity te, int x, int y, int z)
	{
		if(!(te instanceof MagicSymbol)) return;
		MagicSymbol ms = (MagicSymbol) te;
		char c = ms.getCharacter();
		int h = LetterRenderer.height(c);
		int w = LetterRenderer.width(c);
		GL11.glTranslatef(0, 0.01f, 0);
		GL11.glRotated(90, -1, 0, 0);
		float s = (1/16f);
		GL11.glScalef(s, s, s);
		GL11.glTranslatef(8-(w/2), -8-(h/2), 0);
		if(ms.isActive())
			LetterRenderer.render(c, true, new Colour(0.7f, 0.7f, 0.95f), new Colour(0.1f,0.2f,0.9f,0.4f));
		else
			LetterRenderer.render(c, false, new Colour(0.7f, 0.7f, 0.7f), RenderHelper.white);
	}

}
