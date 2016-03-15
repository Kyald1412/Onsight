package com.kyald.onsight.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

import com.kyald.onsight.models.Category;
import com.kyald.onsight.models.MainSettings;
import com.kyald.onsight.models.ParsedApplicationSettings;
import com.kyald.onsight.models.Recipe;
import com.kyald.onsight.models.RecipeIngredient;
import com.kyald.onsight.models.RecipeStep;
import com.kyald.onsight.models.RecipeSummary;

/**
 * The XML Parser used to obtain the information provided in the XML
 */
public class SettingsXMLParser {

	private static final String ns = null;

	public static ParsedApplicationSettings parse(InputStream in)
			throws XmlPullParserException, IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(new InputStreamReader(in));
			parser.nextTag();
			return readFeed(parser);
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	/**
	 * Reads the feed
	 * 
	 * @param parser
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private static ParsedApplicationSettings readFeed(XmlPullParser parser)
			throws XmlPullParserException, IOException {

		MainSettings mainSettings = new MainSettings();

		ArrayList<Category> categories = new ArrayList<Category>();

		ArrayList<Recipe> currentRecipes = new ArrayList<Recipe>();

		parser.require(XmlPullParser.START_TAG, ns,
				RecipesXMLTagConstants.TAG_SETTINGS);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals(RecipesXMLTagConstants.TAG_MAIN_SETTINGS)) {
				mainSettings = readSettings(parser);
			} else if (name.equals(RecipesXMLTagConstants.TAG_CATEGORY)) {
				String categoryName = parser.getAttributeValue(null, "name");
				String categoryIcon = parser.getAttributeValue(null, "icon");
				currentRecipes = readRecipesList(parser,
						RecipesXMLTagConstants.TAG_CATEGORY);
				categories.add(new Category(categoryName, categoryIcon,
						currentRecipes));
			} else {
				skip(parser);
			}
		}
		return new ParsedApplicationSettings(categories, mainSettings);
	}

	/**
	 * Reads the settings
	 * 
	 * @param parser
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private static MainSettings readSettings(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		MainSettings currentSettings = new MainSettings();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = parser.getName();
			if (name.equals(RecipesXMLTagConstants.TAG_LOGO_ACTIVE)) {
				String logoActive = readText(parser);
				currentSettings.setLogoActive(logoActive);
			} else if (name.equals(RecipesXMLTagConstants.TAG_LOGO_INACTIVE)) {
				String logoInActive = readText(parser);
				currentSettings.setLogoInActive(logoInActive);
			}
		}

		return currentSettings;
	}

	/**
	 * Reads the list with recipes
	 * 
	 * @param parser
	 * @param tag
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private static ArrayList<Recipe> readRecipesList(XmlPullParser parser,
			String tag) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, tag);

		ArrayList<Recipe> recipesList = new ArrayList<Recipe>(0);

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals(RecipesXMLTagConstants.TAG_RECIPE_INFO)) {
				recipesList.add(readRecipeInfo(parser));
			} else {
				skip(parser);
			}
		}

		return recipesList;
	}
	
	/**
	 * Reads the list with recipes
	 * 
	 * @param parser
	 * @param tag
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private static ArrayList<String> readRecipesImages(XmlPullParser parser) 
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, RecipesXMLTagConstants.TAG_RECIPE_INFO_PICTURE_LIST);

		ArrayList<String> imageList = new ArrayList<String>(0);

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals(RecipesXMLTagConstants.TAG_RECIPE_INFO_PICTURE)) {
				imageList.add(readText(parser));
			} else {
				skip(parser);
			}
		}

		return imageList;
	}

	/**
	 * Read the Recipe Information
	 * 
	 * @param parser
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private static Recipe readRecipeInfo(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns,
				RecipesXMLTagConstants.TAG_RECIPE_INFO);
		String recipeTitle = null;
		ArrayList<String> recipePictureUriList = new ArrayList<String>(0);
		RecipeSummary recipeSummary = null;
		ArrayList<RecipeIngredient> recipeIngredients = new ArrayList<RecipeIngredient>(
				0);
		ArrayList<RecipeStep> recipeSteps = new ArrayList<RecipeStep>(0);

		String id = parser.getAttributeValue(null, "id");

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}


			String name = parser.getName();
			if (name.equals(RecipesXMLTagConstants.TAG_RECIPE_INFO_TITLE)) {
				recipeTitle = readText(parser);
			} else if (name
					.equals(RecipesXMLTagConstants.TAG_RECIPE_INFO_PICTURE_LIST)) {
				recipePictureUriList = readRecipesImages(parser);
			} else if (name
					.equals(RecipesXMLTagConstants.TAG_RECIPE_INFO_SUMMARY)) {
				recipeSummary = readRecipeInfoSummary(parser);
			} else if (name
					.equals(RecipesXMLTagConstants.TAG_RECIPE_INFO_INGREDIENTS_LIST)) {
				recipeIngredients = readRecipeInfoIngredients(parser);
			} else if (name
					.equals(RecipesXMLTagConstants.TAG_RECIPE_INFO_STEPS_LIST)) {
				recipeSteps = readRecipeInfoStepList(parser);
			} else {
				skip(parser);
			}
		}

		return new Recipe(id, recipeTitle, recipePictureUriList, recipeSummary,
				recipeIngredients, recipeSteps);
	}

	/**
	 * Reads the Recipe Summary
	 * 
	 * @param parser
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private static RecipeSummary readRecipeInfoSummary(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns,
				RecipesXMLTagConstants.TAG_RECIPE_INFO_SUMMARY);

		String recipeSummaryOrigin = null;
		String recipeSummaryPreparationTime = null;
		String recipeSummaryCookingTime = null;
		String recipeSummaryPortions = null;
		String recipeSummaryCalories = null;
		String recipeSummaryDescription = null;

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals(RecipesXMLTagConstants.TAG_RECIPE_INFO_SUMMARY_ORIGIN)) {
				recipeSummaryOrigin = readText(parser);
			} else if (name
					.equals(RecipesXMLTagConstants.TAG_RECIPE_INFO_SUMMARY_PREPARATION_TIME)) {
				recipeSummaryPreparationTime = readText(parser);
			} else if (name
					.equals(RecipesXMLTagConstants.TAG_RECIPE_INFO_SUMMARY_COOKING_TIME)) {
				recipeSummaryCookingTime = readText(parser);
			} else if (name
					.equals(RecipesXMLTagConstants.TAG_RECIPE_INFO_SUMMARY_PORTIONS)) {
				recipeSummaryPortions = readText(parser);
			} else if (name
					.equals(RecipesXMLTagConstants.TAG_RECIPE_INFO_SUMMARY_CALORIES)) {
				recipeSummaryCalories = readText(parser);
			} else if (name
					.equals(RecipesXMLTagConstants.TAG_RECIPE_INFO_SUMMARY_DESCRIPTION)) {
				recipeSummaryDescription = readText(parser);
			} else {
				skip(parser);
			}
		}

		return new RecipeSummary(recipeSummaryOrigin,
				recipeSummaryPreparationTime, recipeSummaryCookingTime,
				recipeSummaryPortions, recipeSummaryCalories,
				recipeSummaryDescription);
	}

	/**
	 * Reads the Recipe Ingredients
	 * 
	 * @param parser
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private static ArrayList<RecipeIngredient> readRecipeInfoIngredients(
			XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns,
				RecipesXMLTagConstants.TAG_RECIPE_INFO_INGREDIENTS_LIST);
		ArrayList<RecipeIngredient> ingredientsList = new ArrayList<RecipeIngredient>(
				0);

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals(RecipesXMLTagConstants.TAG_RECIPE_INFO_INGREDIENTS_LIST_ITEM)) {
				ingredientsList.add(readRecipeInfoIngredientListItem(parser));
			} else {
				skip(parser);
			}
		}

		return ingredientsList;
	}

	/**
	 * Reads the item of the list with ingredients
	 * 
	 * @param parser
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private static RecipeIngredient readRecipeInfoIngredientListItem(
			XmlPullParser parser) throws XmlPullParserException, IOException {

		parser.require(XmlPullParser.START_TAG, ns,
				RecipesXMLTagConstants.TAG_RECIPE_INFO_INGREDIENTS_LIST_ITEM);
		String recipeIngredientName = null;
		String recipeIngredientQuantity = null;

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals(RecipesXMLTagConstants.TAG_RECIPE_INFO_INGREDIENTS_ITEM_NAME)) {
				recipeIngredientName = readText(parser);
			} else if (name
					.equals(RecipesXMLTagConstants.TAG_RECIPE_INFO_INGREDIENTS_ITEM_QUANTITY)) {
				recipeIngredientQuantity = readText(parser);
			} else {
				skip(parser);
			}
		}

		return new RecipeIngredient(recipeIngredientName,
				recipeIngredientQuantity);

	}

	/**
	 * Reads the steps to prepare a Recipe
	 * 
	 * @param parser
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private static ArrayList<RecipeStep> readRecipeInfoStepList(
			XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns,
				RecipesXMLTagConstants.TAG_RECIPE_INFO_STEPS_LIST);

		ArrayList<RecipeStep> stepsList = new ArrayList<RecipeStep>(0);

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals(RecipesXMLTagConstants.TAG_RECIPE_INFO_STEPS_LIST_ITEM)) {
				stepsList.add(readRecipeInfoStepsListItem(parser));
			} else {
				skip(parser);
			}
		}

		return stepsList;
	}

	/**
	 * Reads the items in the list of steps to prepare the Recipe
	 * 
	 * @param parser
	 * @return
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private static RecipeStep readRecipeInfoStepsListItem(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns,
				RecipesXMLTagConstants.TAG_RECIPE_INFO_STEPS_LIST_ITEM);
		String recipeStepName = null;
		String recipeStepDescription = null;

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals(RecipesXMLTagConstants.TAG_RECIPE_INFO_STEPS_ITEM_NAME)) {
				recipeStepName = readText(parser);
			} else if (name
					.equals(RecipesXMLTagConstants.TAG_RECIPE_INFO_STEPS_ITEM_DESCRIPTION)) {
				recipeStepDescription = readText(parser);
			} else {
				skip(parser);
			}
		}

		return new RecipeStep(recipeStepName, recipeStepDescription);
	}

	/**
	 * Reads a text
	 * 
	 * @param parser
	 * @return
	 * @throws IOException
	 * @throws XmlPullParserException
	 */
	private static String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	/**
	 * Skips reading
	 * 
	 * @param parser
	 * @throws XmlPullParserException
	 * @throws IOException
	 */
	private static void skip(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}

}
