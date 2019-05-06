package com.showclix.driver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import com.showclix.seating.Seating;
import com.showclix.seating.exceptions.InvalidSeatException;
import com.showclix.seating.exceptions.MaximumRequestsExceededException;
import com.showclix.seating.util.Output;
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
	private int linesRead = 0;

	public SeatingDriver() {

	}

	public void initialize() {
		this.seating = new Seating(Settings.getInstance().getInt("seating.chart.rowCount", 3), 
								   Settings.getInstance().getInt("seating.chart.seatCount", 11));
		
	}
	
	public void listen() {
		Scanner scanner = new Scanner(System.in);
		scanner.useDelimiter("\n");
		while(scanner.hasNext()) {
			if(linesRead == 0){
				parseReservations(scanner.next());
			} else {
				parseRequest(Integer.parseInt(scanner.next().replaceAll("[^0-9]", "")));
			}
			linesRead++;
		}
		scanner.close();
		Output.getInstance().println(seating.getAvailableSeats());
	}
	
	public void importFile(String filePath) {
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				if(linesRead == 0){
					parseReservations(line);
				} else {
					parseRequest(Integer.parseInt(line.replaceAll("[^0-9]", "")));
				}
				
				linesRead++;
			}
			Output.getInstance().println(seating.getAvailableSeats());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		seating.print();
	}
	
	private boolean parseReservations(String line) {
		boolean success = false;
		try{
			String[] reservations = line.split(" ");
			for(String reservation : reservations) {
				this.seating.preReserveSeat(reservation);
			}
			success = true;
		} catch (InvalidSeatException e) {
			e.printStackTrace();
		}
		return success;
	}
	
	private boolean parseRequest(int seatingRequest) {
		boolean success = false;
		try {
			Output.getInstance().println(this.seating.requestSeats(seatingRequest));
			success = true;
		} catch (MaximumRequestsExceededException e) {
			e.printStackTrace();
		}
		return success;
	}
	
	/**
	 * Entry point into the application
	 * @param args
	 */
	public static void main(String[] args) {
		// Create a new Seating Driver
		SeatingDriver driver = new SeatingDriver();
		
		// Initialize it's contents
			driver.initialize();
		
		if(args.length == 0){
			// Listen for STDIN
			driver.listen();
		} else {
			// The input is in a file, parse the file
			driver.importFile(args[0]);
		}
		
	}

}
