package net.consumerjunk.privy.chat;

import java.util.ArrayList;

public class PrivyHandler {

	public static ArrayList<String> publicChatMessages = new ArrayList<String>();
	public static ArrayList<String> privateChatMessages = new ArrayList<String>();
	public static PrivyClient.Connection connection;
	public static boolean privyEnabled = true;

	public static PrivyClient privyClient;

	public static void setup() {
		privyClient = new PrivyClient();
	}

}
