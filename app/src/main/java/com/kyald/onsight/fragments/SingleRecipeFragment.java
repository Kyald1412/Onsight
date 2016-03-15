package com.kyald.onsight.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kyald.onsight.MainActivity;
import com.kyald.onsight.R;
import com.kyald.onsight.models.Recipe;
import com.kyald.onsight.network.NetworkManager;
import com.kyald.onsight.share.MailManager;
import com.kyald.onsight.utils.CommonUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;


/**
 * Fragment for the Recipe screen
 * 
 * @author dmb Team
 * 
 */
public class SingleRecipeFragment extends Fragment {

	public static final String TAG = SingleRecipeFragment.class.getSimpleName();

	private Recipe mRecipe;

	private DisplayImageOptions mImageOptions;

	private ImageLoader mImageLoader;

	private ImageView mSummaryTab;

	private View mIngredientsTab;

	private View mRecipeTab;

	private TextView mDescription;

	private LinearLayout mIngredients;

	private LinearLayout mHowToPrepare;
	
	private ImageView mShareButton;
	
	private ImageView mEmailButton;
	
	private ImageView mTwitterButton;
	
	private ImageView mFacebookButton;
	
	private LinearLayout mShareLayout;

	private ViewPager mImagePager;
	
	private ImagePagerAdapter mImageAdapter;
	
	private TextView mImageCount;
	/**
	 * Returns new instance of the Fragment
	 * @return
	 */
	public static SingleRecipeFragment newInstance(Recipe recipe) {
		SingleRecipeFragment fragment = new SingleRecipeFragment();
		fragment.mRecipe = recipe;
		return fragment;
	}

	public SingleRecipeFragment() {
		mImageLoader = ImageLoader.getInstance();
		mImageOptions = MainActivity.buildImageOptions(
				ImageScaleType.IN_SAMPLE_INT, true, true, 0, 0, 0);

	}

	/**
	 * Called to have the fragment instantiate its user interface view. This is
	 * optional, and non-graphical fragments can return null (which is the
	 * default implementation). This will be called between onCreate(Bundle) and
	 * onActivityCreated(Bundle).
	 * 
	 * @param inflater
	 *            The LayoutInflater object that can be used to inflate any
	 *            views in the fragment,
	 * @param container
	 *            If non-null, this is the parent view that the fragment's UI
	 *            should be attached to. The fragment should not add the view
	 *            itself, but this can be used to generate the LayoutParams of
	 *            the view.
	 * @paramsavedInstanceState If non-null, this fragment is being
	 *                          re-constructed from a previous saved state as
	 *                          given here.
	 * 
	 * @return Return the View for the fragment's UI, or null.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_single_recipe, null);
		return v;
	}

	/**
	 * Called when the fragment's activity has been created and this fragment's
	 * view hierarchy instantiated. It can be used to do final initialization
	 * once these pieces are in place, such as retrieving views or restoring
	 * state. It is also useful for fragments that use
	 * setRetainInstance(boolean) to retain their instance, as this callback
	 * tells the fragment when it is fully associated with the new activity
	 * instance. This is called after onCreateView(LayoutInflater, ViewGroup,
	 * Bundle) and before onViewStateRestored(Bundle).
	 * 
	 * @param savedInstanceState
	 *            If the fragment is being re-created from a previous saved
	 *            state, this is the state.
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		TextView title = (TextView) getView().findViewById(
				R.id.fra_single_recipe_title);
		title.setSelected(true);
		CommonUtils.setRobotoThinFont(getActivity(), title);
		title.setText(mRecipe.getRecipeTitle());
		mImageAdapter = new ImagePagerAdapter(mRecipe.getRecipePictureUriList());
		mImagePager = (ViewPager) getView().findViewById(R.id.imagepager);
		mImagePager.setAdapter(mImageAdapter);
		mImageCount = (TextView) getView().findViewById(R.id.fra_single_recipe_image_count);
		if (mRecipe.getRecipePictureUriList() != null && mRecipe.getRecipePictureUriList().size() > 1) {
			mImageCount.setText(mRecipe.getRecipePictureUriList().size() + " pics");
			mImageCount.setVisibility(View.VISIBLE);
		} else {
			mImageCount.setVisibility(View.GONE);
		}
				
		/*ImageView mainImage = (ImageView) getView().findViewById(
				R.id.fra_single_recipe_main_image);

		String photoUri;
		if (CommonUtils.isContentLocal()) {
			photoUri = "drawable://"
					+ getResources().getIdentifier(mRecipe.getRecipePictureUri(),
							"drawable", getActivity().getPackageName());
		} else {
			photoUri = mRecipe.getRecipePictureUri();
		}
		mImageLoader.displayImage(photoUri, mainImage, mImageOptions);
		
		mainImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<String> paths = new ArrayList<String>();
				paths.add(mRecipe.getRecipePictureUri());
				paths.add(mRecipe.getRecipePictureUri());
				paths.add(mRecipe.getRecipePictureUri());
				((MainActivity) getActivity()).showImages(paths, 1);
				
			}
		});*/

