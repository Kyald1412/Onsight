package com.kyald.onsight.fragments;

import java.util.ArrayList;

import com.kyald.onsight.MainActivity;
import com.kyald.onsight.R;
import com.kyald.onsight.utils.CommonUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class ImageFragment extends Fragment {
	
	public static final String TAG = ImageFragment.class.getName();

	private View parentView;

	private ViewPager mImagePager;
	private ImagePagerAdapter mImageAdapter;
	
	private DisplayImageOptions mImageOptions;

	private ImageLoader mImageLoader;
	
	private int mPosition;
	
	private ArrayList<String> mPaths;
	
	/**
	 * Returns new instance of the Fragment
	 * @return
	 */
	public static ImageFragment newInstance(ArrayList<String> paths, int position) {
		ImageFragment fragment = new ImageFragment();
		fragment.mPaths = paths;
		fragment.mPosition = position;
		return fragment;
	}

	public ImageFragment() {
		mImageLoader = ImageLoader.getInstance();
		mImageOptions = MainActivity.buildImageOptions(
				ImageScaleType.IN_SAMPLE_INT, true, true, 0, 0, 0);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		setHasOptionsMenu(true);

		setRetainInstance(false);
		
		parentView = inflater.inflate(R.layout.fragment_image, null);
		((MainActivity) getActivity()).hideNavigationView();
		((MainActivity) getActivity()).hideAdmobView();
		
		mImageAdapter = new ImagePagerAdapter(mPaths);
		mImagePager = (ViewPager) parentView.findViewById(R.id.imagepager);
		mImagePager.setAdapter(mImageAdapter);
		mImagePager.setCurrentItem(mPosition);

		return this.parentView;
	}
	
	@Override
	public void onDestroy() {
		((MainActivity) getActivity()).showNavigationView();
		((MainActivity) getActivity()).showAdmobView();
		super.onDestroy();
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
		public Object instantiateItem(ViewGroup collection, int position) {
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
			
			iv.setScaleType(ImageView.ScaleType.FIT_CENTER);
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

