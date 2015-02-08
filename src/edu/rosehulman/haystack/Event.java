package edu.rosehulman.haystack;

import java.util.ArrayList;


public class Event {
	
	private static final int MAX_DESC_LENGTH = 40;
	private String mTitle;
	private int mStartHour;
	private int mStartMinute;
	private int mEndHour;
	private int mEndMinute;
	private String mAddress;
	private String mDescription;
	private int mEventID;
	private int mUpvotes;
	private ArrayList<Comment> mComments;

	public Event(){
		mTitle = "Action Center Plaza";
		mStartHour = 8;
		mStartMinute = 30;
		mEndHour = 11;
		mEndMinute = 0;
		mDescription = "Free Drinks on Tuesdays!";
		mAddress = "5500 Wabash Ave Terre Haute, IN 47803";
		mUpvotes = 0;
		mComments = new ArrayList<Comment>();
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

	public int getId() {
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

	public boolean addComment(String comment) {
		return mComments.add(new Comment(comment));
	}

	public int getUpvotes() {
		return mUpvotes;
	}

}
