package org.matthewjwhite.stata.macro;

import org.matthewjwhite.stata.string.Quotes;

public class MacroList {
	public static String invtokens(String[] list, Quotes.Style style) {
		StringBuilder macroList = new StringBuilder();
		for (int i = 0; i < list.length; i++) {
			if (i > 0)
				macroList.append(" ");
			macroList.append(Quotes.adorn(list[i], style));
		}

		return macroList.toString();
	}

	public static String invtokens(String[] list) {
		return invtokens(list, Quotes.Style.LIST);
	}
}
