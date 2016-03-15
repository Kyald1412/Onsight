package com.kyald.onsight.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kyald.onsight.R;
import com.kyald.onsight.settings.AppConstants;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
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


	Button btnSelFile,btnSubmit;
	TextView judul, category, description, name, txt_uri, real_uri;
	//String textMessage, textMessage1, textMessage2, textMessage3, textMessage4, str;
	//ImageView image;

	Uri orgUri, uriFromPath;
	String convertedPath;
	String subject = "Test Onsight";

	private static final String username = "weeework.studio@gmail.com";
	private static final String password = "spongebobsquarepants";




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
		blogUrl.setText("Kirimkan ulasan tempat wisata kesukaanmu atau makanan favoritmu disini");

		TextView blogMail = (TextView) getView().findViewById(
				R.id.about_fragment_blog_mail);
		blogMail.setText("Beritahu seluruh indonesia bahkan seluruh dunia tentang ulasanmu");

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
		name = (EditText) getView().findViewById(R.id.nama);
		//msg4 = (EditText) getView().findViewById(R.id.et_text4);

		btnSelFile = (Button) getView().findViewById(R.id.btnFile);
		txt_uri = (TextView) getView().findViewById(R.id.text_uri);
		real_uri = (TextView) getView().findViewById(R.id.real_uri);


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
				}else{
					Toast.makeText(getActivity(), "Please connect to any network available first.", Toast.LENGTH_SHORT).show();
				}
			}
		});

		btnSelFile.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_PICK,
						MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, 0);
			}});


	}



	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(resultCode == getActivity().RESULT_OK){

			// image.setImageBitmap(null);

			//Uri return from external activity
			orgUri = data.getData();
			txt_uri.setText("Returned Uri: " + orgUri.toString() + "\n");

			//path converted from Uri
			convertedPath = getRealPathFromURI(orgUri);
			real_uri.setText(convertedPath);

			//Uri convert back again from path
			//uriFromPath = Uri.fromFile(new File(convertedPath));
			//text3.setText("Back Uri: " + uriFromPath.toString() + "\n");
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
			messageBodyPart3.setText("Kategori = "+msg2);

			BodyPart messageBodyPart4 = new MimeBodyPart();
			messageBodyPart4.setText("Description = "+msg3);

			BodyPart messageBodyPart5 = new MimeBodyPart();
			messageBodyPart5.setText("Name = "+msg4);

			//4) create new MimeBodyPart object and set DataHandler object to this object
			MimeBodyPart messageBodyPart2 = new MimeBodyPart();

			String filename = convertedPath;//change accordingly
			DataSource source = new FileDataSource(filename);
			messageBodyPart2.setDataHandler(new DataHandler(source));
			messageBodyPart2.setFileName(filename);


			//5) create Multipart object and add MimeBodyPart objects to this object
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart1);
			multipart.addBodyPart(messageBodyPart2);
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

 /*   private Message createMessage(String email, String subject, String messageBody, Session session) throws MessagingException, UnsupportedEncodingException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("weeework.studio@gmail.com", "spongebobsquarepants"));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email, email));
        message.setSubject(subject);
        message.setText(messageBody);
        return message;
    }
*/

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
			Toast.makeText(getActivity(), "Terimakasih, kami akan me-review isi content anda dan segera mempublishkannya..", Toast.LENGTH_LONG).show();
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
				//msg4.getText().toString().length()>0;

		if (isReady){
			btnSubmit.setBackgroundColor(getResources().getColor(R.color.red));
			btnSubmit.setTextColor(getResources().getColor(R.color.white));
		} else{
			btnSubmit.setBackgroundColor(getResources().getColor(R.color.grey));
		}
		btnSubmit.setEnabled(isReady);

	}

}
