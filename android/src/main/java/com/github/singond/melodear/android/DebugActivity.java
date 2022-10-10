package com.github.singond.melodear.android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

/**
 * Logs the callback methods.
 * For development only.
 *
 * @author Singon
 */
public class DebugActivity extends Activity {

	private static final String TAG = "ActivityDebugging";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate()");
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Log.d(TAG, "onBackPressed()");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.d(TAG, "onStart()");
	}

	@Override
	protected void onResume() {
		super.onResume();
		Log.d(TAG, "onResume()");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.d(TAG, "onRestart()");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause()");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.d(TAG, "onStop()");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "onDestroy()");
	}
}
