package com.google.code.zeroxcafe;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * A utility class to convert numbers from one radix into another. It also
 * offers methods to check the compatibility of numbers with specific radixes.
 * 
 * @author Hans
 */
public class MathUtils {

	/**
	 * Maximum number of loops to go through when converting a decimal number
	 * into a number of another radix. Otherwise there will be an infinity loop.
	 */
	private static final int CONVERT_FROM_DECIMAL_MAX_LOOPS = 100;

	/**
	 * This class always uses the american decimal point for its parsing and
	 * converting.
	 */
	private static final char DECIMAL_POINT = '.';

	/**
	 * Pattern for removing leading zeroes.
	 */
	private static final Pattern REMOVE_LEADING_ZEROES = Pattern.compile("^0*");

	/**
	 * Pattern for removing trailing zeroes.
	 */
	private static final Pattern REMOVE_TRAILING_ZEROES = Pattern
			.compile("0*$");

	/**
	 * Checks whether a number is compatible to a radix. That means it returns
	 * true if there are only points (.) and numbers of that radix (e.g. for
	 * radix 10: 0 to 9) in the number.
	 * 
	 * @param number
	 *            number as a string
	 * @param radix
	 *            the radix to use (2..36)
	 * @return true, if compatible, otherwise false
	 * @throws NullPointerException
	 *             if number is null
	 * @throws IllegalArgumentException
	 *             if radix is outside of the supported range
	 */
	public static boolean isCompatible(String number, int radix) {
		if (number == null)
			throw new NullPointerException("number is null");
		if (radix < Character.MIN_RADIX || radix > Character.MAX_RADIX)
			throw new IllegalArgumentException("Radix out of supported range: "
					+ radix);

		for (int i = 0; i < number.length(); i++) {
			char ch = number.charAt(i);

			if (ch >= '0' && ch <= '9') {
				if (ch - '0' >= radix) {
					return false;
				}
			} else if (ch >= 'a' && ch <= 'z') {
				if (ch - 'a' + 10 >= radix) {
					return false;
				}
			} else if (ch >= 'A' && ch <= 'Z') {
				if (ch - 'A' + 10 >= radix) {
					return false;
				}
			} else if (ch != DECIMAL_POINT) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Checks whether a number has only one decimal point (.).
	 * 
	 * @param number
	 *            number as a string
	 * @return true, if the number has only one decimal point, otherwise false
	 * @throws NullPointerException
	 *             if number is null
	 */
	public static boolean hasMaximumOneDecimalPoint(String number) {
		if (number == null)
			throw new NullPointerException("number is null");

		boolean found = false;

		for (int i = 0; i < number.length(); i++) {
			if (number.charAt(i) == DECIMAL_POINT) {
				if (found) {
					return false;
				} else {
					found = true;
				}
			}
		}

		return true;
	}

	/**
	 * Prüft, ob eine Zahl einen Dezimal-Punkt hat. Checks whether a number has
	 * at least one decimal point (.).
	 * 
	 * @param number
	 *            number as a string
	 * @return true, if at least one decimal point, otherwise false
	 */
	private static boolean hasDecimalPoint(String number) {
		return number.indexOf(DECIMAL_POINT) != -1;
	}

	/**
	 * Converts a number from one radix into another. For that it uses the
	 * {@link java.math.BigDecimal} class if there is no decimal point in the
	 * number, otherwise it uses a self written method that may be slower.
	 * 
	 * @param number
	 *            number as a string
	 * @param radixFrom
	 *            source radix
	 * @param radixTo
	 *            target radix
	 * @return number in the target radix as a string
	 */
	public static String convert(String number, int radixFrom, int radixTo) {
		if (number == null)
			throw new NullPointerException("number is null");
		if (radixFrom < Character.MIN_RADIX || radixFrom > Character.MAX_RADIX)
			throw new IllegalArgumentException(
					"source radix is out of supported range: " + radixFrom);
		if (radixTo < Character.MIN_RADIX || radixTo > Character.MAX_RADIX)
			throw new IllegalArgumentException(
					"target radix is out of supported range: " + radixTo);

		if (hasDecimalPoint(number)) {
			return convertFromDecimal(convertToDecimal(number, radixFrom),
					radixTo);
		} else {
			return new BigInteger(number, radixFrom).toString(radixTo)
					.toUpperCase(Locale.ENGLISH);
		}
	}

	/**
	 * Converts a character of a radix into its decimal value. This method just
	 * wraps {@link java.lang.Character#digit(char, int)}
	 * 
	 * @param c
	 *            character
	 * @param radix
	 *            radix of a number system
	 * @return decimal number that was represented by the character
	 */
	private static int digit(char c, int radix) {
		return Character.digit(c, radix);
	}

	/**
	 * Removes all unneeded characters from a number: Removes all leading
	 * zeroes. If the number has a decimal point then it removes all trailing
	 * zeroes. If number ends with a decimal point then it removes it.
	 * 
	 * @param number
	 *            number to trim
	 * @return trimmed number
	 */
	private static String trimNumber(String number) {
		// remove trailing zeroes
		number = REMOVE_LEADING_ZEROES.matcher(number).replaceFirst("");

		if (hasDecimalPoint(number)) {
			// remove trailing zeroes
			number = REMOVE_TRAILING_ZEROES.matcher(number).replaceFirst("");
			// remove decimal point if there are no decimal digits
			if (number.charAt(number.length() - 1) == DECIMAL_POINT) {
				number = number.substring(0, number.length() - 1);
			}
		}
		if (number.length() == 0) {
			number = "0";
		}

		return number;
	}

	/**
	 * Converts a number from the given radix into a decimal number.
	 * 
	 * @param number
	 *            number as a string in the given radix
	 * @param radix
	 *            source radix
	 * @return decimal number
	 */
	private static BigDecimal convertToDecimal(String number, int radix) {
		if (radix == 10)
			return new BigDecimal(trimNumber(number));

		int decimalPoint = number.indexOf(DECIMAL_POINT);
		boolean hasDecimalPoint = decimalPoint != -1;

		if (decimalPoint == -1)
			decimalPoint = number.length();

		BigDecimal result = BigDecimal.ZERO;

		if (decimalPoint != 0)
			result = new BigDecimal(new BigInteger(number.substring(0,
					decimalPoint), radix));

		if (hasDecimalPoint) {
			BigDecimal factor = BigDecimal.ONE;
			BigDecimal bigRadix = new BigDecimal(radix);

			for (int i = decimalPoint + 1; i < number.length(); i++) {
				int digit = digit(number.charAt(i), radix);
				factor = factor.multiply(bigRadix);
				result = result.add(new BigDecimal(digit).divide(factor));
			}
		}

		return result;
	}

	/**
	 * Converts a decimal number into a number of the given radix.
	 * 
	 * @param number
	 *            decimal number
	 * @param base
	 *            target radix
	 * @return converted number in target radix
	 */
	private static String convertFromDecimal(BigDecimal number, int base) {
		if (base == 10)
			return trimNumber(number.toPlainString());

		StringBuilder result = new StringBuilder();

		String numberString = number.toPlainString();
		int decimalPoint = numberString.indexOf(DECIMAL_POINT);

		if (decimalPoint != -1) {

			if (decimalPoint != 0) {
				BigInteger integerPart = number.toBigInteger();
				result.append(integerPart.toString(base));
				number = number.subtract(new BigDecimal(integerPart));
			}

			result.append(DECIMAL_POINT);

			int counter = 0;

			while (number.compareTo(BigDecimal.ZERO) > 0
					&& counter < CONVERT_FROM_DECIMAL_MAX_LOOPS) {
				number = number.multiply(new BigDecimal(base));
				BigInteger integerPart = number.toBigInteger();
				result.append(integerPart.toString(base).toUpperCase(
						Locale.ENGLISH));
				number = number.subtract(new BigDecimal(integerPart));

				counter++;
			}

		} else {

			result.append(number.toBigInteger().toString(base));

		}

		String res = trimNumber(result.toString());

		return res;
	}
}