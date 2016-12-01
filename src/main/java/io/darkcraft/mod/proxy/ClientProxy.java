package io.darkcraft.mod.proxy;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBVertexShader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

import io.darkcraft.darkcore.mod.client.ShaderHandler;
import io.darkcraft.darkcore.mod.datastore.Pair;
import io.darkcraft.mod.DarkcraftMod;
import io.darkcraft.mod.client.renderer.EntitySpellProjectileRenderer;
import io.darkcraft.mod.client.renderer.gui.ChalkGui;
import io.darkcraft.mod.client.renderer.gui.DebugGui;
import io.darkcraft.mod.client.renderer.gui.SpellCreationGui;
import io.darkcraft.mod.client.renderer.gui.SpellSelectionGui;
import io.darkcraft.mod.client.renderer.gui.StatusOverlay;
import io.darkcraft.mod.client.renderer.tileent.TechGeneratorRenderer;
import io.darkcraft.mod.common.helpers.Helper;
import io.darkcraft.mod.common.magic.blocks.tileent.SpellCreator;
import io.darkcraft.mod.common.magic.entities.EntitySpellProjectile;
import io.darkcraft.mod.common.magic.gui.ChalkContainer;
import io.darkcraft.mod.common.magic.systems.spell.caster.PlayerCaster;
import io.darkcraft.mod.common.tech.tileent.TechGenerator;
import io.darkcraft.mod.proxy.ParticleHandler.ClientParticleHandler;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;

public class ClientProxy extends CommonProxy
{
	public static int enchShader;
	boolean handled = false;
	KeyBinding keyBind;

	{
		particleHandler = new ClientParticleHandler();
	}

	@Override
	public void init()
	{
		FMLCommonHandler.instance().bus().register(this);
		MinecraftForge.EVENT_BUS.register(StatusOverlay.i);
		ClientRegistry.bindTileEntitySpecialRenderer(TechGenerator.class, new TechGeneratorRenderer());
		RenderingRegistry.registerEntityRenderingHandler(EntitySpellProjectile.class, new EntitySpellProjectileRenderer());
		ClientRegistry.registerKeyBinding(keyBind = new KeyBinding("darkcraft.key.open.desc", Keyboard.KEY_Y, "darkcraft.key.category"));
		enchShader = ShaderHandler.getShaderProgram(
				new Pair(DarkcraftMod.class.getResourceAsStream("/assets/darkcraft/shaders/soul.vert"),ARBVertexShader.GL_VERTEX_SHADER_ARB),
				new Pair(DarkcraftMod.class.getResourceAsStream("/assets/darkcraft/shaders/soul.frag"),ARBFragmentShader.GL_FRAGMENT_SHADER_ARB));
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity te = world.getTileEntity(x, y, z);
		switch(id)
		{
			case 1397: return new SpellCreationGui((SpellCreator)te);
			case 1398: return new SpellSelectionGui(Helper.getPlayerCaster(player));
			case 1399: return new ChalkGui(new ChalkContainer(player));
			case 1000: return new DebugGui(player.inventoryContainer);
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
