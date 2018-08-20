package com.github.singond.melodear.android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends DebugActivity {

	private static final String TAG = "MelodEar";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void startMelodicDictation(@SuppressWarnings("unused") View view) {
		Intent intent = new Intent(this, MelodicDictationActivity.class);
		startActivity(intent);
	}

	public void soundTest(@SuppressWarnings("unused") View view) {
		Log.d(TAG, "Sound test");
	}
}