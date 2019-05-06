package com.showclix.seating;

/**
 * The Seat object contains all the information about a single seat and it's
 * location in the puppet show audience seating arrangement.
 * 
 * Each Seat can be compared to another seat. The Seat that is closest to the
 * front, middle seat will be considered the better of the two
 * 
 * @author Bradley Sheets
 * @version 1.0
 * @date May 6, 2019
 * 
 */
public class Seat implements Comparable<Seat> {

	// The row and column number of the seat
	private int rowNumber;
	private int colNumber;

	// The Manhattan distance from the front middle seat
	private int distance;

	// By default the seat is not reserved
	private boolean reserved = false;

	// A pre-reservation is like a "saved seat", where the seat has already
	// been reserved before the general public can requests seats
	private boolean preReservation = false;

	/**
	 * Constructing a seat object requires it's row, column, and distance from
	 * the front middle seat to be set.
	 * 
	 * @param rowNumber
	 *            - Row number of the seat
	 * @param colNumber
	 *            - Column number of the seat
	 * @param distance
	 *            = Manhattan distance from front row, middle seat
	 */
	public Seat(int rowNumber, int colNumber, int distance) {
		this.rowNumber = rowNumber;
		this.colNumber = colNumber;
		this.distance = distance;
	}

	/**
	 * Get the pre-calculated Manhattan distance from the front center seat
	 * 
	 * @return Distance
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * Get the column number of the seat
	 * 
	 * @return Column Number
	 */
	public int getColumn() {
		return colNumber;
	}

	/**
	 * Get the row number of the seat
	 * 
	 * @return Row Number
	 */
	public int getRow() {
		return rowNumber;
	}

	/**
	 * Based on the row and column number, this method will return a string
	 * representation of the seat. For example, Seat in row 4 column 3 will
	 * return "R4C3"
	 * 
	 * @return String representation of the seat location
	 */
	public String getSeatLabel() {
		return "R" + (rowNumber + 1) + "C" + (colNumber + 1);
	}

	/**
	 * Flags the seat as reserved so that it cannot be taken by anyone else
	 */
	public void setReserved() {
		reserved = true;
	}

	/**
	 * Removes the seat reservation so that someone else can have it
	 */
	public void removeReservation() {
		reserved = false;
	}

	/**
	 * Flags the seat as "pre-reserved" - this acts as a reservation, but is
	 * special in that it was reserved before the
	 * "doors opened to the general public"
	 */
	public void setPreReservation() {
		preReservation = true;
	}

	/**
	 * Remove any pre-reservations of the seat so that someone else can have it
	 */
	public void removePreReservation() {
		preReservation = false;
	}

	/**
	 * Checks the availability of the seat. If the seat is reserved, this method
	 * will return true to indicate someone has reserved it or placed a
	 * pre-reservation on it.
	 * 
	 * @return True if the seat is reserved, false if the seat is still
	 *         available
	 */
	public boolean isReserved() {
		return reserved || preReservation;
	}

	/**
	 * This method will return a character to indicate it's current availability
	 * with regard to it's availability.
	 * 
	 * @return '-' if the seat is available, 'X' if the seat was pre-reserved,
	 *         'O' if the seat was reserved
	 */
	public char getSeatStatusSymbol() {
		char symbol = '-';
		if (preReservation) {
			symbol = 'X';
		} else if (reserved) {
			symbol = 'O';
		}
		return symbol;
	}

	/**
	 * Seat objects can be printed with their string representation which is
	 * simply the R1C5 formatting
	 */
	@Override
	public String toString() {
		return getSeatLabel();
	}

	/**
	 * Each Seat can be compared to another seat. The Seat that is closest to
	 * the front, middle seat will be considered the better of the two
	 */
	@Override
	public int compareTo(Seat s) {
		return getDistance() - s.getDistance();
	}
}