		ImageView favIcon = (ImageView) getView().findViewById(
				R.id.fra_single_fav);

		if (((MainActivity) getActivity()).isRecipeIdInFavSharedPrefs(mRecipe
				.getId())) {
			favIcon.setSelected(true);
		}

		favIcon.setOnClickListener(new FavIconClickListener());

		TextView location = (TextView) getView().findViewById(
				R.id.fra_single_recipe_location);
		CommonUtils.setRobotoThinFont(getActivity(), location);
		location.setText(mRecipe.getRecipeSummary().getRecipeSummaryOrigin());

		TextView prepTime = (TextView) getView().findViewById(
				R.id.fra_single_recipe_prep_time);
		prepTime.setText(mRecipe.getRecipeSummary()
				.getRecipeSummaryPreparationTime());
		CommonUtils.setRobotoThinFont(getActivity(), prepTime);

		TextView cookTime = (TextView) getView().findViewById(
				R.id.fra_single_recipe_cook_time);
		cookTime.setText(mRecipe.getRecipeSummary()
				.getRecipeSummaryCookingTime());
		CommonUtils.setRobotoThinFont(getActivity(), cookTime);

		TextView portions = (TextView) getView().findViewById(
				R.id.fra_single_recipe_portions);
		portions.setText(mRecipe.getRecipeSummary().getRecipeSummaryPortions());
		CommonUtils.setRobotoThinFont(getActivity(), portions);

		TextView calories = (TextView) getView().findViewById(
				R.id.fra_single_recipe_calories);
		calories.setText(mRecipe.getRecipeSummary().getRecipeSummaryCalories());
		CommonUtils.setRobotoThinFont(getActivity(), calories);

		// TAB 1
		mDescription = (TextView) getView().findViewById(
				R.id.fra_single_recipe_description);
		mDescription.setText(mRecipe.getRecipeSummary()
				.getRecipeSummaryDescription());
		CommonUtils.setRobotoThinFont(getActivity(), mDescription);

		// TAB 2
		mIngredients = (LinearLayout) getView().findViewById(
				R.id.fra_single_recipe_ingredients);
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		for (int i = 0; i < mRecipe.getRecipeIngredients().size(); i++) {
			LinearLayout ingredient = (LinearLayout) inflater.inflate(R.layout.single_recipe_ingredients, null);
			TextView value = (TextView) ingredient.findViewById(R.id.ingredients_value);
			CommonUtils.setRobotoBoldFont(getActivity(), value);
			TextView description = (TextView) ingredient.findViewById(R.id.ingredients_desc);
			CommonUtils.setRobotoThinFont(getActivity(), description);
			value.setText(mRecipe.getRecipeIngredients().get(i)
					.getRecipeIngredientQuantity());
			description.setText(mRecipe.getRecipeIngredients().get(i)
					.getRecipeIngredientName());
			mIngredients.addView(ingredient);
		}
		
