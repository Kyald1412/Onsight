package com.kyald.onsight.share;

import android.content.Context;
import android.content.Intent;

/**
 * This class is responsible for the Mail functionality of the application.
 * 
 * @author |dmb TEAM|
 * 
 */
public class MailManager {
	
	/**
	 * Opens new <code>Intent</code> to send email with Post data.
	 * 
	 * @param c
	 *            application context
	 * @param subject
	 *            subject of the mail
	 * @param content
	 *            content of the mail
	 */
	public static void openMailIntent(Context c, String subject, String content) {
		Intent mailIntent = new Intent(Intent.ACTION_SEND);

		mailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		mailIntent.putExtra(Intent.EXTRA_TEXT, content);
		mailIntent.setType("plain/text");

		c.startActivity(Intent.createChooser(mailIntent,
				"Choose your mail client"));

	}
}
