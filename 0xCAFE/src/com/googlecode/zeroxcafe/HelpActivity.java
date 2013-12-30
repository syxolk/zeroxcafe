package com.googlecode.zeroxcafe;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Simple activity with help and about text
 * 
 * @author Hans
 */
public class HelpActivity extends Activity {

	private static final String MARKET_DETAILS = "market://details?id=";
	private static final String PLAY_STORE_DETAILS = "http://play.google.com/store/apps/details?id=";

	private static final String MAILTO_PROTOCOL = "mailto";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_help);
		// Show the Up button in the action bar.
		setupActionBar();

		TextView versionView = (TextView) findViewById(R.id.help_version);

		try {
			PackageInfo pInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			versionView.setText(getString(R.string.help_version,
					pInfo.versionName, pInfo.versionCode));
		} catch (Exception e) {
			versionView.setVisibility(TextView.GONE);
		}
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_rate:
			rateApp();
			return true;
		case R.id.action_mail:
			writeMail();
			return true;
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Starts the Google play store to rate the app.
	 */
	private void rateApp() {
		Uri uri = Uri.parse(MARKET_DETAILS + getPackageName());
		Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
		try {
			startActivity(goToMarket);
		} catch (ActivityNotFoundException e) {
			startActivity(new Intent(Intent.ACTION_VIEW,
					Uri.parse(PLAY_STORE_DETAILS + getPackageName())));
		}
	}

	private void writeMail() {
		Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
				MAILTO_PROTOCOL, getString(R.string.mail_to), null));
		emailIntent.putExtra(Intent.EXTRA_SUBJECT,
				getString(R.string.mail_subject));
		emailIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.mail_text));

		try {
			startActivity(Intent.createChooser(emailIntent,
					getString(R.string.mail_chooser)));
		} catch (Exception ex) {
			Toast.makeText(this, getString(R.string.mail_error),
					Toast.LENGTH_SHORT).show();
		}
	}
}