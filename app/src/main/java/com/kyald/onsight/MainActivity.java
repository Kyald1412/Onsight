package com.kyald.onsight;

//test

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.graphics.Bitmap.Config;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.kyald.onsight.adapters.DrawerListMenuAdapter;
import com.kyald.onsight.fragments.AboutFragment;
import com.kyald.onsight.fragments.ImageFragment;
import com.kyald.onsight.fragments.MainScreenFragment;
import com.kyald.onsight.fragments.NavigationBarFragment;
import com.kyald.onsight.fragments.SearchFragment;
import com.kyald.onsight.fragments.ShopFragment;
import com.kyald.onsight.fragments.SingleRecipeCategoryFragment;
import com.kyald.onsight.fragments.SingleRecipeFragment;
import com.kyald.onsight.fragments.SplashScreenFragment;
import com.kyald.onsight.fragments.SubmitFragment;
import com.kyald.onsight.models.Category;
import com.kyald.onsight.models.DrawerListMenuItem;
import com.kyald.onsight.models.Recipe;
import com.kyald.onsight.settings.AppConstants;
import com.kyald.onsight.share.MailManager;
import com.kyald.onsight.utils.CommonUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * This is the Main Activity of the application
 * 
 * @author dmb Team
 * 
 */
public class MainActivity extends FragmentActivity {

	private static final String test = "test";
	private static final String ABOUT = "About";
	private static final String SUBMIT = "Tambah ulasanmu";
	private static final String SHARE = "Share this app";

	// Key for favorite recipes
	public static final String KEY_FAV_RECIPES = "key_fav_recipes";

	// Disc cache 10mb maximum
	private static final int MAX_DISC_CACHE_SIZE = 10 * 1024 * 1024;

	// Set memory storage
	private static final int MEMORY_CACHE_SIZE = (int) (Runtime.getRuntime()
			.maxMemory() * 0.25);

	private static int SPLASH_TIME_OUT = 3000;
	
	private Recipe mSingleRecipe;

	private SharedPreferences prefs;

	private DrawerLayout mDrawerLayout;

	private ListView mDrawerList;

	private DrawerListMenuAdapter mDrawerAdapter;


	private ArrayList<DrawerListMenuItem> mMenuItems;

	/**
	 * Progress bar for loading Facebook events
	 */
	private ProgressBar mLoadingFace;

	/**
	 * Button to login to Facebook
	 */

	private AdView admobView;

	
	/**
	 * Returns the Admob view
	 * @return
	 */
	public AdView getAdmobView() {
		return admobView;
	}

	/**
	 * Called when the activity is starting. This is where most initialization
	 * should go: calling setContentView(int) to inflate the activity's UI,
	 * using findViewById(int) to programmatically interact with widgets in the
	 * UI, calling managedQuery(android.net.Uri, String[], String, String[],
	 * String) to retrieve cursors for data being displayed, etc.
	 * 
	 * You can call finish() from within this function, in which case
	 * onDestroy() will be immediately called without any of the rest of the
	 * activity lifecycle (onStart(), onResume(), onPause(), etc) executing.
	 * 
	 * Derived classes must call through to the super class's implementation of
	 * this method. If they do not, an exception will be thrown.
	 * 
	 * @param savedInstanceState
	 *            If the activity is being re-initialized after previously being
	 *            shut down then this Bundle contains the data it most recently
	 *            supplied in onSaveInstanceState(Bundle). Note: Otherwise it is
	 *            null.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		prefs = getPreferences(Context.MODE_PRIVATE);

		/**
		 * Initialize and configure image loader
		 */
		ImageLoaderConfiguration imageLoaderConfiguration = createImageLoaderConfiguration(this);
		ImageLoader.getInstance().init(imageLoaderConfiguration);
		createLeftMenu();

		showSplash();


