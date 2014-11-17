package org.matthewjwhite.stata.error;

import com.stata.sfi.SFIToolkit;

public class StataError {
	public static void display(String message, Throwable throwable) {
		if (message != null & !message.equals(""))
			SFIToolkit.errorln(message);
		SFIToolkit.errorln("Java error message:\n" + throwable.getMessage());
	}

	public static void display(Throwable throwable) {
		display(null, throwable);
	}
}
