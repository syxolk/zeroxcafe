package com.google.code.zeroxcafe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.webkit.WebView;

public class WikiActivity extends Activity {

	private static final String RAW_HTML_RESOURCE_ENCODING = "utf-8";

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wiki);
		// Show the Up button in the action bar.
		setupActionBar();

		this.webView = (WebView) findViewById(R.id.content);
		
		new HtmlLoaderTask().execute();
	}

	private class HtmlLoaderTask extends AsyncTask<Void, Void, String> {
		protected String doInBackground(Void... params) {
			StringBuilder text = new StringBuilder();

			try {
				BufferedReader buffreader = new BufferedReader(
						new InputStreamReader(getResources().openRawResource(
								R.raw.wiki), RAW_HTML_RESOURCE_ENCODING));

				String line;

				while ((line = buffreader.readLine()) != null) {
					text.append(line);
					text.append('\n');
				}
			} catch (IOException e) {
				text.append(e.toString());
			}

			return text.toString();

		}

		protected void onPostExecute(String result) {
			webView.loadDataWithBaseURL(null, result, "text/html",
					RAW_HTML_RESOURCE_ENCODING, null);
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
