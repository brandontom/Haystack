package edu.rosehulman.haystack;

import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.tombn_songm_haystack.haystack.model.DbEvent;
import com.appspot.tombn_songm_haystack.haystack.model.DbEventProtoCommentsLikes;

public class EventActivity extends Activity {

	private static final String DISLIKE_EVENT_TOAST_TEXT = "You disliked this event!";
	private static final String LIKE_EVENT_TOAST_TEXT = "You like this event!";
	Event mEvent;
	ListView mComments;
	CommentTileAdapter mAdapter;
	EditText mComment;
	Button sendButton;
	String mRecentComment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);
		Intent intent = getIntent();

		int pos = intent.getIntExtra(MainActivity.KEY_EVENT_ID, 0);

		mEvent = getEventByPosition(pos);
		mRecentComment = null;

		TextView distanceView = (TextView) findViewById(R.id.distance);
		double eLat = mEvent.getLat();
		double eLon = mEvent.getLon();
		double distance = MainActivity.haversine(eLat, eLon, MainActivity.mLat, MainActivity.mLon);
		distanceView.setText(getResources().getString(R.string._1f_miles_away, distance));

		TextView address = (TextView) findViewById(R.id.event_activity_address);
		TextView title = (TextView) findViewById(R.id.event_activity_title);
		TextView description = (TextView) findViewById(R.id.event_activity_description);
		mComments = (ListView) findViewById(R.id.event_activity_comment_listview);
		sendButton = (Button) findViewById(R.id.event_activity_send_comment);
		mComment = (EditText) findViewById(R.id.event_activity_edit_comment);
		final TextView numLikes = (TextView) EventActivity.this
				.findViewById(R.id.num_likes_activity);
		int num = mEvent.getLikes().size();
		numLikes.setText(num + (num == 1 ? " like" : " likes"));

		ImageView rating = (ImageView) findViewById(R.id.event_activity_rating);
		if (mEvent.getLikes().contains(MainActivity.id)) {
			rating.setImageDrawable(this.getResources().getDrawable(
					android.R.drawable.btn_star_big_on));
		}
		rating.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// do whatever to like the event.
				if (!mEvent.getLikes().contains(MainActivity.id)) {
					Toast.makeText(EventActivity.this, LIKE_EVENT_TOAST_TEXT, Toast.LENGTH_SHORT)
							.show();
					mEvent.getLikes().add(MainActivity.id);
					((ImageView) v).setImageDrawable(EventActivity.this.getResources().getDrawable(
							android.R.drawable.btn_star_big_on));
					int num = mEvent.getLikes().size();
					numLikes.setText(num + (num == 1 ? " like" : " likes"));
					mEvent.like();
				} else {
					Toast.makeText(EventActivity.this, DISLIKE_EVENT_TOAST_TEXT, Toast.LENGTH_SHORT)
							.show();
					mEvent.getLikes().remove(MainActivity.id);
					((ImageView) v).setImageDrawable(EventActivity.this.getResources().getDrawable(
							android.R.drawable.btn_star_big_off));
					int num = mEvent.getLikes().size();
					numLikes.setText(num + (num == 1 ? " like" : " likes"));
					mEvent.unLike();
				}
			}

		});

		TextView time = (TextView) findViewById(R.id.event_activity_time);

		address.setText(mEvent.getAddress());
		title.setText(mEvent.getTitle());
		description.setText(mEvent.getFullDescription());
		time.setText(Html.fromHtml(mEvent.getDateString()));

		setUpListView();

		sendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String text = mComment.getEditableText().toString();
				if (text.isEmpty()) {
					return;
				}
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mComment.getWindowToken(), 0);
				addComment(text);
			}
		});
		String[] categories = getResources().getStringArray(R.array.category_spinner_array);
		View view = address.getRootView();
		if (mEvent.getCategory().equals(categories[0])) {
			view.setBackgroundColor(getResources().getColor(R.color.Light_Red));
		} else if (mEvent.getCategory().equals(categories[1])) {
			view.setBackgroundColor(getResources().getColor(R.color.Light_Purple));
		} else if (mEvent.getCategory().equals(categories[2])) {
			view.setBackgroundColor(getResources().getColor(R.color.Light_Green));
		} else if (mEvent.getCategory().equals(categories[3])) {
			view.setBackgroundColor(getResources().getColor(R.color.Light_Blue));
		} else if (mEvent.getCategory().equals(categories[4])) {
			view.setBackgroundColor(getResources().getColor(R.color.Light_Grey));
		}
	}

	private Event getEventByPosition(int pos) {
		return MainActivity.mEvents.get(pos);
	}

	private void addComment(String text) {
		mRecentComment = text;
		(new PostComment()).execute(mEvent.getId());
	}

	private void setUpListView() {
		ArrayList<Comment> comments = mEvent.getComments();

		mAdapter = new CommentTileAdapter(this, comments);

		mComments.setAdapter(mAdapter);
	}

	class PostComment extends AsyncTask<String, Void, DbEventProtoCommentsLikes> {

		@Override
		protected DbEventProtoCommentsLikes doInBackground(String... entityKeys) {
			DbEventProtoCommentsLikes returnedQuote = null;
			try {
				returnedQuote = MainActivity.mService.dbevent().one(entityKeys[0]).execute();
			} catch (IOException e) {
				Log.e(MainActivity.HS, "Failed to insert quote" + e);
			}
			return returnedQuote;
		}

		@Override
		protected void onPostExecute(DbEventProtoCommentsLikes result) {
			super.onPostExecute(result);

			if (result == null) {
				Log.e(MainActivity.HS, "Result is null. Failed loading.");
				return;
			}
			// result.getItems() could be null
			mEvent.setComments(result.getComments());
			mEvent.addComment(mRecentComment);
			mEvent.setLikes(result.getLikes());

			DbEvent event = new DbEvent();
			event.setEntityKey(mEvent.getId());
			event.setComments(mEvent.getCommentsAsList());
			event.setLikes(mEvent.getLikesAsList());

			event.setCommentSize((long) mEvent.getComments().size());

			(new PostNewEventActivity.InsertEventTask()).execute(event);
			mRecentComment = null;
			mAdapter.addView();
			mAdapter.notifyDataSetChanged();
			mComment.setText("");
			Toast.makeText(EventActivity.this, getResources().getString(R.string.comment_sent),
					Toast.LENGTH_SHORT).show();
			setUpListView();

		}

	}

}
