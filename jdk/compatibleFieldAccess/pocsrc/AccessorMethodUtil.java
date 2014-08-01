

public class AccessorMethodUtil {

	public static String determineAccessedFieldname(boolean get, String methodName,
			String value) {
		String fieldName;
		if (value == null || value.isEmpty()) {
			fieldName = methodName.startsWith(get ? "get" : "set") ? methodName
					.substring(3) : "";
			if (fieldName.length() > 1
					&& Character.isUpperCase(fieldName.charAt(0))
					&& Character.isLowerCase(fieldName.charAt(1))) {
				fieldName = fieldName.substring(0, 1).toLowerCase()
						+ fieldName.substring(1);
			}
		} else {
			fieldName = value;
		}
		return fieldName;
	}

}
