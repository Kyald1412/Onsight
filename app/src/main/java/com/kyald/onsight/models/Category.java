package com.kyald.onsight.models;

import java.util.List;

/**
 * Model for the Category
 * 
 * @author dmb Team
 * 
 */
public class Category {

	private String mName;
	private String mIcon;
	private List<Recipe> mRecipes;

	/**
	 * Constructor
	 * 
	 * @param name
	 * @param icon
	 * @param recipes
	 */
	public Category(String name, String icon, List<Recipe> recipes) {
		super();
		mName = name;
		mIcon = icon;
		mRecipes = recipes;
	}

	/**
	 * Returns the name of the Category
	 * 
	 * @return
	 */
	public String getName() {
		return mName;
	}

	/**
	 * Returns the name of the Category
	 * 
	 * @return
	 */
	public void setName(String name) {
		mName = name;
	}

	/**
	 * Returns the icon of the Category
	 * 
	 * @return
	 */
	public String getIcon() {
		return mIcon;
	}

	/**
	 * Returns the icon of the Category
	 * 
	 * @return
	 */
	public void setIcon(String icon) {
		mIcon = icon;
	}

	/**
	 * Returns a list with recipes in the Category
	 * 
	 * @return
	 */
	public List<Recipe> getRecipes() {
		return mRecipes;
	}

	/**
	 * Sets the category recipes
	 * 
	 * @param recipes
	 */
	public void setRecipes(List<Recipe> recipes) {
		mRecipes = recipes;
	}

}
