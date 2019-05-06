package com.showclix.seating;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Seating class is responsible for handling and deciding where reservations should
 * be placed based on Manhattan distance from the 1st row, center seat.
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
	
	private List<Seat> priorityList;


	public Seating(int rowCount, int seatCount) {
		this.seatCount = seatCount;
		this.rowCount = rowCount;

		this.priorityList = new ArrayList<>();
		
		// initialize data structure
		initializeSeatingChart();
	}

	/**
	 * This method should be called upon instantiation of the Seating object via the constructor in
	 * order to properly build the seating chart.  The seating chart is a double array where each
	 * value is a Seat object.  The Seat object will be aware of it's location as well as it's 
	 * Manhattan distance from the "best seat in the house".  By calculating this during construction,
	 * it can be referred to thereafter without having to calculate it elsewhere
	 */
	private void initializeSeatingChart() {
		// Initialize the array in memory
		seatingChart = new Seat[rowCount][seatCount];
		
		Seat last = null;
		
		// Populate the rows and columns
		for (int row = 0; row < rowCount; row++) {
			for (int column = 0; column < seatCount; column++) {
				Seat seat = new Seat(row, column, calculateDistance(row, column));
				seatingChart[row][column] = seat;
				priorityList.add(seat);
				if(last != null){
					last.setRight(seat);
					seat.setLeft(last);;
				}
				if(row > 0){
					seatingChart[row-1][column].setBelow(seat);
				}
			}
		}
		
		Collections.sort(priorityList);

	}

	/**
	 * "Pre-Reservations" are reservations that are made before group placements are
	 * searched for.  This method will put a pre-reservation on the seat at the row 
	 * and column specified. 
	 * 
	 * @param row - row number of seat to be pre-reserved
	 * @param column - column number of seat to pre-reserved
	 */
	public void preReserveSeat(int row, int column) {
		// Account for the "off-by-one" behavior of the array
		row = row - 1;
		column = column - 1;
		
		// Assert that the seat is an actual seat that can be reserved
		if (isValidSeat(row, column)) {
			// Pre-Reserve the seat
			seatingChart[row][column].setPreReservation();
		}
	}

	/**
	 * This method will call the helper 'findSeats' method to search for placement
	 * for the group size specified through the argument.  Once a placement is found
	 * this method will reserve those seats so they are no longer available for future 
	 * seating requests.  This method will also handle situations where there are no 
	 * available placements for the group size requested.
	 *  
	 * @param total - Total size of the group that is looking to be seated
	 */
	public void requestSeats(int total) {
		
		// Search for a seating placement
		Seat best = findSeats(total);

		// If a seating placement was found
		if (best != null) {
			
			// Starting with the first seat and continuing until the group size
			for (int i = 0; i < total; i++) {
				// mark the seats as reserved
				seatingChart[best.getRow()][best.getColumn() + i].setReserved();
			}
			
		} else {
			System.out.println("Not Available");
		}

	}



	
	/**
	 * Provide the total number of seats that are required for the group to be seated,
	 * this method will find the best group of seats available for seating.  The return
	 * value from this method will be the first seat in the group.  For example, if the
	 * seat returned is R1C4 and the group size is 3, then the group can be seated at
	 * R1C4, R1C5, R1C6
	 * 
	 * @param total - The total size of the group that is looking to be seated
	 * @return - The starting seat where the group can be assigned.  This method will
	 * 			 return 'null' if there are locations for the group to be placed
	 * 
	 * NOTE: This method was a first attack approach.  I was going to delete it, but figured
	 * might as well leave it.  It's not very efficient, O(n^2) + k - where k = total.
	 */
	private Seat findSeats(int total) {
		// The reference for the first seat of the best group placement will be stored here
		Seat bestSeat = null;
		
		// Ideally, the best weight will be the lowest weight.   The weight is the sum of all
		// the calculated Manhattan distances of the seats in the group.
		int bestWeight = -1;

		// As the search for the best group placement continues, the placement that is
		// currently being evaluated will be placed here
		int workingWeight = 0;
		
		// In order for the placement to be successful, the number of people seated will have to 
		// be equal to the total.  So, we start with 0 and will increment as we find potential placement locations
		int seated = 0;
		
		// We will iterate over each row and column looking for groups of size n (n = total).
		// The working seat will be the first seat of the potential group
		Seat workingSeat = null;
		
		// Iterate over each row
		for (int row = 0; row < rowCount; row++) {

			// Then, iterate over each seat (or column) of that row
			for (int col = 0; col < seatCount - total + 1; col++) {
				// update the current working seat reference
				workingSeat = seatingChart[row][col];
				
				// Reset the 'weight' value and 'seated' value which was most likely
				// updated in the last iteration (unless it is the first cycle)
				workingWeight = 0;
				seated = 0;

				// Starting with the current working seat location and continuing until we are at 
				// the at the working seat location + the total number of seats being requested
				for (int groupSeat = col; groupSeat < total + col; groupSeat++) {
					
					// Get the seat
					Seat seat = seatingChart[row][groupSeat];
					
					// and check if it is reserved
					if (!seat.isReserved()) {
						// if it is not reserved, then update the weight
						workingWeight += seat.getDistance();
						// and increment the number of seats in the group that are able to be seated here
						seated++;
					}
				}

				// If his is the first cycle or the previously calculated working weight is 
				// less than any that came before AND everyone in the group was able to be seated,
				// then we have found a new best seat.  Update the best seat reference and the new
				// best calculated weight
				if ((workingWeight < bestWeight || bestWeight == -1) && seated == total) {
					bestWeight = workingWeight;
					bestSeat = workingSeat;

				}
			}
		}
		
		// Return the first seat reference of the best placement or null if nothing was found
		return bestSeat;
	}

	/**
	 * Verifies that the seat number at the specified row and column
	 * is a an actual seat in the table
	 * 
	 * @param row - Row of the seat to check
	 * @param column - Column of the seat to check
	 * @return True if the seat is valid, False otherwise
	 */
	private boolean isValidSeat(int row, int column) {
		return (row >= 0 && row < rowCount && column >= 0 && column < seatCount);
	}

	/**
	 * Given a row and column value, calculate the distance from the 
	 * best seat in the house which is specified per requirements as 
	 * the first/top row, center seat - this is calculated using Manhattan distance
	 * 
	 * @param r - The row number of the seat checking the distance for
	 * @param c - The column number of the seat we're checking the distance for
	 * @return The Manhattan distance of the seat relative to the row 1, center seat
	 */
	private int calculateDistance(int r, int c) {
		return Math.abs(r - 0) + Math.abs(seatCount/2 - c);
	}
	
	/**
	 * This method will print the seating table with 'X' values
	 * representing the pre-reserved seats in line 1 of the input
	 * and 'O' values representing the algorithmically selected seats
	 * based on the calculated "best" location available
	 */
	public void print() {
		for (int row = 0; row < rowCount; row++) {
			for (int column = 0; column < seatCount; column++) {
				System.out.print(" " + seatingChart[row][column].getSeatStatusSymbol() + " ");
			}
			System.out.print("\n");
		}
		
		

	}
}