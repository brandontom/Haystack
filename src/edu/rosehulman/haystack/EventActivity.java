package edu.rosehulman.haystack;

import java.util.ArrayList;

import com.appspot.tombn_songm_haystack.haystack.model.DbEvent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class EventActivity extends Activity {

	Event mEvent;
	ListView mComments;
	CommentTileAdapter mAdapter;
	EditText mComment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		Intent intent = getIntent();

		int pos = intent.getIntExtra(MainActivity.KEY_EVENT_ID, 0);

		mEvent = getEventByPosition(pos);

		TextView address = (TextView) findViewById(R.id.event_activity_address);
		TextView title = (TextView) findViewById(R.id.event_activity_title);
		TextView description = (TextView) findViewById(R.id.event_activity_description);
		mComments = (ListView) findViewById(R.id.event_activity_comment_listview);
		Button sendButton = (Button) findViewById(R.id.event_activity_send_comment);
		mComment = (EditText) findViewById(R.id.event_activity_edit_comment);

		address.setText(mEvent.getAddress());
		title.setText(mEvent.getTitle());
		description.setText(mEvent.getFullDescription());

		setUpListView();

		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String text = mComment.getEditableText().toString();
				if(text.isEmpty()){
					return;
				}
				addComment(text);
			}
		});
	}

	private Event getEventByPosition(int pos) {
		return MainActivity.mEvents.get(pos);
	}

	private void addComment(String text) {
		mEvent.addComment(text);
		mAdapter.addView();
		mAdapter.notifyDataSetChanged();
		mComment.setText("");
		Toast.makeText(this, getResources().getString(R.string.comment_sent), Toast.LENGTH_SHORT).show();
		DbEvent dbevent = new DbEvent();
	}

	private void setUpListView() {
		ArrayList<Comment> comments = mEvent.getComments();

		mAdapter = new CommentTileAdapter(this,
				comments);

		mComments.setAdapter(mAdapter);
	}

}
