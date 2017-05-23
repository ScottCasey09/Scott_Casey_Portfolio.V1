// Casey Scott
// AID - 1705
// Reviews.java

package com.fullsail.caseyscott.scottcasey_04;


import android.graphics.drawable.Drawable;
import android.widget.RatingBar;

class Reviews {

    public Reviews(String review, Drawable avatar, String date, RatingBar rating, String title) {
        this.review = review;
        this.avatar = avatar;
        this.date = date;
        this.rating = rating;
        this.title = title;
    }

    String getReview() {
        return review;
    }

    Drawable getAvatar() {
        return avatar;
    }

    String getDate() {
        return date;
    }

    RatingBar getRating() {
        return rating;
    }

    String getTitle() {
        return title;
    }

    private final String review;
    private final Drawable avatar;
    private final String date;
    private final RatingBar rating;
    private final String title;

}