		admobView = (AdView) findViewById(R.id.home_screen_admob);

		
	}

	/**
	 * Creates the left menu in the Application
	 */
	private void createLeftMenu() {

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

		mDrawerLayout.setDrawerListener(new DrawerListener() {

			private boolean mIsDrawerOpened;

			@Override
			public void onDrawerStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDrawerSlide(View arg0, float arg1) {
				if (mIsDrawerOpened) {
					Fragment navBar = getSupportFragmentManager()
							.findFragmentByTag(NavigationBarFragment.TAG);

					if (navBar != null) {
						((NavigationBarFragment) navBar).deselectImage();
					}

				}
			}

			@Override
			public void onDrawerOpened(View arg0) {
				mIsDrawerOpened = true;
			}

			@Override
			public void onDrawerClosed(View arg0) {
				mIsDrawerOpened = false;

			}
		});

		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		/**
		 * Create left menu list items
		 */
		mMenuItems = new ArrayList<DrawerListMenuItem>(0);

		ArrayList<Category> categories = ((ApplicationContext) getApplicationContext())
				.getParsedApplicationSettings().getCategories();

		DrawerListMenuItem logoMenuItem = new DrawerListMenuItem("", false, "",
				true);
		logoMenuItem.setLogo(true);


		mMenuItems.add(logoMenuItem);

		/*
		for (int i = 0; i < categories.size(); i++) {
			if (categories.get(i).getRecipes().size() > 0) {
				mMenuItems.add(new DrawerListMenuItem(categories.get(i)
						.getName(), false, categories.get(i).getIcon(), true));
			}
		}

		*/

		mMenuItems.add(new DrawerListMenuItem(AppConstants.CATEGORY_FAVORITE, false, "fav",
				true));
		
		// About Page Creation
		mMenuItems.add(new DrawerListMenuItem(SUBMIT, false, "submit2",
				true));

		// About Page Creation
		mMenuItems.add(new DrawerListMenuItem(SHARE, false, "share_icon_active",
				true));

		mMenuItems.add(new DrawerListMenuItem(ABOUT, false, "about",
				true));

		mDrawerAdapter = new DrawerListMenuAdapter(
				(ApplicationContext) getApplicationContext(), mMenuItems);

		mDrawerList.setAdapter(mDrawerAdapter);
		
		/**
		 * Called when item from the left menu is clicked
		 */
		mDrawerList
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(final AdapterView<?> arg0,
							View arg1, final int arg2, long arg3) {
						mDrawerLayout.closeDrawer(Gravity.LEFT);

						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								DrawerListMenuItem menuItem = (DrawerListMenuItem) arg0
										.getItemAtPosition(arg2);
								if (menuItem != null && !menuItem.isLogo()) {
									if (menuItem.getMenuItemName().equals(ABOUT)) {
										mDrawerAdapter.deselectAll();
										menuItem.setMenuSelected(true);
										mDrawerAdapter.notifyDataSetChanged();
										
										showAbout();
									} else if (menuItem.getMenuItemName().equals(SUBMIT)) {
										mDrawerAdapter.deselectAll();
										menuItem.setMenuSelected(true);
										mDrawerAdapter.notifyDataSetChanged();

										showSubmit();
									} else if (menuItem.getMenuItemName().equals(SHARE)) {
										mDrawerAdapter.deselectAll();
										menuItem.setMenuSelected(true);
										mDrawerAdapter.notifyDataSetChanged();

										showShare();
									} else {
										mDrawerAdapter.deselectAll();
										menuItem.setMenuSelected(true);
										mDrawerAdapter.notifyDataSetChanged();
										String selectedCategoryName = menuItem
												.getMenuItemName();
										Category selectedCategory = findCategoryWithName(selectedCategoryName);

										if (selectedCategory == null
												&& selectedCategoryName
														.equals(AppConstants.CATEGORY_FAVORITE)) {
											selectedCategory = CommonUtils
													.getFavoriteCategory(MainActivity.this);
										}

										showListScreen(
												selectedCategory.getRecipes(),
												true, false, true, false,
												selectedCategory.getName());
									}

								} else {
									mDrawerAdapter.deselectAll();
									menuItem.setMenuSelected(true);
									mDrawerAdapter.notifyDataSetChanged();

									showMainScreen();

								}
							}
						}, 300);

					}
				});
	}

	private void showShare() {

		MailManager.openMailIntent(this,"Test", getEmailBody());
	}

	private String getEmailBody() {
		StringBuilder builder = new StringBuilder();
		builder.append("Share this app:");
		builder.append("\n\n");
		builder.append("Download more Onsight at " + "http://play.google.com/store/apps/details?id=" + this.getPackageName());
		return builder.toString();
	}

	/**
	 * Shows the Splash Screen
	 */
	private void showSplash() {

		SplashScreenFragment splash = SplashScreenFragment.newInstance();

		showScreen(null, splash, SplashScreenFragment.TAG, false);

		if (isNetworkAvailable()) {
			//URL url = new URL(RecipesXMLTagConstants.TAG_URL_SETTINGS);
			//inputStream = url.openStream();

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
//					getSupportFragmentManager().popBackStack();
					showMainScreen();

					if (AppConstants.ENABLE_ADMOB_HOME_PAGE) {
						admobView.setVisibility(View.VISIBLE);
						admobView.setVisibility(View.VISIBLE);
						AdRequest adRequest = new AdRequest.Builder().build();
						admobView.loadAd(adRequest);
					} else {
						admobView.setVisibility(View.GONE);
					}


					if (mDrawerAdapter.getItem(0) != null) {
						mDrawerAdapter.getItem(0).setMenuSelected(true);
						mDrawerAdapter.notifyDataSetChanged();
					}
				}

			}, SPLASH_TIME_OUT);

		} else {
			//shouldShowToastMessage = true;
			//RecipesXMLTagConstants.TAG_URL_SETTINGS = "";
			//inputStream = assetManager.open("recipes_settings.xml");

			Toast.makeText(this,
					getString(R.string.no_internet_connectivity),
					Toast.LENGTH_LONG).show();

			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
			//		getSupportFragmentManager().popBackStack();
					showMainScreen();


					if (AppConstants.ENABLE_ADMOB_HOME_PAGE) {
						admobView.setVisibility(View.VISIBLE);
						admobView.setVisibility(View.VISIBLE);
						AdRequest adRequest = new AdRequest.Builder().build();
						admobView.loadAd(adRequest);
					} else {
						admobView.setVisibility(View.GONE);
					}



					if (mDrawerAdapter.getItem(0) != null) {
						mDrawerAdapter.getItem(0).setMenuSelected(true);
						mDrawerAdapter.notifyDataSetChanged();
					}
				}

			}, SPLASH_TIME_OUT);

		}
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else
			return true;
	}



	/**
	 * Shows the Main Screen
	 */
	private void showMainScreen() {

		MainScreenFragment screen = MainScreenFragment
				.newInstance((ApplicationContext) getApplicationContext());

		NavigationBarFragment navBar = NavigationBarFragment.newInstance(
				(ApplicationContext) getApplicationContext(), "Home", false,
				true, false, true, false);
		// navBar.setTitleText(getString(R.string.recipes));
		showScreen(navBar, screen, MainScreenFragment.TAG, false);
	}

	/**
	 * Shows the list screen with chosen recipes
	 * 
	 * @param chosenRecipes
	 * @param addToBackStack
	 * @param isSearchResult
	 * @param titleScren
	 */
	public void showListScreen(List<Recipe> chosenRecipes,
			boolean addToBackStack, boolean isSearchResult, boolean showFilter, boolean showCart, String titleScren) {

		SingleRecipeCategoryFragment screen = SingleRecipeCategoryFragment
				.newInstance(chosenRecipes, titleScren);

		NavigationBarFragment navBar = NavigationBarFragment.newInstance(
				(ApplicationContext) getApplicationContext(), titleScren,
				isSearchResult, showFilter, showCart, true, false);
		navBar.setShowBack(true);
		showScreen(navBar, screen, SingleRecipeCategoryFragment.TAG,
				addToBackStack);
	}

	/**
	 * Shows the screen with single Recipe
	 * 
	 * @param recipe
	 */
	public void showSingleRecipe(Recipe recipe) {
		setSingleRecipe(recipe);
		SingleRecipeFragment screen = SingleRecipeFragment.newInstance(recipe);
		NavigationBarFragment navBar = NavigationBarFragment.newInstance(
				(ApplicationContext) getApplicationContext(), "", false, false, true, true, false);
		navBar.setShowBack(true);
		// navBar.setTitleText(getString(R.string.recipes));
		showScreen(navBar, screen, SingleRecipeFragment.TAG, true);
	}
	
	/**
	 * Shows the screen with recipe images
	 * 
	 * @param recipe
	 */
	public void showImages(ArrayList<String> paths, int position) {
		ImageFragment screen = ImageFragment.newInstance(paths, position);

		NavigationBarFragment navBar = NavigationBarFragment.newInstance(
				(ApplicationContext) getApplicationContext(), "", false, false, false, true, false);
		navBar.setShowBack(true);
		// navBar.setTitleText(getString(R.string.recipes));
		showScreen(navBar, screen, ImageFragment.TAG, true);
	}
	
	/**
	 * Shows the screen with single Recipe
	 * 
	 * @param recipe
	 */
	public void showAbout() {
		AboutFragment screen = AboutFragment.newInstance();

		NavigationBarFragment navBar = NavigationBarFragment.newInstance(
				(ApplicationContext) getApplicationContext(), ABOUT, false, false, false, true, false);
		navBar.setShowBack(true);
		showScreen(navBar, screen, AboutFragment.TAG, true);
	}

	/**
	 * Shows the submit screen
	 *
	 * @param recipe
	 */
	public void showSubmit() {
		SubmitFragment screen = SubmitFragment.newInstance();

		NavigationBarFragment navBar = NavigationBarFragment.newInstance(
				(ApplicationContext) getApplicationContext(), SUBMIT, false, false, false, false, true);
		navBar.setShowBack(true);
		showScreen(navBar, screen, SubmitFragment.TAG, true);
	}
	
	/**
	 * Shows the screen with shopping list
	 * 
	 * @param recipe
	 */
	public void showShop() {
		if (getSingleRecipe() != null) {
			ShopFragment screen = ShopFragment.newInstance(getSingleRecipe());

			NavigationBarFragment navBar = NavigationBarFragment.newInstance(
					(ApplicationContext) getApplicationContext(), "Shopping List", false, false, false, false, true);
			navBar.setShowBack(true);
			showScreen(navBar, screen, ShopFragment.TAG, true);
		}
		
	}

	/**
	 * Shows or hides the Search element
	 */
	public void showHideSearchFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		View view = findViewById(R.id.placeholder_search);

		// Show search
		if (getSupportFragmentManager().findFragmentByTag(SearchFragment.TAG) == null) {

			SearchFragment searchFragment = SearchFragment.newInstance();

			view.setVisibility(View.VISIBLE);
			view.bringToFront();
			ft.replace(R.id.placeholder_search, searchFragment,
					SearchFragment.TAG);
			ft.addToBackStack(String.valueOf(System
					.identityHashCode(searchFragment)));

			ft.commitAllowingStateLoss();
			fm.executePendingTransactions();
		} else { // Hide search
			view.setVisibility(View.GONE);
			getSupportFragmentManager().popBackStack();
		}

	}

	/**
	 * Shows a screen
	 * 
	 * @param navbar
	 * @param content
	 * @param contentTag
	 * @param addToBackStack
	 */
	private void showScreen(Fragment navbar, Fragment content,
			String contentTag, boolean addToBackStack) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.setCustomAnimations(R.anim.left_slide_in, R.anim.left_slide_out,
				R.anim.right_slide_in, R.anim.right_slide_out);
		// Navigation bar fade animation

		View view = findViewById(R.id.placeholder_navigation_bar);
		if (navbar != null) {
			if (view != null) {
				view.setVisibility(View.VISIBLE);
			}
			ft.replace(R.id.placeholder_navigation_bar, navbar,
					NavigationBarFragment.TAG);
		} else {
			if (view != null) {
				view.setVisibility(View.GONE);
			}
		}

		ft.replace(R.id.placeholder_content, content, contentTag);

		if (addToBackStack) {
			ft.addToBackStack(String.valueOf(System.identityHashCode(content)));
		}

		ft.commitAllowingStateLoss();
		fm.executePendingTransactions();
	}

	/**
	 * Used to build ImageLoader configuration
	 * 
	 * @param appContext
	 *            the context of the application
	 * @return ImageLoaderConfiguration used for the UniversalImageLoader
	 * 
	 */
	@SuppressWarnings("deprecation")
	public static ImageLoaderConfiguration createImageLoaderConfiguration(
			Context appContext) {

		WindowManager windowManager = (WindowManager) appContext
				.getSystemService(Context.WINDOW_SERVICE);

		Display display = windowManager.getDefaultDisplay();
		Log.v("Utils",
				"Start building ImageLoader configuration for API level "
						+ Build.VERSION.SDK_INT);
		ImageLoaderConfiguration.Builder b = new ImageLoaderConfiguration.Builder(
				appContext);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			b.threadPoolSize(1);
		} else {
			b.threadPoolSize(3);
		}
		b.memoryCacheExtraOptions(display.getWidth(), display.getHeight());
		b.threadPriority(Thread.NORM_PRIORITY - 1);
		b.memoryCacheSize(MEMORY_CACHE_SIZE);
		b.discCacheSize(MAX_DISC_CACHE_SIZE);
		b.discCacheFileNameGenerator(new HashCodeFileNameGenerator());
		b.tasksProcessingOrder(QueueProcessingType.FIFO);
		b.defaultDisplayImageOptions(DisplayImageOptions.createSimple());
		return b.build();
	}

	/**
	 * Used to build DisplayImageOptions for ImageLoader
	 * 
	 * @param scaleType
	 *            {@link ImageScaleType}
	 * @param inMemoryCache
	 *            boolean showing if images loaded should be cached in memory
	 * @param resetViewBeforeLoading
	 *            boolean showing if the imageView should be reset before
	 *            loading
	 * @param roundedPixels
	 *            the pixels used with {@link RoundedBitmapDisplayer} to display
	 *            rounded picture corners
	 * @param emptyUrlImage
	 *            the resource to show when no image url is present
	 * @param stubImage
	 *            the resource to show when no image is yet loaded
	 * @return
	 */
	public static DisplayImageOptions buildImageOptions(
			ImageScaleType scaleType, boolean inMemoryCache,
			boolean resetViewBeforeLoading, int roundedPixels,
			int emptyUrlImage, int stubImage) {

		DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		builder.bitmapConfig(Config.RGB_565);
		builder.showStubImage(stubImage > 0 ? stubImage : 0);
		builder.showImageForEmptyUri(emptyUrlImage > 0 ? emptyUrlImage : 0);
		if (inMemoryCache)
			builder.cacheInMemory(true);
		if (resetViewBeforeLoading)
			builder.resetViewBeforeLoading(true);
		builder.cacheOnDisc(true);
		builder.imageScaleType(scaleType);
		if (roundedPixels > 0)
			builder.displayer(new RoundedBitmapDisplayer(roundedPixels));
		builder.cacheOnDisc(true);
		return builder.build();
	}

	/**
	 * Finds a specific category by name
	 * 
	 * @param name
	 * @return
	 */
	private Category findCategoryWithName(String name) {
		ArrayList<Category> categories = ((ApplicationContext) getApplicationContext())
				.getParsedApplicationSettings().getCategories();

		for (int i = 0; i < categories.size(); i++) {
			if (categories.get(i).getName().equals(name)) {
				return categories.get(i);
			}
		}

		return null;
	}

	/**
	 * Shows the left menu
	 * 
	 * @param menuImage
	 */
	public void showLeftMenu(ImageView menuImage) {
		mDrawerLayout.openDrawer(Gravity.LEFT);
	}

	/**
	 * Returns the items of the left menu
	 * 
	 * @return
	 */
	public ArrayList<DrawerListMenuItem> getMenuItems() {
		return mMenuItems;
	}

	/**
	 * Corrects the items in the left menu
	 * 
	 * @param menuItemNameValue
	 */
	public void setCorrectDrawerMenuItem(String menuItemNameValue) {
		mDrawerAdapter.deselectAll();

		for (int i = 0; i < mMenuItems.size(); i++) {
			if (mMenuItems.get(i).getMenuItemName().equals(menuItemNameValue)) {
				mMenuItems.get(i).setMenuSelected(true);
				break;
			}
		}
		mDrawerAdapter.notifyDataSetChanged();
	}
	
	public void deselectDrawerMenuItem() {
		if (mDrawerAdapter != null) {
			mDrawerAdapter.deselectAll();
			mDrawerAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * Returns the id of the Favorites menu item
	 * 
	 * @return
	 */
	public String[] getFavIdsInArray() {
		return prefs.getString(KEY_FAV_RECIPES, "").split(";");
	}

	/**
	 * Puts the id of the recipes in the shared preferences
	 * 
	 * @param id
	 */
	public void putRecipeIdInSharedPrefs(String id) {
		String currentFav = prefs.getString(KEY_FAV_RECIPES, "");
		currentFav += id;
		currentFav += ";";

		SharedPreferences.Editor ed = prefs.edit();
		ed.putString(KEY_FAV_RECIPES, currentFav);
		ed.commit();
	}

	/**
	 * Removes the recipe id from the shared preferences
	 * 
	 * @param id
	 */
	public void removeRecipeIdFromSharedPrefs(String id) {
		String currentFav = prefs.getString(KEY_FAV_RECIPES, "");

		String[] allFavs = currentFav.split(";");

		for (int i = 0; i < allFavs.length; i++) {
			if (allFavs[i].equals(id)) {
				allFavs[i] = null;
			}
		}

		String newFavString = "";
		for (int i = 0; i < allFavs.length; i++) {
			if (allFavs[i] != null) {
				newFavString += allFavs[i];
				newFavString += ";";
			}
		}

		SharedPreferences.Editor ed = prefs.edit();
		ed.putString(KEY_FAV_RECIPES, newFavString);
		ed.commit();
	}

	/**
	 * Shows if a given recipe id is stored in the shared preferences
	 * 
	 * @param id
	 * @return
	 */
	public boolean isRecipeIdInFavSharedPrefs(String id) {

		String currentFav = prefs.getString(KEY_FAV_RECIPES, "");

		String[] allFavs = currentFav.split(";");

		for (int i = 0; i < allFavs.length; i++) {
			if (allFavs[i].equals(id)) {
				return true;
			}
		}

		return false;
	}
	
	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
			openQuitDialog();
		} else {
			if (getSupportFragmentManager().findFragmentByTag(SearchFragment.TAG) != null) {
				findViewById(R.id.placeholder_search).setVisibility(View.GONE);
				getSupportFragmentManager().popBackStack();
			} else if (getSupportFragmentManager().findFragmentByTag(SingleRecipeFragment.TAG) != null) {
				getSupportFragmentManager().popBackStack();
			} else if (getSupportFragmentManager().findFragmentByTag(AboutFragment.TAG) != null) {
				getSupportFragmentManager().popBackStack();
			} else if (getSupportFragmentManager().findFragmentByTag(SubmitFragment.TAG) != null) {
				getSupportFragmentManager().popBackStack();
			} else {
				for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
					getSupportFragmentManager().popBackStack();
				}
			}
			
		}

	}

	private void openQuitDialog() {
		AlertDialog.Builder quitDialog = new AlertDialog.Builder(
				MainActivity.this);
		quitDialog.setTitle("Are you sure you want to exit the app?");

		quitDialog.setPositiveButton("YES", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		quitDialog.setNegativeButton("NO", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				getSupportFragmentManager().popBackStack();
			}
		});

		quitDialog.show();
	}
	
	public void showAdmobView() {
		if (admobView != null && AppConstants.ENABLE_ADMOB_HOME_PAGE) {
			admobView.setVisibility(View.VISIBLE);
		}
		
	}
	
	public void hideAdmobView() {
		if (admobView != null && AppConstants.ENABLE_ADMOB_HOME_PAGE) {
			admobView.setVisibility(View.GONE);
		}
	}
	
	public void showNavigationView() {
		View view = findViewById(R.id.placeholder_navigation_bar);
		if (view != null) {
			view.setVisibility(View.VISIBLE);
		}
		
	}
	
	public void hideNavigationView() {
		View view = findViewById(R.id.placeholder_navigation_bar);
		if (view != null) {
			view.setVisibility(View.GONE);
		}
	}
	
	public Recipe getSingleRecipe() {
		return mSingleRecipe;
	}
	
	public void setSingleRecipe(Recipe recipe) {
		this.mSingleRecipe = recipe;
	}

	public void refreshShoppingList() {
		Fragment shop = getSupportFragmentManager()
				.findFragmentByTag(ShopFragment.TAG);

		if (shop != null) {
			((ShopFragment) shop).refresh();
		}
		
	}
}
