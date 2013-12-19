package com.googlecode.zeroxcafe.test;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.apache.commons.math3.fraction.BigFraction;

import junit.framework.TestCase;
import static com.googlecode.zeroxcafe.BigFractionUtils.fromBigDecimal;
import static com.googlecode.zeroxcafe.BigFractionUtils.integerPart;

/**
 * TestCases for {@link com.googlecode.zeroxcafe.BigFractionUtils}.
 * 
 * @author Hans
 */
public class BigFractionUtilsTest extends TestCase {

	public void testFromBigDecimal() {
		assertEquals(new BigFraction(1123, 1000),
				fromBigDecimal(new BigDecimal("1.123")));

		assertEquals(new BigFraction(10, 2), fromBigDecimal(new BigDecimal(
				"5.0")));

		assertEquals(new BigFraction(6543, 10000),
				fromBigDecimal(new BigDecimal(".6543")));
	}

	public void testFromBigDecimalException() {
		try {
			fromBigDecimal(null);
			fail("no exception");
		} catch (Exception e) {
			assertEquals(NullPointerException.class.getName(), e.getClass()
					.getName());
		}
	}

	public void testIntegerPart() {
		assertEquals(BigInteger.valueOf(5), integerPart(new BigFraction(20, 4)));
		assertEquals(BigInteger.valueOf(5), integerPart(new BigFraction(21, 4)));
		assertEquals(BigInteger.valueOf(5), integerPart(new BigFraction(22, 4)));
		assertEquals(BigInteger.valueOf(5), integerPart(new BigFraction(23, 4)));
		assertEquals(BigInteger.valueOf(6), integerPart(new BigFraction(24, 4)));
	}

	public void testIntegerPartException() {
		try {
			integerPart(null);
			fail("no exception");
		} catch (Exception e) {
			assertEquals(NullPointerException.class.getName(), e.getClass()
					.getName());
		}
	}
}