		// TAB 3
		mHowToPrepare = (LinearLayout) getView().findViewById(
				R.id.fra_single_recipe_how_to_prepare);
		for (int i = 0; i < mRecipe.getRecipeSteps().size(); i++) {

			LinearLayout steps = (LinearLayout) inflater.inflate(R.layout.single_recipe_steps, null);
			TextView value = (TextView) steps.findViewById(R.id.step_value);
			CommonUtils.setRobotoBoldFont(getActivity(), value);
			TextView description = (TextView) steps.findViewById(R.id.step_desc);
			CommonUtils.setRobotoThinFont(getActivity(), description);
			value.setText(mRecipe.getRecipeSteps().get(i)
					.getRecipeStepName());
			description.setText(mRecipe.getRecipeSteps().get(i)
					.getRecipeStepDescription());
			mHowToPrepare.addView(steps);
			
		}

		// TABS SELECTORS
		mSummaryTab = (ImageView) getView().findViewById(
				R.id.fra_single_recipe_summary_tab);
		mSummaryTab.setSelected(true);
		mSummaryTab.setOnClickListener(new SingleRecipeTabSelector(1));

		mIngredientsTab = getView().findViewById(
				R.id.fra_single_recipe_ingredients_tab);
		mIngredientsTab.setOnClickListener(new SingleRecipeTabSelector(2));

		mRecipeTab = getView().findViewById(R.id.fra_single_recipe_tab);
		mRecipeTab.setOnClickListener(new SingleRecipeTabSelector(3));
		
		mShareLayout = (LinearLayout) getView().findViewById(
				R.id.fra_single_share_container);
		mShareLayout.setVisibility(View.INVISIBLE);
		
		mShareButton = (ImageView) getView().findViewById(
				R.id.fra_single_share);
		mShareButton.setOnClickListener(new ShareIconClickListener());
		
