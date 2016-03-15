package com.kyald.onsight.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kyald.onsight.R;

/**
 * Fragment for the Splash Screen
 * 
 * @author dmb Team
 * 
 */
public class SplashScreenFragment extends Fragment {

	public static final String TAG = SplashScreenFragment.class.getSimpleName();

	/**
	 * Constructor
	 */
	public SplashScreenFragment() {
		super();
	}

	/**
	 * Returns new instance of the Fragment
	 * @return
	 */
	public static SplashScreenFragment newInstance() {

		SplashScreenFragment fragment = new SplashScreenFragment();
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
		View view = inflater.inflate(R.layout.splash_screen, container, false);
		return view;
	}




}
