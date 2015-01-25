package edu.rosehulman.haystack;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RowView extends LinearLayout {
	
	private TextView mLeftTextView;
	private TextView mRightTextView;

	public RowView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.view_event_tile, this);
		mLeftTextView = (TextView) findViewById(R.id.left_text_view);
		mRightTextView = (TextView) findViewById(R.id.right_text_view);
	}

	public void setLeftText(String string) {
		mLeftTextView.setText(string);
	}

	public void setRightText(String string) {
		mRightTextView.setText(string);
	}

}
