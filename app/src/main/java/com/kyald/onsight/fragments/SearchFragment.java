package com.kyald.onsight.fragments;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.kyald.onsight.ApplicationContext;
import com.kyald.onsight.MainActivity;
import com.kyald.onsight.R;
import com.kyald.onsight.models.Recipe;
import com.kyald.onsight.utils.CommonUtils;

/**
 * Fragment for the Search functionality
 * 
 * @author dmb Team
 * 
 */
public class SearchFragment extends Fragment {

	public static final String TAG = SearchFragment.class.getSimpleName();
	
	private EditText search;

	/**
	 * Returns new instance of the Fragment
	 * @return
	 */
	public static SearchFragment newInstance() {
		SearchFragment fragment = new SearchFragment();
		return fragment;
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

		View v = inflater.inflate(R.layout.fragment_search, null);

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

		search = (EditText) getView().findViewById(
				R.id.fra_search_edittext);
		
		search.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    setImeVisibility(true);
                } else {
                    setImeVisibility(false);
                }
            }
        });
		
		search.requestFocus();

		ImageView imageGo = (ImageView) getView().findViewById(
				R.id.fra_search_go);

		imageGo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ApplicationContext appContext = (ApplicationContext) getActivity()
						.getApplicationContext();

				List<Recipe> resultRecipes = CommonUtils.performSearchFor(
						appContext, search.getText().toString());

				((MainActivity) getActivity()).showHideSearchFragment();

				((MainActivity) getActivity()).showListScreen(resultRecipes,
						true, true, false, false, "Search Results");
			}
		});
	}
	
	private Runnable mShowImeRunnable = new Runnable() {
	    public void run() {
	        InputMethodManager imm = (InputMethodManager) getActivity()
	                .getSystemService(Context.INPUT_METHOD_SERVICE);

	        if (imm != null) {
	            imm.showSoftInput(search,0);
	        }
	    }
	};

	private void setImeVisibility(final boolean visible) {
	    if (visible) {
	        search.post(mShowImeRunnable);
	    } else {
	        search.removeCallbacks(mShowImeRunnable);
	        InputMethodManager imm = (InputMethodManager) getActivity()
	                .getSystemService(Context.INPUT_METHOD_SERVICE);

	        if (imm != null) {
	            imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
	        }
	    }
	}
}
