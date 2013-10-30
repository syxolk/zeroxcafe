package com.google.code.zeroxcafe;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity implements OnItemSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initSpinner((Spinner)findViewById(R.id.inputType));
		initSpinner((Spinner)findViewById(R.id.outputType));
		
		EditText inputEdit=(EditText)findViewById(R.id.inputText);
		inputEdit.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			public void afterTextChanged(Editable s) {
				updateValue();
			}
		});
	}
	
	private void initSpinner(Spinner spinner) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.types, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		spinner.setOnItemSelectedListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	private void updateValue() {
		Spinner inputSpinner=(Spinner)findViewById(R.id.inputType);
		Spinner outputSpinner=(Spinner)findViewById(R.id.outputType);
		EditText inputEdit=(EditText)findViewById(R.id.inputText);
		TextView outputEdit=(TextView)findViewById(R.id.outputText);
		
		if(inputSpinner.getSelectedItemPosition()==Spinner.INVALID_POSITION
			|| outputSpinner.getSelectedItemPosition()==Spinner.INVALID_POSITION
			|| inputEdit.length()==0) {
			
			outputEdit.setText("-");
			
		} else {
			int baseFrom=getBaseByPos(inputSpinner.getSelectedItemPosition());
			int baseTo=getBaseByPos(outputSpinner.getSelectedItemPosition());
			String input=inputEdit.getText().toString().toLowerCase(Locale.US);
			
			if(MathUtils.isCompatible(input, baseFrom)) {
				String output=MathUtils.convert(input, baseFrom, baseTo);
				outputEdit.setText(output);
			} else {
				outputEdit.setText(getResources().getString(R.string.output_error_incompatible));
			}
		}
	}
	
	private int getBaseByPos(int pos) {
		switch(pos) {
		case 0: return 2;
		case 1: return 8;
		case 2: return 10;
		case 3: return 16;
		default: return 10;
		}
	}
	
	public void onConvert(View view) {
	    updateValue();
	}
	
	public void onItemSelected(AdapterView<?> parent, View view,  int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
		updateValue();
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
}