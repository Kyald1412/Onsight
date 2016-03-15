package com.kyald.onsight.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Model for the Drawe List Menu Item
 * 
 * @author dmb Team
 *
 */
public class DrawerListMenuItem implements Parcelable {

	private boolean isLogo;
	private boolean menuState;
	private boolean isSelectable;
	private String menuName;
	private String mPicName;

	/**
	 * Constructor
	 * 
	 * @param name
	 * @param state
	 * @param picName
	 * @param isSelectable
	 */
	public DrawerListMenuItem(String name, boolean state, String picName,
			boolean isSelectable) {
		this.menuName = name;
		this.menuState = state;
		this.mPicName = picName;
		this.isSelectable = isSelectable;
	}

	/**
	 * Checks if the menu is selected or not
	 * 
	 * @return
	 */
	public boolean isMenuSelected() {
		return menuState;
	}

	/**
	 * Sets the menu as selected or not
	 * 
	 * @param selected
	 */
	public void setMenuSelected(boolean selected) {
		this.menuState = selected;
	}

	/**
	 * Returns the name of the menu item
	 * 
	 * @return
	 */
	public String getMenuItemName() {
		return menuName;
	}

	/**
	 * Sets the name of the menu item
	 * 
	 * @param facilityName
	 */
	public void setMenuItemName(String facilityName) {
		this.menuName = facilityName;
	}

	/**
	 * Returns the name of the picture
	 * 
	 * @return
	 */
	public String getPicName() {
		return mPicName;
	}

	/**
	 * Sets the name of the picture
	 * 
	 * @param picName
	 */
	public void setPicName(String picName) {
		mPicName = picName;
	}

	/**
	 * Checks if the menu item is selectable
	 * 
	 * @return
	 */
	public boolean isSelectable() {
		return isSelectable;
	}
	
	/**
	 * Sets if the menu item is selectable
	 * 
	 * @param isSelectable
	 */
	public void setSelectable(boolean isSelectable) {
		this.isSelectable = isSelectable;
	}

	/**
	 * Creates menu item from Parcel
	 * 
	 * @param in
	 */
	protected DrawerListMenuItem(Parcel in) {

		menuName = in.readString();

		int i = in.readInt();
		if (i == 0) {
			menuState = false;
		} else {
			menuState = true;
		}
	}

	/**
	 * Describes the contents
	 */
	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * Writes to Parcel
	 */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(menuName);
		if (menuState) {
			dest.writeInt(1);
		} else {
			dest.writeInt(0);
		}
	}

	/**
	 * Checks if it is logo
	 * 
	 * @return
	 */
	public boolean isLogo() {
		return isLogo;
	}

	/**
	 * Sets if it is logo
	 * 
	 * @param isLogo
	 */
	public void setLogo(boolean isLogo) {
		this.isLogo = isLogo;
	}

	/**
	 * Parcelable Creator
	 */
	public static final Parcelable.Creator<DrawerListMenuItem> CREATOR = new Parcelable.Creator<DrawerListMenuItem>() {

		@Override
		public DrawerListMenuItem createFromParcel(Parcel source) {
			return new DrawerListMenuItem(source);
		}

		@Override
		public DrawerListMenuItem[] newArray(int size) {
			return new DrawerListMenuItem[size];
		}
	};

}