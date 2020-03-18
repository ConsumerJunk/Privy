package net.consumerjunk.privy.gui;

import javafx.scene.input.KeyCode;
import net.consumerjunk.privy.chat.PrivyHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;

public class GuiPrivyChat extends GuiScreen {

	private GuiTextField messageField;

	public GuiPrivyChat() {

	}

	@Override
	public void initGui() {
		super.initGui();
		messageField = new GuiTextField(0, this.fontRendererObj, 4, this.height - 14, this.width - 8, 12);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		messageField.drawTextBox();
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
	}

	@Override
	protected void mouseClicked(int x, int y, int btn) throws IOException {
		super.mouseClicked(x, y, btn);
		this.messageField.mouseClicked(x, y, btn);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.handleInput();
		messageField.textboxKeyTyped(typedChar, keyCode);
		if(keyCode == 1) {
			Minecraft.getMinecraft().thePlayer.closeScreen();
		}
		if(keyCode == 28 || keyCode == 156) {
			PrivyHandler.privyClient.sendMessage(messageField.getText());
		}
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		messageField.updateCursorCounter();
	}
}