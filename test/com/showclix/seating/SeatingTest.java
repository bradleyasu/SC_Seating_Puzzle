package com.showclix.seating;

import static org.junit.Assert.assertEquals;

import java.util.Random;

import org.junit.Test;

import com.showclix.seating.exceptions.InvalidSeatException;
import com.showclix.seating.exceptions.MaximumRequestsExceededException;

/**
 * Seating Tests
 * 
 * Some basic unit tests. These could be a bit more thorough, but wanted to have
 * a start on some of the main units
 * 
 * @author Bradley Sheets
 * @version 1.0
 * @date May 7, 2019
 * 
 */
public class SeatingTest {

	/**
	 * Does some basic testing of the requestSeats method to see if seats are
	 * correctly found. It does this by creating 200 random seating arrangements
	 * and trying to place seats. TODO - Add additional complexity
	 * 
	 * @throws MaximumRequestsExceededException
	 */
	@Test
	public void testRequestSeats() throws MaximumRequestsExceededException {
		Random rand = new Random();
		for (int i = 0; i < 200; i++) {
			int rows = rand.nextInt(14) + 4; // Minimum of 4 rows
			int columns = rand.nextInt(22) + 4; // Minimum of 6 columns
			Seating seating = new Seating(rows, columns);

			String assignment = seating.requestSeats(4);
			int colStart = columns / 2 - 2 + 1;
			int colEnd = columns / 2 + 2;

			String expected = "R1C" + colStart + " - R1C" + colEnd;
			assertEquals(expected, assignment);

			assignment = seating.requestSeats(4);
			expected = "R2C" + colStart + " - R2C" + colEnd;
			assertEquals(expected, assignment);

			assignment = seating.requestSeats(1);
			if (columns > 4) {
				colStart = columns / 2 + 3;
				expected = "R1C" + colStart;
				assertEquals(expected, assignment);
			} else {
				colStart = columns / 2;
				expected = "R3C" + (colStart + 1);
				assertEquals(expected, assignment);
			}
		}
	}

	/**
	 * Tests that the Manhattan distance is correctly calculated and applied to
	 * the seats correctly by creating 200 random seating arrangements
	 * 
	 * @throws InvalidSeatException
	 */
	@Test
	public void testCalculateDistance() throws InvalidSeatException {
		Random rand = new Random();

		for (int i = 0; i < 200; i++) {
			int rows = rand.nextInt(14) + 4; // Minimum of 4 rows
			int columns = rand.nextInt(22) + 4; // Minimum of 6 columns
			Seating seating = new Seating(rows, columns);

			int seatRow = rand.nextInt(rows);
			int seatColumn = rand.nextInt(columns);

			int distance = Math.abs(seatRow - 0) + Math.abs(columns / 2 - seatColumn);
			assertEquals(distance, seating.getSeat(seatRow, seatColumn).getDistance());
		}

	}

	/**
	 * Creates a couple of seating arrangements and checks to see whether or not
	 * the seat validity is reflected correctly
	 */
	@Test
	public void testIsValidSeat() {
		Seating seating = new Seating(10, 10);

		assertEquals(false, seating.isValidSeat(11, 10));
		assertEquals(false, seating.isValidSeat(10, 10));
		assertEquals(true, seating.isValidSeat(0, 0));
		assertEquals(true, seating.isValidSeat(0, 0));
		assertEquals(true, seating.isValidSeat(5, 8));
	}

	/**
	 * Creates a couple of pre-reservations and applies them to a seating
	 * arrangement to see if the seat at certain locations is correctly
	 * reserved.
	 * 
	 * @throws InvalidSeatException
	 */
	@Test
	public void testPreReserveSeat() throws InvalidSeatException {
		Random rand = new Random();
		for (int i = 0; i < 200; i++) {
			Seating seating = new Seating(21, 24);
			int row = rand.nextInt(21);
			int column = rand.nextInt(24);

			row++; // Account for the seating arrangement columns and rows starting at 1
			column++;

			assertEquals(false, seating.isSeatReserved(row, column));
			seating.preReserveSeat(row, column);
			assertEquals(true, seating.isSeatReserved(row, column));

			// Find a new row and column in the seat arrangement that is not
			// equal to the last random seat
			row += 2;
			column += 2;
			row = row % 21;
			column = column % 24;

			row++; // Account for the seating arrangement columns and rows starting at 1
			column++;

			assertEquals(false, seating.isSeatReserved(row, column));
			seating.preReserveSeat("R" + row + "C" + column);
			assertEquals(true, seating.isSeatReserved(row, column));
		}

	}

}
