package edu.rosehulman.haystack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class EventActivity extends Activity {

	int mEventId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		Intent intent = getIntent();

		mEventId = intent
				.getIntExtra(MainActivity.KEY_EVENT_ID, 0);

//		TextView text = (TextView) findViewById(R.id.event_id);
//		text.setText("" + mEventId);
	}

}
