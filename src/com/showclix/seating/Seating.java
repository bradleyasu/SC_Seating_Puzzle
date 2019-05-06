package com.showclix.seating;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.showclix.seating.exceptions.InvalidSeatException;
import com.showclix.seating.exceptions.MaximumRequestsExceededException;
import com.showclix.seating.util.Output;
import com.showclix.seating.util.Settings;

/**
 * The Seating class is responsible for handling and deciding where reservations
 * should be placed based on Manhattan distance from the 1st row, center seat.
 * 
 * @author Bradley Sheets
 * @version 1.0
 * @date May 1, 2019
 * 
 */
public class Seating {

	// Total number of Rows in the seating chart
	private int rowCount;

	// Total number of columns (or seats per row) in the seating chart
	private int seatCount;

	// Matrix of Seat objects that make up the seating chart
	private Seat[][] seatingChart;

	// This data structure will keep a list of references to seat objects in order from best to worst seat based
	// on Manhattan distance
	private List<Seat> priorityList;

	// A variable that will be set in the constructor that represents the maximum number of seats a user can request 
	private int maxRequests;

	// A counter to keep track of how man seats are still available
	private int availableSeats = 0;

	public Seating(int rowCount, int seatCount) {
		this.seatCount = seatCount;
		this.rowCount = rowCount;

		this.priorityList = new ArrayList<>();

		this.maxRequests = Settings.getInstance().getInt("seating.requests.max", 10);
		// initialize data structure
		initializeSeatingChart();
	}

	/**
	 * This method should be called upon instantiation of the Seating object via
	 * the constructor in order to properly build the seating chart. The seating
	 * chart is a double array where each value is a Seat object. The Seat
	 * object will be aware of it's location as well as it's Manhattan distance
	 * from the "best seat in the house". By calculating this during
	 * construction, it can be referred to thereafter without having to
	 * calculate it elsewhere
	 */
	private void initializeSeatingChart() {
		// Initialize the array in memory
		seatingChart = new Seat[rowCount][seatCount];

		// Populate the rows and columns
		for (int row = 0; row < rowCount; row++) {
			for (int column = 0; column < seatCount; column++) {
				Seat seat = new Seat(row, column, calculateDistance(row, column));
				seatingChart[row][column] = seat;
				priorityList.add(seat);
			}
		}
		// To make the array into a "priority list", sort the array based on Manhattan distance
		Collections.sort(priorityList);
		
		// The default number of available seats is simply the number of rows multiplied by the number of columns
		availableSeats = rowCount * seatCount;
	}

	/**
	 * "Pre-Reservations" are reservations that are made before group placements
	 * are searched for. This method will put a pre-reservation on the seat at
	 * the row and column specified.
	 * 
	 * @param row
	 *            - row number of seat to be pre-reserved
	 * @param column
	 *            - column number of seat to pre-reserved
	 * @throws InvalidSeatException
	 */
	public void preReserveSeat(int row, int column) throws InvalidSeatException {
		// Account for the "off-by-one" behavior of the array
		row = row - 1;
		column = column - 1;

		// Assert that the seat is an actual seat that can be reserved
		if (isValidSeat(row, column)) {
			// Pre-Reserve the seat
			seatingChart[row][column].setPreReservation();
			availableSeats--;
		} else {
			throw new InvalidSeatException("No Seat available at row: " + row + " column: " + column);
		}
	}

