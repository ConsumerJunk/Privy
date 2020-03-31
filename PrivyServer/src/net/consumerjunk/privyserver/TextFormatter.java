package net.consumerjunk.privyserver;

import java.util.HashMap;

public class TextFormatter {

	public static HashMap<String, String> emoticons = new HashMap<>();
	public static HashMap<String, String> colors = new HashMap<>();

	public static void setup() {

		emoticons.put(":lenny:", "( ͡° ͜ʖ ͡°)");
		emoticons.put(":owo:", "(^ω^)");
		emoticons.put(":uwu:", "{uωu}");
		emoticons.put(":owoscared:", "{⚈ω⚈}");
		emoticons.put(":disapproval:", "ಠ_ಠ");
		emoticons.put(":shrug:", "¯\\_(ツ)_/¯");
		emoticons.put(":tm:", "™");
		emoticons.put(":<3:", "♥");
		emoticons.put(":note:", "♪");
		emoticons.put(":doublenote:", "♬");
		emoticons.put(":flip:", " (╯°□°）╯︵ ┻━┻");

		colors.put("dr", "\u00A74");
		colors.put("r", "\u00A7c");
		colors.put("o", "\u00A76");
		colors.put("y", "\u00A7e");
		colors.put("dg", "\u00A72");
		colors.put("g", "\u00A7a");
		colors.put("a", "\u00A7b");
		colors.put("dq", "\u00A73");
		colors.put("db", "\u00A71");
		colors.put("b", "\u00A79");
		colors.put("p", "\u00A7d");
		colors.put("dp", "\u00A75");
		colors.put("w", "\u00A7f");
		colors.put("s", "\u00A77");
		colors.put("ds", "\u00A78");
		colors.put("b", "\u00A70");
		colors.put("R", "\u00A7r");
		colors.put("B", "\u00A7l");
		colors.put("I", "\u00A7o");
		colors.put("U", "\u00A7n");
		colors.put("S", "\u00A7m");
		colors.put("O", "\u00A7k");

	}

	public static String format(String input) {
		return format(input, false);
	}

	public static String format(String input, boolean includeEmoji) {
		String returnString = input;
		if(includeEmoji) {
			for (String key : emoticons.keySet()) {
				returnString = returnString.replace(key, emoticons.get(key));
			}
		}
		for(String key : colors.keySet()) {
			returnString = returnString.replace("$" + key, colors.get(key));
		}
		return returnString;
	}

}
