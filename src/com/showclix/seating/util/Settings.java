package com.showclix.seating.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * The Settings class provides a simple way to load external configurations into
 * the application. As a Singleton, only one instance of the class can be
 * instantiated and it can be accessed throughout the application.
 * 
 * @author Bradley Sheets
 * @version 1.0
 * @date May 1, 2019
 * 
 */
public final class Settings {

	// Instance of the Settings object.
	private static Settings instance = null;

	// Immutable name of the configuration file
	private final String propertiesFile = "configurations.properites";

	// We'll use the Java Properties object as the foundation of handling
	// configurations
	private Properties properties;

	// Once the configurations have successfully loaded into memory, this flag
	// will be set
	private boolean propertiesLoaded = false;

	/**
	 * Being a Singleton, the constructor is private and can only be called once
	 * upon the first instantiation.
	 */
	private Settings() {
		// Instantiate the properties object
		properties = new Properties();

		// Load the configurations from the external properties file
		loadProperties();
	}

	/**
	 * This method should be called upon construction of the Settings class and
	 * will load all of the properties in the defined property file location.
	 * Nothing fancy happens here, the data will be stored upon instantiation
	 * and there are no listeners on the configuration. So, if settings are
	 * changed while the application is running, the previous configurations
	 * will still be what is in memory - the application will need to restart to
	 * load new configurations.
	 */
	private void loadProperties() {
		FileReader reader = null;
		try {
			reader = new FileReader(propertiesFile);
			properties.load(reader);
			propertiesLoaded = true;
		} catch (IOException e) {
			System.out.println("There was an error loading the configuration file.  Default values will be used");
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				/*
				 * Not really important for the scope of this project, but would
				 * probably be logged should this happen
				 */
			}
		}
	}

	/**
	 * Get a string value from the configuration file
	 * 
	 * @param property
	 *            - Property key to retrieve the value for
	 * @param defaultValue
	 *            - The default value if the property isn't found
	 * @return The String representation of the configuration from the
	 *         properties file
	 */
	public String getString(String property, String defaultValue) {
		String value = defaultValue;

		if (propertiesLoaded) {

		}
		return value;
	}

	/**
	 * Get an integer value from the configuration file
	 * 
	 * @param property
	 *            - Property key to retrieve the value for
	 * @param defaultValue
	 *            - The default value if the property isn't found
	 * @return The Integer representation of the configuration from the
	 *         properties file
	 */
	public int getInt(String property, int defaultValue) {
		int value = defaultValue;
		if (propertiesLoaded) {
			value = Integer.parseInt(properties.getProperty(property).replaceAll("[^0-9]", ""));
		}
		return value;
	}

	/**
	 * This method will get the instance of the Settings object. If no instance
	 * exists in memory yet, it will first be instantiated.
	 * 
	 * @return Reference to the instance of the Settings object in memory
	 */
	public static Settings getInstance() {
		if (instance == null) {
			instance = new Settings();
		}
		return instance;
	}
}
