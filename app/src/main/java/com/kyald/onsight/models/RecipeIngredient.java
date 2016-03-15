package com.kyald.onsight.models;

/**
 * Model for the Recipe Ingredient
 * 
 * @author dmb Team
 *
 */
public class RecipeIngredient {

	private String recipeIngredientName;
	private String recipeIngredientQuantity;

	/**
	 * Constructor
	 * 
	 * @param recipeIngredientName
	 * @param recipeIngredientQuantity
	 */
	public RecipeIngredient(String recipeIngredientName,
			String recipeIngredientQuantity) {
		super();
		this.recipeIngredientName = recipeIngredientName;
		this.recipeIngredientQuantity = recipeIngredientQuantity;
	}

	/**
	 * Check if the ingredient contains given text
	 * 
	 * @param searchFor
	 * @return
	 */
	public boolean contains(String searchFor) {
		if ((recipeIngredientName != null && recipeIngredientName.toLowerCase()
				.contains(searchFor.toLowerCase()))
				|| (recipeIngredientQuantity != null && recipeIngredientQuantity.toLowerCase().contains(
						searchFor.toLowerCase()))) {
			return true;
		}

		return false;
	}

	/**
	 * Returns name
	 * 
	 * @return
	 */
	public String getRecipeIngredientName() {
		return recipeIngredientName;
	}

	/**
	 * Sets name
	 * 
	 * @param recipeIngredientName
	 */
	public void setRecipeIngredientName(String recipeIngredientName) {
		this.recipeIngredientName = recipeIngredientName;
	}

	/**
	 * Returns quantity
	 * 
	 * @return
	 */
	public String getRecipeIngredientQuantity() {
		return recipeIngredientQuantity;
	}

	/**
	 * Sets quantity
	 * 
	 * @param recipeIngredientQuantity
	 */
	public void setRecipeIngredientQuantity(String recipeIngredientQuantity) {
		this.recipeIngredientQuantity = recipeIngredientQuantity;
	}

}
