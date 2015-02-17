package edu.rosehulman.haystack;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.tombn_songm_haystack.haystack.model.DbEvent;
import com.appspot.tombn_songm_haystack.haystack.model.DbEventProtoComments;

public class EventActivity extends Activity {

	Event mEvent;
	ListView mComments;
	CommentTileAdapter mAdapter;
	EditText mComment;
	String mRecentComment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		Intent intent = getIntent();

		int pos = intent.getIntExtra(MainActivity.KEY_EVENT_ID, 0);

		mEvent = getEventByPosition(pos);
		mRecentComment = null;

		TextView address = (TextView) findViewById(R.id.event_activity_address);
		TextView title = (TextView) findViewById(R.id.event_activity_title);
		TextView description = (TextView) findViewById(R.id.event_activity_description);
		mComments = (ListView) findViewById(R.id.event_activity_comment_listview);
		Button sendButton = (Button) findViewById(R.id.event_activity_send_comment);
		mComment = (EditText) findViewById(R.id.event_activity_edit_comment);
		
		TextView time = (TextView) findViewById(R.id.event_activity_time);

		address.setText(mEvent.getAddress());
		title.setText(mEvent.getTitle());
		description.setText(mEvent.getFullDescription());
		time.setText(mEvent.getStartTime() + " - " + mEvent.getEndTime());

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
		mRecentComment = text;
	}

	private void setUpListView() {
		ArrayList<Comment> comments = mEvent.getComments();

		mAdapter = new CommentTileAdapter(this,
				comments);

		mComments.setAdapter(mAdapter);
	}
	
	class QueryForComments extends AsyncTask<String, Void, DbEventProtoComments> {

		@Override
		protected DbEventProtoComments doInBackground(String... entityKeys) {
			DbEventProtoComments returnedQuote = null;
			try {
				returnedQuote = MainActivity.mService.dbevent().comments(entityKeys[0]).execute();
			} catch (IOException e) {
				Log.e("BRANDON", "Failed to insert quote" + e);
			}
			return returnedQuote;
		}

		@Override
		protected void onPostExecute(DbEventProtoComments result) {
			super.onPostExecute(result);

			if (result == null) {
				Log.e("BRANDON", "Result is null. Failed loading.");
				return;
			}
			// result.getItems() could be null
			mEvent.setComments(result.getComments());
			mEvent.addComment(mRecentComment);
			
			DbEvent event = new DbEvent();
			event.setEntityKey(mEvent.getId());
			event.setComments(mEvent.getCommentsAsList());
			
			(new PostNewEventActivity.InsertEventTask()).execute(event);
			mRecentComment = null;
			mAdapter.addView();
			mAdapter.notifyDataSetChanged();
			mComment.setText("");
			Toast.makeText(EventActivity.this, getResources().getString(R.string.comment_sent), Toast.LENGTH_SHORT).show();
			setUpListView();
			
		}

	}

}
