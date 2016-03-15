package com.kyald.onsight.models;

/**
 * Model for the Main Settings
 * 
 * @author dmb Team
 * 
 */
public class MainSettings {
	private String logoActive;
	private String logoInActive;

	/**
	 * Returns the active logo
	 * 
	 * @return
	 */
	public String getLogoActive() {
		return logoActive;
	}

	/**
	 * Sets the active logo
	 * 
	 * @param logoActive
	 */
	public void setLogoActive(String logoActive) {
		this.logoActive = logoActive;
	}

	/**
	 * Returns the inactive logo
	 * 
	 * @return
	 */
	public String getLogoInActive() {
		return logoInActive;
	}

	/**
	 * Sets the inactive logo
	 * 
	 * @param logoInActive
	 */
	public void setLogoInActive(String logoInActive) {
		this.logoInActive = logoInActive;
	}

}
