package com.kyald.onsight.models;

import java.util.ArrayList;

/**
 * Model for the Parsed Application Settings
 * 
 * @author dmb Team
 *
 */
public class ParsedApplicationSettings {

	private ArrayList<Category> mCategories;
	private MainSettings mMainSettings;

	/**
	 * Constructor
	 * 
	 * @param categories
	 * @param mainSettings
	 */
	public ParsedApplicationSettings(ArrayList<Category> categories,
			MainSettings mainSettings) {
		super();
		this.mCategories = categories;
		this.mMainSettings = mainSettings;
	}

	/**
	 * Returns all recipe categories
	 * 
	 * @return
	 */
	public ArrayList<Category> getCategories() {
		return mCategories;
	}

	/**
	 * Returns the main settings of the application
	 * 
	 * @return
	 */
	public MainSettings getMainSettings() {
		return mMainSettings;
	}

}
