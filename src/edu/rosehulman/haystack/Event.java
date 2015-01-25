package edu.rosehulman.haystack;

import android.text.format.Time;

public class Event {
	
	private static final int MAX_DESC_LENGTH = 40;
	private String mTitle;
	private int mStartHour;
	private int mStartMinute;
	private int mEndHour;
	private int mEndMinute;
	private String mLocation;
	private String mDescription;
	private int mEventID;

	public Event(){
		mTitle = "Action Center Plaza";
		mStartHour = 8;
		mStartMinute = 30;
		mEndHour = 11;
		mEndMinute = 0;
		mDescription = "Free Drinks on Tuesdays!";
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
	
	private String convertTime(int hour, int minute){
		return (hour%12 != 0 ? hour%12 : 12) + ":" + (minute<10 ? "0"+minute : minute) + " " + (hour/12 == 1 ? "PM" : "AM");
	}

	public String getShortDescription() {
		if(mDescription.length() > MAX_DESC_LENGTH){
			return mDescription.substring(0, MAX_DESC_LENGTH) + "...";
		}
		return mDescription.substring(0, mDescription.length());
	}

}
