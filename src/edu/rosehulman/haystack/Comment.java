package edu.rosehulman.haystack;

public class Comment {
	
	private String mComment;
	
	public Comment(){
		setComment(null);
	}
	
	public Comment(String comment){
		setComment(comment);
	}

	public String getComment() {
		return mComment;
	}

	public void setComment(String mComment) {
		this.mComment = mComment;
	}

}