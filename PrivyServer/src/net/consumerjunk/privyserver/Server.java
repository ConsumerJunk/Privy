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

	private static int port;
	private static int limit = 10;
	public static String password = null;
	public static boolean secured = false;

	private static ArrayList<ClientHandler> clients = new ArrayList<>();
	private static HashMap<UUID, String> clientUsernames = new HashMap<>();
	private static ExecutorService pool;

	public static void main(String args[]) {

		for(String arg : args) {
			if(arg.startsWith("port=")) {
				String parseString = arg.replace("port=", "");
				try {
					port = Integer.parseInt(parseString);
				} catch (NumberFormatException e) {
					System.out.println("Invalid port \"" + parseString + "\". Stopping server.");
					return;
				}
			}
			if(arg.startsWith("limit=")) {
				String parseString = arg.replace("limit=", "");
				try {
					limit = Integer.parseInt(parseString);
				} catch (NumberFormatException e) {
					System.out.println("Invalid limit \"" + parseString + "\". Defaulting to 10.");
					return;
				}
			}
			if(arg.startsWith("limit=")) {
				String parseString = arg.replace("limit=", "");
				try {
					limit = Integer.parseInt(parseString);
				} catch (NumberFormatException e) {
					System.out.println("Invalid limit \"" + parseString + "\". Defaulting to 10.");
					return;
				}
			}
		}

		System.out.println("Staring server on port " + port + " with a client limit of " + limit + " and " + (secured ? "password \"" + password + "\"." : " with no password.");
		pool = Executors.newFixedThreadPool(limit + 1);

		try {

			ServerSocket listener = new ServerSocket(port);

			while(true) {

				System.out.println("Server: Waiting for client to connect...");
				Socket client = listener.accept();
				System.out.println("Server: Client connected.");
				ClientHandler clientHandler = new ClientHandler(client);
				clients.add(clientHandler);

				pool.execute(clientHandler);

				if(clients.size() > limit) {
					clientHandler.sendDisconnectMessage("Maximum client connections reached.");
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void setUsername(UUID clientUUID, String clientUsername) {
		if(clientUsernames.containsKey(clientUUID)) {
			System.out.println("Server: Client " + clientUUID + " attempted to register pre-existing username.");
		} else {
			clientUsernames.put(clientUUID, clientUsername);
			System.out.println("Server: Client " + clientUUID + " set username to " + clientUsername + ".");
		}
	}

	public static void messageReceived(UUID clientUUID, String message) {

		System.out.println(clientUsernames.containsKey(clientUUID)?clientUsernames.get(clientUUID):clientUUID.toString() + ": " + message);
		clients.forEach(client -> {
			if(!client.getUuid().equals(clientUUID)) {
				client.sendMessage(clientUsernames.get(clientUUID) + ": " + message);
			}
		});

	}

	public boolean isValid(String hash) {
		
	}
}

