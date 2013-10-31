package com.google.code.zeroxcafe;

import java.math.BigInteger;

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
			} else {
				return false;
			}
		}

		return true;
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
		return new BigInteger(number, baseFrom).toString(baseTo);
	}
}