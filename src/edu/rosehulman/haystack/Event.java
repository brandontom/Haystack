package edu.rosehulman.haystack;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.util.Log;

public class Event {

	private static final int MAX_DESC_LENGTH = 40;
	private String mTitle;
	private int mStartHour;
	private int mStartMinute;
	private String mFromDateTime;
	private GregorianCalendar mFromCalendar;
	private String mToDateTime;
	private GregorianCalendar mToCalendar;
	private int mEndHour;
	private int mEndMinute;
	private String mAddress;
	private String mDescription;
	private String mEventID;
	private int mUpvotes;
	private String mCategory;
	private ArrayList<Comment> mComments;
	private String mLastModified;

	public Event() {
		mTitle = "Action Center Plaza";
		mStartHour = 8;
		mStartMinute = 30;
		mEndHour = 11;
		mEndMinute = 0;
		mDescription = "Free Drinks on Tuesdays!";
		mAddress = "5500 Wabash Ave Terre Haute, IN 47803";
		mUpvotes = 0;
		mComments = new ArrayList<Comment>();
		mFromCalendar = new GregorianCalendar();
		mToCalendar = new GregorianCalendar();
	}

	public Event(String title, String address, String toDateTime, String fromDateTime,
			String entityKey, String description, String lastTouchDateTime, List<String> comments) {
		mFromCalendar = new GregorianCalendar();
		mToCalendar = new GregorianCalendar();
		mTitle = title;
		mAddress = address;
		mEventID = entityKey;
		mDescription = description;
		mLastModified = lastTouchDateTime;
		mUpvotes = 0;

		mFromDateTime = fromDateTime;
		mToDateTime = toDateTime;
		mComments = new ArrayList<Comment>();
		if (comments != null) {
			for (String comment : comments) {
				mComments.add(0, new Comment(comment));
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss.SSS");
		if (mFromDateTime != null && mToDateTime != null) {
			try {
				Date fromDate = sdf.parse(mFromDateTime);
				Date toDate = sdf.parse(mToDateTime);
				mFromCalendar.setTime(fromDate);
				mToCalendar.setTime(toDate);
				mStartHour = mFromCalendar.get(Calendar.HOUR);
				mStartMinute = mFromCalendar.get(Calendar.MINUTE);
				mEndHour = mToCalendar.get(Calendar.HOUR);
				mEndMinute = mToCalendar.get(Calendar.MINUTE);

			} catch (ParseException e) {
				// Auto-generated catch block
				Log.d("MIN", "parsing dates error: " + e);
			}
		}

		// String[] ar = fromDateTime.split(":");
		// mStartMinute = Integer.parseInt(ar[1]);
		// mStartHour = Integer.parseInt(ar[0].substring(ar[0].length()-2,
		// ar[0].length()));
		// ar = toDateTime.split(":");
		// mEndMinute = Integer.parseInt(ar[1]);
		// mEndHour = Integer.parseInt(ar[0].substring(ar[0].length()-2,
		// ar[0].length()));

	}

	public GregorianCalendar getFromDate() {
		return mFromCalendar;
	}

	public GregorianCalendar getToDate() {
		return mToCalendar;

	}

	public String getTitle() {
		return mTitle;
	}

	public String getStartTime() {
		return convertTime(mStartHour, mStartMinute);
	}

	public String getEndTime() {
		return convertTime(mEndHour, mEndMinute);
	}

	private String convertTime(int hour, int minute) {

		return (hour % 12 != 0 ? hour % 12 : 12) + ":" + (minute < 10 ? "0" + minute : minute)
				+ " " + (hour / 12 == 1 ? "PM" : "AM");
	}

	public String getShortDescription() {
		if (mDescription == null) {
			return "No Description";
		}
		if (mDescription.length() > MAX_DESC_LENGTH) {
			return mDescription.substring(0, MAX_DESC_LENGTH) + "...";
		}
		return mDescription.substring(0, mDescription.length());
	}

	public String getId() {
		return mEventID;
	}

	public CharSequence getAddress() {
		return mAddress;
	}

	public CharSequence getFullDescription() {
		return mDescription;
	}

	public ArrayList<Comment> getComments() {
		return mComments;
	}

	public void addComment(String comment) {
		mComments.add(0, new Comment(comment));
	}

	public int getUpvotes() {
		return mUpvotes;
	}

	public String getCategory() {
		return mCategory;
	}

	public void setCategory(String mCategory) {
		this.mCategory = mCategory;
	}

	public void setComments(List<String> comments) {
		mComments = new ArrayList<Comment>();
		for (String comment : comments) {
			addComment(comment);
		}
	}

	public List<String> getCommentsAsList() {
		List<String> comments = new ArrayList<String>();
		for (Comment comment : mComments) {
			comments.add(0, comment.getComment());
		}
		return comments;
	}

}
