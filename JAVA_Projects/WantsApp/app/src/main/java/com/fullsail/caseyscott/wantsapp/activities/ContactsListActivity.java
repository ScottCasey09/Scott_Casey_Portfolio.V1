// Casey Scott
// PAPVI - 1710
// ContactsListActivity.java

package com.fullsail.caseyscott.wantsapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fullsail.caseyscott.wantsapp.R;
import com.fullsail.caseyscott.wantsapp.objects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ContactsListActivity extends AppCompatActivity {

    private static final String TAG = "SearchListActivity";
    public static final String TO_MESSAGING_ACTION = "com.fullsail.caseyscott_wantsapp_TO_MESSAGING_ACTION";
    private ListView listView;
    private FirebaseAuth mAuth;
    private ArrayList<User> contactsList;
    private String uID = "";
    TextView emptyView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts_list_layout);
        setTitle(getString(R.string.my_contacts));

        emptyView = (TextView) findViewById(R.id.gifts_for_contact_EmptylistView);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(listClickListener);
        Button tabContactsList = (Button) findViewById(R.id.barButton2);
        Button tabMyList = (Button) findViewById(R.id.barButton1);
        Button tabSearch = (Button) findViewById(R.id.barButton3);
        tabContactsList.setEnabled(false);
        tabMyList.setOnClickListener(mClickListener);
        tabSearch.setOnClickListener(mClickListener);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null) {
            uID = mAuth.getCurrentUser().getUid();
        }

        if (savedInstanceState != null) {
            contactsList = (ArrayList<User>) savedInstanceState.getSerializable("contacts");
            listView.setAdapter(new ContactsListAdapter(this, contactsList));
        } else {
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
            mRef.addListenerForSingleValueEvent(readContactsListListener);
        }
    }

    private final AdapterView.OnItemClickListener listClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            User user = (User) view.getTag(R.id.object);
            Toast.makeText(getApplicationContext(), user.toString(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(GiftsListsForContact.TO_CONTACTS_ITEMS);
            intent.putExtra("user", user);
            startActivity(intent);
        }
    };

    //EventListener for reading in the contacts list from firebase
    private final ValueEventListener readContactsListListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            contactsList = new ArrayList<>();
            for (DataSnapshot snap : dataSnapshot.child("users/" + uID).child("contacts").getChildren()) {
                contactsList.add(snap.getValue(User.class));
            }
            listView.setAdapter(new ContactsListAdapter(ContactsListActivity.this, contactsList));
            if (contactsList.size() < 1){
                emptyView.setVisibility(View.VISIBLE);
            }else{
                emptyView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.i(TAG, databaseError.getMessage());
        }
    };

    private final View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.barButton1:
                    navigateToMyList();
                    return;
                case R.id.barButton3:
                    navigateToSearch();
            }
        }
    };

    private void navigateToSearch() {

        if (mAuth != null) {
            Intent intent = new Intent(LogInActivity.TO_SEARCH_LIST);
            intent.putExtra("contacts", contactsList);
            startActivity(intent);
        }
    }

    private void navigateToMyList() {
        if (mAuth != null) {
            Intent intent = new Intent(LogInActivity.TO_USERS_LIST);
            startActivity(intent);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("contacts", contactsList);
    }
    private void signOutUserAndReturnToLogin() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(getString(R.string.sign_out_qmark));
        dialog.setMessage(getString(R.string.sign_out_message));
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), getString(R.string.signed_out) ,Toast.LENGTH_SHORT).show();
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


    private class ContactsListAdapter extends BaseAdapter {

        private static final int ID_CONSTANT = 0X0000001;
        final Context context;
        final ArrayList<User> contacts;

        ContactsListAdapter(Context context, ArrayList<User> contacts) {
            this.context = context;
            this.contacts = contacts;
        }

        class ViewHolder {
            //final ImageView imageView;
            final TextView textView;
            final ImageButton imageButton;

            ViewHolder(View v) {
                //this.imageView = (ImageView) v.findViewById(R.id.user_icon_IV);
                this.textView = (TextView) v.findViewById(R.id.username_searchCell);
                imageButton = (ImageButton) v.findViewById(R.id.message_button);
            }
        }

        @Override
        public int getCount() {
            return contacts.size();
        }

        @Override
        public Object getItem(int position) {
            return contacts.get(position);
        }

        @Override
        public long getItemId(int position) {
            return ID_CONSTANT + position;
        }


        @Override
        public View getView(int position, View _convertView, ViewGroup _parent) {
            ViewHolder holder;

            try {
                if (_convertView == null) {
                    _convertView = LayoutInflater.from(context).inflate(R.layout.contacts_list_cell_layout, _parent, false);
                    holder = new ContactsListAdapter.ViewHolder(_convertView);
                    _convertView.setTag(R.id.view, holder);

                } else {

                    holder = (ContactsListAdapter.ViewHolder) _convertView.getTag(R.id.view);
                }

                User contact = contacts.get(position);
                _convertView.setTag(R.id.object, contact);
                holder.textView.setText(contact.toString());
//                    if (!contact.getProfilePicUrl().equals("")){
//                        Picasso.with(context).load(contact.getProfilePicUrl()).into(holder.imageView);
//                    }
                holder.imageButton.setTag(contact);
                holder.imageButton.setOnClickListener(mListener);
                holder.imageButton.setFocusable(false);
                holder.imageButton.setFocusableInTouchMode(false);


            } catch (Exception e) {
                e.printStackTrace();
            }

            return _convertView;
        }

        final View.OnClickListener mListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.getId() == R.id.message_button) {
                    Intent intent = new Intent(ContactsListActivity.TO_MESSAGING_ACTION);
                    intent.putExtra("contact",(User) v.getTag());
                    startActivityForResult(intent, RESULT_CANCELED);
                }
            }
        };
    }
    //sign out user on back pressed
    @Override
    public void onBackPressed() {
        if(mAuth.getCurrentUser() != null) {
            final AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setTitle(getString(R.string.sign_out_qmark));
            dialog.setMessage(getString(R.string.back_button_message));
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), getString(R.string.signed_out), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LogInActivity.TO_LOG_IN);
                    mAuth.signOut();
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
        }else{
            super.onBackPressed();
        }

    }


}


