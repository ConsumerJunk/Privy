package net.consumerjunk.privy.gui;

import net.consumerjunk.privy.PrivyMain;
import net.consumerjunk.privy.chat.PrivyClient;
import net.consumerjunk.privy.chat.PrivyHandler;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;

public class GuiSetupConnnection extends GuiScreen {

	public GuiTextField ip;
	public GuiTextField password;
	public GuiButton connect;

	private static final int INPUT_WIDTH = 300;
	private static final int BUTTON_WIDTH = 80;

	@Override
	public void initGui() {
		ip = new GuiTextField(1, this.fontRendererObj, this.width / 2 - INPUT_WIDTH / 2, this.height / 3, 100, 20);
		ip.width = INPUT_WIDTH;
		password = new GuiTextField(1, this.fontRendererObj, this.width / 2 - INPUT_WIDTH / 2, this.height / 3 + 30, 100, 20);
		password.width = INPUT_WIDTH;
		connect = new GuiButton(0, this.width / 2 - BUTTON_WIDTH / 2, this.height/3*2, !PrivyHandler.privyClient.running ? "Connect" : "Disconnect");
		connect.width = BUTTON_WIDTH;
		buttonList.add(connect);
	}

	@Override
	public void updateScreen() {
		ip.updateCursorCounter();
		password.updateCursorCounter();
	}

	@Override
	protected void keyTyped(char par1, int par2) throws IOException {
		super.keyTyped(par1, par2);
		this.ip.textboxKeyTyped(par1, par2);
		this.password.textboxKeyTyped(par1, par2);
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
		if(button.id == connect.id) {
			System.out.println(button.id + " ?= " + connect.id);
			if(PrivyHandler.privyClient.running) {
				PrivyHandler.privyClient.disconnect();
			} else {
				System.out.println("CONNECTING");
				int port = 5421;
				if (ip.getText().contains(":")) {
					try {
						port = Integer.parseInt(ip.getText().split(":")[1]);
					} catch (NumberFormatException e) {
					}
				}
				PrivyHandler.privyClient.connect(ip.getText(), port);
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.drawDefaultBackground();
		ip.drawTextBox();
		password.drawTextBox();
		connect.drawButton(mc, mouseX, mouseY);
	}

}
