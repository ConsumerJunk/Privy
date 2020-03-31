package net.consumerjunk.privyserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.UUID;

public class ClientHandler implements Runnable {

	private Socket client;
	private PrintWriter out;
	private BufferedReader in;
	private UUID uuid;
	private boolean valid = false;
	private boolean disconnected = false;
	private UUID lastPM;

	public ClientHandler(Socket client) throws IOException {
		this.client = client;
		this.out = new PrintWriter(client.getOutputStream(), true);
		this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		this.uuid = UUID.randomUUID();
		this.lastPM = null;
	}

	public void sendDisconnectMessage(String reason) {
		System.out.println("Server: Sending disconnect message \"" + reason + "\" to client.");
		out.println("_SERVER_DISCONNECT:" + reason);
		try {
			out.close();
			in.close();
			System.out.println("Server: Client disconnected.");
			Server.clients.remove(this);
			disconnected = true;
		} catch (IOException e) {
			System.out.println("Server: There was an error disconnecting client " + uuid.toString() + ".");
		}
	}

	public void sendErrorMessage(String reason) {
		System.out.println("Server: Sending error message \"" + reason + "\" to client.");
		out.println("_SERVER_ERROR:" + reason);
	}

	public void sendMessage(String message) {
		out.println(TextFormatter.format(message));
	}

	public UUID getUuid() {
		return uuid;
	}

	@Override
	public void run() {

		try {

			while (!disconnected) {
				try {
					String message = in.readLine();
					if (message != null) {
						if (message.startsWith("_CLIENT_DISCONNECTED")) {
							System.out.println("Server: Client disconnected.");
							Server.clients.remove(this);
							this.disconnected = true;
							Server.playerLeft(uuid);
							client.close();
						} else if (message.startsWith("_CLIENT_LOGIN:")) {
							message = message.replace("_CLIENT_LOGIN:", "");
							String checkPassword = UUID.nameUUIDFromBytes(Server.password.getBytes()).toString();
							if (!message.contains(":")) {
								sendDisconnectMessage("Malformed login information.");
								return;
							}
							if (checkPassword.equals(message.split(":")[1])) {
								valid = Server.playerJoined(this.uuid, message.split(":")[0]);
							} else {
								sendDisconnectMessage("Incorrect password.");
							}
						} else if (message.startsWith("/nick")) {
							if(message.contains(" ")) {
								Server.setNick(this.uuid, TextFormatter.format(message.split(" ")[1], false));
							} else {
								sendErrorMessage("You must provide a nickname.");
							}
						} else if (message.startsWith("/emoji")) {
							sendMessage("====== EMOJI List ======");
							for(String emoticonTag : TextFormatter.emoticons.keySet()) {
								sendMessage(emoticonTag + " = " + TextFormatter.emoticons.get(emoticonTag));
							}
						} else if (message.startsWith("/colors")) {
							sendMessage("====== COLOR List ======");
							for(String colorKey : TextFormatter.colors.keySet()) {
								if(!colorKey.startsWith("_")) {
									sendMessage("\u00A7r> " + TextFormatter.colors.get(colorKey) + "$" + colorKey);
								} else {
									if(colorKey.startsWith("_")) {
										if(colorKey.startsWith("_B"))
											sendMessage("\u00A7r> " + TextFormatter.colors.get(colorKey) + "$" + colorKey + "\u00A7r (Bold)");
										if(colorKey.startsWith("_R"))
											sendMessage("\u00A7r> " + TextFormatter.colors.get(colorKey) + "$" + colorKey + "\u00A7r (Reset)");
										if(colorKey.startsWith("_I"))
											sendMessage("\u00A7r> " + TextFormatter.colors.get(colorKey) + "$" + colorKey + "\u00A7r (Italics)");
										if(colorKey.startsWith("_U"))
											sendMessage("\u00A7r> " + TextFormatter.colors.get(colorKey) + "$" + colorKey + "\u00A7r (Underline)");
										if(colorKey.startsWith("_S"))
											sendMessage("\u00A7r> " + TextFormatter.colors.get(colorKey) + "$" + colorKey + "\u00A7r (Strike)");
										if(colorKey.startsWith("_O"))
											sendMessage("\u00A7r> " + TextFormatter.colors.get(colorKey) + "$" + colorKey + "\u00A7r (Obfuscated)");
									}
								}
							}
						} else if (message.startsWith("/msg")) {
							if (!message.contains(" ")) {
								sendErrorMessage("You must provide a recipient and a message.");
								return;
							}
							if(message.replace("/msg " + message.split(" ")[1], "").contains(" ")) {
								String toUsername = message.split(" ")[1];
								String msg = message.replace("/msg " + message.split(" ")[1], "");
								if(Server.clientUsernames.containsValue(toUsername)) {
									UUID clientUUID = null;
									for(UUID check : Server.clientUsernames.keySet()) {
										if(Server.clientUsernames.get(check).equalsIgnoreCase(toUsername)) clientUUID = check;
										continue;
									}
									if(clientUUID == null) {
										sendErrorMessage("Player \"" + toUsername + "\" not found.");
									} else {
										sendMessage("_MESSAGE:[YOU to " + Server.clientUsernames.get(uuid) + "]:" + msg);
										Server.sendMessageToPlayer(clientUUID, "_MESSAGE:[" + Server.clientUsernames.get(uuid) + "]:" + msg);
									}
								} else {
									sendErrorMessage("Player \"" + toUsername + "\" not found.");
								}
							} else {
								sendErrorMessage("You must provide a message");
							}
						} else {
							if (valid) {
								if(!message.startsWith("/")) {
									Server.messageReceived(this.uuid, message);
								} else {
									sendErrorMessage("\"" + message.split(" ")[0] + "\" is not a valid command.");
								}
							}
						}
					}
				} catch (IOException e) {
					System.out.println("Server: Client disconnected unexpectedly.");
					Server.clients.remove(this);
					this.disconnected = true;
					Server.playerLeft(uuid);
					client.close();
				}
			}

		} catch (IOException e) {

			e.printStackTrace();
			System.err.println("IO Exception from client handler.");
			System.err.println(e.getStackTrace());

		} finally {
			out.close();
			try {
				in.close();
			} catch (IOException e) {

				System.err.println("IO Exception closing client buffered reader.");
				System.err.println(e.getStackTrace());

			}
		}

	}

}
