// Casey Scott
// PAPVI - 1710
// MessageActivity.java

package com.fullsail.caseyscott.wantsapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;
import com.fullsail.caseyscott.wantsapp.GetMessagesService;
import com.fullsail.caseyscott.wantsapp.R;
import com.fullsail.caseyscott.wantsapp.adapter.MessageAdapter;
import com.fullsail.caseyscott.wantsapp.objects.MessagePost;
import com.fullsail.caseyscott.wantsapp.objects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MessageActivity extends AppCompatActivity {

    private static final String TAG = " MessageActivity ";
    private String uID = "";
    private User contact;
    private ListView messageView;
    private EditText message_ET;
    private Receiver mReceiver;
    private Timer timer;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_screen_layout);
        mAuth = FirebaseAuth.getInstance();
        messageView = (ListView) findViewById(R.id.message_listView);
        (findViewById(R.id.send_message_button)).setOnClickListener(mListener);
        message_ET = (EditText) findViewById(R.id.enter_message);
        if(mAuth.getCurrentUser() != null) {
            uID = mAuth.getCurrentUser().getUid();
        }
        if(savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent.hasExtra("contact")) {
                Bundle bundle = intent.getExtras();
                contact = new User();
                contact = (User) bundle.get("contact");
                queryForMessages();
                setTitle("Chat With " + contact.getFirstName());

            }
        }else {
            contact = new User();
            contact = (User) savedInstanceState.getSerializable("contact");
            String message = savedInstanceState.getString("message");
            message_ET.setText(message);
            ArrayList<MessagePost> messages = (ArrayList<MessagePost>) savedInstanceState.getSerializable("messages");
            messageView.setAdapter(new MessageAdapter(MessageActivity.this, messages, uID));

        }
        timer = new Timer();
        runTimer();


    }
    private void runTimer(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(MessageActivity.this, GetMessagesService.class);
                intent.putExtra("contact", contact);
                intent.putExtra("uid", uID);
                startService(intent);
            }

        }, 10000, 10000);
    }

    private final View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.send_message_button){
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/"+uID+"/contacts/"+contact.getId()+"/messages");
                String message = message_ET.getText().toString();
                long unixTime = System.currentTimeMillis() / 5000L;
                ref.push().setValue(new MessagePost(message,uID, unixTime));
                ref = FirebaseDatabase.getInstance().getReference("users/"+contact.getId()+"/contacts/"+uID+"/messages");
                ref.push().setValue(new MessagePost(message,uID, unixTime));
                message_ET.setText(null);
            }
        }
    };

    private void queryForMessages() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        ref.addListenerForSingleValueEvent(mSingleValueEventListener);
    }
    private final ValueEventListener mSingleValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ArrayList<MessagePost> messages = new ArrayList<>();
            for (DataSnapshot snapshot:dataSnapshot.child("users").child(uID).child("contacts").child(contact.getId()).child("messages").getChildren()){

                    MessagePost post = snapshot.getValue(MessagePost.class);
                    messages.add(post);
            }
            messageView.setAdapter(new MessageAdapter(MessageActivity.this, messages, uID));
            if(messages.size()!=0) {
                messageView.smoothScrollToPosition(messages.size() - 1);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.i(TAG, databaseError.getMessage());
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mReceiver = new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(GetMessagesService.ACTION_SEND_BROADCAST);
        //Register the receiver
        registerReceiver(mReceiver, filter);
        runTimer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
        //UnRegister the receiver
        unregisterReceiver(mReceiver);
    }
    public class Receiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(GetMessagesService.ACTION_SEND_BROADCAST)){

                ArrayList<MessagePost> posts = new ArrayList<>();
                if(intent.hasExtra("posts")) {
                    posts = (ArrayList<MessagePost>) intent.getExtras().get("posts");
                }
                //Update the UI
                messageView.setAdapter(new MessageAdapter(context, posts, uID));
            }
        }
    }
    private void signOutUserAndReturnToLogin() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(getString(R.string.sign_out_qmark));
        dialog.setMessage(getString(R.string.sign_out_message));
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.signOut();
                Intent intent = new Intent(LogInActivity.TO_LOG_IN);
                startActivity(intent);
            }
        });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog1) {
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getColor(R.color.colorPrimaryDark));
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getColor(R.color.colorPrimaryDark));
            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.basic_options, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.signOut_option){
            signOutUserAndReturnToLogin();
        }
        return super.onOptionsItemSelected(item);
    }
}
