package com.googlecode.zeroxcafe;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Main-activity of the app.
 * 
 * @author Hans
 */
public class MainActivity extends Activity {

	CustomKeyboard mCustomKeyboard;

	private TextView outputView;
	private Spinner inputSpinner;
	private Spinner outputSpinner;
	private EditText inputEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		outputView = (TextView) findViewById(R.id.outputText);
		inputSpinner = (Spinner) findViewById(R.id.inputType);
		outputSpinner = (Spinner) findViewById(R.id.outputType);
		inputEdit = (EditText) findViewById(R.id.inputText);

		initSpinner((Spinner) findViewById(R.id.inputType));
		initSpinner((Spinner) findViewById(R.id.outputType));

		EditText inputEdit = (EditText) findViewById(R.id.inputText);
		inputEdit.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			public void afterTextChanged(Editable s) {
				updateValue();
			}
		});

		mCustomKeyboard = new CustomKeyboard(this, R.id.keyboardview,
				R.xml.hexkbd);
		mCustomKeyboard.registerEditText(R.id.inputText);
	}

	private void initSpinner(Spinner spinner) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.bases, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onNothingSelected(AdapterView<?> parent) {
			}

			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {

				updateValue();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * This method is being called if the input number or other parameters
	 * change.
	 */
	private void updateValue() {
		new ComputeTask().execute();
	}

	private class ComputeTask extends AsyncTask<Void, Void, String> {

		protected String doInBackground(Void... params) {
			if (inputSpinner.getSelectedItemPosition() == Spinner.INVALID_POSITION
					|| outputSpinner.getSelectedItemPosition() == Spinner.INVALID_POSITION
					|| inputEdit.length() == 0) {

				return getString(R.string.output_empty);

			} else {
				char decimalChar = getString(R.string.keyboard_decimalpoint)
						.charAt(0);

				int baseFrom = getBaseByPos(inputSpinner
						.getSelectedItemPosition());
				int baseTo = getBaseByPos(outputSpinner
						.getSelectedItemPosition());
				String input = NumberFormatUtils.deformat(inputEdit.getText()
						.toString(), decimalChar);

				boolean compatible = MathUtils.isCompatible(input, baseFrom);
				boolean maximum1DP = MathUtils.hasMaximumOneDecimalPoint(input);

				if (!compatible)
					return getString(R.string.output_error_incompatible);
				if (!maximum1DP)
					return getString(R.string.output_error_too_much_decimal_points);

				String output = MathUtils.convert(input, baseFrom, baseTo);
				return NumberFormatUtils.format(output, decimalChar);
			}
		}

		protected void onPostExecute(String result) {
			outputView.setText(result);
		}
	}

	private int getBaseByPos(int pos) {
		return pos + 2;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_copy_result:
			copyResultToClipboard();
			return true;
		case R.id.action_help:
			startActivity(new Intent(this, HelpActivity.class));
			return true;
		case R.id.action_wiki:
			startActivity(new Intent(this, WikiActivity.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/**
	 * Copies the current result of the calculation to the clipboard.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void copyResultToClipboard() {
		CharSequence text = outputView.getText();

		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				ClipData clip = ClipData.newPlainText(
						getString(R.string.clip_data_label), text);
				clipboard.setPrimaryClip(clip);
			} else {
				copyResultToClipboardOldDevices(text);
			}

			Toast toast = Toast.makeText(this, R.string.copy_result_success,
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		} catch (Exception e) {
			Toast toast = Toast.makeText(this, R.string.copy_result_error,
					Toast.LENGTH_LONG);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		}
	}

	/**
	 * Older clipboard API call. <a href=
	 * "http://stackoverflow.com/questions/6624763/android-copy-to-clipboard-selected-text-from-a-textview"
	 * >description for older/newer device API</a>
	 * 
	 * @param text
	 *            text to copy
	 */
	@SuppressWarnings("deprecation")
	private void copyResultToClipboardOldDevices(CharSequence text) {
		android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
		clipboard.setText(text);
	}

	@Override
	public void onBackPressed() {
		if (mCustomKeyboard.isCustomKeyboardVisible())
			mCustomKeyboard.hideCustomKeyboard();
		else
			this.finish();
	}
}