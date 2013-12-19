package com.googlecode.zeroxcafe;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import org.apache.commons.math3.fraction.BigFraction;

/**
 * A utility class to convert numbers from one radix into another. It also
 * offers methods to check the compatibility of numbers with specific radixes.
 * 
 * @author Hans
 */
public final class MathUtils {

	/**
	 * Private constructor. No content.
	 */
	private MathUtils() {
	}

	/**
	 * Maximum number of loops to go through when converting a decimal number
	 * into a number of another radix. Otherwise there will be an infinity loop.
	 */
	private static final int CONVERT_FROM_DECIMAL_MAX_LOOPS = 200;

	/**
	 * This character is used in a converted number to show that all digits
	 * after it are the periodical fraction.
	 */
	public static final char PERIODICAL_FRACTION_INDICATOR = '|';

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
	 * @throws NullPointerException
	 *             if number is null
	 * @throws IllegalArgumentException
	 *             if radixFrom or radixTo is out of range
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

		if (hasDecimalPoint(number)
				&& number.indexOf(PERIODICAL_FRACTION_INDICATOR) == -1) {
			// remove trailing zeroes
			number = REMOVE_TRAILING_ZEROES.matcher(number).replaceFirst("");
		}
		// remove decimal point if there are no decimal digits
		if (number.length() > 0
				&& number.charAt(number.length() - 1) == DECIMAL_POINT) {
			number = number.substring(0, number.length() - 1);
		}
		// if number empty then just a zero
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
	private static BigFraction convertToDecimal(String number, int radix) {
		if (radix == 10)
			return BigFractionUtils.fromBigDecimal(new BigDecimal(
					trimNumber(number)));

		int decimalPoint = number.indexOf(DECIMAL_POINT);
		boolean hasDecimalPoint = decimalPoint != -1;

		if (!hasDecimalPoint)
			decimalPoint = number.length();

		BigFraction result = BigFraction.ZERO;

		if (decimalPoint != 0)
			result = new BigFraction(new BigInteger(number.substring(0,
					decimalPoint), radix));

		if (hasDecimalPoint) {
			BigInteger factor = BigInteger.ONE;
			BigInteger bigRadix = BigInteger.valueOf(radix);

			for (int i = decimalPoint + 1; i < number.length(); i++) {
				int digit = digit(number.charAt(i), radix);
				factor = factor.multiply(bigRadix);
				result = result.add(new BigFraction(BigInteger.valueOf(digit),
						factor));
			}
		}

		return result;
	}

	/**
	 * Converts a decimal number into a number of the given radix.
	 * 
	 * @param number
	 *            decimal number
	 * @param radix
	 *            target radix
	 * @return converted number in target radix
	 */
	private static String convertFromDecimal(BigFraction number, int radix) {
//		if (radix == 10)
//			return trimNumber(number.toPlainString());

		StringBuilder result = new StringBuilder();

		BigInteger integerPart=BigFractionUtils.integerPart(number);
		
		result.append(integerPart.toString(radix).toUpperCase(
				Locale.ENGLISH));
		number=number.subtract(integerPart);
		
		result.append(DECIMAL_POINT);

		int counter = 0;
		StringBuilder fractionalResult = new StringBuilder();
		List<BigFraction> fractionalPartList = new ArrayList<BigFraction>();

		while (number.compareTo(BigFraction.ZERO) > 0
				&& counter < CONVERT_FROM_DECIMAL_MAX_LOOPS) {

			int fractionalPartListIndex = fractionalPartList
					.indexOf(number);

			if (fractionalPartListIndex != -1) {
				fractionalResult.insert(fractionalPartListIndex,
						PERIODICAL_FRACTION_INDICATOR);
				break;
			}

			fractionalPartList.add(number);

			number = number.multiply(radix);
			BigInteger integerPartDigit = BigFractionUtils.integerPart(number);
			fractionalResult.append(integerPartDigit.toString(radix)
					.toUpperCase(Locale.ENGLISH));
			number = number.subtract(integerPartDigit);

			counter++;
		}

		result.append(fractionalResult);

		String res = trimNumber(result.toString());

		return res;
	}

	/**
	 * Formats a converted number. Currently this method just replaces the
	 * PERIODICAL_FRACTION_INDICATOR with an overline character.
	 * 
	 * @param number
	 *            converted number
	 * @throws NullPointerException
	 *             if number is null
	 * @return formatted number
	 */
	public static String format(String number) {
		if (number == null)
			throw new NullPointerException("number is null");

		if (number.indexOf(PERIODICAL_FRACTION_INDICATOR) != -1) {
			// number=number.replace("|","<span style=\"text-decoration:overline;\">")+"</span>";
			number = number.replace('|', '\u00AF');
		}

		return number;
	}
}