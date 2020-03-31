package net.consumerjunk.privy.gui;

import joptsimple.internal.Strings;
import net.consumerjunk.privy.Privy;
import net.consumerjunk.privy.networking.PrivyController;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiPrivyChat extends GuiChat {

	private GuiButton swapButton;
	private int index;

	public GuiPrivyChat(String defaultText) {
		super(defaultText);
	}

	@Override
	public void initGui() {
		index = -2;
		swapButton = new GuiButton(1, 0, this.height - 35, Privy.privyController.clientRunning && Privy.privyController.inPrivy ? "PUBLIC" : "PRIVY");
		swapButton.width = 50;
		swapButton.xPosition = this.width - swapButton.width - 2;

		this.buttonList.add(swapButton);
		String oldText = inputField != null ? inputField.getText() : null;
		super.initGui();
		inputField.width = inputField.width - 36;
		if(!Strings.isNullOrEmpty(oldText)) {
			inputField.setText(oldText);
		}

	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if(button.id == swapButton.id) {
			Minecraft.getMinecraft().ingameGUI.getChatGUI().clearChatMessages();
			mc.thePlayer.addChatMessage(new TextComponentString("Now chatting in " + (Privy.privyController.inPrivy && Privy.privyController.clientRunning ? "PUBLIC" : "PRIVY")));
			if(!Privy.privyController.clientRunning) {
				mc.displayGuiScreen(new GuiSetupConnnection());
			} else {
				Privy.privyController.inPrivy = !Privy.privyController.inPrivy;
				button.displayString = Privy.privyController.clientRunning && Privy.privyController.inPrivy ? "PUBLIC" : "PRIVY";
			}
		}
	}

	@Override
	public void keyTyped(char typedChar, int keyCode) throws IOException {

		if(inputField.isFocused() && PrivyController.recentMessages.size() > 0) {
			if(keyCode == Keyboard.KEY_UP) {
				if(index == -2) {
					index = PrivyController.recentMessages.size() - 1;
					System.out.println(index);
				} else {
					if(index > 0) {
						index --;
					}
				}
			}
			if(keyCode == Keyboard.KEY_DOWN) {
				if(index < PrivyController.recentMessages.size() - 1) {
					index ++;
				}
			}
			if(keyCode == Keyboard.KEY_DOWN || keyCode == Keyboard.KEY_UP) {
				System.out.println(index);
				if(index < PrivyController.recentMessages.size() - 1) {
					System.out.println(PrivyController.recentMessages.get(index));
				} else {
					System.out.println("New line!");
				}
			}
		}
		if(Privy.privyController.clientRunning && Privy.privyController.inPrivy) {
			if (keyCode != Keyboard.KEY_TAB) {
				super.keyTyped(typedChar, keyCode);
			}
		} else {
			super.keyTyped(typedChar, keyCode);
		}
	}

	@Override
	public void sendChatMessage(String message, boolean addToSentMessages) {

		if(Privy.privyController.clientRunning && Privy.privyController.inPrivy) {
			PrivyController.recentMessages.add(message);
			Privy.privyController.sendMessage(message);
		} else {
			super.sendChatMessage(message, addToSentMessages);
		}

	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		PrivyController controller = Privy.privyController;
		if(controller.clientRunning && controller.inPrivy) {
			drawRect(2, this.height - 14, this.width - 2, this.height - 2, 0xEE00AAEE);
		} else {
			drawRect(2, this.height - 14, this.width - 2, this.height - 2, Integer.MIN_VALUE);
		}
		swapButton.drawButton(mc, mouseX, mouseY);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

}
