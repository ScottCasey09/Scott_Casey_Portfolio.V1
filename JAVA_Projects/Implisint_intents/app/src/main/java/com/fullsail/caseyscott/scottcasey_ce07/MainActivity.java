// Casey Scott
// AID - 1705
// MainActivity.JAVA

package com.fullsail.caseyscott.scottcasey_ce07;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;

import com.fullsail.caseyscott.scottcasey_ce07.Objects.Person;
import com.fullsail.caseyscott.scottcasey_ce07.fragments.FormFragment;

import java.util.Objects;


public class MainActivity extends AppCompatActivity implements FormFragment.Listener{


    private static final String TAG = "MainActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        getFragmentManager().beginTransaction()
                .replace(R.id.main_content, FormFragment.newInstance(),FormFragment.TAG)
                .commit();

        Log.e(TAG, "onCreate() executed");

    }

    @Override
    public void addPerson(Person person) {

        Log.e(TAG, "addPerson() interface method executed");


         if (checkInputs()) {

             Log.e(TAG, "checkInputs = " + checkInputs().toString());
             Intent shareIntent = new Intent();

             shareIntent.putExtra("com.fullsail.android.jav2ce08.EXTRA_STRING_FIRST_NAME", person.getFirstName());
             shareIntent.putExtra("com.fullsail.android.jav2ce08.EXTRA_STRING_LAST_NAME", person.getLastName());
             shareIntent.putExtra("com.fullsail.android.jav2ce08.EXTRA_INT_AGE", Integer.parseInt(person.getAge()));

             setResult(RESULT_OK, shareIntent);
             finish();
         }


    }

    private Boolean checkInputs(){

        EditText firstName = (EditText) findViewById(R.id.firstName);
        EditText lastName = (EditText) findViewById(R.id.lastname);
        EditText age = (EditText) findViewById(R.id.age);

        if(Objects.equals(firstName.getText().toString(), "")){
            firstName.setError("Enter first name!");
        }
        if(Objects.equals(lastName.getText().toString(), "")){
            lastName.setError("Enter last name!");
        }
        if(Objects.equals(age.getText().toString(), "")){
            age.setError("Enter age!");
        }

        return !(Objects.equals(age.getText().toString(), "") || Objects.equals(lastName.getText().toString(), "") || Objects.equals(firstName.getText().toString(), ""));
    }
}
