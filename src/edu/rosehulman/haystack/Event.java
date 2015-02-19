package edu.rosehulman.haystack;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

import com.appspot.tombn_songm_haystack.haystack.model.DbEvent;
import com.appspot.tombn_songm_haystack.haystack.model.DbEventProtoCommentsLikes;

public class Event {

	private static final int MAX_DESC_LENGTH = 32;
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
	private ArrayList<String> mLikes;
	private String mCategory;
	private ArrayList<Comment> mComments;

	private DateFormatSymbols dfs = new DateFormatSymbols();
	private String[] months = dfs.getMonths();
	private double mLat;
	private double mLon;

	public Event() {
		mTitle = "Action Center Plaza";
		mStartHour = 8;
		mStartMinute = 30;
		mEndHour = 11;
		mEndMinute = 0;
		mDescription = "Free Drinks on Tuesdays!";
		mAddress = "5500 Wabash Ave Terre Haute, IN 47803";
		mLikes = new ArrayList<String>();
		mComments = new ArrayList<Comment>();
		mFromCalendar = new GregorianCalendar();
		mToCalendar = new GregorianCalendar();
		mLat = 0;
		mLon = 0;
	}

	public Event(String title, String address, String toDateTime, String fromDateTime,
			String entityKey, String description, String lastTouchDateTime, List<String> comments,
			List<String> likes, Double lat, Double lon) {
		mFromCalendar = new GregorianCalendar();
		mToCalendar = new GregorianCalendar();
		mTitle = title;
		mAddress = address;
		mEventID = entityKey;
		mDescription = description;
		mLikes = new ArrayList<String>();
		if (likes != null) {
			for (String like : likes) {
				mLikes.add(like);
			}
		}
		mLat = lat;
		mLon = lon;

		mFromDateTime = fromDateTime;
		mToDateTime = toDateTime;
		mComments = new ArrayList<Comment>();
		if (comments != null) {
			for (String comment : comments) {
				mComments.add(0, new Comment(comment));
			}
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		if (mFromDateTime != null && mToDateTime != null) {
			try {
				Date fromDate = sdf.parse(mFromDateTime);
				Date toDate = sdf.parse(mToDateTime);
				mFromCalendar.setTime(fromDate);
				mToCalendar.setTime(toDate);
				mStartHour = mFromCalendar.get(Calendar.HOUR_OF_DAY);
				mStartMinute = mFromCalendar.get(Calendar.MINUTE);
				mEndHour = mToCalendar.get(Calendar.HOUR_OF_DAY);
				mEndMinute = mToCalendar.get(Calendar.MINUTE);

			} catch (ParseException e) {
				// Auto-generated catch block
				Log.d("MIN", "parsing dates error: " + e);
			}
		}

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

	public String getStartDate() {
		String month = months[mFromCalendar.get(Calendar.MONTH)].substring(0, 3);
		int date = mFromCalendar.get(Calendar.DAY_OF_MONTH);
		String day = date + "";
		if (date < 10) {
			day = "0" + day;
		}
		return month + " " + day;
	}

	public String getEndDate() {
		String month = months[mToCalendar.get(Calendar.MONTH)].substring(0, 3);
		int date = mToCalendar.get(Calendar.DAY_OF_MONTH);
		String day = date + "";
		if (date < 10) {
			day = "0" + day;
		}
		return month + " " + day;
	}

	public String getStartTime() {
		return convertTime(mStartHour, mStartMinute);
	}

	public String getEndTime() {
		return convertTime(mEndHour, mEndMinute);
	}

	public static String convertTime(int hour, int minute) {
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

	public ArrayList<String> getLikes() {
		return mLikes;
	}

	public String getCategory() {
		return mCategory;
	}

	public void setCategory(String mCategory) {
		this.mCategory = mCategory;
	}

	public void setComments(List<String> comments) {
		mComments = new ArrayList<Comment>();
		if (comments != null) {
			for (String comment : comments) {
				addComment(comment);
			}
		}
	}

	public List<String> getCommentsAsList() {
		List<String> comments = new ArrayList<String>();
		for (Comment comment : mComments) {
			comments.add(0, comment.getComment());
		}
		return comments;
	}

	public List<String> getLikesAsList() {
		List<String> likes = new ArrayList<String>();
		for (String like : mLikes) {
			likes.add(like);
		}
		return likes;
	}

	public void setLikes(List<String> likes) {
		mLikes = new ArrayList<String>();
		if (likes != null) {
			for (String like : likes) {
				mLikes.add(like);
			}
		}
	}

	public void like() {
		(new LikeEventTask()).execute(mEventID);
	}

	public void unLike() {
		(new LikeEventTask()).execute(mEventID);
	}

	class LikeEventTask extends AsyncTask<String, Void, DbEventProtoCommentsLikes> {

		@Override
		protected DbEventProtoCommentsLikes doInBackground(String... entityKeys) {
			DbEventProtoCommentsLikes returnedQuote = null;
			try {
				returnedQuote = MainActivity.mService.dbevent().one(entityKeys[0]).execute();

			} catch (IOException e) {
				Log.e("BRANDON", "Failed to insert quote" + e);
			}
			return returnedQuote;
		}

		@Override
		protected void onPostExecute(DbEventProtoCommentsLikes result) {
			super.onPostExecute(result);

			if (result == null) {
				Log.e("BRANDON", "Result is null. Failed loading.");
				return;
			}
			// result.getItems() could be null
			setLikes(result.getLikes());
			if (mLikes.contains(MainActivity.id)) {
				mLikes.remove(MainActivity.id);
			} else if (!mLikes.contains(MainActivity.id)) {
				mLikes.add(MainActivity.id);
			}
			setComments(result.getComments());
			DbEvent event = new DbEvent();
			event.setEntityKey(getId());
			event.setLikes(getLikesAsList());
			event.setComments(getCommentsAsList());

			event.setLikesSize((long) mLikes.size());

			(new PostNewEventActivity.InsertEventTask()).execute(event);
		}

	}

	public String getDateString() {

		return getStartDate() + " " + getStartTime() + " - " + getEndDate() + " " + getEndTime();
	}

	public double getLat() {
		return mLat;
	}

	public double getLon() {
		return mLon;
	}

}
