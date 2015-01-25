package edu.rosehulman.haystack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class EventActivity extends Activity {

	Event mEvent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		Intent intent = getIntent();

		int eventId = intent
				.getIntExtra(MainActivity.KEY_EVENT_ID, 0);
		
		Event mEvent = getEventById(eventId);

		TextView address = (TextView) findViewById(R.id.event_activity_address);
		TextView title = (TextView) findViewById(R.id.event_activity_title);
		TextView description = (TextView) findViewById(R.id.event_activity_description);
		ListView comments = (ListView) findViewById(R.id.event_activity_comment_listview);
		Button sendButton = (Button) findViewById(R.id.event_activity_send_comment);
		EditText comment = (EditText) findViewById(R.id.event_activity_edit_comment);
		
		address.setText(mEvent.getAddress());
		title.setText(mEvent.getTitle());
		description.setText(mEvent.getFullDescription());
		
		
	}

	private Event getEventById(int eventId) {
		return new Event();
	}

}
