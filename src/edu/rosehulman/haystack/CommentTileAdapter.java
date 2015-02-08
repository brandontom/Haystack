package edu.rosehulman.haystack;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CommentTileAdapter extends BaseAdapter {
	private Context mContext;
	private int mNumRows = 0;
	private ArrayList<Comment> mComments;

	public CommentTileAdapter(Context context, ArrayList<Comment> comments) {
		mContext = context;
		mComments = comments;
		mNumRows = comments.size();
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
		return mComments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommentView view;
		if (convertView == null) {
			view = new CommentView(mContext);
		} else {
			view = (CommentView) convertView;
		}

		final Comment comment = (Comment) getItem(position);
		// Fill the view with data

		view.setCommentText(comment.getComment());
		return view;
	}

}
