package com.kyald.onsight.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.kyald.onsight.R;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class SubmitFragment extends Fragment {


	Button btnSelFile,btnSelFile1,btnSelFile2,btnSubmit;
	TextView judul, category, description, name, txt_uri, real_uri, txt_uri1, real_uri1, txt_uri2, real_uri2, txtjdudul, txtdeskripsi, txtnama;
	View dividerjudul, dividerdeskripsi, dividernama;

	Uri orgUri,fileUri;
	String convertedPath = "";
	String convertedPath1 = "";
	String convertedPath2 = "";
	String subject = "Onsight";

	ImageButton imgbtn1, imgbtn2;

	private static final String username = "weeework.studio@gmail.com";
	private static final String password = "spongebobsquarepants";

	private Spinner spinner;
	private static final String[]paths = {"- Pilih kategori -","Flora", "Tokoh", "Tempat Wisata","Kuliner","Fauna","Kerajinan tangan","Alat Musik","Lain-lain..."};

	String [] filename;

	int gambar = 0;

	public static String TAG = SubmitFragment.class.getSimpleName();

	public static SubmitFragment newInstance() {

		return new SubmitFragment();
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.fragment_submit, null);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		TextView blogTitle = (TextView) getView().findViewById(
				R.id.about_fragment_blog_title);
		blogTitle.setText("Tambah ulasan");

		TextView blogUrl = (TextView) getView().findViewById(
				R.id.about_fragment_blog_url);
		blogUrl.setText("Kirimkan ulasan tempat favoritmu disini");

		TextView blogMail = (TextView) getView().findViewById(
				R.id.about_fragment_blog_mail);
		blogMail.setText("Beritahu seluruh indonesia bahkan seluruh dunia tempat favoritmu");

		TextView blogPhone = (TextView) getView().findViewById(
				R.id.about_fragment_blog_phone);
		blogPhone.setText("--- OnSight - Indonesia ---");

		btnSubmit = (Button) getView().findViewById(R.id.button);


		btnSubmit.setEnabled(false);
		btnSubmit.setBackgroundColor(getResources().getColor(R.color.grey));
		btnSubmit.setTextColor(getResources().getColor(R.color.white));

		judul = (EditText) getView().findViewById(R.id.judul);
		category = (EditText) getView().findViewById(R.id.cat);
		description = (EditText) getView().findViewById(R.id.desc);
		name = (EditText) getView().findViewById(R.id.fra_single_recipe_prep_time);

		btnSelFile = (Button) getView().findViewById(R.id.btnFile);
		txt_uri = (TextView) getView().findViewById(R.id.text_uri);
		real_uri = (TextView) getView().findViewById(R.id.real_uri);

		btnSelFile1 = (Button) getView().findViewById(R.id.btnFile1);
		txt_uri1 = (TextView) getView().findViewById(R.id.text_uri1);
		real_uri1 = (TextView) getView().findViewById(R.id.real_uri1);

		btnSelFile2 = (Button) getView().findViewById(R.id.btnFile2);
		txt_uri2 = (TextView) getView().findViewById(R.id.text_uri2);
		real_uri2 = (TextView) getView().findViewById(R.id.real_uri2);

		txtjdudul = (TextView) getView().findViewById(R.id.textView3); // nama tokoh/wisata/dll
		txtdeskripsi = (TextView) getView().findViewById(R.id.textView5); // deskripsi
		txtnama = (TextView) getView().findViewById(R.id.textView6); // nama kamu

		dividerjudul = getView().findViewById(R.id.limit1);
		dividerdeskripsi = getView().findViewById(R.id.limit3);
		dividernama = getView().findViewById(R.id.limit4);

		imgbtn1 = (ImageButton) getView().findViewById(R.id.imageButton);
		imgbtn2 = (ImageButton) getView().findViewById(R.id.imageButton2);



		judul.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				enableSubmitIfReady();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});

		real_uri.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				enableSubmitIfReady();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});


		category.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				enableSubmitIfReady();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});


		description.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				enableSubmitIfReady();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});



		name.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable arg0) {
				enableSubmitIfReady();
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
		});

		imgbtn1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				btnSelFile1.setVisibility(View.VISIBLE);
				real_uri1.setVisibility(View.VISIBLE);
				imgbtn1.setVisibility(View.INVISIBLE);
				imgbtn2.setVisibility(View.VISIBLE);

			}
		});


		imgbtn2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				btnSelFile2.setVisibility(View.VISIBLE);
				real_uri2.setVisibility(View.VISIBLE);
				imgbtn2.setVisibility(View.INVISIBLE);
			}
		});


		btnSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if (isNetworkAvailable()) {
					String email = "hunginda@gmail.com";
					String message1 = judul.getText().toString();
					String message2 = category.getText().toString();
					String message3 = description.getText().toString();
					String message4 = name.getText().toString();
					sendMail(email, message1, message2, message3, message4);
				} else {
					Toast.makeText(getActivity(), "Please connect to any network available first.", Toast.LENGTH_SHORT).show();
				}
			}
		});

		btnSelFile.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				gambar = 1;
				selectImage();
			}
		});

		btnSelFile1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				gambar = 2;
				selectImage();
			}
		});

		btnSelFile2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				gambar = 3;
				selectImage();
			}
		});


	}

	private void selectImage() {
		final CharSequence[] items = { "Take Photo", "Choose from Gallery", "Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Add Photo!");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				if (items[item].equals("Take Photo")) {


					// create Intent to take a picture and return control to the calling application
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

					fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
					intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

					// start the image capture Intent
					startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);


				} else if (items[item].equals("Choose from Gallery")) {
					Intent intent = new Intent(
							Intent.ACTION_PICK,
							android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
					startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}


	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;

	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type){
		return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES), "MyCameraApp");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (! mediaStorageDir.exists()){
			if (! mediaStorageDir.mkdirs()){
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE){
			mediaFile = new File(mediaStorageDir.getPath() + File.separator +
					"IMG_"+ timeStamp + ".jpg");
		} else if(type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator +
					"VID_"+ timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}


	private void setviewenable(){
		judul.setVisibility(View.VISIBLE);
		description.setVisibility(View.VISIBLE);
		name.setVisibility(View.VISIBLE);

		txtjdudul.setVisibility(View.VISIBLE);
		txtdeskripsi.setVisibility(View.VISIBLE);
		txtnama.setVisibility(View.VISIBLE);

		dividerjudul.setVisibility(View.VISIBLE);
		dividerdeskripsi.setVisibility(View.VISIBLE);
		dividernama.setVisibility(View.VISIBLE);

		real_uri.setVisibility(View.VISIBLE);
		btnSelFile.setVisibility(View.VISIBLE);

		btnSubmit.setVisibility(View.VISIBLE);

		imgbtn1.setVisibility(View.VISIBLE);
	}

	private void setviewdisable(){

		judul.setVisibility(View.GONE);
		description.setVisibility(View.GONE);
		name.setVisibility(View.GONE);

		txtjdudul.setVisibility(View.INVISIBLE);
		txtdeskripsi.setVisibility(View.GONE);
		txtnama.setVisibility(View.GONE);

		dividerjudul.setVisibility(View.GONE);
		dividerdeskripsi.setVisibility(View.INVISIBLE);
		dividernama.setVisibility(View.GONE);

		real_uri.setVisibility(View.INVISIBLE);
		btnSelFile.setVisibility(View.INVISIBLE);
		real_uri1.setVisibility(View.INVISIBLE);
		btnSelFile1.setVisibility(View.INVISIBLE);
		real_uri2.setVisibility(View.INVISIBLE);
		btnSelFile2.setVisibility(View.INVISIBLE);


		imgbtn1.setVisibility(View.INVISIBLE);
		imgbtn2.setVisibility(View.INVISIBLE);

		btnSubmit.setVisibility(View.GONE);
	}

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
			if (resultCode == getActivity().RESULT_OK) {
				// Image captured and saved to fileUri specified in the Intent
				Toast.makeText(getActivity(), "Image saved to " + fileUri, Toast.LENGTH_LONG).show();

				String ext = fileUri.toString().substring(7);

				if ( gambar == 1 ){
					real_uri.setText(ext);
					convertedPath = ext;
				} else if ( gambar == 2){
					real_uri1.setText(ext);
					convertedPath1 = ext;
				} else {
					real_uri2.setText(ext);
					convertedPath2 = ext;
				}



			} else if (resultCode == getActivity().RESULT_CANCELED) {
				// User cancelled the image capture
			} else {
				// Image capture failed, advise user
			}
		} else if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
			if(resultCode == getActivity().RESULT_OK){

				// image.setImageBitmap(null);

				//Uri return from external activity

				orgUri = data.getData();


				txt_uri.setText("Returned Uri: " + orgUri.toString() + "\n");

				if ( gambar == 1 ){
					convertedPath = getRealPathFromURI(orgUri);
					real_uri.setText(convertedPath);
				} else if ( gambar == 2){
					convertedPath1 = getRealPathFromURI(orgUri);
					real_uri1.setText(convertedPath1);
				} else {
					convertedPath2 = getRealPathFromURI(orgUri);
					real_uri2.setText(convertedPath2);
				}

			}
		}

	}

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };

		//This method was deprecated in API level 11
		//Cursor cursor = managedQuery(contentUri, proj, null, null, null);

		CursorLoader cursorLoader = new CursorLoader(
				getActivity(),
				contentUri, proj, null, null, null);
		Cursor cursor = cursorLoader.loadInBackground();

		int column_index =
				cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}




	private void sendMail(String email, String msg1, String msg2, String msg3, String msg4) {
		Session session = createSessionObject();

		try {
			//Message message = createMessage(email, subject, messageBody, session);

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress("weeework.studio@gmail.com", "spongebobsquarepants"));
			message.addRecipient(Message.RecipientType.TO,new InternetAddress(email, email));
			message.setSubject(subject);

			//3) create MimeBodyPart object and set your message content
			BodyPart messageBodyPart1 = new MimeBodyPart();
			messageBodyPart1.setText("Judul = " + msg1);

			BodyPart messageBodyPart3 = new MimeBodyPart();
			messageBodyPart3.setText("Lokasi = " + msg2);

			BodyPart messageBodyPart4 = new MimeBodyPart();
			messageBodyPart4.setText("Description = " + msg3);

			BodyPart messageBodyPart5 = new MimeBodyPart();
			messageBodyPart5.setText("Name = " + msg4);

			if (!convertedPath.equals("") && convertedPath1.equals("") && convertedPath2.equals("")){
				filename = new String[1];
				filename[0] = convertedPath;

			} else if (!convertedPath.equals("") && !convertedPath1.equals("") && convertedPath2.equals("")){
				filename = new String[2];
				filename[0] = convertedPath;
				filename[1] = convertedPath1;

			} else if (!convertedPath.equals("") && !convertedPath1.equals("") && !convertedPath2.equals("")) {

				filename = new String[3];
				filename[0] = convertedPath;
				filename[1] = convertedPath1;
				filename[2] = convertedPath2;
			}

			//5) create Multipart object and add MimeBodyPart objects to this object
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart1);

			for(int i=0 ; i<filename.length ; i++)
			{
				MimeBodyPart messageBodyPart2 = new MimeBodyPart();
				DataSource source = new FileDataSource(filename[i]);
				messageBodyPart2.setDataHandler(new DataHandler(source));
				messageBodyPart2.setFileName(filename[i]);
				multipart.addBodyPart(messageBodyPart2);
			}

			multipart.addBodyPart(messageBodyPart3);
			multipart.addBodyPart(messageBodyPart4);
			multipart.addBodyPart(messageBodyPart5);

			//6) set the multiplart object to the message object
			message.setContent(multipart);



			new SendMailTask().execute(message);
		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private Session createSessionObject() {
		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.port", "587");

		return Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
	}


	private class SendMailTask extends AsyncTask<Message, Void, Void> {
		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = ProgressDialog.show(getActivity(), "Mohon tunggu", "Sedang memproses....", true, false);
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			progressDialog.dismiss();

			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Sukses!");
			builder.setMessage("Terimakasih, kami akan me-review isi content anda dan segera mempublishkannya..")
					.setCancelable(false)
					.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							//do things
						}
					});
			AlertDialog alert = builder.create();
			alert.show();

		}

		@Override
		protected Void doInBackground(Message... messages) {
			try {
				Transport.send(messages[0]);
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			return false;
		} else
			return true;
	}

	private void enableSubmitIfReady() {

		boolean isReady = judul.getText().toString().length()>0 &&
				description.getText().toString().length()>0 &&
				real_uri.getText().toString().length()>10 &&
				name.getText().toString().length()>0 &&
				category.getText().toString().length()>0;

		if (isReady){
			btnSubmit.setBackgroundColor(getResources().getColor(R.color.red));
			btnSubmit.setTextColor(getResources().getColor(R.color.white));
		} else{
			btnSubmit.setBackgroundColor(getResources().getColor(R.color.grey));
		}
		btnSubmit.setEnabled(isReady);

	}

}
