package org.matthewjwhite.stata.string;

import java.util.regex.Pattern;

public class Quotes {
	// Style of double quotes adornment. With the exception of Style.SIMPLE,
	// enclosed strings are adorned in compound double quotes if they contain
	// an embedded double quote.
	public enum Style {
		// Simple double quotes if the string does not contain
		// an embedded double quote; otherwise, compound double quotes.
		DEFAULT,
		SIMPLE,
		COMPOUND,
		// Quotes for macro lists, for instance, those specified to -foreach-:
		// a string is enclosed if it contains white space or a double quote or
		// if it is blank.
		LIST,
		/* Quotes for strings specified to the -local- and
		-global- commands (not other uses of macros). A string is enclosed if
		it contains leading or trailing white space, starts with a double quote,
		or starts with one of the operators "=" and ":". It is also enclosed if
		the command is located in a loop and the string contains
		multiple, consecutive internal white space characters:
		such white space is stripped in loops. See
		<http://www.stata.com/statalist/archive/2013-08/msg00888.html>. */
		MACRO, GLOBAL, LOCAL,
		/* Quotes for strings specified to -char define- (not other uses of
		characteristics): a string is enclosed if it contains leading or
		trailing white space or starts with a double quote, or if the command is
		located in a loop and the string contains
		multiple, consecutive internal white space characters. */
		CHAR
	}

	private static final Pattern white = Pattern.compile("[ \t]");
	private static final Pattern consecutiveWhite = Pattern.compile("[ \t]{2}");
	private static final Pattern openDoubleQuote = Pattern.compile("`?\"");
	private static final Pattern macroOperator = Pattern.compile("[=:]");

	/* Adorns a string with double quotes.

	s: The string to adorn. It is assumed that s can be enclosed in
		double quotes.
	style: The quotes style.
	loop: Whether the string is used in a loop. This affects adornment for
		certain styles. For instance, a string specified to
		a -local- command (Style.LOCAL) that is within a loop requires quotes if
		it contains multiple, consecutive internal white space characters,
		even if it does not require quotes for other reasons.
	*/
	public static String adorn(String s, Style style, boolean loop) {
		if (s == null)
			throw new NullPointerException();

		boolean adorn;
		// "dq" for "double quote"
		boolean dq = s.contains("\"");
		switch (style) {
			case SIMPLE:
				return "\"" + s + "\"";
				/*NOTREACHED*/
			case COMPOUND:
				return "`\"" + s + "\"'";
				/*NOTREACHED*/
			case LIST:
				adorn = s.equals("") || white.matcher(s).find() || dq;
				break;
			case MACRO:
			case GLOBAL:
			case LOCAL:
			case CHAR:
				adorn = white.matcher(s).lookingAt() ||
					white.matcher(s.substring(s.length() - 1)).find() ||
					openDoubleQuote.matcher(s).lookingAt();
				if (!adorn && loop)
					adorn = consecutiveWhite.matcher(s).find();
				if (!adorn && (style == Style.MACRO || style == Style.GLOBAL ||
					style == Style.LOCAL))
					adorn = macroOperator.matcher(s).lookingAt();
				break;
			case DEFAULT:
			default:
				adorn = true;
				break;
		}

		StringBuilder adorned = new StringBuilder(s.length() + 4);
		if (adorn) {
			if (dq)
				adorned.append('`');
			adorned.append('\"');
		}
		adorned.append(s);
		if (adorn) {
			adorned.append('\"');
			if (dq)
				adorned.append('\'');
		}
		return adorned.toString();
	}

	public static String adorn(String s, Style style) {
		return adorn(s, style, false);
	}

	public static String adorn(String s) {
		return adorn(s, Style.DEFAULT);
	}
}
