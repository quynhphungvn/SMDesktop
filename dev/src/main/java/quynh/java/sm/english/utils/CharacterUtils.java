package quynh.java.sm.english.utils;

public class CharacterUtils {
	public static String removeUnexpectedChar(String s) {
		return s.trim()
				.toLowerCase()
				.replace(".", "")
				.replace("-", "")
				.replace(",", "")
				.replace("\\", "")
				.replace("!", "")
				.replace("~", "")
				.replace("'?", "")
				.replace("?", "")
				.replace(":", "")
				.replace(";", "")
				.replace("\"", "")
				.replace("(", "")
				.replace(")", "")
				.replace("[", "")
				.replace("]", "")
				.replace("'ll", "")
				.replace("'d", "")
				.replace("'ve", "")
				.replace("'re", "")
				.replace("'m", "")
				.replace("'s", "");
	}
}
