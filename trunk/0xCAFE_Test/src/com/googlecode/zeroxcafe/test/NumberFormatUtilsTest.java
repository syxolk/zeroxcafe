package com.googlecode.zeroxcafe.test;

import com.googlecode.zeroxcafe.NumberFormatUtils;

import junit.framework.TestCase;

/**
 * TestCases for {@link com.googlecode.zeroxcafe.NumberFormatUtils}.
 * 
 * @author Hans
 */
public class NumberFormatUtilsTest extends TestCase {
	public void testFormat() {
		assertEquals("1.¯2", NumberFormatUtils.format("1.|2", '.'));
		assertEquals("1.2…", NumberFormatUtils.format("1.2+", '.'));
		assertEquals("1,2", NumberFormatUtils.format("1.2", ','));
	}

	public void testFormatException() {
		try {
			NumberFormatUtils.format(null, '.');
			fail("no exception");
		} catch (Exception e) {
			assertEquals(NullPointerException.class, e.getClass());
		}
	}

	public void testDeformat() {
		assertEquals("1.2", NumberFormatUtils.deformat("1,2", ','));
		assertEquals("1.2", NumberFormatUtils.deformat("1.2", '.'));
	}

	public void testDeformatException() {
		try {
			NumberFormatUtils.deformat("null", '.');
		} catch (Exception e) {
			assertEquals(NullPointerException.class, e.getClass());
		}
	}
}