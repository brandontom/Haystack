package edu.rosehulman.haystack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends Activity implements
OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup1);
		radioGroup.check(getResources().getIdentifier("radio"+MainActivity.mSearchRadius, "id", getPackageName()));
		
		((RadioButton) findViewById(R.id.radio5)).setOnClickListener(this);
		((RadioButton) findViewById(R.id.radio10)).setOnClickListener(this);
		((RadioButton) findViewById(R.id.radio20)).setOnClickListener(this);
		((RadioButton) findViewById(R.id.radio50)).setOnClickListener(this);
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.radio5:
			MainActivity.mSearchRadius = 5;
			break;
		case R.id.radio10:
			MainActivity.mSearchRadius = 10;
			break;
		case R.id.radio20:
			MainActivity.mSearchRadius = 20;
			break;
		case R.id.radio50:
			MainActivity.mSearchRadius = 50;
			break;
		}
		finish();
	}

}
