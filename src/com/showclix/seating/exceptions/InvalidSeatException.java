package com.showclix.seating.exceptions;

/**
 * An exception to indicate when an invalid seat was requested for a reservation
 * 
 * @author Bradley Sheets
 * @version 1.0
 * @date May 6, 2019
 */
public class InvalidSeatException extends Exception {

	private static final long serialVersionUID = 1L;

	public InvalidSeatException() {
	}

	/**
	 * This method will allow the developer to provide the user with information
	 * about what exactly happened to cause an invalid seat to be found
	 * 
	 * @param message
	 *            A message to provide the user
	 */
	public InvalidSeatException(String message) {
		super(message);
	}
}
