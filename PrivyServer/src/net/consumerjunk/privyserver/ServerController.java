package net.consumerjunk.privyserver;

import com.sun.xml.internal.ws.handler.ClientLogicalHandlerTube;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Pattern;

public class ServerController {

	public static String password;
	public static int port = -2;

	public static ServerSocket currentServer;
	public static int connectedClients = 0;
	public static ArrayList<ClientController> connectedClientList = new ArrayList<ClientController>();

	public static void main(String args[]) {
		int passwordArg = -1;
		int portArg = -1;

		for (int x = 0; x < args.length; x++) {
			if (args[x].startsWith("password=")) passwordArg = x;
			if (args[x].startsWith("port=")) portArg = x;
		}

		try {
			if (portArg == -1) {
				System.out.println("Port not set.");
				return;
			}
			port = Integer.parseInt(args[portArg].replace("port=", ""));
		} catch (NumberFormatException e) {
			System.out.println("Port not valid.");
			return;
		}

		if (passwordArg != -1) {
			password = args[passwordArg].replace("password=", "");
		} else {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Set password:");
			password = scanner.nextLine();
			scanner.close();
		}

		try {

			currentServer = new ServerSocket(port);
			currentServer.setReuseAddress(true);
			while (true) {

				Socket client = currentServer.accept();
				System.out.println("New client from " + client.getInetAddress().getHostAddress());
				ClientHandler clientSocket = new ClientHandler(client);
				new Thread(clientSocket).start();

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (currentServer != null) {
				try {
					currentServer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static class ClientHandler implements Runnable {

		private final Socket clientSocket;

		public ClientHandler(Socket socket) {
			this.clientSocket = socket;
		}

		@Override
		public void run() {

			PrintWriter out = null;
			BufferedReader in = null;

			try {
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String line;
				while((line = in.readLine()) != null) {
					System.out.printf("From client: %s\n", line);
					out.println(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if(out != null) {
						out.close();
					}
					if(in != null) {
						in.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

	{/*
	int passwordArg = -1;
		int portArg = -1;

		for(int x = 0; x < args.length; x++) {
			if(args[x].startsWith("password=")) passwordArg = x;
			if(args[x].startsWith("port=")) portArg = x;
		}

		try {
			if(portArg == -1) {
				System.out.println("Port not set.");
				return;
			}
			port = Integer.parseInt(args[portArg].replace("port=", ""));
		} catch (NumberFormatException e) {
			System.out.println("Port not valid.");
			return;
		}

		if(passwordArg != -1) {
			password = args[passwordArg].replace("password=", "");
		} else {
			Scanner scanner = new Scanner(System.in);
			System.out.println("Set password:");
			password = scanner.nextLine();
			scanner.close();
		}

		currentServer = new Server();
		currentServer.start();

		Scanner scanner = new Scanner(System.in);
		boolean running = true;
		while (running) {
			if(scanner.hasNext()) {
				String line = scanner.nextLine();
				if (line.equalsIgnoreCase("stop") || line.equalsIgnoreCase("quit")) {
					currentServer.stopServer();
					running = false;
				}
			}
		}
		scanner.close();

	 */}
}

