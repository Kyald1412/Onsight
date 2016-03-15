package com.kyald.onsight;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kyald.onsight.models.ParsedApplicationSettings;
import com.google.gson.Gson;

public class ApplicationContext extends Application {
	
	private static final String SETTINGS = "settings";

	private SharedPreferences prefs;

	/**
	 * The settings obtained after parsing the XML file
	 */
	public ParsedApplicationSettings mParsedApplicationSettings = null;

	/**
	 * 
	 * @return the parsed application data from the XML
	 */
	public ParsedApplicationSettings getParsedApplicationSettings() {
		if (mParsedApplicationSettings == null) {
			prefs = PreferenceManager.getDefaultSharedPreferences(this);
			Gson gson = new Gson();
			String settings = prefs.getString(SETTINGS, "");
			if (settings.equalsIgnoreCase("")) {
				mParsedApplicationSettings = null;
			} else {
				mParsedApplicationSettings = gson.fromJson(settings, ParsedApplicationSettings.class);
			}
		}
		
		return mParsedApplicationSettings;
	}

	/**
	 * Setter for ParsedApplicationSettings
	 * 
	 * @param parsedApplicationSettings
	 *            the parsed XML settings to set for the applicationContext;
	 */
	public void setParsedApplicationSettings(
			ParsedApplicationSettings parsedApplicationSettings) {
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		Gson gson = new Gson();
		String settings = gson.toJson(parsedApplicationSettings);
		SharedPreferences.Editor ed = prefs.edit();
        ed.putString(SETTINGS, settings);
        ed.commit();
		this.mParsedApplicationSettings = parsedApplicationSettings;
	}

}
