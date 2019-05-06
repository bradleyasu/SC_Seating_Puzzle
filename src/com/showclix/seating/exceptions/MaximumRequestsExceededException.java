package com.showclix.seating.exceptions;


/**
 * An exception to indicate when to many seats were requested in a single
 * request
 * 
 * @author Bradley Sheets
 * @version 1.0
 * @date May 6, 2019
 */
public class MaximumRequestsExceededException extends Exception {

	private static final long serialVersionUID = 1L;

	public MaximumRequestsExceededException() {
	}

	/**
	 * This method will allow the developer to provide the user with information
	 * about what exactly happened to cause this exception
	 * 
	 * @param message
	 *            A message to provide the user
	 */
	public MaximumRequestsExceededException(String message) {
		super(message);
	}

}
