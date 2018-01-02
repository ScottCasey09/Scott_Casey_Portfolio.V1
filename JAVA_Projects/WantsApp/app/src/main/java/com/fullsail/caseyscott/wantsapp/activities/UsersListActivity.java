// Casey Scott
// PAPVI - 1710
// UsersListActivity.java

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
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.fullsail.caseyscott.wantsapp.R;
import com.fullsail.caseyscott.wantsapp.objects.Items;
import com.fullsail.caseyscott.wantsapp.objects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class UsersListActivity extends AppCompatActivity {

    private static final String TO_ITEM_ENTRY = "com.fullsail.caseyscott.wantsapp.TO_ITEM_ENTRY";
    private static final String TAG = "UsersListActivity";
    private ListView listView;
    private Spinner spinner;
    private ArrayList<String> listNames;
    private FirebaseAuth mAuth;
    private String uID = "";
    private Switch privacySwitch;
    private HashMap<String, ArrayList<Items>> allListsAndItems;
    private HashMap<String, String> privacyIsList;
    private TextView emptyView;
    private ArrayList<User> contactsList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null){
            uID = mAuth.getCurrentUser().getUid();
        }

        setContentView(R.layout.users_list_screen);
        setTitle(getString(R.string.my_wants));

        emptyView = (TextView) findViewById(R.id.gifts_for_contact_EmptylistView);
        listView = (ListView) findViewById(R.id.listView);
        TextView textView = new TextView(this);
        textView.setText(R.string.no_wants);
        textView.setHeight(listView.getHeight());
        textView.setWidth(listView.getWidth());
        textView.setGravity(View.TEXT_ALIGNMENT_CENTER);
        listView.setEmptyView(textView);
        listView.setOnItemClickListener(mItemClickListener);
        findViewById(R.id.add_item_button).setOnClickListener(mClickListener);
        findViewById(R.id.barButton1).setOnClickListener(mClickListener);
        findViewById(R.id.barButton1).setEnabled(false);
        findViewById(R.id.barButton2).setOnClickListener(mClickListener);

        findViewById(R.id.barButton3).setOnClickListener(mClickListener);
        spinner = (Spinner) findViewById(R.id.list_selector_spinner);
        spinner.setOnItemSelectedListener(onItemSelectedListener);
        privacySwitch = (Switch) findViewById(R.id.privacy_switch);
        privacySwitch.setOnCheckedChangeListener(mCheckChangedListener);

        listNames = new ArrayList<>();
        allListsAndItems = new HashMap<>();
        privacyIsList = new HashMap<>();
        contactsList = new ArrayList<>();
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("users/"+uID);
        mRef.addListenerForSingleValueEvent(valueEventListener);
    }

    private final AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            (view.findViewById(R.id.itemDetailsView)).setVisibility(View.VISIBLE);
        }
    };

    //Listener for the toggle switch
    private final CompoundButton.OnCheckedChangeListener mCheckChangedListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switchPrivacy();
        }
    };
        //Switch method for privacy changes
        private void switchPrivacy() {
            if (spinner.getCount() != 0) {
                if (privacySwitch.isChecked()) {
                    privacySwitch.setText(getString(R.string._private));
                    removeListFromPublicDB(spinner.getSelectedItem().toString());
                    Toast.makeText(getApplicationContext(), R.string.list_is_public, Toast.LENGTH_SHORT).show();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + uID + "/lists/" + spinner.getSelectedItem().toString() + "/privacy");
                    ref.setValue("private");
                } else {
                    privacySwitch.setText(getString(R.string._public));
                    addListToPublicDB();
                    Toast.makeText(getApplicationContext(), R.string.list_is_private, Toast.LENGTH_SHORT).show();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + uID + "/lists/" + spinner.getSelectedItem().toString() + "/privacy");
                    ref.setValue("public");
                }
            }

        }

        //Method to add the list to the public database
        private void addListToPublicDB() {
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference ref = db.getReference("users/" + uID + "/lists");
            ref.addListenerForSingleValueEvent(addToPublicListener);
        }

        //Method to remove the selected from a public database
        private void removeListFromPublicDB(String s) {
            FirebaseDatabase db = FirebaseDatabase.getInstance();
            DatabaseReference ref_remove = db.getReference("public_lists/" + getUserName() + "/lists/" + s);
            ref_remove.setValue(null);
        }

    private final ValueEventListener addToPublicListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FirebaseDatabase db_public = FirebaseDatabase.getInstance();
                DatabaseReference ref_public = db_public.getReference("public_lists/" + getUserName() + "/lists/");
                ArrayList<Items> items = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.child(spinner.getSelectedItem().toString()).getChildren()) {
                    if (!snapshot.getKey().equals("privacy")) {
                        items.add(snapshot.getValue(Items.class));
                    }
                }

                ref_public.child(spinner.getSelectedItem().toString()).setValue(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        };

        private String username = "";

        private String getUserName() {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.addListenerForSingleValueEvent(getUsernameListener);
            Log.i(TAG, username);
            return username;
        }

        private final ValueEventListener getUsernameListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.child("users").child(uID).child("username").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
            }
        };

        private final AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (privacyIsList.containsKey(spinner.getSelectedItem().toString())) {
                    if (privacyIsList.get(spinner.getSelectedItem().toString()).equals("private")) {
                        privacySwitch.setChecked(true);
                        privacyIsList.put(spinner.getSelectedItem().toString(), "private");
                        removeListFromPublicDB(spinner.getSelectedItem().toString());
                    } else {
                        privacySwitch.setChecked(false);
                        privacyIsList.put(spinner.getSelectedItem().toString(), "public");
                        addListToPublicDB();
                    }

                    final String listName = spinner.getSelectedItem().toString();
                    listView.setAdapter(new ItemListAdapter(UsersListActivity.this, allListsAndItems.get(spinner.getSelectedItem().toString()), uID, listName));
                    if (allListsAndItems.get(spinner.getSelectedItem().toString()).size() < 1){
                        emptyView.setVisibility(View.VISIBLE);
                    }else{
                        emptyView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        private final ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listNames.clear();
                for (DataSnapshot snapshot : dataSnapshot.child("lists").getChildren()) {
                    listNames.add(snapshot.getKey());
                    ArrayList<Items> allItems = new ArrayList<>();
                    for(DataSnapshot snap : snapshot.getChildren()){
                        if(!snap.getKey().equals("privacy")) {
                            allItems.add(snap.getValue(Items.class));
                        }else{
                            privacyIsList.put(snapshot.getKey(), snap.getValue(String.class));
                        }
                    }
                    allListsAndItems.put(snapshot.getKey(), allItems);
                }
                spinner.setAdapter(new ArrayAdapter<>(UsersListActivity.this, android.R.layout.simple_list_item_1, listNames));

                for(DataSnapshot snapshot: dataSnapshot.child("contacts").getChildren()){
                    contactsList.add(snapshot.getValue(User.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e(TAG, databaseError.getMessage());
                Toast.makeText(getApplicationContext(), R.string.db_error, Toast.LENGTH_SHORT).show();
            }
        };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("users/"+uID);
        mRef.addListenerForSingleValueEvent(valueEventListener);
        if (resultCode == RESULT_OK){
            Bundle b = data.getExtras();

            int spinCount = spinner.getAdapter().getCount();
            if(spinCount-1 < b.getInt("spinner")){
                spinner.setSelection(spinCount-1);
            }else {
                spinner.setSelection(b.getInt("spinner", 0));
            }
            if(data.hasExtra("new_list_name")) {
                String name = b.getString("new_list_name");
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/" + uID + "/lists/" + name + "/privacy");
                ref.setValue("public");
            }
        }

    }
    private final View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()){
                case R.id.barButton3:
                    navigateToSearchList();
                    return;
                case R.id.barButton2:
                    navigateToContacts();
                    return;
                case R.id.add_item_button:
                    Intent intent = new Intent(TO_ITEM_ENTRY);
                    if (spinner.getSelectedItem() != null) {
                        intent.putExtra("list_name", spinner.getSelectedItemPosition());
                        intent.putExtra("list_names", listNames);
                    }
                    startActivityForResult(intent, 1);
            }
        }
    };

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

    private void navigateToContacts() {

        if(mAuth != null){
            Intent intent = new Intent(LogInActivity.TO_CONTACTS_LIST);
            intent.putExtra("contacts", contactsList);
            startActivity(intent);
        }
    }

    private void navigateToSearchList() {
        if(mAuth != null){
            Intent intent = new Intent(LogInActivity.TO_SEARCH_LIST);
            intent.putExtra("contacts", contactsList);
            startActivity(intent);
        }
    }
    //sign out user on back pressed
    @Override
    public void onBackPressed() {
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setTitle(getString(R.string.sign_out_qmark));
        dialog.setMessage(getString(R.string.back_button_message));
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), getString(R.string.signed_out) ,Toast.LENGTH_SHORT).show();
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

    }

    //Inner class for the listView so I can update the list when the delete button is selected and an item is deleted
    private class ItemListAdapter extends BaseAdapter {
        private static final long ID_CONSTANT = 0x01000000;//0x010101010L
        final ArrayList<Items> items;
        final String uID;
        final String listName;
        final Context context;
        ItemListAdapter(Context context, ArrayList<Items> list, String uID, String listName){
            this.items = list;
            this.context = context;
            this.uID = uID;
            this.listName = listName;
        }

        private class ViewHolder{

            final TextView itemName;
            final TextView itemPrice;
            final ImageButton editButton;
            final ImageButton deleteButton;
            final LinearLayout locat_layout;
            final LinearLayout desc_layout;
            final ImageButton closeButton;
            final TextView detailsPriority;
            final LinearLayout mainDetailsLayout;

            ViewHolder(View v){
                itemName = (TextView) v.findViewById(R.id.name_cell);
                itemPrice = (TextView) v.findViewById(R.id.price_cell);
                editButton = (ImageButton) v.findViewById(R.id.editButton);
                deleteButton = (ImageButton) v.findViewById(R.id.deleteButton);
                locat_layout = (LinearLayout) v.findViewById(R.id.locations_layout);
                desc_layout = (LinearLayout) v.findViewById(R.id.extraInfo_layout);
                closeButton = (ImageButton) v.findViewById(R.id.closeButton);
                detailsPriority = (TextView) v.findViewById(R.id.priority_level_TextView);
                mainDetailsLayout = (LinearLayout) v.findViewById(R.id.itemDetailsView);
            }
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return ID_CONSTANT + position;
        }

        @Override
        public View getView(int _position, View _convertView, ViewGroup _parent) {

            //Variable for the view holder and used to recycle views
            final ViewHolder holder;

            try {
                if (_convertView == null) {
                    _convertView = LayoutInflater.from(context).inflate(R.layout.users_list_cell_layout, _parent, false);
                    holder = new ViewHolder(_convertView);
                    _convertView.setTag(holder);

                } else {

                    holder = (ViewHolder) _convertView.getTag();
                }

                Items item = items.get(_position);
                TextView textView = new TextView(context);
                textView.setText(item.getLocations());
                textView.setPadding(40, 8, 0, 8);
                textView.setTextSize(14);
                holder.locat_layout.addView(textView);
                textView = new TextView(context);
                textView.setPadding(40, 8, 0, 8);
                textView.setTextSize(14);
                textView.setText(item.getDescription());
                holder.desc_layout.addView(textView);
                holder.itemName.setText(item.getName());
                String price;
                if (!item.getPrice().equals(context.getString(R.string.unknown))) {
                    price = context.getString(R.string.price)+ " " + context.getString(R.string._$) + item.getPrice();
                } else {
                    price = context.getString(R.string.price) +" "+ item.getPrice();
                }
                holder.itemPrice.setText(price);
                holder.editButton.setOnClickListener(mListener);
                holder.deleteButton.setTag(R.id.object, item);
                holder.editButton.setTag(item);
                holder.editButton.setFocusable(false);
                holder.editButton.setFocusableInTouchMode(false);
                holder.deleteButton.setFocusableInTouchMode(false);
                holder.deleteButton.setFocusable(false);
                holder.deleteButton.setOnClickListener(mListener);
                holder.detailsPriority.setText(getTextForPriorityLevel(item.getPriority_level()));
                holder.closeButton.setFocusable(true);
                holder.closeButton.setFocusableInTouchMode(true);
                holder.closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.mainDetailsLayout.setVisibility(View.GONE);
                    }
                });

            }catch (Exception e){
                e.printStackTrace();
            }

            return _convertView;
        }
        private String getTextForPriorityLevel(int i) {

            switch (i) {
                case 1:
                    return context.getString(R.string.kinda_want);
                case 2:
                    return context.getString(R.string.kinda_want_plus);
                case 3:
                    return context.getString(R.string.want);
                case 4:
                    return context.getString(R.string.want_plus);
                case 5:
                    return context.getString(R.string.really_want);
            }
            return context.getString(R.string.want);
        }

        final View.OnClickListener mListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (v.getId()){
                    case R.id.editButton:
                        Intent intent = new Intent(UsersListActivity.TO_ITEM_ENTRY);
                        intent.putExtra("item", (Serializable) v.getTag());
                        if (spinner.getSelectedItem() != null) {
                            intent.putExtra("list_name", spinner.getSelectedItemPosition());
                            intent.putExtra("list_names", listNames);
                        }
                        context.startActivity(intent);
                        return;
                    case R.id.deleteButton:
                        Toast.makeText(UsersListActivity.this, "Delete Button",Toast.LENGTH_SHORT).show();
                        final Items item = (Items) v.getTag(R.id.object);
                        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                        alertDialog.setMessage(getString(R.string.sure_to_remove_message));
                        alertDialog.setTitle(getString(R.string.caution));
                        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.dont_want),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(context,item+" is Deleted", Toast.LENGTH_SHORT).show();
                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users/"+uID+"/lists/"+listName+"/"+item.getId());
                                        ref.removeValue();
                                        if(!privacySwitch.isChecked()){
                                            privacySwitch.setChecked(true);
                                            privacySwitch.setChecked(false);
                                        }
                                        final int i = spinner.getSelectedItemPosition();
                                        final int spinnerCount = spinner.getAdapter().getCount();
                                        final String selectedList = spinner.getSelectedItem().toString();
                                        recreate();
                                        if (spinner.getAdapter().getCount() < spinnerCount){
                                            removeListFromPublicDB(selectedList);
                                        }
                                        spinner.setSelection(i);
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.i_want),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog1) {
                                alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getColor(R.color.colorPrimaryDark));
                                alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getColor(R.color.colorPrimaryDark));
                            }
                        });
                        alertDialog.show();


                }


            }
        };

    }


}
