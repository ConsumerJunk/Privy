package net.consumerjunk.privy.gui;

import net.consumerjunk.privy.Privy;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.text.TextComponentString;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiSetupConnnection extends GuiScreen {

	public GuiTextField ip;
	public GuiTextField password;
	public GuiButton toggle;

	private static final float INPUT_WIDTH_MULTIPLE = 0.6f;
	private static final float PASSWORD_WIDTH_MULTIPLE = 0.8f;
	private static final int BUTTON_WIDTH = 80;

	public static String lastIP = "";
	public static String lastPassword = "";

	@Override
	public void initGui() {

		Keyboard.enableRepeatEvents(true);
		int inputWidth = Math.round(this.width * INPUT_WIDTH_MULTIPLE);
		ip = new GuiTextField(1, this.fontRendererObj, this.width / 2 - inputWidth / 2, this.height / 3, 100, 20);
		ip.width = inputWidth;
		ip.setText(lastIP);
		inputWidth = Math.round(inputWidth * PASSWORD_WIDTH_MULTIPLE);
		password = new GuiTextField(1, this.fontRendererObj, this.width / 2 - inputWidth / 2, this.height / 3 + 28, 100, 20);
		password.width = inputWidth;
		password.setText(lastPassword);
		toggle = new GuiButton(0, this.width / 2 - BUTTON_WIDTH / 2, this.height/3*2, Privy.privyController.clientRunning ? "Disconnect" : "Connect");
		toggle.width = BUTTON_WIDTH;
		buttonList.add(toggle);
		ip.setFocused(true);

	}

	@Override
	public void updateScreen() {
		ip.updateCursorCounter();
		password.updateCursorCounter();
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		this.ip.textboxKeyTyped(typedChar, keyCode);
		this.password.textboxKeyTyped(typedChar, keyCode);
		if((ip.isFocused() || password.isFocused()) && keyCode == Keyboard.KEY_RETURN) {
			actionPerformed(toggle);
		}
		if(keyCode == Keyboard.KEY_TAB) {
			if(ip.isFocused()) {
				ip.setFocused(false);
				password.setFocused(true);
			} else if(password.isFocused()) {
				password.setFocused(false);
				ip.setFocused(true);
			}
		}
		lastIP = ip.getText();
		lastPassword = password.getText();
	}

	@Override
	protected void mouseClicked(int x, int y, int btn) throws IOException {
		super.mouseClicked(x, y, btn);
		this.ip.mouseClicked(x, y, btn);
		this.password.mouseClicked(x, y, btn);
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		if(button.id == toggle.id) {
			if(!Privy.privyController.clientRunning) {
				String regex = "^(([a-z0-9-]+\\.)+[a-z]{2,6}|((25[0-5]|2[0-4][0-9]|[01]?[0-9]?[0-9])\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9]?[0-9]))(:(6553[0-5]|655[0-2]\\\\d|65[0-4]\\\\d{2}|6[0-4]\\d{3}|[1-5]\\d{4}|[1-9]\\d{0,3}))?(/[\\w/.]*)?$";
				if(ip.getText().replaceAll(regex, "").equalsIgnoreCase(ip.getText())) {
					boolean hasPort = ip.getText().contains(":");
					String ipName = ip.getText();
					int portNumber = 708;
					if(hasPort) {
						ipName = ip.getText().split(":")[0];
						try {
							portNumber = Integer.parseInt(ip.getText().split(":")[1]);
						} catch (NumberFormatException e) {
							Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Port must be numerical."));
							return;
						}
					}
					if(ip.getText().length() == 0) {
						Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Server IP is empty."));
						return;
					}
					Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Connecting to \"" + ipName + "\"" + (hasPort ? " on port #" + portNumber + "." : " on default port (708)." )));
					Minecraft.getMinecraft().displayGuiScreen(null);
					Privy.privyController.startConnection(ipName, portNumber, password.getText());
				} else {
					Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Invalid server IP."));
					return;
				}
			} else {
				Privy.privyController.endConnection();
				Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Disconnected from Privy server."));
				Minecraft.getMinecraft().displayGuiScreen(null);
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.drawDefaultBackground();
		ip.drawTextBox();
		password.drawTextBox();
		drawCenteredString(fontRendererObj, "Privy - Privatize chat", this.width / 2, this.height / 5, 0x23E1F8);
		if(ip.getText().length() == 0) {
			drawString(fontRendererObj, "Server IP", ip.xPosition + 4, this.height / 3 + 6, 0xB0B0B0);
		}
		if(password.getText().length() == 0) {
			drawString(fontRendererObj, "Server password", password.xPosition + 4, this.height / 3 + 34, 0xB0B0B0);
		}
		toggle.drawButton(mc, mouseX, mouseY);
	}

}
