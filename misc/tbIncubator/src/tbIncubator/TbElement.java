package tbIncubator;

import java.util.Collections;
import java.util.List;

public class TbElement {

	public static String replaceAll(String str) {
		String replaceAll = str.replaceAll("( |,|<|>|\\?|=|!|\"|%|\\(|\\))", "_"); // Und
																			// andere
		replaceAll = replaceAll.replace('`', '_');
		// Verbodene
		// Zeichen
		if (replaceAll.isEmpty()) {
			replaceAll = "_";
		}
		if (replaceAll.charAt(0) == '-') {
			replaceAll = "m" + replaceAll.substring(1);
		}
		replaceAll = replaceAll.replaceAll("-", "");
		replaceAll = replaceAll.replaceAll("�", "ae");
		replaceAll = replaceAll.replaceAll("�", "oe");
		replaceAll = replaceAll.replaceAll("�", "�e");
		replaceAll = replaceAll.replaceAll("�", "Ae");
		replaceAll = replaceAll.replaceAll("�", "Oe");
		replaceAll = replaceAll.replaceAll("�", "�e");
		if (Character.isDigit(replaceAll.charAt(0))) {
			replaceAll = "_" + replaceAll;
		}
		while (replaceAll.contains("__")) {
			replaceAll = replaceAll.replaceAll("__", "_");
		}
		if (replaceAll.endsWith("_")) {
			replaceAll = replaceAll.substring(0, replaceAll.length() - 1);
		}
		if (replaceAll.startsWith("_") && replaceAll.length() > 1
				&& Character.isAlphabetic(replaceAll.charAt(1))) {
			replaceAll = replaceAll.substring(1);
		}
		return replaceAll;
	}

	public final String name;
	public final String pk;
	public final List<String> path;

	public TbElement(String name, List<String> path, String pk) {
		this.name = name;
		this.path = Collections.unmodifiableList(path);
		this.pk = pk;
	}

	public String getPackage() {
		StringBuilder sb = new StringBuilder();
		for (String p : path) {
			sb.append(p);
			sb.append(".");
		}
		sb.setLength(sb.length() - 1);
		return replaceAll(sb.toString());
	}

	public String toJavaName() {
		StringBuilder sb = new StringBuilder();
		sb.append(getPackage());
		sb.append(".");
		sb.append(name);
		return replaceAll(sb.toString());
	}

	public String getSimpleName() {
		return replaceAll(name.trim());
	}

}
