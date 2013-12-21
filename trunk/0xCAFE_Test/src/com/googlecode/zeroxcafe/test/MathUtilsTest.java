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
		assertTrue(isCompatible("347297120", 10));
		assertTrue(isCompatible("34f7d2a712e0", 16));
		assertTrue(isCompatible("0.12", 3));

		assertFalse(isCompatible("-0.1", 2));
		assertFalse(isCompatible("0.12", 2));
		assertFalse(isCompatible("03.12", 3));
		assertFalse(isCompatible("34729a7120", 10));
		assertFalse(isCompatible("34f7dg2a712e0", 16));
	}

	public void testIsCompatibleException() {
		try {
			isCompatible(null, 10);
			fail("no exception");
		} catch (Exception e) {
			assertEquals(NullPointerException.class.getName(), e.getClass()
					.getName());
		}

		try {
			isCompatible("", 1);
			fail("no exception");
		} catch (Exception e) {
			assertEquals(IllegalArgumentException.class.getName(), e.getClass()
					.getName());
		}

		try {
			isCompatible("", 37);
			fail("no exception");
		} catch (Exception e) {
			assertEquals(IllegalArgumentException.class.getName(), e.getClass()
					.getName());
		}
	}

	public void testHasMaximumOneDecimalPoint() {
		assertTrue(hasMaximumOneDecimalPoint("123.1"));
		assertTrue(hasMaximumOneDecimalPoint("AB.C"));
		assertTrue(hasMaximumOneDecimalPoint(".1234"));
		assertTrue(hasMaximumOneDecimalPoint("1234."));
		assertTrue(hasMaximumOneDecimalPoint("1234"));
		assertTrue(hasMaximumOneDecimalPoint(""));

		assertFalse(hasMaximumOneDecimalPoint("AB..C"));
		assertFalse(hasMaximumOneDecimalPoint("A.B.C"));
		assertFalse(hasMaximumOneDecimalPoint("ABC.."));
		assertFalse(hasMaximumOneDecimalPoint("..ABC"));
		assertFalse(hasMaximumOneDecimalPoint(".ABC."));
		assertFalse(hasMaximumOneDecimalPoint(".."));
	}

	public void testHasMaximumOneDecimalPointException() {
		try {
			hasMaximumOneDecimalPoint(null);
			fail("no exception");
		} catch (Exception e) {
			assertEquals(NullPointerException.class.getName(), e.getClass()
					.getName());
		}
	}

	public void testConvert() {
		assertEquals("10011010011", convert("10011010011", 2, 2));
		assertEquals("1200202", convert("10011010011", 2, 3));
		assertEquals("103103", convert("10011010011", 2, 4));
		assertEquals("14420", convert("10011010011", 2, 5));
		assertEquals("5415", convert("10011010011", 2, 6));
		assertEquals("3413", convert("10011010011", 2, 7));
		assertEquals("2323", convert("10011010011", 2, 8));
		assertEquals("1622", convert("10011010011", 2, 9));
		assertEquals("1235", convert("10011010011", 2, 10));
		assertEquals("A23", convert("10011010011", 2, 11));
		assertEquals("86B", convert("10011010011", 2, 12));
		assertEquals("740", convert("10011010011", 2, 13));
		assertEquals("643", convert("10011010011", 2, 14));
		assertEquals("575", convert("10011010011", 2, 15));
		assertEquals("4D3", convert("10011010011", 2, 16));

		assertEquals("10011010010", convert("2322", 8, 2));
		assertEquals("1200201", convert("2322", 8, 3));
		assertEquals("103102", convert("2322", 8, 4));
		assertEquals("14414", convert("2322", 8, 5));
		assertEquals("5414", convert("2322", 8, 6));
		assertEquals("3412", convert("2322", 8, 7));
		assertEquals("2322", convert("2322", 8, 8));
		assertEquals("1621", convert("2322", 8, 9));
		assertEquals("1234", convert("2322", 8, 10));
		assertEquals("A22", convert("2322", 8, 11));
		assertEquals("86A", convert("2322", 8, 12));
		assertEquals("73C", convert("2322", 8, 13));
		assertEquals("642", convert("2322", 8, 14));
		assertEquals("574", convert("2322", 8, 15));
		assertEquals("4D2", convert("2322", 8, 16));

		assertEquals("10011010010", convert("1234", 10, 2));
		assertEquals("1200201", convert("1234", 10, 3));
		assertEquals("103102", convert("1234", 10, 4));
		assertEquals("14414", convert("1234", 10, 5));
		assertEquals("5414", convert("1234", 10, 6));
		assertEquals("3412", convert("1234", 10, 7));
		assertEquals("2322", convert("1234", 10, 8));
		assertEquals("1621", convert("1234", 10, 9));
		assertEquals("1234", convert("1234", 10, 10));
		assertEquals("A22", convert("1234", 10, 11));
		assertEquals("86A", convert("1234", 10, 12));
		assertEquals("73C", convert("1234", 10, 13));
		assertEquals("642", convert("1234", 10, 14));
		assertEquals("574", convert("1234", 10, 15));
		assertEquals("4D2", convert("1234", 10, 16));

		assertEquals("1100101011111110", convert("CAFE", 16, 2));
		assertEquals("2122021200", convert("CAFE", 16, 3));
		assertEquals("30223332", convert("CAFE", 16, 4));
		assertEquals("3130331", convert("CAFE", 16, 5));
		assertEquals("1040330", convert("CAFE", 16, 6));
		assertEquals("304335", convert("CAFE", 16, 7));
		assertEquals("145376", convert("CAFE", 16, 8));
		assertEquals("78250", convert("CAFE", 16, 9));
		assertEquals("51966", convert("CAFE", 16, 10));
		assertEquals("36052", convert("CAFE", 16, 11));
		assertEquals("260A6", convert("CAFE", 16, 12));
		assertEquals("1A865", convert("CAFE", 16, 13));
		assertEquals("14D1C", convert("CAFE", 16, 14));
		assertEquals("105E6", convert("CAFE", 16, 15));
		assertEquals("CAFE", convert("CAFE", 16, 16));
	}

	public void testConvertException() {
		try {
			convert(null, 3, 4);
			fail("no exception");
		} catch (Exception e) {
			assertEquals(NullPointerException.class.getName(), e.getClass()
					.getName());
		}

		try {
			convert("1", 1, 4);
			fail("no exception");
		} catch (Exception e) {
			assertEquals(IllegalArgumentException.class.getName(), e.getClass()
					.getName());
		}

		try {
			convert("1", 37, 4);
			fail("no exception");
		} catch (Exception e) {
			assertEquals(IllegalArgumentException.class.getName(), e.getClass()
					.getName());
		}

		try {
			convert("1", 3, 1);
			fail("no exception");
		} catch (Exception e) {
			assertEquals(IllegalArgumentException.class.getName(), e.getClass()
					.getName());
		}

		try {
			convert("1", 3, 37);
			fail("no exception");
		} catch (Exception e) {
			assertEquals(IllegalArgumentException.class.getName(), e.getClass()
					.getName());
		}
	}

	public void testConvertBorderCases() {
		assertEquals("0", convert(".", 10, 2));
		assertEquals("0", convert("0.", 10, 2));
		assertEquals("0", convert(".0", 10, 2));

		assertEquals("0", convert(".", 3, 10));
		assertEquals("0", convert("0.", 3, 10));
		assertEquals("0", convert(".0", 3, 10));

		assertEquals("0", convert(".", 7, 13));
		assertEquals("0", convert("0.", 7, 13));
		assertEquals("0", convert(".0", 7, 13));
	}

	public void testConvertWithDecimalPoint() {
		assertEquals("10011010011.101", convert("10011010011.1010", 2, 2));
		assertEquals("1200202.|12", convert("10011010011.1010", 2, 3));
		assertEquals("103103.22", convert("10011010011.1010", 2, 4));
		assertEquals("14420.|30", convert("10011010011.1010", 2, 5));
		assertEquals("5415.343", convert("10011010011.1010", 2, 6));
		assertEquals("3413.|42", convert("10011010011.1010", 2, 7));
		assertEquals("2323.5", convert("10011010011.1010", 2, 8));
		assertEquals("1622.|5", convert("10011010011.1010", 2, 9));
		assertEquals("1235.625", convert("10011010011.1010", 2, 10));
		assertEquals("A23.|69", convert("10011010011.1010", 2, 11));
		assertEquals("86B.76", convert("10011010011.1010", 2, 12));
		assertEquals("740.|81", convert("10011010011.1010", 2, 13));
		assertEquals("643.8A7", convert("10011010011.1010", 2, 14));
		assertEquals("575.|95", convert("10011010011.1010", 2, 15));
		assertEquals("4D3.A", convert("10011010011.1010", 2, 16));

		assertEquals("1010011100.101", convert("1234.5", 8, 2));
		assertEquals("220202.|12", convert("1234.5", 8, 3));
		assertEquals("22130.22", convert("1234.5", 8, 4));
		assertEquals("10133.|30", convert("1234.5", 8, 5));
		assertEquals("3032.343", convert("1234.5", 8, 6));
		assertEquals("1643.|42", convert("1234.5", 8, 7));
		assertEquals("1234.5", convert("1234.5", 8, 8));
		assertEquals("822.|5", convert("1234.5", 8, 9));
		assertEquals("668.625", convert("1234.5", 8, 10));
		assertEquals("558.|69", convert("1234.5", 8, 11));
		assertEquals("478.76", convert("1234.5", 8, 12));
		assertEquals("3C5.|81", convert("1234.5", 8, 13));
		assertEquals("35A.8A7", convert("1234.5", 8, 14));
		assertEquals("2E8.|95", convert("1234.5", 8, 15));
		assertEquals("29C.A", convert("1234.5", 8, 16));

		assertEquals("10011010010.1", convert("1234.5", 10, 2));
		assertEquals("1200201.|1", convert("1234.5", 10, 3));
		assertEquals("103102.2", convert("1234.5", 10, 4));
		assertEquals("14414.|2", convert("1234.5", 10, 5));
		assertEquals("5414.3", convert("1234.5", 10, 6));
		assertEquals("3412.|3", convert("1234.5", 10, 7));
		assertEquals("2322.4", convert("1234.5", 10, 8));
		assertEquals("1621.|4", convert("1234.5", 10, 9));
		assertEquals("1234.5", convert("1234.5", 10, 10));
		assertEquals("A22.|5", convert("1234.5", 10, 11));
		assertEquals("86A.6", convert("1234.5", 10, 12));
		assertEquals("73C.|6", convert("1234.5", 10, 13));
		assertEquals("642.7", convert("1234.5", 10, 14));
		assertEquals("574.|7", convert("1234.5", 10, 15));
		assertEquals("4D2.8", convert("1234.5", 10, 16));

		assertEquals("10100001001000110100.01011011",
				convert("A1234.5B", 16, 2));
		assertEquals("2201020310.1123", convert("A1234.5B", 16, 4));
		assertEquals("22051352.20444043", convert("A1234.5B", 16, 6));
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