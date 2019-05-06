package com.showclix.seating.exceptions;

public class MaximumRequestsExceededException extends Exception{

	private static final long serialVersionUID = 1L;

	public MaximumRequestsExceededException(){}
	
	public MaximumRequestsExceededException(String message){
		super(message);
	}
	
}
