// Casey Scott
// PAPVI - 1710
// AddItemActivity.java

package com.fullsail.caseyscott.wantsapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import com.fullsail.caseyscott.wantsapp.objects.Items;
import com.fullsail.caseyscott.wantsapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AddItemActivity extends AppCompatActivity {

    //private static final String TAG = "AddItemActivity";
    private EditText title;
    private EditText location;
    private EditText desc;
    private EditText price;
    private EditText listName;
    private RadioGroup priority_group;
    private String titleInput;
    private int priorityInput;
    private Spinner spinner;
    private LinearLayout linearLayout;
    private Items itemToBeEdited;
    private boolean isEditing = false;
    private String uID;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_entry_screen);
        setTitle(getString(R.string.new_wants));

        Button addButton = (Button) findViewById(R.id.add_button);
        addButton.setOnClickListener(mOnClickListener);
        title = (EditText) findViewById(R.id.itemName);
        location = (EditText) findViewById(R.id.itemLocations);
        desc = (EditText) findViewById(R.id.itemDescription);
        price = (EditText) findViewById(R.id.itemPrice);
        priority_group = (RadioGroup) findViewById(R.id.radioGroup);
        priority_group.setOnCheckedChangeListener(mCheckChangedListener);
        priority_group.check(R.id.radio_5);
        spinner = (Spinner) findViewById(R.id.spinner_entry_screen);
        listName = (EditText) findViewById(R.id.listName);
        linearLayout = (LinearLayout) findViewById(R.id.newList_LinearLayout);
        linearLayout.removeAllViews();
        spinner.setOnItemSelectedListener(selectedItemListener);
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser()!=null){
            uID = mAuth.getCurrentUser().getUid();
        }
        priorityInput = 5;

        //Get the item to be edited if exists
        Bundle b = getIntent().getExtras();
        if(getIntent().hasExtra("item")) {
            if (b.containsKey("item")) {
                itemToBeEdited = (Items) b.get("item");
                if (itemToBeEdited != null) {
                    isEditing = true;

                    //populate the fields
                    title.setText(itemToBeEdited.getName());
                    price.setText(itemToBeEdited.getPrice());
                    desc.setText(itemToBeEdited.getDescription());
                    location.setText(itemToBeEdited.getLocations());
                    setPriorityLevel(itemToBeEdited.getPriority_level());
                }
            }
        }
        if(getIntent().hasExtra("list_names")){
            ArrayList<String> listNames;
            listNames = b.getStringArrayList("list_names");
            if (listNames != null) {
                listNames.add(getString(R.string.new_list));
                spinner.setAdapter(new ArrayAdapter<>(AddItemActivity.this, android.R.layout.simple_list_item_1, listNames));
            }

        }else{
            String[] newList = new String[]{getString(R.string.new_list)};
            spinner.setAdapter(new ArrayAdapter<>(AddItemActivity.this, android.R.layout.simple_list_item_1, newList));

        }
        if(getIntent().hasExtra("list_name")){
            int pos = b.getInt("list_name");
                spinner.setSelection(pos);
        }


        if(savedInstanceState != null){
            listName.setText(savedInstanceState.getString("list_name"));
            title.setText(savedInstanceState.getString("item_name"));
            location.setText(savedInstanceState.getString("item_locat"));
            desc.setText(savedInstanceState.getString("item_desc"));
            setPriorityLevel(savedInstanceState.getInt("item_priority"));
        }

    }

        private final AdapterView.OnItemSelectedListener selectedItemListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (Objects.equals(parent.getSelectedItem().toString(), getString(R.string.new_list))) {
                    linearLayout.addView(listName);
                } else {
                    linearLayout.removeAllViews();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (v.getId() == R.id.add_button) {
                    if (checkTitleInput()) {
                        String locatInput;
                        if (location.getText().toString().trim().equals("")) {
                            locatInput = getString(R.string.unknown);
                        } else {
                            locatInput = location.getText().toString().trim();
                        }
                        String descInput;
                        if (desc.getText().toString().trim().equals("")) {
                            descInput = getString(R.string.unknown);
                        } else {
                            descInput = desc.getText().toString().trim();
                        }
                        String priceInput;
                        if (price.getText().toString().trim().equals("")) {
                            priceInput = getString(R.string.unknown);
                        } else {
                            StringBuilder builder = new StringBuilder();
                            priceInput = price.getText().toString().trim();
                            int decimalCount = 0;
                            for(char c : priceInput.toCharArray()){
                                if(c=='.'){
                                    decimalCount+=1;
                                }
                                if(decimalCount != 2){
                                    builder.append(c);
                                }
                            }
                            priceInput = String.format(Locale.US, "%.2f", Double.parseDouble(builder.toString()));
                        }
                        String id = "";
                        Items item = new Items(titleInput, locatInput, descInput, priceInput, priorityInput, id);

                        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference mRef = firebaseDatabase.getReference("users/"+ uID + "/lists/");

                        String selectedList;
                        if (listName.isAttachedToWindow()) {
                            selectedList = listName.getText().toString().trim();
                        } else {
                            selectedList = spinner.getSelectedItem().toString();
                        }

                        //IF Updating item po NOT PUSH() just grab the unique key and update that entry
                        Intent intent = new Intent();
                        if (isEditing) {
                            item.setId(itemToBeEdited.getId());
                            Map<String, Object> map = new HashMap<>();
                            map.put(item.getId(), item);
                            mRef.child(selectedList).updateChildren(map);
                            Intent intent1 = new Intent(LogInActivity.TO_USERS_LIST);
                            startActivity(intent1);
                        } else {
                            mRef.child(selectedList).push().setValue(item);
                            intent.putExtra("new_list_name", listName.getText().toString().trim());
                            intent.putExtra("spinner", spinner.getSelectedItemPosition());
                            setResult(RESULT_OK, intent);
                            finish();
                        }


                    }
                }
            }
        };

        private final RadioGroup.OnCheckedChangeListener mCheckChangedListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {

                if (group.getId() == R.id.radioGroup) {

                    switch (checkedId) {
                        case R.id.radio_1:
                            priorityInput = 1;
                            return;
                        case R.id.radio_2:
                            priorityInput = 2;
                            return;
                        case R.id.radio_3:
                            priorityInput = 3;
                            return;
                        case R.id.radio_4:
                            priorityInput = 4;
                            return;
                        case R.id.radio_5:
                            priorityInput = 5;
                    }
                }
            }
        };

    private boolean checkTitleInput() {

        if (title.getText().toString().trim().equals("")) {
            title.setError(getString(R.string.need_name));
            return false;
        }
        titleInput = title.getText().toString().trim();
        return true;
    }

    private void setPriorityLevel(int level) {
        switch (level) {
            case 1:
                priority_group.check(R.id.radio_1);
                return;
            case 2:
                priority_group.check(R.id.radio_2);
                return;
            case 3:
                priority_group.check(R.id.radio_3);
                return;
            case 4:
                priority_group.check(R.id.radio_4);
                return;
            case 5:
                priority_group.check(R.id.radio_5);
        }
    }

    private int getPriorityLevel(int id){

        switch (id){
            case R.id.radio_1:
                return 1;
            case R.id.radio_2:
                return 2;
            case R.id.radio_3:
                return 3;
            case R.id.radio_4:
                return 4;
            case  R.id.radio_5:
                return 5;
        }

        return 1;
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

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

        outState.putString("item_name", title.getText().toString());
        outState.putString("item_locat", location.getText().toString());
        outState.putString("item_desc", desc.getText().toString());
        outState.putInt("item_priority",getPriorityLevel(priority_group.getCheckedRadioButtonId()));
        outState.putString("list_name", listName.getText().toString());
    }
}
