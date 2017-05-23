package com.fullsail.caseyscott.actionbardemo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fullsail.caseyscott.actionbardemo.fragments.FormFragment;
import com.fullsail.caseyscott.actionbardemo.fragments.MainFragment;
import com.fullsail.caseyscott.actionbardemo.objects.Contact;

public class MainActivity extends AppCompatActivity implements MainFragment.Listener, FormFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, MainFragment.newInstance()).commit();
    }



    @Override
    public void openForm() {
        getFragmentManager()
                .beginTransaction().addToBackStack("form")
                .setCustomAnimations(R.animator.slide_in_left, 0, 0,R.animator.slide_out_right)
                .replace(R.id.fragmentContainer, FormFragment.newInstance()).commit();
    }

    @Override
    public void addContact(Contact contact) {
        getFragmentManager().popBackStack();

        Toast.makeText(
                this,
                contact.getFirstName() + " " + contact.getLastName() + " has been added.",
                Toast.LENGTH_LONG
        ).show();
    }
}