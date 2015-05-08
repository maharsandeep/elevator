package com.sm.elevator.suite;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.sm.elevator.ElevatorExtremeCaseTest;
import com.sm.elevator.MultipleElevatorTest;
import com.sm.elevator.SingleElevatorTest;

/**
 * Test suite for Elevator application. Runs all test cases in this application
 */
public class ElevatorAppTestSuite extends TestCase {
	/**
	 * Create the test case
	 * 
	 */
	public ElevatorAppTestSuite(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(SingleElevatorTest.class);
		suite.addTestSuite(MultipleElevatorTest.class);
		suite.addTestSuite(ElevatorExtremeCaseTest.class);
		return suite;
	}
}
