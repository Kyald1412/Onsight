package com.kyald.onsight.fragments;

import java.util.ArrayList;
import java.util.List;

import com.kyald.onsight.R;
import com.kyald.onsight.models.Recipe;
import com.kyald.onsight.models.RecipeIngredient;
import com.kyald.onsight.models.ShoppingCart;
import com.kyald.onsight.models.ShoppingItem;
import com.kyald.onsight.utils.CommonUtils;
import com.kyald.onsight.utils.ShakeDetector;
import com.google.gson.Gson;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;


public class ShopFragment extends Fragment {

	public static String TAG = ShopFragment.class.getSimpleName();
	private static String SHOPPING_CART = "ShoppingCart";
	
	private Recipe mRecipe;
	
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private ShakeDetector mShakeDetector;

	private ShoppingCart mShoppingCart;

	private LinearLayout mProductsList;

	public static ShopFragment newInstance(Recipe recipe) {
		ShopFragment fragment = new ShopFragment();
		fragment.mRecipe = recipe;
		return fragment;
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_shop, null);
		mProductsList = (LinearLayout) v.findViewById(
				R.id.shoppingList);
		
		addProducts();

		return v;
	}

	private void removeProducts() {
		mProductsList.removeAllViews();
		mShoppingCart.removeAllItemsFromCart(mRecipe.getId());
		storeShoppingCart(mShoppingCart);
	}

	private void addProducts() {
		mShoppingCart = retrieveShoppingCart();
		for (RecipeIngredient  ingredient : mRecipe.getRecipeIngredients()) {
			addProduct(ingredient.getRecipeIngredientQuantity() + " " + ingredient.getRecipeIngredientName());
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		
		
		mSensorManager = (SensorManager) getActivity().getSystemService(
				Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		// mShakeDetector = new ShakeDetector();
		mShakeDetector = new ShakeDetector(new ShakeDetector.OnShakeListener() {
			@Override
			public void onShake() {
				// Get instance of Vibrator from current Context
				Vibrator v = (Vibrator) getActivity().getSystemService(
						Context.VIBRATOR_SERVICE);
				// Vibrate for 400 milliseconds
				v.vibrate(400);
				
				clearProducts();
			}

		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		mSensorManager.registerListener(mShakeDetector, mAccelerometer,
				SensorManager.SENSOR_DELAY_UI);
	}

	@Override
	public void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(mShakeDetector);
		storeShoppingCart(mShoppingCart);
	}
	
	private void clearProducts() {
		List<TextView> toBeRemoved = new ArrayList<TextView>();
		for (int i = 0; i < mProductsList.getChildCount(); i++) {
			if (mProductsList.getChildAt(i) instanceof TextView) {
				TextView product = (TextView) mProductsList.getChildAt(i);
				if (product.getPaint().getFlags() == (Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG)) {
					toBeRemoved.add(product);
				}
			}
		}
		for (int i = 0; i < toBeRemoved.size(); i++) {
			mShoppingCart.clearItemInCart(mRecipe.getId(), toBeRemoved.get(i).getText().toString());
			mProductsList.removeViewAt(mProductsList.indexOfChild(toBeRemoved.get(i)) + 1);
			mProductsList.removeView(toBeRemoved.get(i));
		}

	}
	
	protected void addProduct(String product) {
		ShoppingItem item; 
		if (!mShoppingCart.containsItem(mRecipe.getId(), product)) {
			item = new ShoppingItem(product, false, false);
			mShoppingCart.addItemToCart(mRecipe.getId(), item);
		} else {
			item = mShoppingCart.getItem(mRecipe.getId(), product);
		}
		
		if (item != null && !item.isCleared()) {
			LayoutParams lpView = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			TextView productView = new TextView(getActivity());
			
			productView.setLayoutParams(lpView);
			productView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			productView.setText(product);
			productView.setTag(product);
			productView.setOnClickListener(new CheckProductListener());
			if (item.isChecked()) {
				productView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
			} else {
				productView.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
			}
			
			mProductsList.addView(productView);
			View delimiter = new View(getActivity());
			LayoutParams lpViewDelimiter = new LayoutParams(LayoutParams.WRAP_CONTENT,
					CommonUtils.dipsToPixels(getActivity(), 1));
			delimiter.setLayoutParams(lpViewDelimiter);
			delimiter.setBackgroundColor(getActivity().getResources().getColor(R.color.delimiter_color));
			mProductsList.addView(delimiter);
		}
		

	}

	private void checkProduct(TextView v) {
		if (v.getPaint().getFlags() == (Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG)) {
			v.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
			mShoppingCart.restoreItemInCart(mRecipe.getId(), v.getText().toString());
		} else {
			v.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
			mShoppingCart.checkItemInCart(mRecipe.getId(), v.getText().toString());
		}
		
		v.invalidate();
	}
	
	private class CheckProductListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			if (v instanceof TextView) {
				checkProduct((TextView) v);
			}
		}

	}
	
	public void storeShoppingCart(ShoppingCart shoppingCart) {
		SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
		Gson gson = new Gson();
		String cart = gson.toJson(shoppingCart);
		SharedPreferences.Editor ed = prefs.edit();
        ed.putString(SHOPPING_CART, cart);
        ed.commit();
	}
	
	public ShoppingCart retrieveShoppingCart() {
		ShoppingCart result;
		SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
		Gson gson = new Gson();
		String shoppingCart = prefs.getString(SHOPPING_CART, "");
		if (shoppingCart.equalsIgnoreCase("")) {
			result = new ShoppingCart();
		} else {
			result = gson.fromJson(shoppingCart, ShoppingCart.class);
		}
		return result;
	}


	public void refresh() {
		removeProducts();
		addProducts();
	}

}
