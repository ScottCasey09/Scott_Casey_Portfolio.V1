// Casey Scott
// PAPVI - 1710
// GiftsListsForContact.java

package com.fullsail.caseyscott.wantsapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.fullsail.caseyscott.wantsapp.R;
import com.fullsail.caseyscott.wantsapp.adapter.GiftsItemsListAdapter;
import com.fullsail.caseyscott.wantsapp.objects.Items;
import com.fullsail.caseyscott.wantsapp.objects.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;


public class GiftsListsForContact extends AppCompatActivity {

    public static final String TO_CONTACTS_ITEMS = "com.fullsail.caseyscott.wantsapp.TO_CONTACTS_ITEMS";
    private static final String TAG = "GiftsListsForContact";

    private ListView listView;
    private Spinner listNamesSpinner;
    private HashMap<String, ArrayList<Items>> allListsWithItems;
    private FirebaseAuth mAuth;
    private TextView emptyView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gift_lists_for_contact);
        User user = (User) getIntent().getSerializableExtra("user");
        setTitle("Wants for " + user.getFirstName());
        listView = (ListView) findViewById(R.id.gifts_for_contact_listView);
        listNamesSpinner = (Spinner) findViewById(R.id.sort_spinner);
        allListsWithItems = new HashMap<>();
        listNamesSpinner.setOnItemSelectedListener(spinnerListener);
        listView.setOnItemClickListener(mItemClickListener);
        emptyView = (TextView) findViewById(R.id.gifts_for_contact_EmptylistView);
        mAuth = FirebaseAuth.getInstance();
        Query ref;
        if (mAuth != null) {
            ref = FirebaseDatabase.getInstance().getReference("users/" + user.getId()+"/lists").orderByChild("priority");
        }else {
            ref = FirebaseDatabase.getInstance().getReference("public_lists/" + user.getUsername()+"/lists").orderByChild("priority");
        }
        ref.addListenerForSingleValueEvent(getListsValueEventListener);

    }

    private final AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            (view.findViewById(R.id.itemDetailsView)).setVisibility(View.VISIBLE);
        }
    };

    private final ValueEventListener getListsValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            ArrayList<String> listNames = new ArrayList<>();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                listNames.add(snapshot.getKey());
                ArrayList<Items> listOfItems = new ArrayList<>();
                for (DataSnapshot shot : snapshot.getChildren()) {

                    if (!Objects.equals(shot.getKey(), "privacy")) {
                        listOfItems.add(shot.getValue(Items.class));
                        allListsWithItems.put(snapshot.getKey(), listOfItems);
                    }
                }
            }
            //Set the adapter for the spinner
            listNamesSpinner.setAdapter(new ArrayAdapter<>(GiftsListsForContact.this, android.R.layout.simple_list_item_1, listNames));
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.e(TAG, databaseError.getDetails());
        }
    };
    private final AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            //Set the adapter for the list based on which item is selected
            sortByPriority("H");
            listView.setAdapter(new GiftsItemsListAdapter(GiftsListsForContact.this, allListsWithItems.get(listNamesSpinner.getSelectedItem().toString())));

            if (allListsWithItems.get(listNamesSpinner.getSelectedItem().toString()).size() < 1){
                emptyView.setVisibility(View.VISIBLE);
            }else{
                emptyView.setVisibility(View.GONE);
            }
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sort_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.sortMenu){

                AlertDialog.Builder dialog = new AlertDialog.Builder(GiftsListsForContact.this);
                dialog.setTitle("Select filter");
                dialog.setItems(R.array.sort_array, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      switch (which){
                          case 0:
                              sortByPriority("H");
                              Log.i(TAG, "Priority");
                              return;
                          case 1:
                              sortByPriority("L");
                              Log.i(TAG, "Priority");
                              return;
                          case 2:
                              sortByTitle("H");
                              Log.i(TAG, "Title");
                              return;
                          case 3:
                              sortByTitle("L");
                              Log.i(TAG, "Title");
                              return;
                          case 4:
                              sortByPrice("H");
                              Log.i(TAG, "Price");
                              return;
                          case 5:
                              sortByPrice("L");
                              Log.i(TAG, "Price");

                      }
                    }
                }).show();

        }else if (item.getItemId() == R.id.signOut_option){
            signOutUserAndReturnToLogin();
        }

        return super.onOptionsItemSelected(item);
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

    private void sortByPrice(final String s) {
        String listName = listNamesSpinner.getSelectedItem().toString();
        ArrayList<Items> allItems = allListsWithItems.get(listName);
        Items[] itemsArray = allItems.toArray(new Items[allItems.size()]);
        Arrays.sort(itemsArray, new Comparator<Items>() {
            @Override
            public int compare(Items o1, Items o2) {
                if(s.equals("H")) {
                    if (!o1.getPrice().equals("Unknown") && !o2.getPrice().equals("Unknown")) {
                        return (int) (Math.round(Double.parseDouble(o2.getPrice())) - Math.round(Double.parseDouble(o1.getPrice())));
                    }
                }
                if (!o1.getPrice().equals("Unknown") && !o2.getPrice().equals("Unknown")) {
                    return (int) (Math.round(Double.parseDouble(o1.getPrice())) - Math.round(Double.parseDouble(o2.getPrice())));
                }
                return o1.getPrice().compareTo(o2.getPrice());
            }
        });
        ArrayList<Items> arrayList = new ArrayList<>();
        Collections.addAll(arrayList, itemsArray);
        listView.setAdapter(new GiftsItemsListAdapter(GiftsListsForContact.this, arrayList));
    }

    private void sortByTitle(final String s) {
        String listName = listNamesSpinner.getSelectedItem().toString();
        ArrayList<Items> allItems = allListsWithItems.get(listName);
        Items[] itemsArray = allItems.toArray(new Items[allItems.size()]);
        Arrays.sort(itemsArray, new Comparator<Items>() {
            @Override
            public int compare(Items o1, Items o2) {
                if(s.equals("H")) {
                    return o2.getName().compareTo(o1.getName());
                }else{
                    return o1.getName().compareTo(o2.getName());
                }
            }
        });
        ArrayList<Items> arrayList = new ArrayList<>();
        Collections.addAll(arrayList, itemsArray);
        listView.setAdapter(new GiftsItemsListAdapter(GiftsListsForContact.this, arrayList));
    }

    private void sortByPriority(final String s) {
        String listName = listNamesSpinner.getSelectedItem().toString();
        ArrayList<Items> allItems = allListsWithItems.get(listName);
        Items[] itemsArray = allItems.toArray(new Items[allItems.size()]);
        Arrays.sort(itemsArray, new Comparator<Items>() {
            @Override
            public int compare(Items o1, Items o2) {
                if(s.equals("H")) {
                    return o2.getPriority_level().compareTo(o1.getPriority_level());
                }else{
                    return o1.getPriority_level().compareTo(o2.getPriority_level());
                }
            }
        });
        ArrayList<Items> arrayList = new ArrayList<>();
        Collections.addAll(arrayList, itemsArray);
        listView.setAdapter(new GiftsItemsListAdapter(GiftsListsForContact.this, arrayList));
    }


}
