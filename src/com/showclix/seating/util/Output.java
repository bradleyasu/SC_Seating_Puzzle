package com.showclix.seating.util;

/**
 * Wrapper class for stdout. Probably a bit unneccessary, the thinking here was
 * to how the output, debugging etc is handled. The way data is output could be
 * changed globally here since the rest of the application uses this as the
 * output interface.
 * 
 * @author Bradley Sheets
 * @version 1.0
 * @date May 6, 2019
 * 
 */
public final class Output {

	// The singleton instance of the output object
	public static Output instance = null;

	// Debug output can be turned on or off in this flag.
	// A flag could be added to the configuration file to toggle this mode
	private boolean debug = false;

	private Output() {
	}

	/**
	 * Handle an output message. Currently, this is just passed to system.out
	 * 
	 * @param message
	 *            - Message to output
	 */
	public void print(Object message) {
		System.out.print(message);
	}

	/**
	 * Handle an output message. Currently, this is just passed to system.out
	 * 
	 * @param message
	 *            - Message to output
	 */
	public void println(Object message) {
		System.out.println(message);
	}

	/**
	 * Handle a debug message. This message will be printed to output only if
	 * the debug flag is set to true
	 * 
	 * @param message
	 *            - Message to output
	 */
	public void debug(Object message) {
		if (debug) {
			println(message);
		}
	}

	/**
	 * Get the instance of the Output object. If no instance exists yet in
	 * memory, one will be created and returned.
	 * 
	 * @return instance of the output object
	 */
	public static Output getInstance() {
		if (instance == null) {
			instance = new Output();
		}
		return instance;
	}

}
