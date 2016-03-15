package com.kyald.onsight.models;

import java.util.ArrayList;

/**
 * Model for the Recipe
 * 
 * @author dmb Team
 * 
 */
public class Recipe {

	private String id;
	private String recipeTitle;
	private ArrayList<String> recipePictureUriList;
	private RecipeSummary recipeSummary;
	private ArrayList<RecipeIngredient> recipeIngredients;
	private ArrayList<RecipeStep> recipeSteps;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param recipeTitle
	 * @param recipePictureUri
	 * @param recipeSummary
	 * @param recipeIngredients
	 * @param recipeSteps
	 */
	public Recipe(String id, String recipeTitle, ArrayList<String> recipePictureUriList,
			RecipeSummary recipeSummary,
			ArrayList<RecipeIngredient> recipeIngredients,
			ArrayList<RecipeStep> recipeSteps) {
		super();
		this.id = id;
		this.recipeTitle = recipeTitle;
		this.recipePictureUriList = recipePictureUriList;
		this.recipeSummary = recipeSummary;
		this.recipeIngredients = recipeIngredients;
		this.recipeSteps = recipeSteps;
	}

	/**
	 * Check if the recipe contains given text
	 * 
	 * @param searchFor
	 * @return
	 */
	public boolean contains(String searchFor) {
		if ((getRecipeTitle() != null && getRecipeTitle().toLowerCase().contains(searchFor.toLowerCase()))
				|| (getRecipeSummary() != null && getRecipeSummary().contains(searchFor))) {
			return true;
		}

		for (int i = 0; i < recipeIngredients.size(); i++) {
			if (recipeIngredients.get(i) != null && recipeIngredients.get(i).contains(searchFor)) {
				return true;
			}
		}

		for (int i = 0; i < recipeSteps.size(); i++) {
			if (recipeSteps.get(i) != null && recipeSteps.get(i).contains(searchFor)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns the title
	 * 
	 * @return
	 */
	public String getRecipeTitle() {
		return recipeTitle;
	}

	/**
	 * Sets the title
	 * 
	 * @param recipeTitle
	 */
	public void setRecipeTitle(String recipeTitle) {
		this.recipeTitle = recipeTitle;
	}

	/**
	 * Returns the picture uri
	 * 
	 * @return
	 */
	public ArrayList<String> getRecipePictureUriList() {
		return recipePictureUriList;
	}

	/**
	 * Sets the picture uri
	 * 
	 * @param recipePictureUri
	 */
	public void setRecipePictureUriList(ArrayList<String> recipePictureUriList) {
		this.recipePictureUriList = recipePictureUriList;
	}

	/**
	 * Returns the summary
	 * 
	 * @return
	 */
	public RecipeSummary getRecipeSummary() {
		return recipeSummary;
	}

	/**
	 * Sets the summary
	 * 
	 * @param recipeSummary
	 */
	public void setRecipeSummary(RecipeSummary recipeSummary) {
		this.recipeSummary = recipeSummary;
	}

	/**
	 * Returns the ingredients
	 * 
	 * @return
	 */
	public ArrayList<RecipeIngredient> getRecipeIngredients() {
		return recipeIngredients;
	}

	/**
	 * Sets the ingredients
	 * 
	 * @param recipeIngredients
	 */
	public void setRecipeIngredients(
			ArrayList<RecipeIngredient> recipeIngredients) {
		this.recipeIngredients = recipeIngredients;
	}

	/**
	 * Returns the steps
	 * 
	 * @return
	 */
	public ArrayList<RecipeStep> getRecipeSteps() {
		return recipeSteps;
	}

	/**
	 * Sets the steps
	 * 
	 * @param recipeSteps
	 */
	public void setRecipeSteps(ArrayList<RecipeStep> recipeSteps) {
		this.recipeSteps = recipeSteps;
	}

	/**
	 * Returns the identifier
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the identifier
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

}
