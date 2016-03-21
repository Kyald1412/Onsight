package com.kyald.onsight.fragments;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyald.onsight.ApplicationContext;
import com.kyald.onsight.MainActivity;
import com.kyald.onsight.R;
import com.kyald.onsight.models.Recipe;
import com.kyald.onsight.utils.CommonUtils;

/**
 * 
 * Fragment used for the Header of the screen
 * 
 * @author dmb Team
 * 
 */
public class NavigationBarFragment extends Fragment {

	public static final String TAG = NavigationBarFragment.class
			.getSimpleName();

	private String mTitleText;

	private boolean isShowBack;
	
	private boolean mShowFilter;
	
	private boolean mShowCart;
	
	private boolean mShowSearch;
	
	private boolean mShowRefresh;
	
	private ImageView mSearchImage;
	
	private ImageView mFilterImage;
	
	private ImageView mCartImage;
	
	private TextView backView;

	private ImageView menuImage;
	
	private ImageView mRefreshImage;

	private boolean isSearchResult;
	

	/**
	 * Returns new instance of the Fragment
	 * @return
	 */
	public static NavigationBarFragment newInstance(
			ApplicationContext appContext, String title,
			boolean isSearchResult, boolean showFilter, boolean showCart,
			boolean showSearch, boolean showRefresh) {

		NavigationBarFragment fragment = new NavigationBarFragment();
		fragment.isSearchResult = isSearchResult;
		fragment.mShowFilter = showFilter;
		fragment.mShowCart = showCart;
		fragment.mTitleText = title;
		fragment.mShowRefresh = showRefresh;
		fragment.mShowSearch = showSearch;
		return fragment;
	}

	/**
	 * Called when a fragment is first attached to its activity.
	 * onCreate(Bundle) will be called after this.
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	/**
	 * Called to do initial creation of a fragment. This is called after
	 * onAttach(Activity) and before onCreateView(LayoutInflater, ViewGroup,
	 * Bundle).
	 * 
	 * Note that this can be called while the fragment's activity is still in
	 * the process of being created. As such, you can not rely on things like
	 * the activity's content view hierarchy being initialized at this point. If
	 * you want to do work once the activity itself is created, see
	 * onActivityCreated(Bundle).
	 * 
	 * @param savedInstanceState
	 *            If the fragment is being re-created from a previous saved
	 *            state, this is the state.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
		View view = inflater.inflate(R.layout.fragment_navigation_bar,
				container, false);

		menuImage = (ImageView) view.findViewById(R.id.navigationBarMenu);
		if (isShowBack()) {
			menuImage.setImageResource(R.drawable.action_bar_back);
			menuImage.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					((MainActivity) getActivity()).onBackPressed();
				}
			});
		} else {
			menuImage.setImageResource(R.drawable.action_bar_sliding_menu);
			menuImage.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					menuImage.setSelected(true);
					((MainActivity) getActivity()).showLeftMenu(menuImage);
				}
			});
		}
		

		mSearchImage = (ImageView) view.findViewById(R.id.navigationBarSearch);
		mSearchImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).showHideSearchFragment();
			}
		});
		
		mCartImage = (ImageView) view.findViewById(R.id.navigationBarCart);
		mCartImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).showShop();
				
			}
		});
		
		mRefreshImage = (ImageView) view.findViewById(R.id.navigationBarRefresh);
		mRefreshImage.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((MainActivity) getActivity()).refreshShoppingList();
				
			}
		});
		
		mFilterImage = (ImageView) view.findViewById(R.id.navigationBarFilter);
		mFilterImage.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				List<String> originsList = CommonUtils.getOrigins((ApplicationContext) getActivity()
						.getApplicationContext());
				final String[] origins = new String[originsList.size()];
				originsList.toArray(origins);
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						getActivity());
				alertDialog.setTitle("Please select origin:");
				alertDialog.setItems(origins, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int selected) {
						List<Recipe> recipes = CommonUtils.performCategorySearchFor((MainActivity) getActivity(), mTitleText, origins[selected]);
						((MainActivity) getActivity()).showListScreen(recipes, true, false, false, false, mTitleText);
						
					}
				});
				alertDialog.show();
				
			}
		});
		
		TextView titleText = (TextView) view
				.findViewById(R.id.navigationBarTitle);

		if (mTitleText != null && mTitleText.length() > 0) {
			titleText.setVisibility(View.VISIBLE);
			titleText.setText(mTitleText);
		} else {
			titleText.setVisibility(View.GONE);
		}

		backView = (TextView) view.findViewById(R.id.navigationBack);
		backView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().getSupportFragmentManager().popBackStack();
				getActivity().getSupportFragmentManager().popBackStack();
				getActivity().getSupportFragmentManager().popBackStack();

			}
		});

		if (mShowCart) {
			//showCartButton();
			hideCartButton();
		} else {
			hideCartButton();
		}
		
		if (isSearchResult) {
			showBackButton();
		}
		
		if (mShowFilter) {
			//showFilterButton();
			hideFilterButton();
		} else {
			hideFilterButton();
		}
		
		if (mShowSearch) {
			showSearchButton();
		} else {
			hideSearchButton();
		}
		
		if (mShowRefresh) {
			showRefreshButton();
		} else {
			hideRefreshButton();
		}
		
		
		return view;
	}

	/**
	 * Show the back button in the header or not
	 * @return
	 */
	public boolean isShowBack() {
		return isShowBack;
	}

	/**
	 * Set if the back button in the header should appear
	 * @param isShowBack
	 */
	public void setShowBack(boolean isShowBack) {
		this.isShowBack = isShowBack;
	}

	/**
	 * Sets the title of the screen
	 * @param mTitleText
	 */
	public void setTitleText(String mTitleText) {
		this.mTitleText = mTitleText;
	}

	/**
	 * Shows the back button in the header
	 */
	private void showBackButton() {
		backView.setVisibility(View.VISIBLE);
		menuImage.setVisibility(View.GONE);
	}

	/**
	 * Shows the filter button in the header
	 */
	private void showFilterButton() {
		mFilterImage.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Hides the filter button in the header
	 */
	private void hideFilterButton() {
		mFilterImage.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * Shows the search button in the header
	 */
	private void showSearchButton() {
		mSearchImage.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Hides the search button in the header
	 */
	private void hideSearchButton() {
		mSearchImage.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * Shows the refresh button in the header
	 */
	private void showRefreshButton() {
		mRefreshImage.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * Hides the refresh button in the header
	 */
	private void hideRefreshButton() {
		mRefreshImage.setVisibility(View.INVISIBLE);
	}
	
	/**
	 * Deselects the image
	 */
	public void deselectImage() {
		menuImage.setSelected(false);
	}
	
	/**
	 * Shows the filter button in the header
	 */
	private void showCartButton() {
		mCartImage.setVisibility(View.VISIBLE);
	}
	
	/**
	 * Hides the filter button in the header
	 */
	private void hideCartButton() {
		mCartImage.setVisibility(View.INVISIBLE);
	}
}
