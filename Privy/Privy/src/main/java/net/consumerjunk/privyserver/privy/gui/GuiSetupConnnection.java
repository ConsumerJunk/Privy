package net.consumerjunk.privyserver.privy.gui;

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
		connect = new GuiButton(0, this.width / 2 - BUTTON_WIDTH / 2, this.height/3*2, "Connect to Privy server");
		connect.width = BUTTON_WIDTH;
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