		mEmailButton = (ImageView) getView().findViewById(
				R.id.fra_single_mail);
		mEmailButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mShareLayout.setVisibility(View.INVISIBLE);
				mShareButton.setSelected(false);
				shareEmail();
			}

			
		});
		
		mTwitterButton = (ImageView) getView().findViewById(
				R.id.fra_single_tweet);
		mTwitterButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mShareLayout.setVisibility(View.INVISIBLE);
				mShareButton.setSelected(false);
				shareTwitter();
			}

			
		});
		
		mFacebookButton = (ImageView) getView().findViewById(
				R.id.fra_single_face);
		mFacebookButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mShareLayout.setVisibility(View.INVISIBLE);
				mShareButton.setSelected(false);
				shareFacebook();
			}

			
		});

	}
	
	private void shareEmail() {
		
		MailManager.openMailIntent(getActivity(), mRecipe.getRecipeTitle(), getEmailBody());
		
	}
	
	private void shareFacebook() {

		if (NetworkManager.isNetworkConnected(getActivity())) {
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent
					.putExtra(Intent.EXTRA_SUBJECT, mRecipe.getRecipeTitle());
			shareIntent.putExtra(Intent.EXTRA_TEXT,
					"https://play.google.com/store/apps/details?id="
							+ getActivity().getPackageName());
			PackageManager pm = getActivity().getPackageManager();
			List<ResolveInfo> activityList = pm.queryIntentActivities(
					shareIntent, 0);
			boolean hasFacebookApp = false;
			for (final ResolveInfo app : activityList) {
				if ((app.activityInfo.name).contains("facebook")) {
					final ActivityInfo activity = app.activityInfo;
					final ComponentName name = new ComponentName(
							activity.applicationInfo.packageName, activity.name);
					shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
					shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					shareIntent.setComponent(name);
					getActivity().startActivity(shareIntent);
					hasFacebookApp = true;
					break;
				}
			}
			if (!hasFacebookApp) {
				Toast.makeText(getActivity(),
						"There is no Facebook app installed.",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(
					getActivity(),
					getResources().getString(R.string.no_internet_connectivity),
					Toast.LENGTH_SHORT).show();
		}
	}
	
	private void shareTwitter() {
		
		if (NetworkManager.isNetworkConnected(getActivity())) {
			String message;
			if (mRecipe.getRecipeTitle().length() > 70) {
				message = mRecipe.getRecipeTitle().substring(0, 71)
						+ "... Read more: "
						+ "http://play.google.com/store/apps/details?id="
						+ getActivity().getPackageName();
			} else {
				message = mRecipe.getRecipeTitle() + "... Read more: "
						+ "http://play.google.com/store/apps/details?id="
						+ getActivity().getPackageName();
			}
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, message);

			PackageManager pm = getActivity().getPackageManager();
			List<ResolveInfo> activityList = pm.queryIntentActivities(
					shareIntent, 0);
			boolean hasTwitterApp = false;
			for (final ResolveInfo app : activityList) {
				if (app.activityInfo.name.contains("twitter")) {
					final ActivityInfo activity = app.activityInfo;
					final ComponentName name = new ComponentName(
							activity.applicationInfo.packageName, activity.name);
					shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
					shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
							| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
					shareIntent.setComponent(name);
					getActivity().startActivity(shareIntent);
					hasTwitterApp = true;
					break;
				}
			}
			if (!hasTwitterApp) {
				Toast.makeText(getActivity(),
						"There is no Twitter app installed.",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(
					getActivity(),
					getResources().getString(R.string.no_internet_connectivity),
					Toast.LENGTH_SHORT).show();
		}
		
	}

	public void deselectAll() {
		mDescription.setVisibility(View.GONE);
		mIngredients.setVisibility(View.GONE);
		mHowToPrepare.setVisibility(View.GONE);

		mSummaryTab.setSelected(false);
		mIngredientsTab.setSelected(false);
		mRecipeTab.setSelected(false);
	}
	
	private String getEmailBody() {
		StringBuilder builder = new StringBuilder();
		builder.append("SUMMARY:");
		builder.append("\nOrigin: ");
		builder.append(mRecipe.getRecipeSummary().getRecipeSummaryOrigin());
		builder.append("\nPreparation Time: ");
		builder.append(mRecipe.getRecipeSummary().getRecipeSummaryPreparationTime());
		builder.append("\nCooking Time: ");
		builder.append(mRecipe.getRecipeSummary().getRecipeSummaryCookingTime());
		builder.append("\nPortions: ");
		builder.append(mRecipe.getRecipeSummary().getRecipeSummaryPortions());
		builder.append("\nCalories: ");
		builder.append(mRecipe.getRecipeSummary().getRecipeSummaryCalories());
		builder.append("\nDescription: ");
		builder.append(mRecipe.getRecipeSummary().getRecipeSummaryDescription());
		builder.append("\n\nINGREDIENTS:");
		for (int i = 0; i < mRecipe.getRecipeIngredients().size(); i++) {
			builder.append("\n" + mRecipe.getRecipeIngredients().get(i).getRecipeIngredientName() + " - " + mRecipe.getRecipeIngredients().get(i).getRecipeIngredientQuantity());
		}
		builder.append("\n\nRECIPE:");
		for (int i = 0; i < mRecipe.getRecipeSteps().size(); i++) {
			builder.append("\n" + mRecipe.getRecipeSteps().get(i).getRecipeStepName() + " - " + mRecipe.getRecipeSteps().get(i).getRecipeStepDescription());
		}
		builder.append("\n\n");
		builder.append("Download app with more recipes: " + "http://play.google.com/store/apps/details?id=" + getActivity().getPackageName());
		return builder.toString();
	}

	private class SingleRecipeTabSelector implements OnClickListener {

		int mTab;

		public SingleRecipeTabSelector(int tab) {
			this.mTab = tab;
		}

		@Override
		public void onClick(View v) {
			deselectAll();
			v.setSelected(true);

			if (mTab == 1) {
				mDescription.setVisibility(View.VISIBLE);
			} else if (mTab == 2) {
				mIngredients.setVisibility(View.VISIBLE);
			} else {
				mHowToPrepare.setVisibility(View.VISIBLE);
			}
		}
	}

	private class FavIconClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			if (v.isSelected()) {
				((MainActivity) getActivity())
						.removeRecipeIdFromSharedPrefs(mRecipe.getId());
			} else {
				((MainActivity) getActivity()).putRecipeIdInSharedPrefs(mRecipe
						.getId());
			}

			if (v instanceof ImageView) {
				v.setSelected(!v.isSelected());
			}

		}

	}
	
	private class ShareIconClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {

			if (mShareLayout.getVisibility() == View.INVISIBLE) {
				mShareLayout.setVisibility(View.VISIBLE);
				v.setSelected(true);
			} else {
				mShareLayout.setVisibility(View.INVISIBLE);
				v.setSelected(false);
			}

		}

	}
	
	private class ImagePagerAdapter extends PagerAdapter {

		public ImagePagerAdapter(ArrayList<String> paths) {
			this.paths = paths;
		}

		private ArrayList<String> paths;

		@Override
		public int getCount() {
			return paths.size();
		}

		/**
		 * Create the page for the given position. The adapter is responsible
		 * for adding the view to the container given here, although it only
		 * must ensure this is done by the time it returns from
		 * {@link #finishUpdate(android.view.ViewGroup)}.
		 * 
		 * @param collection
		 *            The containing View in which the page will be shown.
		 * @param position
		 *            The page position to be instantiated.
		 * @return Returns an Object representing the new page. This does not
		 *         need to be a View, but can be some other container of the
		 *         page.
		 */
		@Override
		public Object instantiateItem(ViewGroup collection, final int position) {
			ImageView iv = new ImageView(getActivity());
			String path = paths.get(position);
			
			
			String photoUri;
			if (CommonUtils.isContentLocal()) {
				photoUri = "drawable://"
						+ getResources().getIdentifier(path,
								"drawable", getActivity().getPackageName());
			} else {
				photoUri = path;
			}
			
			iv.setScaleType(ImageView.ScaleType.FIT_XY);
			iv.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					((MainActivity) getActivity()).showImages(paths, position);
					
				}
			});
			collection.addView(iv, 0);
			
			mImageLoader.displayImage(photoUri, iv, mImageOptions);

			return iv;
		}

		/**
		 * Remove a page for the given position. The adapter is responsible for
		 * removing the view from its container, although it only must ensure
		 * this is done by the time it returns from
		 * {@link #finishUpdate(android.view.ViewGroup)}.
		 * 
		 * @param collection
		 *            The containing View from which the page will be removed.
		 * @param position
		 *            The page position to be removed.
		 * @param view
		 *            The same object that was returned by
		 *            {@link #instantiateItem(android.view.View, int)}.
		 */
		@Override
		public void destroyItem(ViewGroup collection, int position, Object view) {
			collection.removeView((ImageView) view);
		}

		/**
		 * Determines whether a page View is associated with a specific key
		 * object as returned by instantiateItem(ViewGroup, int). This method is
		 * required for a PagerAdapter to function properly.
		 * 
		 * @param view
		 *            Page View to check for association with object
		 * @param object
		 *            Object to check for association with view
		 * @return
		 */
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return (view == object);
		}

		/**
		 * Called when the a change in the shown pages has been completed. At
		 * this point you must ensure that all of the pages have actually been
		 * added or removed from the container as appropriate.
		 * 
		 * @param arg0
		 *            The containing View which is displaying this adapter's
		 *            page views.
		 */
		@Override
		public void finishUpdate(ViewGroup arg0) {
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(ViewGroup arg0) {
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {
			super.setPrimaryItem(container, position, object);

		}

	}

}
