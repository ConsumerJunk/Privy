package net.consumerjunk.privy.events;

import net.consumerjunk.privy.PrivyMain;
import net.consumerjunk.privy.chat.PrivyHandler;
import net.consumerjunk.privy.gui.GuiPrivyChat;
import net.consumerjunk.privy.gui.GuiSetupConnnection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import netscape.security.PrivilegeTable;

public class ClientEventHandler {

	@SubscribeEvent
	public void onGuiChange(GuiOpenEvent e) {
		if(e.getGui() instanceof GuiChat) {
			System.out.println("REPLACING!");
			if (PrivyHandler.privyEnabled) {
				e.setGui(new GuiPrivyChat());
			}
		}
	}

	@SubscribeEvent
	public void onUpdate(TickEvent.ClientTickEvent event) {

		if(PrivyMain.PRIVY_SETTINGS.isPressed()) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiSetupConnnection());
		}

		if(PrivyMain.SWITCH_CHAT.isPressed()) {
			PrivyHandler.privyEnabled = !PrivyHandler.privyEnabled;
			System.out.println("PRIVY: " + PrivyHandler.privyEnabled);
		}

	}

}
