package com.showclix.testsuite;

/**
 * The TestSuite is a wrapper class that will execute all of the other jUnit
 * tests
 * 
 * @author Bradley Sheets
 * @version 1.0
 * @date May 7, 2019
 * 
 */
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.showclix.seating.SeatTest;
import com.showclix.seating.SeatingTest;

// List of test classes to execute
@RunWith(Suite.class)
@Suite.SuiteClasses({
	SeatTest.class,
	SeatingTest.class
})

public class TestSuite {

}
