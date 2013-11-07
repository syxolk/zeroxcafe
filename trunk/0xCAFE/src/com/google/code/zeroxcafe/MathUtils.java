package com.google.code.zeroxcafe;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Locale;

/**
 * Klasse zum Umrechnen von Zahlen in andere Basen und prüfen der Kompatibilät
 * von Zahlen auf bestimmte Basen.
 * 
 * @author Hans
 */
public class MathUtils {

	/**
	 * Prüft, ob eine Zahl kompatibel zu einer Basis ist, d.h. ob nur gültige
	 * Ziffern und Zeichen aus der Basis verwendet werden.
	 * 
	 * @param number
	 *            Zahl als String
	 * @param base
	 *            Basis
	 * @return true, wenn kompatibel, sonst false
	 */
	public static boolean isCompatible(String number, int base) {
		for (int i = 0; i < number.length(); i++) {
			char ch = number.charAt(i);

			if (ch >= '0' && ch <= '9') {
				if (ch - '0' >= base) {
					return false;
				}
			} else if (ch >= 'a' && ch <= 'z') {
				if (ch - 'a' + 10 >= base) {
					return false;
				}
			} else if (ch >= 'A' && ch <= 'Z') {
				if (ch - 'A' + 10 >= base) {
					return false;
				}
			} else if (ch != '.') {
				return false;
			}
		}

		return true;
	}

	/**
	 * Prüft, ob eine Zahl maximal einen Dezimal-Punkt besitzt.
	 * 
	 * @param number
	 *            Zahl
	 * @return true, wenn maximal ein Dezimalpunkt, sonst false
	 */
	public static boolean hasMaximumOneDecimalPoint(String number) {
		boolean found = false;

		for (int i = 0; i < number.length(); i++) {
			if (number.charAt(i) == '.') {
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
	 * Prüft, ob eine Zahl einen Dezimal-Punkt hat.
	 * 
	 * @param number
	 *            Zahl
	 * @return true, wenn mindestens ein Dezimal-Punkt.
	 */
	private static boolean hasDecimalPoint(String number) {
		return number.indexOf('.') != -1;
	}

	/**
	 * Rechnet eine Zahl von einer Basis in eine andere um. Dazu wird die
	 * BigInteger-Klasse verwendet.
	 * 
	 * @param number
	 *            Zahl als String
	 * @param baseFrom
	 *            altuelle Basis
	 * @param baseTo
	 *            Ziel-Basis
	 * @return Zahl in der Zahl-Basis als String
	 */
	public static String convert(String number, int baseFrom, int baseTo) {
		if (hasDecimalPoint(number)) {
			return convertFromInt(convertToInt(number, baseFrom), baseTo);
		} else {
			return new BigInteger(number, baseFrom).toString(baseTo).toUpperCase(Locale.ENGLISH);
		}
	}

	/**
	 * Wandelt ein Symbol eines Zahlensystems in seinen Dezimal-Wert um.
	 * 
	 * @param c
	 *            Symbol
	 * @param base
	 *            Basis des Zahlensystems
	 * @return
	 */
	private static int digit(char c, int base) {
		return Character.digit(c, base);
	}

	/**
	 * Wandelt einen Dezimal-Wert in in Zeichen eines Zahlensystems um. Dabei
	 * werden die die Zahlen ab 10 mit den Zeichen ab A repräsentiert.
	 * 
	 * @param digit
	 *            Dezimal-Wert
	 * @return Zeichen
	 */
	private static char character(int digit) {
		if (digit >= 0 && digit <= 9) {
			return (char) (digit + '0');
		} else if (digit >= 10 && digit <= 35) {
			return (char) (digit - 10 + 'A');
		} else {
			throw new IllegalArgumentException("Invalid digit: " + digit);
		}
	}

	/**
	 * Konvertiert eine Zahl einer beliebigen Basis in eine Dezimal-Zahl.
	 * 
	 * @param number
	 *            Zahl einer beliebigen Basis
	 * @param base
	 *            Eingabe-Basis
	 * @return Dezimal-Zahl
	 */
	private static BigDecimal convertToInt(String number, int base) {
		if (base == 10)
			return new BigDecimal(number);

		BigDecimal result = new BigDecimal(0);

		int decimalPoint = number.indexOf('.');
		boolean hasDecimalPoint = decimalPoint != -1;

		if (decimalPoint == -1)
			decimalPoint = number.length();

		for (int i = 0; i < decimalPoint; i++) {
			int digit = digit(number.charAt(decimalPoint - 1 - i), base);
			result = result.add(new BigDecimal(digit).multiply(new BigDecimal(
					base).pow(i)));
		}

		if (hasDecimalPoint) {
			for (int i = decimalPoint + 1; i < number.length(); i++) {
				int exponent = i - decimalPoint;
				int digit = digit(number.charAt(i), base);
				result = result.add(new BigDecimal(digit)
						.divide(new BigDecimal(base).pow(exponent)));
			}
		}

		return result;
	}

	/**
	 * Konvertiert eine Dezimal-Zahl in eine Zahl beliebiger Basis.
	 * 
	 * @param number
	 *            Dezimal-Zahl
	 * @param base
	 *            Ausgabe-Basis
	 * @return Zahl beliebiger Basis
	 */
	private static String convertFromInt(BigDecimal number, int base) {
		if (base == 10)
			return number.toPlainString();

		int smallestExponent = 0;

		while (number.compareTo(new BigDecimal(base).pow(smallestExponent)) >= 0) {
			smallestExponent++;
		}

		StringBuilder result = new StringBuilder();

		for (int ex = smallestExponent; ex >= 0; ex--) {
			for (int factor = base - 1; factor >= 0; factor--) {
				if (new BigDecimal(base).pow(ex)
						.multiply(new BigDecimal(factor)).compareTo(number) <= 0) {
					result.append(character(factor));
					number = number.subtract(new BigDecimal(base).pow(ex)
							.multiply(new BigDecimal(factor)));
					break;
				}
			}
		}

		if (!number.equals(BigDecimal.ZERO)) {
			result.append('.');
		}

		int ex = 1; // negative exponenten
		int MAX_EX = 100;
		while (!number.equals(BigDecimal.ZERO)) {
			for (int factor = base - 1; factor >= 0; factor--) {
				if (new BigDecimal(factor).divide(new BigDecimal(base).pow(ex))
						.compareTo(number) <= 0) {
					result.append(character(factor));
					number = number.subtract(new BigDecimal(factor)
							.divide(new BigDecimal(base).pow(ex)));
					break;
				}
			}
			ex++;
			if (ex >= MAX_EX)
				break;
		}

		String res = result.toString().replaceAll("^0*", "");
		if (hasDecimalPoint(res)) {
			res = res.replaceAll("0*$", "");
		}

		return res;
	}
}