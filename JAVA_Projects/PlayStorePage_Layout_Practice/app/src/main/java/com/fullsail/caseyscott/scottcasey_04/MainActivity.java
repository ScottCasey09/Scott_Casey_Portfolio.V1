// Casey Scott
// AID - 1705
// MainActivity.java
package com.fullsail.caseyscott.scottcasey_04;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout reviewsLayout = (LinearLayout) findViewById(R.id.reviewsLinearLayout);
        ViewAdapter adapter = new ViewAdapter(this, reviews());

        for (int i = 0; i < reviews().size(); i++) {
            View v = adapter.getView(i, null, reviewsLayout);

            reviewsLayout.addView(v);
        }
        ArrayList<String> list = new ArrayList<>();
        list.add("\u2022 Updated look");
        list.add("\u2022 Bug fixes");
        list.add("\u2022 Added features");
        list.add("\u2022 New navigation feel");
        list.add("\u2022 Drag and drop");
        LinearLayout whatsNewLayout = (LinearLayout) findViewById(R.id.whatsNewSection);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);


        for (int i = 0; i < list.size(); i++) {
            View v = arrayAdapter.getView(i, null, whatsNewLayout);

            whatsNewLayout.addView(v);
        }
        //Hide nav and status bar
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);

    }

    private ArrayList<Reviews> reviews(){

        ArrayList<Reviews> reviewsList = new ArrayList<>();
        String reviewStr1 = getString(R.string.rev_body_1);
        Drawable avatar1 = getDrawable(R.drawable.boy_1);
        String date1 = getString(R.string.rev_date_1);
        RatingBar rb1 = new RatingBar(this);
        rb1.setRating(4f);
        String title1 = getString(R.string.rev_title_1);
        Reviews review1 = new Reviews(reviewStr1, avatar1, date1, rb1, title1 );

        String reviewStr2 = getString(R.string.rev_body_2);
        Drawable avatar2 = getDrawable(R.drawable.man_1);
        String date2 = getString(R.string.rev_date_2);
        RatingBar rb2 = new RatingBar(this);
        rb2.setRating(5f);
        String title2 = getString(R.string.rev_title_2);
        Reviews review2 = new Reviews(reviewStr2, avatar2, date2, rb2, title2 );

        String reviewStr3 = getString(R.string.rev_body_3);
        Drawable avatar3 = getDrawable(R.drawable.girl);
        String date3 = getString(R.string.rev_date_3);
        RatingBar rb3 = new RatingBar(this);
        rb3.setRating(3f);
        String title3 = getString(R.string.rev_title_3);
        Reviews review3 = new Reviews(reviewStr3, avatar3, date3, rb3, title3 );

        String reviewStr4 = getString(R.string.rev_body_4);
        Drawable avatar4 = getDrawable(R.drawable.man_w);
        String date4 = getString(R.string.rev_date_4);
        RatingBar rb4 = new RatingBar(this);
        rb4.setRating(2f);
        String title4 = getString(R.string.rev_title_4);
        Reviews review4 = new Reviews(reviewStr4, avatar4, date4, rb4, title4 );

        String reviewStr5 = getString(R.string.rev_body_5);
        Drawable avatar5 = getDrawable(R.drawable.girl_1);
        String date5 = getString(R.string.rev_date_5);
        RatingBar rb5 = new RatingBar(this);
        rb5.setRating(1f);
        String title5 = getString(R.string.rev_title_5);
        Reviews review5 = new Reviews(reviewStr5, avatar5, date5, rb5, title5);

        reviewsList.add(review1);
        reviewsList.add(review2);
        reviewsList.add(review3);
        reviewsList.add(review4);
        reviewsList.add(review5);
        return reviewsList;
    }

}
