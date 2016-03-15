package com.kyald.onsight.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyald.onsight.R;
import com.kyald.onsight.models.Recipe;
import com.kyald.onsight.utils.CommonUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 
 * Adapter used for the SingleCategory Fragment
 * 
 */
public class SingleCategoryListAdapter extends ArrayAdapter<Recipe> {

	private ImageLoader mImageLoader;
	private DisplayImageOptions mImageOptions;

	/**
	 * 
	 * @param context
	 *            the application context
	 * @param items
	 *            the recipe items to show
	 */
	public SingleCategoryListAdapter(Context context,
			List<Recipe> items) {
		super(context, 0, items);

		mImageLoader = ImageLoader.getInstance();
		mImageOptions = com.kyald.onsight.MainActivity.buildImageOptions(
				ImageScaleType.IN_SAMPLE_INT, true, true, 0, 0, 0);

	}
	
	/**
	 * Returns the View
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = getInflater()
					.inflate(R.layout.list_item_single, null);

			holder = new ViewHolder();

			holder.mainImage = (ImageView) convertView
					.findViewById(R.id.list_item_single_main_image);
			
			holder.recipeTitle = (TextView) convertView
					.findViewById(R.id.list_item_single_recipe_title);
			CommonUtils.setRobotoThinFont(getContext(), holder.recipeTitle);
			
			holder.place = (TextView) convertView
					.findViewById(R.id.list_item_single_place);
			CommonUtils.setRobotoThinFont(getContext(), holder.place);

			holder.preparetime = (TextView) convertView
					.findViewById(R.id.list_item_single_prep_time);
			CommonUtils.setRobotoThinFont(getContext(), holder.preparetime);

			holder.cookTime = (TextView) convertView
					.findViewById(R.id.list_item_single_cook_time);
			CommonUtils.setRobotoThinFont(getContext(), holder.cookTime);

			holder.calories = (TextView) convertView
					.findViewById(R.id.list_item_single_calories);
			CommonUtils.setRobotoThinFont(getContext(), holder.calories);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		String photoUri;
		if (CommonUtils.isContentLocal()) {
			photoUri = "drawable://"
					+ getContext().getResources().getIdentifier(
							getItem(position).getRecipePictureUriList().get(0), "drawable",
							getContext().getPackageName());
		} else {
			photoUri = getItem(position).getRecipePictureUriList().get(0);
		}

		mImageLoader.displayImage(photoUri, holder.mainImage, mImageOptions);

		holder.recipeTitle.setText(getItem(position).getRecipeTitle());
		holder.recipeTitle.setSelected(true);
		
		holder.place.setText(getItem(position).getRecipeSummary()
				.getRecipeSummaryOrigin());
		holder.place.setSelected(true);
		
		holder.preparetime.setText(getItem(position).getRecipeSummary()
				.getRecipeSummaryPreparationTime());
		holder.preparetime.setSelected(true);

		holder.cookTime.setText(getItem(position).getRecipeSummary()
				.getRecipeSummaryCookingTime());
		holder.cookTime.setSelected(true);
		
		holder.calories.setText(getItem(position).getRecipeSummary()
				.getRecipeSummaryCalories());

		return convertView;

	}

	/**
	 * Returns the layout inflater
	 * @return
	 */
	private LayoutInflater getInflater() {
		return LayoutInflater.from(getContext());
	}

	/**
	 * Class to hold the view
	 */
	public static class ViewHolder {
		ImageView mainImage;
		TextView recipeTitle;
		TextView place;
		TextView preparetime;
		TextView cookTime;
		TextView calories;
	}
}
