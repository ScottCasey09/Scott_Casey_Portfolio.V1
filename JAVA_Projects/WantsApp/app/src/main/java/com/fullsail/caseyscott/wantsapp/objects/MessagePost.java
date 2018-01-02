// Casey Scott
// PAPVI - 1710
// MessagePost.java

package com.fullsail.caseyscott.wantsapp.objects;


import java.io.Serializable;

public class MessagePost implements Serializable{
    public MessagePost(){

    }

    public MessagePost(String message, String usersId, long timeStamp) {
        this.message = message;
        this.usersId = usersId;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }


    public String getUsersId() {
        return usersId;
    }


    public long getTimeStamp() {
        return timeStamp;
    }


    private String message;
    private String usersId;
    private long timeStamp;

}
