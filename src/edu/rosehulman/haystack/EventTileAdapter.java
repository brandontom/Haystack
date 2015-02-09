package edu.rosehulman.haystack;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

public class EventTileAdapter extends BaseAdapter {
	private Context mContext;
	private int mNumRows;
	private ArrayList<Event> mEvents;

	public EventTileAdapter(Context context, ArrayList<Event> events) {
		mContext = context;
		mEvents = events;
		mNumRows = events.size();
	}

	public void addView() {
		mNumRows++;
	}

	@Override
	public int getCount() {
		return mNumRows;
	}

	@Override
	public Object getItem(int position) {
		return mEvents.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RowView view;
		if (convertView == null) {
			view = new RowView(mContext);
		} else {
			view = (RowView) convertView;
		}

		final Event event = (Event) getItem(position);
		
		ImageView rating = (ImageView) view.findViewById(R.id.rowview_rating);
		rating.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//do whatever to like the event.
				Toast.makeText(mContext, "You like this event!",
						Toast.LENGTH_SHORT).show();
			}
		});
		// Fill the view with data

		view.setTitleText(event.getTitle());
		view.setTimeText(event.getStartTime() + " - " + event.getEndTime());
		view.setDescriptionText(event.getShortDescription());
		return view;
	}

}
