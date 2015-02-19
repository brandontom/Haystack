package edu.rosehulman.haystack;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RowView extends LinearLayout {

	private TextView mTitleTextView;
	private TextView mTimeTextView;
	private TextView mDescriptionTextView;

	public RowView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.rowview_event, this);
		mTitleTextView = (TextView) findViewById(R.id.rowview_title);
		mTimeTextView = (TextView) findViewById(R.id.rowview_time);
		mDescriptionTextView = (TextView) findViewById(R.id.rowview_description);
	}

	public void setTitleText(String string) {
		mTitleTextView.setText(string);
	}

	public void setTimeText(String string) {
		mTimeTextView.setText(Html.fromHtml(string));

	}

	public void setDescriptionText(String string) {
		mDescriptionTextView.setText(string);
	}

}
