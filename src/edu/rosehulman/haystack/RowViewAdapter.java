package edu.rosehulman.haystack;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RowViewAdapter extends BaseAdapter {
	private Context mContext;
	private int mNumRows;
	private ArrayList<Event> mEvents;

	public RowViewAdapter(Context context, ArrayList<Event> events) {
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
		if (event.getLikes().contains(MainActivity.id)) {
			rating.setImageDrawable(mContext.getResources().getDrawable(
					android.R.drawable.btn_star_big_on));
		} else {
			rating.setImageDrawable(mContext.getResources().getDrawable(
					android.R.drawable.btn_star_big_off));
		}
		final TextView numLikes = (TextView) view.findViewById(R.id.num_likes);
		int num = event.getLikes().size();
		numLikes.setText(num + (num == 1 ? " like" : " likes"));
		 

		rating.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// do whatever to like the event.
				if (!event.getLikes().contains(MainActivity.id)) {
					Toast.makeText(mContext, "You like this event!", Toast.LENGTH_SHORT).show();
					event.getLikes().add(MainActivity.id);
					((ImageView) v).setImageDrawable(mContext.getResources().getDrawable(
							android.R.drawable.btn_star_big_on));
					int num = event.getLikes().size();
					numLikes.setText(num + (num == 1 ? " like" : " likes"));
					event.like();
				} else {
					Toast.makeText(mContext, "You disliked this event!", Toast.LENGTH_SHORT).show();
					event.getLikes().remove(MainActivity.id);
					((ImageView) v).setImageDrawable(mContext.getResources().getDrawable(
							android.R.drawable.btn_star_big_off));
					int num = event.getLikes().size();
					numLikes.setText(num + (num == 1 ? " like" : " likes"));
					event.unLike();
				}
			}
		});
		// Fill the view with data
		String[] categories = mContext.getResources()
				.getStringArray(R.array.category_spinner_array);
		
		if (event.getCategory().equals(categories[0])) {
			view.setBackgroundColor(mContext.getResources().getColor(R.color.Light_Red));
		} else if (event.getCategory().equals(categories[1])) {
			view.setBackgroundColor(mContext.getResources().getColor(R.color.Light_Purple));
		} else if (event.getCategory().equals(categories[2])) {
			view.setBackgroundColor(mContext.getResources().getColor(R.color.Light_Green));
		} else if (event.getCategory().equals(categories[3])) {
			view.setBackgroundColor(mContext.getResources().getColor(R.color.Light_Blue));
		} else if (event.getCategory().equals(categories[4])) {
			view.setBackgroundColor(mContext.getResources().getColor(R.color.Light_Grey));
		}

		view.setTitleText(event.getTitle());
		view.setTimeText(event.getDateString());
		view.setDescriptionText(event.getShortDescription());
		return view;
	}

}
