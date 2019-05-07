package com.showclix.seating;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SeatingTest {
	@Test
	public void testIsValidSeat() {
		Seating sOne = new Seating(10, 10);
		assertEquals(false, sOne.isValidSeat(11, 10));
		assertEquals(false, sOne.isValidSeat(10, 10));
		assertEquals(true, sOne.isValidSeat(0, 0));
		assertEquals(true, sOne.isValidSeat(0, 0));
		assertEquals(true, sOne.isValidSeat(5, 8));
	}
	
}
