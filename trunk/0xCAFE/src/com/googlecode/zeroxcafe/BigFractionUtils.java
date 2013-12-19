package com.googlecode.zeroxcafe;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.math3.fraction.BigFraction;

/**
 * Utility class for {@link BigFraction}.
 * 
 * @author Hans
 */
public final class BigFractionUtils {
	/**
	 * Private constructor. Do nothing.
	 */
	private BigFractionUtils() {
	}

	/**
	 * Convert a BigDecimal into a BigFraction object.
	 * 
	 * @param decimal
	 *            BigDecimal object
	 * @return BigFraction object
	 * @throws NullPointerException
	 *             if decimal is null
	 */
	public static BigFraction fromBigDecimal(BigDecimal decimal) {

		if (decimal == null)
			throw new NullPointerException("decimal is null");

		BigInteger denominator = BigInteger.ONE;

		while (decimal.subtract(new BigDecimal(decimal.toBigInteger()))
				.compareTo(BigDecimal.ZERO) > 0) {

			decimal = decimal.multiply(BigDecimal.TEN);
			denominator = denominator.multiply(BigInteger.TEN);
		}

		return new BigFraction(decimal.toBigInteger(), denominator);
	}

	/**
	 * Returns the integer part of the fraction. Example: 5/2 = 2 R 1; return 2
	 * 
	 * @param fraction
	 *            A {@link BigFraction} object
	 * @return the integer part as a {@link BigInteger}
	 * @throws NullPointerException
	 *             if fraction is null
	 */
	public static BigInteger integerPart(BigFraction fraction) {
		if (fraction == null)
			throw new NullPointerException("fraction is null");

		return fraction.getNumerator().divide(fraction.getDenominator());
	}
}