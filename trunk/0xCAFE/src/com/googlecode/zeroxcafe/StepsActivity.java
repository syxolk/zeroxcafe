package com.googlecode.zeroxcafe;

import java.math.BigInteger;
import java.util.Locale;

import org.apache.commons.math3.fraction.BigFraction;

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

				// get position of decimal point
				int decimalPoint = number.indexOf(MathUtils.DECIMAL_POINT);

				boolean isCompat = MathUtils.isCompatible(number, baseFrom);
				boolean hasMax1DP = MathUtils.hasMaximumOneDecimalPoint(number);

				if (isCompat && hasMax1DP) {

					char localizedDecimalPoint = getString(
							R.string.keyboard_decimalpoint).charAt(0);

					content.append("<h1>")
							.append(getString(R.string.steps_title))
							.append("</h1>");

					content.append("<p>")
							.append(getString(R.string.steps_what,
									NumberFormatUtils.format(number,
											localizedDecimalPoint), baseFrom,
									baseTo)).append("</p>");

					// STEP 1

					content.append("<h2>")
							.append(getString(R.string.steps_step1))
							.append("</h2>");

					content.append("("
							+ NumberFormatUtils.format(number,
									localizedDecimalPoint) + ")<sub>"
							+ baseFrom + "</sub>" + " = ");

					String numberBeforeDec = "", numberAfterDec = "";

					if (decimalPoint == -1) {
						numberBeforeDec = number;
					} else if (decimalPoint == 0) {
						numberAfterDec = number.substring(1);
					} else if (decimalPoint == number.length() - 1) {
						numberBeforeDec = number.substring(0,
								number.length() - 1);
					} else {
						numberBeforeDec = number.substring(0, decimalPoint);
						numberAfterDec = number.substring(decimalPoint + 1);
					}

					BigFraction sum = BigFraction.ZERO;
					boolean mustWritePlus = false;

					if (numberBeforeDec.length() > 0) {
						for (int i = 0; i < numberBeforeDec.length(); i++) {

							int digit = Character.digit(
									numberBeforeDec.charAt(i), baseFrom);
							int exp = numberBeforeDec.length() - 1 - i;

							sum = sum.add(BigInteger.valueOf(baseFrom).pow(exp)
									.multiply(BigInteger.valueOf(digit)));

							if (mustWritePlus)
								content.append(" + ");

							content.append(digit + "&middot;" + baseFrom
									+ "<sup>" + exp + "</sup>");

							mustWritePlus = true;
						}
					}

					if (numberAfterDec.length() > 0) {
						for (int i = 0; i < numberAfterDec.length(); i++) {

							int digit = Character.digit(
									numberAfterDec.charAt(i), baseFrom);
							int exp = i + 1;

							sum = sum.add(new BigFraction(BigInteger
									.valueOf(digit), BigInteger.valueOf(
									baseFrom).pow(exp)));

							if (mustWritePlus)
								content.append(" + ");

							content.append(digit + " &middot; " + baseFrom
									+ "<sup>-" + exp + "</sup>");

							mustWritePlus = true;
						}
					}

					content.append(" = " + sum);

					BigInteger sumInteger = BigFractionUtils.integerPart(sum);
					BigFraction sumDecPart = sum.subtract(sumInteger);

					if (sumInteger.compareTo(BigInteger.ZERO) > 0
							&& sumDecPart.compareTo(BigFraction.ZERO) > 0) {
						
						content.append(" = ").append(sumInteger).append(" + ")
								.append(sumDecPart);
					}

					// STEP 2

					content.append("<h2>")
							.append(getString(R.string.steps_step2))
							.append("</h2>");

					StringBuilder resNumber = new StringBuilder();

					if (sumInteger.compareTo(BigInteger.ZERO) > 0) {

						content.append("<h3>")
								.append(getString(R.string.steps_step2_method_mod))
								.append("</h3>");

						content.append("<table><tr><th>")
								.append(getString(R.string.steps_step2_div))
								.append("</th><th>")
								.append(getString(R.string.steps_step2_rem))
								.append("</th><th>")
								.append(getString(R.string.steps_step2_digit))
								.append("</th></tr>");

						// Ganzzahl-Teil

						while (sumInteger.compareTo(BigInteger.ZERO) > 0) {
							BigInteger[] res = sumInteger
									.divideAndRemainder(BigInteger
											.valueOf(baseTo));
							char rem = res[1].toString(baseTo)
									.toUpperCase(Locale.US).charAt(0);

							content.append("<tr>");
							content.append("<td>").append(sumInteger)
									.append(" &divide; ").append(baseTo)
									.append(" = ").append(res[0].toString())
									.append("</td>");
							content.append("<td>R ").append(res[1].toString())
									.append("</th>");
							content.append("<td>").append(rem).append("</td>");
							content.append("</tr>");

							sumInteger = res[0];
							resNumber.insert(0, rem);
						}

						content.append("</table>");
					}

					if (sumDecPart.compareTo(BigFraction.ZERO) > 0) {

						content.append("<h3>")
								.append(getString(R.string.steps_step2_method_mul))
								.append("</h3>");

						resNumber.append(MathUtils.DECIMAL_POINT);

						content.append("<table><tr><th>")
								.append(getString(R.string.steps_step2_mul))
								.append("</th><th>")
								.append(getString(R.string.steps_step2_int))
								.append("</th><th>")
								.append(getString(R.string.steps_step2_digit))
								.append("</th></tr>");

						int maximumSteps = 10, stepCounter = 0;

						while (sumDecPart.compareTo(BigFraction.ZERO) > 0
								&& stepCounter < maximumSteps) {

							content.append("<tr><td> " + sumDecPart
									+ " &middot; " + baseTo + " = ");

							sumDecPart = sumDecPart.multiply(BigInteger
									.valueOf(baseTo));

							content.append(sumDecPart + "</td><td>");

							BigInteger intPart = BigFractionUtils
									.integerPart(sumDecPart);

							content.append(intPart).append("</td><td>");

							char digit = intPart.toString(baseTo)
									.toUpperCase(Locale.US).charAt(0);

							content.append(digit).append("</td></tr>");

							sumDecPart = sumDecPart.subtract(intPart);

							resNumber.append(digit);
							stepCounter++;
						}

						content.append("</table>");
					}

					content.append("<h2>")
							.append(getString(R.string.steps_result))
							.append("</h2>");

					content.append("(")
							.append(NumberFormatUtils.format(number,
									localizedDecimalPoint))
							.append(")<sub>")
							.append(baseFrom)
							.append("</sub> = (")
							.append(NumberFormatUtils.format(
									resNumber.toString(), localizedDecimalPoint))
							.append(")<sub>").append(baseTo).append("</sub>");

				} else if (!isCompat) {
					content.append(getString(R.string.output_error_incompatible));
				} else if (!hasMax1DP) {
					content.append(getString(R.string.output_error_too_much_decimal_points));
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
