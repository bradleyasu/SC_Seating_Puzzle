package com.showclix.seating;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Seat Tests
 * 
 * Again, some basic unit testing that could be more thorough.
 * 
 * @author Bradley Sheets
 * @version 1.0
 * @date May 7, 2019
 * 
 */
public class SeatTest {

	/**
	 * Creates a couple of seats and checks to see if
	 * the seat label matches the seats row and column information
	 * 
	 */
	@Test
	public void testGetSeatLabel() {
		Seat seatOne = new Seat(2,2, 0);
		Seat seatTwo = new Seat(1,9, 0);
		Seat seatThree = new Seat(8,7, 0);
		Seat seatFour = new Seat(30,1001, 0);
		
		assertEquals("R3C3", seatOne.getSeatLabel());
		assertEquals("R2C10", seatTwo.getSeatLabel());
		assertEquals("R9C8", seatThree.getSeatLabel());
		assertEquals("R31C1002", seatFour.getSeatLabel());
	}
	
	
	/**
	 * Creates a couple of seats and checks to see if the distance
	 * set in the seat is correctly set.  This method does not 
	 * test calculating the distance.  That is done in the Seating
	 * class
	 */
	@Test
	public void testGetDistance() {
		Seat seatOne = new Seat(1,1, 1008);
		Seat seatTwo = new Seat(1,1, 2);
		Seat seatThree = new Seat(1,1, 0);
		Seat seatFour = new Seat(1,1, 1880);
		
		assertEquals(1008, seatOne.getDistance());
		assertEquals(2, seatTwo.getDistance());
		assertEquals(0, seatThree.getDistance());
		assertEquals(1880, seatFour.getDistance());
	}
	
	
	/**
	 * Creats a couple of seats and checks to see if
	 * they were placed in the correct row
	 */
	@Test
	public void testGetRow() {
		Seat seatOne = new Seat(2,2, 0);
		Seat seatTwo = new Seat(1,9, 0);
		Seat seatThree = new Seat(8,7, 0);
		Seat seatFour = new Seat(30,1001, 0);
		
		assertEquals(2, seatOne.getRow());
		assertEquals(1, seatTwo.getRow());
		assertEquals(8, seatThree.getRow());
		assertEquals(30, seatFour.getRow());
	}
	
	
	/**
	 * Creates a couple of Seats and checks to see
	 * if they were placed in the correct column
	 */
	@Test
	public void testGetColumn() {
		Seat seatOne = new Seat(2,2, 0);
		Seat seatTwo = new Seat(1,9, 0);
		Seat seatThree = new Seat(8,7, 0);
		Seat seatFour = new Seat(30,1001, 0);
		
		assertEquals(2, seatOne.getColumn());
		assertEquals(9, seatTwo.getColumn());
		assertEquals(7, seatThree.getColumn());
		assertEquals(1001, seatFour.getColumn());
	}
	
	

	
}
