package com.google.code.zeroxcafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Main-activity of the app.
 * 
 * @author Hans
 */
public class MainActivity extends Activity {

	CustomKeyboard mCustomKeyboard;
	private TextView outputView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		outputView=(TextView)findViewById(R.id.outputText);
		
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

	private void setOutput(String text) {
		outputView.setText(text);
	}
	
	private void setOutput(int resId) {
		setOutput(getString(resId));
	}
	
	/**
	 * This method is being called if the input number or other parameters
	 * change.
	 */
	private void updateValue() {
		Spinner inputSpinner = (Spinner) findViewById(R.id.inputType);
		Spinner outputSpinner = (Spinner) findViewById(R.id.outputType);
		EditText inputEdit = (EditText) findViewById(R.id.inputText);

		if (inputSpinner.getSelectedItemPosition() == Spinner.INVALID_POSITION
				|| outputSpinner.getSelectedItemPosition() == Spinner.INVALID_POSITION
				|| inputEdit.length() == 0) {

			setOutput(R.string.output_empty);

		} else {
			int baseFrom = getBaseByPos(inputSpinner.getSelectedItemPosition());
			int baseTo = getBaseByPos(outputSpinner.getSelectedItemPosition());
			String input = inputEdit.getText().toString();

			boolean compatible = MathUtils.isCompatible(input, baseFrom);
			boolean maximum1DP = MathUtils.hasMaximumOneDecimalPoint(input);

			if (compatible && maximum1DP) {
				String output = MathUtils.convert(input, baseFrom, baseTo);
				setOutput(MathUtils.format(output));
			} else if (!compatible) {
				setOutput(R.string.output_error_incompatible);
			} else if (!maximum1DP) {
				setOutput(R.string.output_error_too_much_decimal_points);
			}
		}
	}

	private int getBaseByPos(int pos) {
		switch (pos) {
		case 0:
			return 2;
		case 1:
			return 8;
		case 2:
			return 10;
		case 3:
			return 16;
		default:
			return 10;
		}
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
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

	@Override
	public void onBackPressed() {
		if (mCustomKeyboard.isCustomKeyboardVisible())
			mCustomKeyboard.hideCustomKeyboard();
		else
			this.finish();
	}
}