package net.consumerjunk.privyserver;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class ClientController extends Thread {

	protected Socket clientSocket;
	protected int clientId;

	public ClientController(Socket clientSocket) {
		this.clientSocket = clientSocket;
		ServerController.connectedClients ++;
		clientId = ServerController.connectedClients;
		System.out.println("Client " + clientId + " joined.");
	}

	public void run() {

		InputStream inp = null;
		BufferedReader brinp = null;
		DataOutputStream out = null;

		try {
			inp = clientSocket.getInputStream();
			brinp = new BufferedReader(new InputStreamReader(inp));
			out = new DataOutputStream(clientSocket.getOutputStream());
		} catch (IOException e) {
			return;
		}

		String line;

		while(true) {
			try {
				line = brinp.readLine();
				if((line==null) || line.equalsIgnoreCase("QUIT")) {
					ServerController.connectedClients --;
					clientSocket.close();
					System.out.println("Client " + clientId + " left.");
					return;
				} else {
					System.out.println("INCOMING");
					out.writeBytes(line + "\n\r" + "TEST");
					out.flush();
				}
			} catch (IOException e) {
				e.printStackTrace();
				ServerController.connectedClients --;
				ServerController.connectedClientList.remove(this);
				try {
					clientSocket.close();
				} catch (IOException ex){}
				return;
			}
		}

	}

}
