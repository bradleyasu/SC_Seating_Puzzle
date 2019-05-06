package com.showclix.driver;

import com.showclix.seating.Seating;
import com.showclix.seating.util.Settings;

/**
 * SeatingDriver
 * 
 * The SeatingDriver class acts as the controller that will interface with the
 * Seating algorithms to request and recieve the best available seats.
 * 
 * @author Bradley Sheets
 * @version 1.0
 * @date May 1, 2019
 * 
 */
public class SeatingDriver {

	// Reference to the Seating object.  For now, it's just one reference,
	// but you could have some sort of data structures of different Seating objects
	// of different configurations if it were needed
	private Seating seating;

	public SeatingDriver() {

	}

	public void initialize() {
		this.seating = new Seating(Settings.getInstance().getInt("seating.chart.rowCount", 3), 
								   Settings.getInstance().getInt("seating.chart.seatCount", 11));

		this.seating.preReserveSeat(1, 4);
		this.seating.preReserveSeat(1, 6);
		this.seating.preReserveSeat(2, 3);
		this.seating.preReserveSeat(2, 7);
		this.seating.preReserveSeat(3, 9);
		this.seating.preReserveSeat(3, 10);
//		this.seating.requestSeats(2);
//		this.seating.requestSeats(2);
		
		this.seating.requestSeats(3);
		this.seating.requestSeats(3);
		this.seating.requestSeats(3);
		this.seating.requestSeats(1);
		
		this.seating.requestSeats(2);
		// this.seating.requestSeats(10);

		this.seating.print();
	}
	
	/**
	 * Entry point into the application
	 * @param args
	 */
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		// Create a new Seating Driver
		SeatingDriver driver = new SeatingDriver();
		
		// Initialize it's contents
		driver.initialize();
		System.out.println("Total Time: "+(System.currentTimeMillis()-start)+"ms");
	}

}
