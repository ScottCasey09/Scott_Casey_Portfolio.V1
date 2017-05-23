// Casey Scott
// Java 1 - 1704
// Member.Java
package com.fullsail.android.ScottCasey_CE12.object;

public class Member {

	private int mId;
	private String mName;
	private String mParty;
	
	private Member() {
		mId = 0;
		mName = mParty = "";
	}
	
	private Member(int _id) {
		this();
		mId = _id;
	}
	
	private Member(int _id, String _name) {
		this(_id);
		mName = _name;
	}
	
	public Member(int _id, String _name, String _party) {
		this(_id, _name);
		mParty = _party;
	}
	

	public int getId() {
		return mId;
	}
	

	
	public String getName() {
		return mName;
	}
	

	
	public String getParty() {
		return mParty;
	}
}