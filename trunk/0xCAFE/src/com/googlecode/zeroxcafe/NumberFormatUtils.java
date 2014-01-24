package com.googlecode.zeroxcafe;

/**
 * Utility class for formatting and deformatting numbers.
 * 
 * @author Hans
 */
public final class NumberFormatUtils {
	private NumberFormatUtils() {
	}

	/**
	 * Overline character
	 */
	private static final char OVERLINE = '\u00AF';

	/**
	 * Character for unfinished decimal places (3 dots)
	 */
	private static final char UNFINISHED_DECIMAL_PLACES = '\u2026';

	/**
	 * HTML fragment that is inserted before a periodical fraction.
	 */
	private static final String OVERLINE_HTML_PREFIX = "<span style=\"text-decoration:overline;\">";

	/**
	 * HTML fragment that is inserted after a periodicla fraction.
	 */
	private static final String OVERLINE_HTML_SUFFIX = "</span>";

	/**
	 * Formats a converted number. Currently this method replaces the
	 * PERIODICAL_FRACTION_INDICATOR with an overline character and replaces the
	 * decimal point '.' to the localized character.
	 * 
	 * @param number
	 *            converted number
	 * @param localizedDecimalPoint
	 *            character for the decimal point
	 * @param html
	 *            true, if the formatted number is for HTML, otherwise false
	 * @throws NullPointerException
	 *             if number is null
	 * @return formatted number
	 */
	public static String format(String number, char localizedDecimalPoint,
			boolean html) {
		if (number == null)
			throw new NullPointerException("number is null");

		if (number.indexOf(MathUtils.PERIODICAL_FRACTION_INDICATOR) != -1) {
			if (html)
				number = number
						.replace(
								String.valueOf(MathUtils.PERIODICAL_FRACTION_INDICATOR),
								OVERLINE_HTML_PREFIX)
						+ OVERLINE_HTML_SUFFIX;
			else
				number = number.replace(
						MathUtils.PERIODICAL_FRACTION_INDICATOR, OVERLINE);
		}

		number = number.replace(MathUtils.DECIMAL_POINT, localizedDecimalPoint);

		number = number.replace(MathUtils.UNFINISHED_DECIMAL_PLACES_INDICATOR,
				UNFINISHED_DECIMAL_PLACES);

		return number;
	}

	/**
	 * Deformats a number from user input with localized decimal point.
	 * 
	 * @param number
	 *            number to deformat
	 * @param localizedDecimalPoint
	 *            character for the decimal point
	 * @return deformatted number
	 */
	public static String deformat(String number, char localizedDecimalPoint) {
		if (number == null)
			throw new NullPointerException("number is null");

		number = number.replace(localizedDecimalPoint, MathUtils.DECIMAL_POINT);

		return number;
	}
}