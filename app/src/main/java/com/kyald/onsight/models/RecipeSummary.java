package com.kyald.onsight.models;

/**
 * Model for the Recipe Summary
 * 
 * @author dmb Team
 * 
 */
public class RecipeSummary {

	private String recipeSummaryOrigin;
	private String recipeSummaryPreparationTime;
	private String recipeSummaryCookingTime;
	private String recipeSummaryPortions;
	private String recipeSummaryCalories;
	private String recipeSummaryDescription;

	/**
	 * Constructor
	 * 
	 * @param recipeSummaryOrigin
	 * @param recipeSummaryPreparationTime
	 * @param recipeSummaryCookingTime
	 * @param recipeSummaryPortions
	 * @param recipeSummaryCalories
	 * @param recipeSummaryDescription
	 */
	public RecipeSummary(String recipeSummaryOrigin,
			String recipeSummaryPreparationTime,
			String recipeSummaryCookingTime, String recipeSummaryPortions,
			String recipeSummaryCalories, String recipeSummaryDescription) {
		super();
		this.recipeSummaryOrigin = recipeSummaryOrigin;
		this.recipeSummaryPreparationTime = recipeSummaryPreparationTime;
		this.recipeSummaryCookingTime = recipeSummaryCookingTime;
		this.recipeSummaryPortions = recipeSummaryPortions;
		this.recipeSummaryCalories = recipeSummaryCalories;
		this.recipeSummaryDescription = recipeSummaryDescription;
	}

	/**
	 * Check if the summary contains given text
	 * 
	 * @param searchFor
	 * @return
	 */
	public boolean contains(String searchFor) {
		if ((this.recipeSummaryOrigin != null && this.recipeSummaryOrigin.toLowerCase().contains(
				searchFor.toLowerCase()))
				|| (this.recipeSummaryDescription != null && this.recipeSummaryDescription.toLowerCase().contains(
						searchFor.toLowerCase()))) {
			return true;
		}

		return false;
	}

	/**
	 * Returns the origin
	 * 
	 * @return
	 */
	public String getRecipeSummaryOrigin() {
		return recipeSummaryOrigin;
	}

	/**
	 * Sets the origin
	 * 
	 * @param recipeSummaryOrigin
	 */
	public void setRecipeSummaryOrigin(String recipeSummaryOrigin) {
		this.recipeSummaryOrigin = recipeSummaryOrigin;
	}

	/**
	 * Returns the preparation time
	 * 
	 * @return
	 */
	public String getRecipeSummaryPreparationTime() {
		return recipeSummaryPreparationTime;
	}
	
	/**
	 * Sets the preparation time
	 * 
	 * @param recipeSummaryPreparationTime
	 */
	public void setRecipeSummaryPreparationTime(
			String recipeSummaryPreparationTime) {
		this.recipeSummaryPreparationTime = recipeSummaryPreparationTime;
	}

	/**
	 * Returns the cooking time
	 * 
	 * @return
	 */
	public String getRecipeSummaryCookingTime() {
		return recipeSummaryCookingTime;
	}

	/**
	 * Sets the cooking time
	 * 
	 * @param recipeSummaryCookingTime
	 */
	public void setRecipeSummaryCookingTime(String recipeSummaryCookingTime) {
		this.recipeSummaryCookingTime = recipeSummaryCookingTime;
	}

	/**
	 * Returns the portions
	 * 
	 * @return
	 */
	public String getRecipeSummaryPortions() {
		return recipeSummaryPortions;
	}

	/**
	 * Sets the portions
	 * 
	 * @param recipeSummaryPortions
	 */
	public void setRecipeSummaryPortions(String recipeSummaryPortions) {
		this.recipeSummaryPortions = recipeSummaryPortions;
	}

	/**
	 * Returns the calories
	 * 
	 * @return
	 */
	public String getRecipeSummaryCalories() {
		return recipeSummaryCalories;
	}

	/**
	 * Sets the calories
	 * 
	 * @param recipeSummaryCalories
	 */
	public void setRecipeSummaryCalories(String recipeSummaryCalories) {
		this.recipeSummaryCalories = recipeSummaryCalories;
	}

	/**
	 * Returns the description
	 * 
	 * @return
	 */
	public String getRecipeSummaryDescription() {
		return recipeSummaryDescription;
	}

	/**
	 * Sets the description
	 * 
	 * @param recipeSummaryDescription
	 */
	public void setRecipeSummaryDescription(String recipeSummaryDescription) {
		this.recipeSummaryDescription = recipeSummaryDescription;
	}

}
