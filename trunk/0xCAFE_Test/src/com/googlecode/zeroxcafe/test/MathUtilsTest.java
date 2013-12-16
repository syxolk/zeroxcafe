package com.googlecode.zeroxcafe.test;

import static com.googlecode.zeroxcafe.MathUtils.convert;
import static com.googlecode.zeroxcafe.MathUtils.hasMaximumOneDecimalPoint;
import static com.googlecode.zeroxcafe.MathUtils.isCompatible;
import junit.framework.TestCase;

/**
 * TestCases for {@link com.googlecode.zeroxcafe.MathUtils}.
 * 
 * @author Hans
 */
public class MathUtilsTest extends TestCase {

	public void testIsCompatible() {
		assertTrue(isCompatible("01", 2));
		assertTrue(isCompatible("0.1", 2));
		assertFalse(isCompatible("-0.1", 2));
		assertFalse(isCompatible("0.12", 2));
	}

	public void testHasMaximumOneDecimalPoint() {
		assertTrue(hasMaximumOneDecimalPoint("123.1"));
		assertTrue(hasMaximumOneDecimalPoint("AB.C"));
		assertFalse(hasMaximumOneDecimalPoint("AB..C"));
		assertFalse(hasMaximumOneDecimalPoint("ABC.."));
		assertFalse(hasMaximumOneDecimalPoint("..ABC"));
		assertFalse(hasMaximumOneDecimalPoint(".ABC."));
	}

	public void testConvert() {
		assertEquals("10011010011", convert("10011010011", 2, 2));
		assertEquals("2323", convert("10011010011", 2, 8));
		assertEquals("1235", convert("10011010011", 2, 10));
		assertEquals("4D3", convert("10011010011", 2, 16));

		assertEquals("10011010010", convert("2322", 8, 2));
		assertEquals("2322", convert("2322", 8, 8));
		assertEquals("1234", convert("2322", 8, 10));
		assertEquals("4D2", convert("2322", 8, 16));

		assertEquals("10011010010", convert("1234", 10, 2));
		assertEquals("2322", convert("1234", 10, 8));
		assertEquals("1234", convert("1234", 10, 10));
		assertEquals("4D2", convert("1234", 10, 16));

		assertEquals("1100101011111110", convert("CAFE", 16, 2));
		assertEquals("145376", convert("CAFE", 16, 8));
		assertEquals("51966", convert("CAFE", 16, 10));
		assertEquals("CAFE", convert("CAFE", 16, 16));
	}

	public void testConvertBorderCases() {
		assertEquals("0", convert(".", 10, 2));
		assertEquals("0", convert("0.", 10, 2));
		assertEquals("0", convert(".0", 10, 2));
	}

	public void testConvertWithDecimalPoint() {
		assertEquals("10011010011.101", convert("10011010011.1010", 2, 2));
		assertEquals("2323.5", convert("10011010011.1010", 2, 8));
		assertEquals("1235.625", convert("10011010011.1010", 2, 10));
		assertEquals("4D3.A", convert("10011010011.1010", 2, 16));

		assertEquals("1010011100.101", convert("1234.5", 8, 2));
		assertEquals("1234.5", convert("1234.5", 8, 8));
		assertEquals("668.625", convert("1234.5", 8, 10));
		assertEquals("29C.A", convert("1234.5", 8, 16));

		assertEquals("10011010010.1", convert("1234.5", 10, 2));
		assertEquals("2322.4", convert("1234.5", 10, 8));
		assertEquals("1234.5", convert("1234.5", 10, 10));
		assertEquals("4D2.8", convert("1234.5", 10, 16));

		assertEquals("10100001001000110100.01011011",
				convert("A1234.5B", 16, 2));
		assertEquals("2411064.266", convert("A1234.5B", 16, 8));
		assertEquals("660020.35546875", convert("A1234.5B", 16, 10));
		assertEquals("A1234.5B", convert("A1234.5B", 16, 16));
	}

	public void testConvertWithDecimalPeriodical() {
		assertEquals("1.0|0011", convert("1.1", 10, 2));
		assertEquals("1.0|6314", convert("1.1", 10, 8));
		assertEquals("1.1|9", convert("1.1", 10, 16));
		
		assertEquals("1.|0011", convert("1.2", 10, 2));
		assertEquals("1.|1463", convert("1.2", 10, 8));
		assertEquals("1.|3", convert("1.2", 10, 16));
		
		assertEquals("1.0|1001", convert("1.3", 10, 2));
		assertEquals("1.2|3146", convert("1.3", 10, 8));
		assertEquals("1.4|C", convert("1.3", 10, 16));
		
		assertEquals("1.|0110", convert("1.4", 10, 2));
		assertEquals("1.|3146", convert("1.4", 10, 8));
		assertEquals("1.|6", convert("1.4", 10, 16));
		
		assertEquals("1.|1001", convert("1.6", 10, 2));
		assertEquals("1.|4631", convert("1.6", 10, 8));
		assertEquals("1.|9", convert("1.6", 10, 16));
		
		assertEquals("1.1|0110", convert("1.7", 10, 2));
		assertEquals("1.5|4631", convert("1.7", 10, 8));
		assertEquals("1.B|3", convert("1.7", 10, 16));
		
		assertEquals("1.|1100", convert("1.8", 10, 2));
		assertEquals("1.|6314", convert("1.8", 10, 8));
		assertEquals("1.|C", convert("1.8", 10, 16));
		
		assertEquals("1.1|1100", convert("1.9", 10, 2));
		assertEquals("1.7|1463", convert("1.9", 10, 8));
		assertEquals("1.E|6", convert("1.9", 10, 16));
	}
}