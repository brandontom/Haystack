package edu.rosehulman.haystack;


public class Event {
	
	private String mTitle;
	private int mStartHour;
	private int mStartMinute;
	private int mEndHour;
	private int mEndMinute;
	private String mLocation;
	private String mDescription;
	private int mEventID;
	
	public Event(){
		mTitle = "woohoo";
		mStartHour = 8;
		mStartMinute = 30;
		mEndHour = 11;
		mEndMinute = 0;
	}

	public String getTitle() {
		return mTitle;
	}

	public String getStartTime() {
		return (mStartHour%12 != 0 ? mStartHour%12 : 12) + ":" + (mStartMinute<10 ? "0"+mStartMinute : mStartMinute) + " " + (mStartHour/12 == 1 ? "p.m." : "a.m.");
	}
	
	public String getEndTime() {
		return (mEndHour%12 != 0 ? mEndHour%12 : 12) + ":" + (mEndMinute<10 ? "0"+mEndMinute : mEndMinute) + " " + (mEndHour/12 == 1 ? "p.m." : "a.m.");
	}

}
