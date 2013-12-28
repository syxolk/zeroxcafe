package com.googlecode.zeroxcafe;

import java.math.BigInteger;
import java.util.Locale;

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
			StringBuilder content = new StringBuilder();

			content.append("<!doctype html><html><head><style type=\"text/css\">");

			try {
				content.append(RawUtils.loadStringRes(StepsActivity.this,
						R.raw.style));

				Log.i(StepsActivity.class.getName(), "style loaded");
			} catch (Exception e) {
				// do nothing
				Log.w(StepsActivity.class.getName(), e.toString());
			}

			content.append("</style></head><body>");

			if (getIntent() != null && getIntent().getExtras() != null) {
				Bundle extras = getIntent().getExtras();

				String number = extras.getString(INTENT_NUMBER);
				int baseFrom = extras.getInt(INTENT_FROM_BASE);
				int baseTo = extras.getInt(INTENT_TO_BASE);

				// remove decimal places and decimal point
				int decimalPoint = number.indexOf(MathUtils.DECIMAL_POINT);

				if (decimalPoint != -1) {
					if (decimalPoint == 0) {
						number = "0";
					} else {
						number = number.substring(0, decimalPoint);
					}
				}

				boolean isCompat = MathUtils.isCompatible(number, baseFrom);

				if (isCompat) {

					content.append("<h1>")
							.append(getString(R.string.steps_title))
							.append("</h1>");

					content.append("<p>")
							.append(getString(R.string.steps_what, number,
									baseFrom, baseTo)).append("</p>");

					// STEP 1

					content.append("<h2>")
							.append(getString(R.string.steps_step1))
							.append("</h2>");

					content.append("(" + number + ")<sub>" + baseFrom
							+ "</sub>" + " = ");

					BigInteger sum = BigInteger.ZERO;

					for (int i = 0; i < number.length(); i++) {

						int digit = Character.digit(number.charAt(i), baseFrom);
						int exp = number.length() - 1 - i;

						sum = sum.add(BigInteger.valueOf(baseFrom).pow(exp)
								.multiply(BigInteger.valueOf(digit)));

						if (i != 0)
							content.append(" + ");

						content.append(digit + "&middot;" + baseFrom + "<sup>"
								+ exp + "</sup>");
					}

					content.append(" = " + sum.toString());

					// STEP 2

					content.append("<h2>")
							.append(getString(R.string.steps_step2))
							.append("</h2>");

					content.append("<table><tr><th>")
							.append(getString(R.string.steps_step2_div))
							.append("</th><th>")
							.append(getString(R.string.steps_step2_rem))
							.append("</th><th>")
							.append(getString(R.string.steps_step2_digit))
							.append("</th></tr>");

					StringBuilder resNumber = new StringBuilder();

					while (sum.compareTo(BigInteger.ZERO) > 0) {
						BigInteger[] res = sum.divideAndRemainder(BigInteger
								.valueOf(baseTo));
						char rem = res[1].toString(baseTo)
								.toUpperCase(Locale.US).charAt(0);

						content.append("<tr>");
						content.append("<td>").append(sum).append(" &divide; ")
								.append(baseTo).append(" = ")
								.append(res[0].toString()).append("</td>");
						content.append("<td>R ").append(res[1].toString())
								.append("</th>");
						content.append("<td>").append(rem).append("</td>");
						content.append("</tr>");

						sum = res[0];
						resNumber.insert(0, rem);
					}

					content.append("</table>");

					content.append("<h2>")
							.append(getString(R.string.steps_result))
							.append("</h2>");

					content.append("(").append(number).append(")<sub>")
							.append(baseFrom).append("</sub> = (")
							.append(resNumber.toString()).append(")<sub>")
							.append(baseTo).append("</sub>");

				} else {
					content.append(getString(R.string.output_error_incompatible));
				}
			}

			content.append("</body></html>");

			return content.toString();
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
