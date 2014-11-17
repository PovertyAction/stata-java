package org.matthewjwhite.stata.util;

import com.stata.sfi.SFIToolkit;

public class Conversion {
	public static double real(boolean b) {
		return b ? 1.0 : 0.0;
	}

	public static String string(double x) {
		return SFIToolkit.formatValue(x, "%24.0g").trim();
	}

	public static String string(boolean b) {
		return string(real(b));
	}
}
