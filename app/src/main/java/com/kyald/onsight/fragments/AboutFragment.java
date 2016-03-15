package com.kyald.onsight.fragments;

import com.kyald.onsight.R;
import com.kyald.onsight.settings.AppConstants;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class AboutFragment extends Fragment {

	public static String TAG = AboutFragment.class.getSimpleName();

	public static AboutFragment newInstance() {
		AboutFragment fragment = new AboutFragment();

		return fragment;
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_about, null);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		TextView blogTitle = (TextView) getView().findViewById(
				R.id.about_fragment_blog_title);
		blogTitle.setText(AppConstants.USER_BLOG_TITLE);

		TextView blogUrl = (TextView) getView().findViewById(
				R.id.about_fragment_blog_url);
		blogUrl.setText(AppConstants.USER_WEBSITE_URL);

		TextView blogMail = (TextView) getView().findViewById(
				R.id.about_fragment_blog_mail);
		blogMail.setText(AppConstants.USER_MAIL);

		TextView blogPhone = (TextView) getView().findViewById(
				R.id.about_fragment_blog_phone);
		blogPhone.setText(AppConstants.USER_PHONE);
		
		ImageView facebook = (ImageView) getView().findViewById(R.id.facebook);
		facebook.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(AppConstants.FACEBOOK_URL));
				startActivity(i);
			}
		});
		
		ImageView twitter = (ImageView) getView().findViewById(R.id.twitter);
		twitter.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(AppConstants.TWITTER_URL));
				startActivity(i);
			}
		});
	}
}
