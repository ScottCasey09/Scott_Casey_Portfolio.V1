// Casey Scott
// PAP VI - 1710
// GetMessagesService.java

package com.fullsail.caseyscott.wantsapp;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fullsail.caseyscott.wantsapp.objects.MessagePost;
import com.fullsail.caseyscott.wantsapp.objects.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class GetMessagesService extends IntentService {
    public static final String ACTION_SEND_BROADCAST = "com.fullsail.android.scottcasey_ce07_ACTION_SEND_BROADCAST";
    private static final String TAG = " GetMessagesService ";
    private String uID = "";
    private User contact;

    public GetMessagesService() {
        super("GetMessagesService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        contact= new User();
        if(intent!=null) {
            if (intent.hasExtra("uid")) {
                uID = intent.getExtras().getString("uid");
            }
            if (intent.hasExtra("contact")) {
                contact = (User) intent.getExtras().get("contact");
            }
            getMessagesFromData();
        }
    }

    private void getMessagesFromData(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        ref.addValueEventListener(mValueEventListener);
    }
    private final ValueEventListener mValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ArrayList<MessagePost> messages = new ArrayList<>();
            for (DataSnapshot snapshot:dataSnapshot.child("users").child(uID).child("contacts").child(contact.getId()).child("messages").getChildren()){

                    MessagePost post = snapshot.getValue(MessagePost.class);
                    messages.add(post);


            }
            Intent intent = new Intent(ACTION_SEND_BROADCAST);
            intent.putExtra("posts", messages);
            sendBroadcast(intent);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(TAG, databaseError.getMessage());
        }
    };
}
