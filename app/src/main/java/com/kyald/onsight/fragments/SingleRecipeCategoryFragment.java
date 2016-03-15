package com.kyald.onsight.fragments;

import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.kyald.onsight.MainActivity;
import com.kyald.onsight.R;
import com.kyald.onsight.adapters.SingleCategoryListAdapter;
import com.kyald.onsight.models.Recipe;

/**
 * Fragment for the result list with recipes screen
 * 
 * @author dmb Team
 * 
 */
public class SingleRecipeCategoryFragment extends Fragment {

	public static String TAG = SingleRecipeCategoryFragment.class
			.getSimpleName();

	private static List<Recipe> mChosenRecipes;
	private static String mCategoryName;

	/**
	 * Returns new instance of the Fragment
	 * @param titleScren 
	 * @return
	 */
	public static SingleRecipeCategoryFragment newInstance(
			List<Recipe> chosenRecipes, String categoryName) {
		SingleRecipeCategoryFragment fragment = new SingleRecipeCategoryFragment();

		mChosenRecipes = chosenRecipes;
		
		mCategoryName = categoryName;

		return fragment;

	}

	private ListView mListView;

	private SingleCategoryListAdapter mAdapter;

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

		View parentView = inflater.inflate(
				R.layout.fragment_single_recipe_category, null);
		
		((MainActivity) getActivity()).setCorrectDrawerMenuItem(mCategoryName);

		return parentView;
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

		mListView = (ListView) getView().findViewById(
				R.id.fra_single_recipe_category_listview);

		mAdapter = new SingleCategoryListAdapter(
				getActivity(), mChosenRecipes);

		mListView.setAdapter(mAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				((MainActivity) getActivity()).showSingleRecipe(mChosenRecipes
						.get(position));
			}
		});
	}

	public void refresh(List<Recipe> chosenRecipes) {
		mAdapter = new SingleCategoryListAdapter(
				getActivity(), chosenRecipes);

		mListView.setAdapter(mAdapter);

	}

}
