package net.consumerjunk.privy.networking;

import net.consumerjunk.privy.Privy;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

public class PrivyController {

	public boolean inPrivy = false;
	public boolean clientRunning = false;

	public String host;
	public int port;
	public Socket clientSocket;
	public PrintWriter out;
	public BufferedReader in;

	public static ArrayList<String> recentMessages = new ArrayList<String>();

	public void startConnection(String host, int port, String password) {
		try {

			if(!Privy.privyController.clientRunning) {
				this.host = host;
				this.port = port;
				clientSocket = new Socket(host, port);
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				InputStream input = clientSocket.getInputStream();
				in = new BufferedReader(new InputStreamReader(input));

				Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Connected to Privy server."));
				out.println("_CLIENT_LOGIN:" + Minecraft.getMinecraft().thePlayer.getName() + ":" + UUID.nameUUIDFromBytes(password.getBytes()).toString());
				clientRunning = true;
				inPrivy = true;
				new Thread(new Connection(in)).start();
				recentMessages.clear();
			}

		} catch (IOException e) {
			Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("An error occurred while connecting to the server. (Is it on?)"));
		}

	}

	public static void endConnection() {

		if(Privy.privyController.clientRunning) {
			Privy.privyController.sendMessage("_CLIENT_DISCONNECTED");
			try {
				Privy.privyController.clientSocket.close();
			} catch (IOException e) {
			}
			Privy.privyController.clientRunning = false;
		}
	}

	public void sendMessage(String message) {
		out.println(message);
	}

}

class Connection implements Runnable {

	private BufferedReader in;

	public Connection(BufferedReader in) {
		this.in = in;
	}

	public void run() {

		String line;
		try {
			while((line = in.readLine()) != null) {
				if(line.startsWith("_SERVER_DISCONNECT:")) {
					Privy.privyController.endConnection();
					Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("Disconnected: " + line.replace("_SERVER_DISCONNECT:", "")));
				} else {
					if(line.startsWith("_SERVER_ERROR:")) {
						line = line.replace("_SERVER_ERROR:", "");
						Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("\u00A74\u00A7n" + line));
					} else if (line.startsWith("_MESSAGE:")) {
						line = line.replace("_MESSAGE:", "");
						Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString("\u00A77\u00A7o" + line));
					} else {
						Minecraft.getMinecraft().thePlayer.addChatMessage(new TextComponentString(line));
					}
				}
			}
		} catch (IOException e) {
		} finally {
			Privy.privyController.sendMessage("_CLIENT_DISCONNECTED");
			Privy.privyController.endConnection();
		}

	}

}
