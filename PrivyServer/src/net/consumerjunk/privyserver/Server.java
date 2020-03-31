package net.consumerjunk.privyserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

	private static int port = -1;
	private static int limit = 10;
	public static String password = "";
	public static boolean secured = false;
	public static boolean allowNicks = false;

	public static ArrayList<ClientHandler> clients = new ArrayList<>();
	public static HashMap<UUID, String> clientUsernames = new HashMap<>();
	private static ExecutorService pool;

	public static void main(String args[]) {
		for (String arg : args) {
			if (arg.startsWith("port=")) {
				String parseString = arg.replace("port=", "");
				try {
					port = Integer.parseInt(parseString);
				} catch (NumberFormatException e) {
					System.out.println("Invalid port \"" + parseString + "\". Stopping server.");
					return;
				}
			} else if (arg.startsWith("limit=")) {
				String parseString = arg.replace("limit=", "");
				try {
					limit = Integer.parseInt(parseString);
				} catch (NumberFormatException e) {
					System.out.println("Invalid limit \"" + parseString + "\". Defaulting to 10.");
					return;
				}
			} else if (arg.startsWith("password=")) {
				String parseString = arg.replace("password=", "");
				password = parseString;
				secured = true;
			} else if (arg.startsWith("nicks=")) {
				String parseString = arg.replace("nicks=", "");
				allowNicks = parseString.contentEquals("true");
			} else {
				System.out.println("Ignoring argument \"" + arg + "\".");
			}
		}

		if(port == -1) {
			System.out.println("No port set, defaulting to 708");
			port = 708;
		}

		System.out.println();

		pool = Executors.newFixedThreadPool(limit + 1);
		TextFormatter.setup();

		try {

			ServerSocket listener = new ServerSocket(port);

			while (true) {

				System.out.println("Server: Waiting for client to connect...");
				Socket client = listener.accept();
				ClientHandler clientHandler = new ClientHandler(client);
				clients.add(clientHandler);
				System.out.println("Server: Client #" + clients.size() + " connected.");

				pool.execute(clientHandler);

				if (clients.size() > limit) {
					clientHandler.sendDisconnectMessage("Maximum client connections reached.");
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void setNick(UUID clientUUID, String nickname) {
		if(allowNicks) {
			boolean taken = false;
			String priorName = Server.clientUsernames.get(clientUUID);
			for(UUID uuid : clientUsernames.keySet()) {
				taken |= clientUsernames.get(uuid).equalsIgnoreCase(nickname);
			}
			if(!taken) {
				Server.messageReceived(null, priorName + " is now \"" + nickname + "\u00A7r\".");
				clientUsernames.remove(clientUUID);
				clientUsernames.put(clientUUID, nickname);
				sendMessageToPlayer(clientUUID, "Nickname set to " + nickname + "\u00A7r.");
			} else {
				sendMessageToPlayer(clientUUID, "Identity theft is not a joke, Jim!");
			}
		} else {
			sendMessageToPlayer(clientUUID, "This server doesn't allow nicknames :(");
			return;
		}
	}

	public static boolean sendMessageToPlayer(UUID player, String message) {
		clients.forEach(client -> {
			if(client.getUuid() == player) {
				client.sendMessage(message);
			}
		});
		return clientUsernames.containsKey(player);
	}

	public static boolean playerJoined(UUID clientUUID, String clientUsername) {
		if (clientUsernames.containsKey(clientUUID)) {
			System.out.println("Server: Client " + clientUUID + " attempted to register pre-existing username.");
			return false;
		} else {
			clientUsernames.put(clientUUID, clientUsername);
			System.out.println("Server: Client " + clientUUID + " set username to " + clientUsername + ".");
			messageReceived(null, clientUsername + " joined the chat.");
			return true;
		}
	}

	public static boolean playerLeft(UUID clientUUID) {
		messageReceived(null, clientUsernames.get(clientUUID) + "\u00A7r left the chat.");
		return true;
	}

	public static void messageReceived(UUID clientUUID, String message) {

		if(clientUUID == null) {
			clients.forEach(client -> {
				client.sendMessage(message);
			});
		} else if(clientUsernames.containsKey(clientUUID)) {
			String username = clientUsernames.get(clientUUID);
			String formattedMessage = TextFormatter.format(message);
			clients.forEach(client -> {
				client.sendMessage("<" + username + "\u00A7r> " + formattedMessage);
			});
		}

	}
}

