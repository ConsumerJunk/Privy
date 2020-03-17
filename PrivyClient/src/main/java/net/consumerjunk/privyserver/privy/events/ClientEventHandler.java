package net.consumerjunk.privyserver.privy.events;

import net.consumerjunk.privyserver.privy.PrivyMain;
import net.consumerjunk.privyserver.privy.gui.GuiSetupConnnection;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class ClientEventHandler {

	@SubscribeEvent
	public void onUpdate(TickEvent.ClientTickEvent event) {

		if(PrivyMain.PRIVY_SETTINGS.isKeyDown()) {
			Minecraft.getMinecraft().displayGuiScreen(new GuiSetupConnnection());
		}

		if(PrivyMain.SWITCH_CHAT.isKeyDown()) {

		}

	}

}
