// Casey Scott
// JAV1 - 1704
// MainActivity.java
package com.fullsail.caseyscott.scottcasey_ce06;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    //Properties of the activity
    private ArrayList<Person> people;
    private ListView listView;
    private GridView gridView;
    private SimpleAdapter simpleAdapter;
    private ArrayAdapter<Person> arrayAdapter;
    private ViewAdapter listBaseAdapter;
    private ViewAdapter gridBaseAdapter;


    //Create method / Happens 1st
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instantiate the people collection of Person objects
        people = new ArrayList<>();

        //Assign the UI elements a variable for reference
        listView = (ListView) findViewById(R.id.list_view_layout);
        Spinner listSpinner = (Spinner) findViewById(R.id.spinner);
        Spinner adapterSpinner = (Spinner) findViewById(R.id.spinner2);
        gridView = (GridView) findViewById(R.id.grid_view_layout);

        //Set the list/grid listeners for the click events
        listView.setOnItemClickListener(mListClickListener);
        gridView.setOnItemClickListener(mListClickListener);

        //Set the listeners fir the spinners
        adapterSpinner.setOnItemSelectedListener(mSelectedListener);
        listSpinner.setOnItemSelectedListener(mSelectedListener);
        people.addAll(addPeople());

        //Set the grid view to be gone by default
        gridView.setVisibility(View.GONE);

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, people);
        //Set the arrayAdapter to be the default adapter
        setArrayAdapter();

        //Field identifiers for the map keys
        final String keyName = "name";
        final String keybday = "bday";

        //A list of hashmaps
        ArrayList<HashMap<String, String>> dataCollection = new ArrayList<>();

        for (Person p : people){

            //Make a new hash map for the person object
            HashMap<String, String> map = new HashMap<>();

            //Match the person data with the matching key
            map.put(keyName, p.toString());
            map.put(keybday, p.birthdate);

            dataCollection.add(map);
        }
        //String array for the map keys
        String[] keys = new String[]{

                keyName,
                keybday
        };
        //Array for the view ids
        int[] vewIDs = {

                R.id.name_view,
                R.id.date_view
        };
        //Instantiate a Simple Adapter Object
        simpleAdapter = new SimpleAdapter(this, dataCollection,R.layout.simple_adapter_list, keys,vewIDs);

        listBaseAdapter = new ViewAdapter(this, people, R.layout.list_view_layout);
        gridBaseAdapter = new ViewAdapter(this,people, R.layout.grid_view_layout);

    }

    //ONClickListener for when an item is selected  in the list or gird views to chow the information
    private final AdapterView.OnItemClickListener mListClickListener = new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        //Alert Dialog for displaying the relative information of the selected item
           AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
        TextView myMessage = new TextView(MainActivity.this);
        myMessage.setText(people.get(position).getBirthdate());
        myMessage.setGravity(Gravity.CENTER_HORIZONTAL);
        myMessage.setTextSize(20.00f);
        myMessage.setPadding(30,30,30,30);
        adb.setView(myMessage);
                    adb.setTitle(people.get(position).toString());
                    adb.setIcon(people.get(position).getPicture());

                   adb .setPositiveButton("Back",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int whichButton) {
                                    dialog.dismiss();
                                }
                            });

                   adb.create();
                   adb.show();
        }

};
        //OnClick listener for when the spinner values change
    private final AdapterView.OnItemSelectedListener mSelectedListener = new AdapterView.OnItemSelectedListener() {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selectedItem = parent.getSelectedItem().toString().toLowerCase();

            switch (selectedItem){
                case "list view":
                    gridView.setVisibility(View.GONE);
                    listView.setVisibility(View.VISIBLE);
                    setAdapter(selectedItem);
                    break;
                case "grid view":
                    listView.setVisibility(View.GONE);
                    gridView.setVisibility(View.VISIBLE);
                    setAdapter(selectedItem);
                    break;
                case "array adapter":
                    setArrayAdapter();
                    break;
                case "simple adapter":
                    setSimpleAdapter();
                    break;
                case "base adapter":
                    setBaseAdapter();
                    break;

            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            //No code to implement
        }
    };

     //Method to add all of the person objects to the arrayList(people)
    private ArrayList<Person> addPeople(){

        Person p1 = new Person("John","Doe","1/25/1986", getResources().getDrawable(R.mipmap.afroboy, null));
        Person p2 = new Person("Jane","Doe","4/02/1959", getResources().getDrawable(R.mipmap.girl, null));
        Person p3 = new Person("Pat","Johnson","7/30/1997", getResources().getDrawable(R.mipmap.boy, null));
        Person p4 = new Person("Kelly","Klane","9/17/1989", getResources().getDrawable(R.mipmap.brownhairgirl, null));
        Person p5 = new Person("Timmy","Toppers","6/25/1954", getResources().getDrawable(R.mipmap.man, null));
        Person p6 = new Person("James","Casey","8/24/1987", getResources().getDrawable(R.mipmap.man_w, null));
        Person p7 = new Person("Art","Maker","1/02/1974", getResources().getDrawable(R.mipmap.man_x, null));
        Person p8 = new Person("Ben","Thang","4/27/1968", getResources().getDrawable(R.mipmap.man_y, null));
        Person p9 = new Person("Frank","Gore","3/18/1981", getResources().getDrawable(R.mipmap.man_z, null));
        Person p10 = new Person("Annita","Powers","5/05/1985", getResources().getDrawable(R.mipmap.girl, null));
        ArrayList<Person> p = new ArrayList<>();
        p.add(p1);
        p.add(p2);
        p.add(p3);
        p.add(p4);
        p.add(p5);
        p.add(p6);
        p.add(p7);
        p.add(p8);
        p.add(p9);
        p.add(p10);
        return p;
    }

    //Method to set the adapter to an ArrayAdapter
    private void setArrayAdapter() {


        //Assign the adapter to the views
        listView.setAdapter(arrayAdapter);
        gridView.setAdapter(arrayAdapter);

    }

    //Method to set the simple adapter
    private void  setSimpleAdapter(){

        //Assign the adapter to the views
        listView.setAdapter(simpleAdapter);
        gridView.setAdapter(simpleAdapter);
    }
    //Method to set the Base Adapter
    private void setBaseAdapter(){

        //Assign the adapter to the views
       listView.setAdapter(listBaseAdapter);
        gridView.setAdapter(gridBaseAdapter);
    }

    //Method for setting the adapters for the selected spinner list item
    private void setAdapter(String spinnerValue){

        switch (spinnerValue){
            case "array adapter":
                setArrayAdapter();
                break;
            case "simple adapter":
                setSimpleAdapter();
                break;
            case "base adapter":
                setBaseAdapter();
                break;
        }
    }
}
