package com.showclix.testsuite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.showclix.seating.SeatTest;
import com.showclix.seating.SeatingTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
	SeatTest.class,
	SeatingTest.class
})

public class TestSuite {

}
