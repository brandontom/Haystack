package edu.rosehulman.haystack;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class EventTileAdapter extends BaseAdapter {
	private Context mContext;
	private int mNumRows = 5;
	private ArrayList<Event> mEvents;

	public EventTileAdapter(Context context, ArrayList<Event> events) {
		mContext = context;
		mEvents = events;
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
		// Fill the view with data
		Event event = (Event) getItem(position);

		view.setTitleText(event.getTitle());
		view.setTimeText(event.getStartTime() + " - " + event.getEndTime());
		view.setDescriptionText(event.getShortDescription());
		return view;
	}

}
