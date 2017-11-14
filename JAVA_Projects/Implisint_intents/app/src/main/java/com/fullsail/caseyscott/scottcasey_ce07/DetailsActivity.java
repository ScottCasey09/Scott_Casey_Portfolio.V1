// Casey Scott
// AID - 1705
// DetailsActivity.JAVA

package com.fullsail.caseyscott.scottcasey_ce07;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.fullsail.caseyscott.scottcasey_ce07.Objects.Person;
import com.fullsail.caseyscott.scottcasey_ce07.fragments.DetailsFragment;

public class DetailsActivity extends AppCompatActivity implements DetailsFragment.Listener{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

       getFragmentManager().beginTransaction()
               .replace(R.id.details_content, DetailsFragment.newInstance(getIntent().getExtras()))
               .commit();



    }


    @Override
    public void deletePerson(Person person) {

        Intent deleteIntent = new Intent();

        deleteIntent.putExtra("com.fullsail.android.jav2ce08.EXTRA_STRING_FIRST_NAME", person.getFirstName());
        deleteIntent.putExtra("com.fullsail.android.jav2ce08.EXTRA_STRING_LAST_NAME", person.getLastName());
        deleteIntent.putExtra("com.fullsail.android.jav2ce08.EXTRA_INT_AGE", Integer.parseInt(person.getAge()));
        setResult(RESULT_OK, deleteIntent);
        finish();

    }
}