	/**
	 * This is a wrapper method that will parse the Label version of a seat to
	 * find the row and column of the requested seat and reserve it
	 * 
	 * @param reservation  The seat label to reserve - example: R1C4
	 * @throws InvalidSeatException
	 */
	public void preReserveSeat(String reservation) throws InvalidSeatException {
		String[] parts = reservation.split("C");
		int row = Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));
		int col = Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
		preReserveSeat(row, col);
	}

	/**
	 * This method will call the helper 'findSeats' method to search for
	 * placement for the group size specified through the argument. Once a
	 * placement is found this method will reserve those seats so they are no
	 * longer available for future seating requests. This method will also
	 * handle situations where there are no available placements for the group
	 * size requested.
	 * 
	 * @param total
	 *            - Total size of the group that is looking to be seated
	 * @throws MaximumRequestsExceededException
	 */
	public String requestSeats(int total)
			throws MaximumRequestsExceededException {

		String seatLabel = "Not Available";

		if (total > maxRequests) {
			throw new MaximumRequestsExceededException(total + " has exceeded the maximum number of requets: " + maxRequests);
		}

		// Search for a seating placement
		List<Seat> seats = findSeats(total);
		// If a seating placement was found
		if (!seats.isEmpty()) {
			for (Seat seat : seats) {
				seat.setReserved();
			}

			// Format the output to show a range of seats
			if (seats.size() > 1) {
				seatLabel = seats.get(0) + " - " + seats.get(seats.size() - 1);

				// Or single seat depending on how many seats were requested
			} else {
				seatLabel = seats.get(0).toString();
			}

			// decrement the total number available seats by how many seats were just reserved
			availableSeats -= total;
		}

		return seatLabel;

	}

	public int getAvailableSeats() {
		return this.availableSeats;
	}

	/**
	 * Provide the total number of seats that are required for the group to be
	 * seated, this method will find the best group of seats available for
	 * seating. The return value from this method will be an array of the seats
	 * that can be reserved
	 * 
	 * @param total
	 *            - The total size of the group that is looking to be seated
	 * @return - The starting seat where the group can be assigned. This method
	 *         will return 'null' if there are locations for the group to be
	 *         placed
	 * 
	 */
	private List<Seat> findSeats(int total) {

		List<Seat> seats = new ArrayList<Seat>();

		// All of the seats have been pre-ordered. Iterate
		// over the list of seats from best to worst
		for (Seat prioritySeat : priorityList) {

			seats = checkNeighbors(prioritySeat, total);

			if (seats.size() < total) {
				seats.clear();
			} else {
				break;
			}

		}

		return seats;
	}

	/**
	 * Returns a list of all the seats surrounding the seat passed in
	 * (inclusive) by first checking if the seat is available. If it is, it will
	 * then check the seat to it's left, then to it's right, and then two over
	 * to the left, then two over to the right, etc, until there are no
	 * available seats or the total number of seats requested has been found
	 * 
	 * @param seat
	 *            - Seat to check the availability of and the availability of
	 *            it's neighboring seats
	 * @param total
	 *            - The total number of seats needed to be available
	 * @return A list of the best available seats around the neighbor. The
	 *         number of seats in the list will be equal to or less than the
	 *         total number requested depending on how many were found.
	 */
	private List<Seat> checkNeighbors(Seat seat, int total) {
		// The array of seats that will be returned
		List<Seat> seats = new ArrayList<Seat>();

		// By default we will be looking at the seat passed in, eg: the middle
		// seat so the offset and seat count are 0
		int offset = 0;
		int count = 0;

		// The multiplier will be used to check left, then right, then 2 left,
		// etc
		int multiplier = -1;

		// starting with the seat passed into the method, until the number of
		// seats found equals the total
		for (int column = seat.getColumn(); seats.size() < total; column += offset) {

			// Break if the seat isn't valid/out of bounds
			if (isValidSeat(seat.getRow(), column)) {
				
				// Get the next seat
				Seat temp = seatingChart[seat.getRow()][column];

				// check if it's reserved and if it's not, add it into the list
				if (!temp.isReserved()) {
					
					// Depending on which way we're checking, left or right, we will add it to the start
					// or end of the list
					if (multiplier == -1) {
						seats.add(temp);
					} else {
						seats.add(0, temp);
					}
				} else {
					break;
				}
				
				// increment the count and calculate the next offset
				count++;
				offset = count * multiplier;
				multiplier *= -1;
			} else {
				break;
			}
		}
		return seats;
	}

	/**
	 * Verifies that the seat number at the specified row and column is a an
	 * actual seat in the table
	 * 
	 * @param row
	 *            - Row of the seat to check
	 * @param column
	 *            - Column of the seat to check
	 * @return True if the seat is valid, False otherwise
	 */
	private boolean isValidSeat(int row, int column) {
		return (row >= 0 && row < rowCount && column >= 0 && column < seatCount);
	}

	/**
	 * Given a row and column value, calculate the distance from the best seat
	 * in the house which is specified per requirements as the first/top row,
	 * center seat - this is calculated using Manhattan distance
	 * 
	 * @param r
	 *            - The row number of the seat checking the distance for
	 * @param c
	 *            - The column number of the seat we're checking the distance
	 *            for
	 * @return The Manhattan distance of the seat relative to the row 1, center
	 *         seat
	 */
	private int calculateDistance(int r, int c) {
		return Math.abs(r - 0) + Math.abs(seatCount / 2 - c);
	}

	/**
	 * This method will print the seating table with 'X' values representing the
	 * pre-reserved seats in line 1 of the input and 'O' values representing the
	 * algorithmically selected seats based on the calculated "best" location
	 * available
	 */
	public void print() {
		for (int row = 0; row < rowCount; row++) {
			for (int column = 0; column < seatCount; column++) {
				Output.getInstance().print(" " + seatingChart[row][column].getSeatStatusSymbol() + " ");
			}
			Output.getInstance().print("\n");
		}

	}
}
