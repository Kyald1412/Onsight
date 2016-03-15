package com.kyald.onsight.models;

/**
 * Model for the Recipe Step
 * 
 * @author dmb Team
 * 
 */
public class RecipeStep {
	private String recipeStepName;
	private String recipeStepDescription;

	/**
	 * Constructor
	 * 
	 * @param recipeStepName
	 * @param recipeStepDescription
	 */
	public RecipeStep(String recipeStepName, String recipeStepDescription) {
		super();
		this.recipeStepName = recipeStepName;
		this.recipeStepDescription = recipeStepDescription;
	}

	/**
	 * Check if the step contains given text
	 * 
	 * @param searchFor
	 * @return
	 */
	public boolean contains(String searchFor) {
		if ((recipeStepName != null && recipeStepName.toLowerCase().contains(searchFor.toLowerCase()))
				|| (recipeStepDescription != null && recipeStepDescription.toLowerCase().contains(
						searchFor.toLowerCase()))) {
			return true;
		}

		return false;
	}

	/**
	 * Returns the name
	 * 
	 * @return
	 */
	public String getRecipeStepName() {
		return recipeStepName;
	}

	/**
	 * Sets the name
	 * 
	 * @param recipeStepName
	 */
	public void setRecipeStepName(String recipeStepName) {
		this.recipeStepName = recipeStepName;
	}

	/**
	 * Returns the description
	 * 
	 * @return
	 */
	public String getRecipeStepDescription() {
		return recipeStepDescription;
	}

	/**
	 * Sets the description
	 * 
	 * @param recipeStepDescription
	 */
	public void setRecipeStepDescription(String recipeStepDescription) {
		this.recipeStepDescription = recipeStepDescription;
	}

}
