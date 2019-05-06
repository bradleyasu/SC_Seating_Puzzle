package com.showclix.seating.util;

public final class Output {

	public static Output instance = null;
	private boolean debug = false;
	
	private Output() {
	}
	
	public void print(Object message) {
		System.out.println(message);
	}
	
	public void debug(Object message) {
		if(debug){
			print(message);
		}
	}
	
	public static Output getInstance() {
		if(instance == null) {
			instance = new Output();
		}
		return instance;
	}
	
}
