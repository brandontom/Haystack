package edu.rosehulman.haystack;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CommentView extends LinearLayout {
	
	private TextView mCommentTextView;

	public CommentView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.rowview_comment, this);
		mCommentTextView = (TextView) findViewById(R.id.rowview_comment_title);
	}

	public void setCommentText(String string) {
		mCommentTextView.setText(string);
	}

}
