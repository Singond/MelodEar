package com.github.singond.melodear.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void startMelodicDictation(@SuppressWarnings("unused") View view) {
		Intent intent = new Intent(this, MelodicDictationActivity.class);
		startActivity(intent);
	}
}