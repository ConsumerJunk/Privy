package net.consumerjunk.privyserver;

import java.util.HashMap;

public class EmoticonReplacer {

	public static HashMap<String, String> emoticons = new HashMap<>();

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

	}

	public static String replaceEmoticons(String input) {
		String returnString = input;
		for(String key : emoticons.keySet()) {
			returnString = returnString.replace(key, emoticons.get(key));
		}
		return returnString;
	}

}
