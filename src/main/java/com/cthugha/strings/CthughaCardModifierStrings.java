package com.cthugha.strings;

import java.util.Map;

public class CthughaCardModifierStrings {
	public String NAME;
	public String DESCRIPTION;
	public String[] EXTENDED_DESCRIPTION = null;

	static Map<String, CthughaCardModifierStrings> strings = null;

	public static void init(Map<String, CthughaCardModifierStrings> strings) {
		CthughaCardModifierStrings.strings = strings;
	}

	public static CthughaCardModifierStrings get(String id) {
		return strings.get(id);
	}
}
