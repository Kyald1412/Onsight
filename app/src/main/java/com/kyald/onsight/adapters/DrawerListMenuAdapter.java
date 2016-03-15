package com.kyald.onsight.adapters;

import java.util.ArrayList;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kyald.onsight.ApplicationContext;
import com.kyald.onsight.R;
import com.kyald.onsight.models.DrawerListMenuItem;
import com.kyald.onsight.models.MainSettings;
import com.kyald.onsight.utils.CommonUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 
 * Adapter used for the left list menu
 * 
 */
public class DrawerListMenuAdapter extends ArrayAdapter<DrawerListMenuItem> {

	private ApplicationContext mContext;
	private ImageLoader mImageLoader;
	private DisplayImageOptions mImageOptions;
	private MainSettings mMainSettings;

	/**
	 * 
	 * @param context
	 *            the application context
	 * @param items
	 *            the menu items to show
	 */
	public DrawerListMenuAdapter(ApplicationContext context,
			ArrayList<DrawerListMenuItem> items) {
		super(context, 0, items);
		mContext = context;

		mImageLoader = ImageLoader.getInstance();
		mImageOptions = com.kyald.onsight.MainActivity.buildImageOptions(
				ImageScaleType.IN_SAMPLE_INT, true, true, 0, 0, 0);

		mMainSettings = ((ApplicationContext) getContext()
				.getApplicationContext()).getParsedApplicationSettings()
				.getMainSettings();
	}

	/**
	 * Returns the View
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (getItem(position).isLogo()) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.drawer_header_view, null);

			if (getItem(position).isMenuSelected()) {
				String logoSelectedUri = "drawable://"
						+ getContext().getResources().getIdentifier(
								mMainSettings.getLogoActive(), "drawable",
								getContext().getPackageName());
				mImageLoader.displayImage(logoSelectedUri,
						((ImageView) convertView), mImageOptions);

			} else {
				String logoDeselectedUri = "drawable://"
						+ getContext().getResources().getIdentifier(
								mMainSettings.getLogoInActive(), "drawable",
								getContext().getPackageName());
				mImageLoader.displayImage(logoDeselectedUri,
						((ImageView) convertView), mImageOptions);

			}

		} else {

			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.drawer_list_item, null);

			View selectedIndicator = convertView
					.findViewById(R.id.drawerMenuSelectedIndicator);

			TextView menuItemText = (TextView) convertView
					.findViewById(R.id.drawerMenuItemTextView);

			ImageView menuIteLogoImage = (ImageView) convertView
					.findViewById(R.id.drawerMenuItemLogoImageView);

			DrawerListMenuItem item = getItem(position);

			if (item.isMenuSelected()) {
				menuItemText.setTypeface(null, Typeface.BOLD);
				menuItemText.setTextColor(mContext.getResources().getColor(
						R.color.green));
				selectedIndicator.setVisibility(View.VISIBLE);
			} else {
				menuItemText.setTypeface(null, Typeface.NORMAL);
				menuItemText.setTextColor(mContext.getResources().getColor(
						android.R.color.black));
				selectedIndicator.setVisibility(View.GONE);
			}

			menuItemText.setText(item.getMenuItemName());
			CommonUtils.setRobotoThinFont(getContext(), menuItemText);

			String photoUri = "drawable://"
					+ getContext().getResources().getIdentifier(
							getItem(position).getPicName(), "drawable",
							getContext().getPackageName());

			mImageLoader
					.displayImage(photoUri, menuIteLogoImage, mImageOptions);

		}
		return convertView;
	}

	/**
	 * Used to deselect all menu items
	 */
	public void deselectAll() {
		for (int i = 0; i < getCount(); i++) {
			if (getItem(i) instanceof DrawerListMenuItem) {
				getItem(i).setMenuSelected(false);
			}
		}
	}

	/**
	 * Used to select a certain menu item
	 * 
	 * @param menuItemText
	 *            the menu item to be selected
	 */
	public void setSelected(String menuItemText) {
		deselectAll();
		for (int i = 0; i < getCount(); i++) {
			if (getItem(i) instanceof DrawerListMenuItem) {
				DrawerListMenuItem item = getItem(i);
				if (item.getMenuItemName().equalsIgnoreCase(menuItemText)) {
					item.setMenuSelected(true);
				}
			}
		}
		notifyDataSetChanged();
	}
}
