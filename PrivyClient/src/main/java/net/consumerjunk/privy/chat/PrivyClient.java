package net.consumerjunk.privy.chat;

import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class PrivyClient {

	public static String CLOSE_STRING = "QUIT";

	public boolean running = false;
	private Socket clientSocket = null;
	private DataOutputStream out;
	private BufferedReader in;

	public void messsageReceived(String message) {
		System.out.println(message);
		Minecraft.getMinecraft().thePlayer.sendChatMessage(message);
	}

	public void sendMessage(String message) {
		try {
			System.out.println("SENDING: " + message);
			if(message.equalsIgnoreCase(CLOSE_STRING)) {
				message = "§" + CLOSE_STRING;
			}
			out.writeBytes(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void connect(String server, int port) {
		if(running) {
			System.out.println("Client must be disconnected before connecting.");
		} else {
			try {
				Socket clientSocket = new Socket(server, port);
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				out = new DataOutputStream(clientSocket.getOutputStream());

				PrivyHandler.connection = new Connection();
				new Thread(PrivyHandler.connection).start();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void disconnect() {
		if(!running) {
			System.out.println("Client must be connected before disconnecting.");
		} else {
			try {
				out.writeBytes(CLOSE_STRING);
				clientSocket.close();
				running = false;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void restart(String server, int port) {
		disconnect();
		connect(server, port);
	}

	public class Connection implements Runnable {

		public void run() {
			String line;
			try {
				while ((line = in.readLine()) != null) {
					messsageReceived(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
