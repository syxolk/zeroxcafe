package com.googlecode.zeroxcafe;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;

public class StepsActivity extends Activity {

	private static final String PACKAGE = "com.googlecode.zeroxcafe";

	public static final String INTENT_NUMBER = PACKAGE + ".number";
	public static final String INTENT_FROM_BASE = PACKAGE + ".from_base";
	public static final String INTENT_TO_BASE = PACKAGE + ".to_base";

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_steps);
		// Show the Up button in the action bar.
		setupActionBar();

		webView = (WebView) findViewById(R.id.content);

		new GeneratorTask().execute();
	}

	private class GeneratorTask extends AsyncTask<Void, Void, String> {
		protected String doInBackground(Void... params) {
			if (getIntent() == null || getIntent().getExtras() == null) {
				Log.w(StepsActivity.class.getName(), "Intent extras are null");
				return "";
			}

			Bundle extras = getIntent().getExtras();

			String number = extras.getString(INTENT_NUMBER);
			int baseFrom = extras.getInt(INTENT_FROM_BASE);
			int baseTo = extras.getInt(INTENT_TO_BASE);

			StepsHtmlGenerator generator = new StepsHtmlGenerator(
					StepsActivity.this, number, baseFrom, baseTo);
			return generator.generate();
		}

		protected void onPostExecute(String result) {
			webView.loadDataWithBaseURL(null, result, "text/html",
					RawUtils.RESOURCE_ENCODING, null);
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

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
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

}
