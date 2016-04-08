package io.darkcraft.mod.client;

import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.EntitySpellProjectileRenderer;
import io.darkcraft.mod.client.renderer.gui.SpellCreationGui;
import io.darkcraft.mod.client.renderer.gui.SpellSelectionGui;
import io.darkcraft.mod.client.renderer.gui.StatusOverlay;
import io.darkcraft.mod.client.renderer.item.ComponentBookRenderer;
import io.darkcraft.mod.client.renderer.item.ItemStaffRenderer;
import io.darkcraft.mod.client.renderer.item.MagicScrollRenderer;
import io.darkcraft.mod.client.renderer.item.SoulGemRenderer;
import io.darkcraft.mod.client.renderer.tileent.MagicAnvilRenderer;
import io.darkcraft.mod.client.renderer.tileent.MagicFieldMeasurerRenderer;
import io.darkcraft.mod.client.renderer.tileent.MagicGuideRenderer;
import io.darkcraft.mod.client.renderer.tileent.MagicStaffChangerRenderer;
import io.darkcraft.mod.client.renderer.tileent.MagicVortexCrystalRenderer;
import io.darkcraft.mod.client.renderer.tileent.MagicVortexRenderer;
import io.darkcraft.mod.client.renderer.tileent.SpellCreatorRenderer;
import io.darkcraft.mod.client.renderer.tileent.TechGeneratorRenderer;
import io.darkcraft.mod.common.CommonProxy;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.caster.PlayerCaster;
import io.darkcraft.mod.common.magic.entities.EntitySpellProjectile;
import io.darkcraft.mod.common.magic.tileent.MagicAnvil;
import io.darkcraft.mod.common.magic.tileent.MagicFieldMeasurer;
import io.darkcraft.mod.common.magic.tileent.MagicGuide;
import io.darkcraft.mod.common.magic.tileent.MagicStaffChanger;
import io.darkcraft.mod.common.magic.tileent.MagicVortex;
import io.darkcraft.mod.common.magic.tileent.MagicVortexCrystal;
import io.darkcraft.mod.common.magic.tileent.SpellCreator;
import io.darkcraft.mod.common.registries.ItemBlockRegistry;
import io.darkcraft.mod.common.tech.tileent.TechGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

public class ClientProxy extends CommonProxy
{
	boolean handled = false;
	KeyBinding keyBind;

	@Override
	public void init()
	{
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(StatusOverlay.i);
		MinecraftForgeClient.registerItemRenderer(ItemBlockRegistry.itemStaff, new ItemStaffRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(MagicFieldMeasurer.class, new MagicFieldMeasurerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(MagicVortex.class, new MagicVortexRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(MagicVortexCrystal.class, new MagicVortexCrystalRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(TechGenerator.class, new TechGeneratorRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(MagicStaffChanger.class, new MagicStaffChangerRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(SpellCreator.class, new SpellCreatorRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(MagicAnvil.class, MagicAnvilRenderer.i);
		ClientRegistry.bindTileEntitySpecialRenderer(MagicGuide.class, new MagicGuideRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ItemBlockRegistry.spellCreatorBlock),new SpellCreatorRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ItemBlockRegistry.magicAnvil),MagicAnvilRenderer.i);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ItemBlockRegistry.magicGuide),new MagicGuideRenderer());
		MinecraftForgeClient.registerItemRenderer(ItemBlockRegistry.soulGem, new SoulGemRenderer());
		MinecraftForgeClient.registerItemRenderer(ItemBlockRegistry.scroll, new MagicScrollRenderer());
		MinecraftForgeClient.registerItemRenderer(ItemBlockRegistry.compBook, new ComponentBookRenderer());
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellProjectile.class, new EntitySpellProjectileRenderer());
		ClientRegistry.registerKeyBinding(keyBind = new KeyBinding("darkcraft.key.open.desc", Keyboard.KEY_Y, "darkcraft.key.category"));
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		switch(id)
		{
			case 1397: return new SpellCreationGui((SpellCreator)te);
			case 1398: return new SpellSelectionGui();
		}
		return null;
	}

	@SubscribeEvent
	public void keyEvent(KeyInputEvent event)
	{
		if(!FMLClientHandler.instance().getClient().inGameHasFocus || FMLClientHandler.instance().isGUIOpen(GuiChat.class))
			return;
		if(keyBind.isPressed() && !handled)
		{
			handled = true;
			EntityPlayer pl = Minecraft.getMinecraft().thePlayer;
			Minecraft.getMinecraft().thePlayer.openGui(DarkcraftMod.i, 1398, pl.worldObj, (int) pl.posX, (int) pl.posY, (int) pl.posZ);
			return;
		}
		if(!keyBind.isPressed() && handled)
		{
			handled = false;
			return;
		}
		EntityPlayer pl = Minecraft.getMinecraft().thePlayer;
		PlayerCaster pc = Helper.getPlayerCaster(pl);
		if(!Helper.canCast(pl)) return;
		if(pl.isSneaking())
		{
			GameSettings gs = Minecraft.getMinecraft().gameSettings;
			KeyBinding[] kbs  = gs.keyBindsHotbar;
			int op = -1;
			for(int i = 0; (i < 10) && (i < kbs.length); i++)
			{
				if(kbs[i].getIsKeyPressed())
				{
					op = i;
					break;
				}
			}
			if(op > -1)
			{
				int slot = (op+1)%10;
				int i = pc.getHotkeyIndex(slot);
				if(i != -1)
				{
					pc.setCurrentSpell(i);
					while(kbs[op].isPressed());
					KeyBinding.setKeyBindState(kbs[op].getKeyCode(), false);
				}
			}
		}
	}
}
