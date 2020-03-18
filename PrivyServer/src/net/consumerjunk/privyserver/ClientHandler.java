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

	public ClientHandler(Socket client) throws IOException {
		this.client = client;
		this.out = new PrintWriter(client.getOutputStream(), true);
		this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
		this.uuid = UUID.randomUUID();
		sendMessage("_SERVER_VALIDATION_REQUEST");
	}

	public void sendDisconnectMessage(String reason) {
		System.out.println("Server: Sending disconnect message \"" + reason + "\" to client.");
		out.println("_SERVER_DISCONNECT:" + reason);
	}

	public void sendErrorMessage(String reason) {
		System.out.println("Server: Sending error message \"" + reason + "\" to client.");
		out.println("_SERVER_ERROR:" + reason);
	}

	public void sendMessage(String message) {
		out.println(message);
	}

	public UUID getUuid() {
		return uuid;
	}

	@Override
	public void run() {

		try {

			while (true) {
				String message = in.readLine();
				if (message.startsWith("_CLIENT_DISCONNECTED")) {
					System.out.println("Server: Client disconnected.");
				} else if (message.startsWith("_CLIENT_VALIDATE:")) {
					Server.setUsername(this.uuid, message.replace("_CLIENT_USERNAME:", ""));
				} else if (message.startsWith("_CLIENT_USERNAME:")) {
					Server.setUsername(this.uuid, message.replace("_CLIENT_USERNAME:", ""));
				} else {
					Server.messageReceived(this.uuid, message);
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
