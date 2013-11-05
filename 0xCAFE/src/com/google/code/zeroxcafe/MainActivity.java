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
 * Haupt-Activity der App.
 * 
 * @author Hans
 */
public class MainActivity extends Activity {

	 CustomKeyboard mCustomKeyboard;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
		
		 mCustomKeyboard= new CustomKeyboard(this, R.id.keyboardview, R.xml.hexkbd );
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
	 * Diese Methode muss aufgerufen werden, wenn sich die Eingabe-Zahl oder die
	 * Basen geändert haben.
	 */
	private void updateValue() {
		Spinner inputSpinner = (Spinner) findViewById(R.id.inputType);
		Spinner outputSpinner = (Spinner) findViewById(R.id.outputType);
		EditText inputEdit = (EditText) findViewById(R.id.inputText);
		TextView outputEdit = (TextView) findViewById(R.id.outputText);

		if (inputSpinner.getSelectedItemPosition() == Spinner.INVALID_POSITION
				|| outputSpinner.getSelectedItemPosition() == Spinner.INVALID_POSITION
				|| inputEdit.length() == 0) {

			outputEdit.setText("-");

		} else {
			int baseFrom = getBaseByPos(inputSpinner.getSelectedItemPosition());
			int baseTo = getBaseByPos(outputSpinner.getSelectedItemPosition());
			String input = inputEdit.getText().toString();

			if (MathUtils.isCompatible(input, baseFrom)) {
				String output = MathUtils.convert(input, baseFrom, baseTo);
				outputEdit.setText(output);
			} else {
				outputEdit.setText(R.string.output_error_incompatible);
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
	        startActivity(new Intent(this,HelpActivity.class));
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	@Override public void onBackPressed() {
	    if( mCustomKeyboard.isCustomKeyboardVisible() ) mCustomKeyboard.hideCustomKeyboard(); else this.finish();
	}	
}