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

	/**
	 * Initializing a Driver object will create a new
	 * seating arrangement based on the number of rows and columns specified in the configuration
	 * file.  By default, the number of rows is 3 and the number of columns is 11
	 */
	public void initialize() {
		// If no configuration file is found, the values 3 and 11 will be used per original spec
		this.seating = new Seating(Settings.getInstance().getInt("seating.chart.rowCount", 3), Settings.getInstance().getInt("seating.chart.seatCount", 11));
		
	}
	
	/**
	 * This method will listen for user input, expecting the format specified in the project requirements.  As input
	 * is entered, reservations will be attempted and the status of the reservation will be output back to the
	 * console/stdout
	 */
	public void listen() {
		Scanner scanner = new Scanner(System.in);
		scanner.useDelimiter("\n");
		while(scanner.hasNext()) {
			// If it's the first line read, then parse for pre-reservations
			if(linesRead == 0){
				parseReservations(scanner.next());
			} else {
				// otherwise, interpret the line as a number that represents a group size
				// and try to find the best seating for the group
				parseRequest(Integer.parseInt(scanner.next().replaceAll("[^0-9]", "")));
			}
			linesRead++;
		}
		scanner.close();
		Output.getInstance().println(seating.getAvailableSeats());
	}
	
	/**
	 * If the user passed the filename in as an argument, instead of listening for user input,
	 * this method will will read the file line by line.  The first line is expected to be a 
	 * space delimited list of pre-reservations while the following lines are expected to be
	 * the number of seats requested per reservation.
	 * 
	 * @param filePath - path/file name of the input data on disk
	 */
	public void importFile(String filePath) {
		try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
			String line;
			while ((line = reader.readLine()) != null) {
				// If it's the first line read, then parse for pre-reservations
				if(linesRead == 0){
					parseReservations(line);
				} else {
					// otherwise, interpret the line as a number that represents a group size
					// and try to find the best seating for the group
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
	
	/**
	 * This is a helper method that will split the input line on space characters
	 * and try to parse out the row and column of each entry.  Once parsed, a 
	 * pre-reservation will be made on the seat.
	 * 
	 * @param line Input string, expected format: "R4C3 R3C9 R1C4 R8C4" etc
	 * @return True if the reservations were made successfully, false otherwise
	 */
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
	
	/**
	 * This is a helper method that will allow the driver to ask the seating arrangement
	 * to place a group in the audience.  The seats will be printed or Not Available if 
	 * they couldn't be placed.
	 * 
	 * @param seatingRequest The number of people in the group that need to be seated 
	 * @return True if the input data was parsed and submitted successfully, false otherwise
	 */
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
