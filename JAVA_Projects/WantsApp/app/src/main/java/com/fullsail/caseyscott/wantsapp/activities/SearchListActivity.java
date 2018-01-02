// Casey Scott
// PAPVI - 1710
// SearchListActivity.java

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
import android.widget.EditText;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchListActivity extends AppCompatActivity {


    private static final String TAG = "SearchListActivity";
    private ListView listView;
    private EditText searchBar;
    private FirebaseAuth mAuth;
    private boolean isAuthorized = true;
    private String uID = "";
    private TextView emptyView;
    private HashMap<String, User> contactsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_screen_layout);
        setTitle(getString(R.string.search_for_users));

        if (getIntent().hasExtra("not_auth")){
            isAuthorized = (boolean) getIntent().getExtras().get("not_auth");
        }

        emptyView = (TextView) findViewById(R.id.gifts_for_contact_EmptylistView);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(mOnItemClickListener);
        searchBar = (EditText) findViewById(R.id.search_bar);
        ImageButton searchButton = (ImageButton) findViewById(R.id.search_button);
        searchButton.setOnClickListener(mClickListener);
        Button tabContactsList = (Button) findViewById(R.id.barButton2);
        Button tabMyList = (Button) findViewById(R.id.barButton1);
        Button tabSearch = (Button) findViewById(R.id.barButton3);
        tabSearch.setEnabled(false);
        tabContactsList.setOnClickListener(mClickListener);
        tabMyList.setOnClickListener(mClickListener);
        tabSearch.setOnClickListener(mClickListener);
        contactsList = new HashMap<>();
        if(savedInstanceState == null) {
            ArrayList<User> contacts = new ArrayList<>();
            if (getIntent().hasExtra("contacts")) {
                contacts = (ArrayList<User>) getIntent().getSerializableExtra("contacts");
            }

            for (User contact : contacts) {
                contactsList.put(contact.getId(), contact);
            }
        }
        if(isAuthorized) {
            mAuth = FirebaseAuth.getInstance();
            if(mAuth.getCurrentUser()!=null){
                uID = mAuth.getCurrentUser().getUid();
            }
        }

        if(savedInstanceState != null){
            searchBar.setText(savedInstanceState.getString("search_bar"));
            contactsList = (HashMap<String, User>) savedInstanceState.getSerializable("contacts");
        }

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

    private final AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            User user = (User) view.getTag(R.id.object);
            Toast.makeText(SearchListActivity.this, user.toString(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(GiftsListsForContact.TO_CONTACTS_ITEMS);
            intent.putExtra("user", user);
            startActivity(intent);
        }
    };

    private final View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.search_button:
                    Toast.makeText(getApplicationContext(), "Performing Search", Toast.LENGTH_SHORT).show();
                    performSearch(searchBar.getText().toString().trim(), getSearchKey(searchBar.getText().toString().trim()));
                    return;
                case R.id.barButton1:
                    navigateToMyList();
                    return;
                case R.id.barButton2:
                    navigateToContacts();
            }
        }
    };

    private String getSearchKey(String entry) {

        if(isPhoneNum(entry)){
            Toast.makeText(getApplicationContext(), "Phone Number Search", Toast.LENGTH_SHORT).show();
            return "phoneNumber";
        }
        char[] entryToArray = entry.trim().toCharArray();
        for(char c : entryToArray){
            if (c == '@'){
                Toast.makeText(getApplicationContext(), "Email Search", Toast.LENGTH_SHORT).show();
                return "searchByEmail";
            }
            if (c == ' '){
                return "searchByFullName";
            }
        }
        Toast.makeText(getApplicationContext(), "Username Search", Toast.LENGTH_SHORT).show();
        return "searchByUsername";
    }
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private boolean isPhoneNum(String entry){
        try{
            Long.parseLong(entry);
        }catch (NumberFormatException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void performSearch(String entry, String nodeKey) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Query query = databaseReference.orderByChild(nodeKey).equalTo(entry.toLowerCase().trim());
        query.addListenerForSingleValueEvent(getSearchResultsListener);

        Log.i(TAG, databaseReference.toString());
    }
    private final ValueEventListener getSearchResultsListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ArrayList<User> userList = new ArrayList<>();
            for(DataSnapshot snap : dataSnapshot.getChildren()){

                   User user = snap.getValue(User.class);
                   Log.i(TAG, user.toString());
                   userList.add(user);

            }
            listView.setAdapter(new ContactsListAdapter(SearchListActivity.this, userList, contactsList));
            if (userList.size() < 1){
                emptyView.setVisibility(View.VISIBLE);
            }else{
                emptyView.setVisibility(View.GONE);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void navigateToContacts() {

        if(isAuthorized){
            Intent intent = new Intent(LogInActivity.TO_CONTACTS_LIST);
            startActivity(intent);
        }else{
            alertDialogForSignUp();
        }
    }

    private void navigateToMyList() {
        if(isAuthorized){
            Intent intent = new Intent(LogInActivity.TO_USERS_LIST);
            startActivity(intent);
        }else{
            alertDialogForSignUp();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("search_bar",searchBar.getText().toString());
        outState.putSerializable("contacts", contactsList);
    }



    private  class ContactsListAdapter extends BaseAdapter{

        private static final int ID_CONSTANT = 0X0000001;
        final Context context;
        final ArrayList<User> contacts;
        ContactsListAdapter(Context context, ArrayList<User> contacts, HashMap<String, User> usersContacts){
            this.context = context;
            this.contacts = contacts;
        }
        class ViewHolder{
            //final ImageView imageView;
            final TextView textView;
            final ImageButton imageButton;
            final ImageButton imageButton_add;
            final TextView isContact_TextView;
            ViewHolder(View v){
               // this.imageView = (ImageView) v.findViewById(R.id.user_icon_IV);
                this.textView = (TextView) v.findViewById(R.id.username_searchCell);
                imageButton = (ImageButton) v.findViewById(R.id.message_button);
                imageButton_add = (ImageButton) v.findViewById(R.id.add_contact_button);
                isContact_TextView = (TextView) v.findViewById(R.id.isMyContact);
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
            final ViewHolder holder;

            try {
                if (_convertView == null) {
                    _convertView = LayoutInflater.from(context).inflate(R.layout.search_lists_cell_layout, _parent, false);
                    holder = new ContactsListAdapter.ViewHolder(_convertView);
                    _convertView.setTag(R.id.view,holder);

                } else {

                    holder = (ContactsListAdapter.ViewHolder) _convertView.getTag();
                }

                User contact = contacts.get(position);
                _convertView.setTag(R.id.object, contact);

                holder.textView.setText(contact.toString());
//                if (!contact.getProfilePicUrl().equals("")){
//                    Picasso.with(context).load(contact.getProfilePicUrl()).into(holder.imageView);
//                }
                holder.imageButton.setOnClickListener(mListener);
                holder.imageButton.setTag(contact);
                holder.imageButton.setFocusable(false);
                holder.imageButton.setFocusableInTouchMode(false);
                holder.imageButton_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch ((v.getId())){
                            case R.id.add_contact_button:
                                if (mAuth != null) {
                                    User user = (User) v.getTag();
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + uID + "/contacts");
                                    Map<String, Object> map = new HashMap<>();
                                    map.put(user.getId(), user);
                                    ref.updateChildren(map);
                                    v.setEnabled(false);
                                    v.setVisibility(View.INVISIBLE);
                                    holder.isContact_TextView.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(), user.toString() + getString(R.string._added_to_contacts), Toast.LENGTH_SHORT).show();                            return;

                                }else {
                                    alertDialogForSignUp();
                                }
                        }
                    }
                });
                holder.imageButton_add.setFocusable(false);
                holder.imageButton_add.setFocusableInTouchMode(false);
                holder.imageButton_add.setTag(contact);
               if(checkIfalreadyAContact(contact,contactsList)){
                   holder.imageButton_add.setEnabled(false);
                   holder.imageButton_add.setVisibility(View.INVISIBLE);
                   holder.isContact_TextView.setVisibility(View.VISIBLE);
               }

            }catch (Exception e){
                e.printStackTrace();
            }

            return _convertView;
        }

        final View.OnClickListener mListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.message_button:
                        if (mAuth != null) {
                            Intent intent = new Intent(ContactsListActivity.TO_MESSAGING_ACTION);
                            intent.putExtra("contact",(User) v.getTag());
                            startActivityForResult(intent, RESULT_CANCELED);
                        }
                        else {
                            alertDialogForSignUp();
                        }

                }

            }
        };
    }
    //sign out user on back pressed
    @Override
    public void onBackPressed() {
        if(isAuthorized) {
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
        private void alertDialogForSignUp(){
            final AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setTitle(getString(R.string.sign_up_for_wants));
            dialog.setMessage(getString(R.string.sign_up_offer_message));
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.sign_me_up), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(LogInActivity.TO_SIGN_UP);
                    startActivity(intent);
                }
            });
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.dont_want), new DialogInterface.OnClickListener() {
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

        private boolean checkIfalreadyAContact(User user, HashMap<String, User> usersContacts){

            return usersContacts.containsKey(user.getId());
        }


}
