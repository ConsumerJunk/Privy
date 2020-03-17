package net.consumerjunk.privyserver;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {

	private static Socket socket = null;
	private static ServerSocket server = null;
	private static InputStreamReader in = null;

	public void run() {
		try {
			startServer();
		} catch (IOException e) {
			System.out.println("Couldn't start server.\nError: " + e.getMessage());
		}
	}

	public void stopServer() {
		try {
			server.close();
		} catch (IOException e) {
			System.out.println("Error closing server");
		}
	}

	private static void startServer() throws IOException {

		server = new ServerSocket(ServerController.port);
		System.out.println("Starting on port: " + ServerController.port);

		while (true) {
			socket = server.accept();
			ClientController newClient = new ClientController(socket);
			newClient.start();
			ServerController.connectedClientList.add(newClient);
		}
	}

}
