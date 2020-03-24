package net.consumerjunk.privy.events;

import net.consumerjunk.privy.Privy;
import net.consumerjunk.privy.gui.GuiPrivyChat;
import net.consumerjunk.privy.gui.GuiSetupConnnection;
import net.consumerjunk.privy.networking.PrivyController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import org.lwjgl.input.Keyboard;

public class ClientEventHandler {

	private boolean isCommand = false;

	@SubscribeEvent
	public void onPlayerDisconnectEvent(FMLNetworkEvent.ClientConnectedToServerEvent event) {
		PrivyController.endConnection();
	}

	@SubscribeEvent
	public void onServerDisconnectEvent(FMLNetworkEvent.ServerDisconnectionFromClientEvent event) {
		PrivyController.endConnection();
	}

	@SubscribeEvent
	public void onGuiChange(GuiOpenEvent event) {
		if(event.getGui() != null) {
			if(event.getGui().getClass() == GuiMainMenu.class || event.getGui().getClass() == GuiMultiplayer.class) {
				PrivyController.endConnection();
			}
			if(event.getGui().getClass() == GuiChat.class) {
				//if (Privy.privyController.inPrivy && Privy.privyController.clientRunning) {
					event.setGui(new GuiPrivyChat(isCommand ? "/" : ""/*((GuiChat) event.getGui()).defaultInputFieldText*/));
				//}
			}
		}
	}

	@SubscribeEvent
	public void onChatMessageReceived(ClientChatReceivedEvent event) {
		if(Privy.privyController.clientRunning && Privy.privyController.inPrivy) {
			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onUpdate(TickEvent.ClientTickEvent event) {

		if(Privy.PRIVY_SETTINGS.isPressed()) {
			GuiScreen optionScreen = new GuiSetupConnnection();
			optionScreen.initGui();
			Minecraft.getMinecraft().displayGuiScreen(optionScreen);
		}

		if(Privy.SWITCH_CHAT.isPressed()) {
			if(Privy.privyController.clientRunning) {
				Privy.privyController.inPrivy = !Privy.privyController.inPrivy;
				Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages();
				Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Now chatting in " + (Privy.privyController.inPrivy && Privy.privyController.clientRunning ? "PRIVY" : "PUBLIC")));

			} else {
				Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Not connected to a Privy server."));
			}
		}

		if(Keyboard.isKeyDown(Keyboard.KEY_SLASH)) {
			isCommand = true;
		} else {
			isCommand = false;
		}

	}

}
