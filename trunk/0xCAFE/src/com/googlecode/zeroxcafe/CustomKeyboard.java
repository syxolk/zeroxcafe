package com.googlecode.zeroxcafe;

import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.Editable;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import com.googlecode.zeroxcafe.R;

/**
 * Custom keyboard helper class. The keybboard ist designed for typing numbers
 * and hex-characters. The keyboard was inspired by a <a
 * href="http://www.fampennings.nl/maarten/android/09keyboard/index.htm">custom
 * keyboard tutorial</a>.
 * 
 * @author Hans
 */
public class CustomKeyboard {
	public final static int CodeDelete = -5; // Keyboard.KEYCODE_DELETE
	public final static int CodeCancel = -3; // Keyboard.KEYCODE_CANCEL
	public final static int CodeDecimalPoint = 46; //decimal point '.'
	public final static int CodeAllLeft = 55001;
	public final static int CodeLeft = 55002;
	public final static int CodeRight = 55003;
	public final static int CodeAllRight = 55004;
	public final static int CodeClear = 55006;
	public final static int CodeSwap = 55007;

	private Activity activity;
	private KeyboardView mKeyboardView;
	
	private CustomKeyboardListener listener;

	public CustomKeyboard(Activity activity, int keyboardViewId,
			int keyboardXmlId, CustomKeyboardListener listener) {
		this.activity = activity;
		this.listener = listener;
		
		// Create the Keyboard
		Keyboard mKeyboard = new Keyboard(activity, keyboardXmlId);

		// Lookup the KeyboardView
		mKeyboardView = (KeyboardView) activity.findViewById(keyboardViewId);
		// Attach the keyboard to the view
		mKeyboardView.setKeyboard(mKeyboard);
		// Do not show the preview balloons
		mKeyboardView.setPreviewEnabled(false);

		// Install the key handler
		mKeyboardView.setOnKeyboardActionListener(mOnKeyboardActionListener);

		// Hide the standard keyboard initially
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	public void hideCustomKeyboard() {
		mKeyboardView.setVisibility(View.GONE);
		mKeyboardView.setEnabled(false);
	}

	public void showCustomKeyboard(View v) {
		mKeyboardView.setVisibility(View.VISIBLE);
		mKeyboardView.setEnabled(true);
		if (v != null)
			((InputMethodManager) activity
					.getSystemService(Activity.INPUT_METHOD_SERVICE))
					.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	public boolean isCustomKeyboardVisible() {
		return mKeyboardView.getVisibility() == View.VISIBLE;
	}

	public void registerEditText(int resid) {
		EditText inputEdit = (EditText) activity.findViewById(resid);

		inputEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus)
					showCustomKeyboard(v);
				else
					hideCustomKeyboard();
			}
		});

		inputEdit.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				showCustomKeyboard(v);
			}
		});

		inputEdit.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				EditText edittext = (EditText) v;
				int inType = edittext.getInputType(); // Backup the input type
				edittext.setInputType(InputType.TYPE_NULL); // Disable standard
															// keyboard
				edittext.onTouchEvent(event); // Call native handler
				edittext.setInputType(inType); // Restore input type
				return true; // Consume touch event
			}
		});

		// Disable spell check (hex strings look like words to Android)
		inputEdit.setInputType(inputEdit.getInputType()
				| InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
	}

	private OnKeyboardActionListener mOnKeyboardActionListener = new OnKeyboardActionListener() {

		public void onPress(int primaryCode) {
		}

		public void onRelease(int primaryCode) {
		}

		@Override
		public void onKey(int primaryCode, int[] keyCodes) {
			// Get the EditText and its Editable
			View focusCurrent = activity.getWindow().getCurrentFocus();
			if (focusCurrent == null
					|| focusCurrent.getClass() != EditText.class)
				return;
			EditText edittext = (EditText) focusCurrent;
			Editable editable = edittext.getText();
			int start = edittext.getSelectionStart();
			// Handle key
			if (primaryCode == CodeCancel) {
				hideCustomKeyboard();
			} else if (primaryCode == CodeDelete) {
				if (editable != null && start > 0)
					editable.delete(start - 1, start);
			} else if (primaryCode == CodeClear) {
				if (editable != null)
					editable.clear();
			} else if (primaryCode == CodeLeft) {
				if (start > 0)
					edittext.setSelection(start - 1);
			} else if (primaryCode == CodeRight) {
				if (start < edittext.length())
					edittext.setSelection(start + 1);
			} else if (primaryCode == CodeAllLeft) {
				edittext.setSelection(0);
			} else if (primaryCode == CodeAllRight) {
				edittext.setSelection(edittext.length());
			} else if (primaryCode == CodeSwap) {
				if(listener != null) {
					listener.swapBases();
				}
			} else {// Insert character
				if (primaryCode == CodeDecimalPoint) {
					primaryCode = edittext.getContext()
							.getString(R.string.keyboard_decimalpoint)
							.charAt(0);
				}

				editable.insert(start, Character.toString((char) primaryCode));
			}
		}

		public void onText(CharSequence text) {
		}

		public void swipeLeft() {
		}

		public void swipeRight() {
		}

		public void swipeDown() {

		}

		public void swipeUp() {
		}

	};
}