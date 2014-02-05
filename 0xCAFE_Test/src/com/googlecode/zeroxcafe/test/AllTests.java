package com.googlecode.zeroxcafe.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Perform all tests of 0xCAFE.
 * 
 * @author Hans
 */
public class AllTests extends TestCase {

	public static Test suite() {
		TestSuite suite = new TestSuite(AllTests.class.getName());
		//$JUnit-BEGIN$
		suite.addTestSuite(BigFractionUtilsTest.class);
		suite.addTestSuite(MathUtilsTest.class);
		suite.addTestSuite(NumberFormatUtilsTest.class);
		//$JUnit-END$
		return suite;
	}

}