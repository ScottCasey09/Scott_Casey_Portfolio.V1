// Casey Scott
// JAV1 - 1704
// Person.java
package com.fullsail.caseyscott.scottcasey_ce06;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

class Person extends Drawable {


    //Variables of the class
    private final String firstname;

    private final String lastname;

    final String birthdate;
    String getBirthdate() {
        return birthdate;
    }

    private final Drawable picture;
    Drawable getPicture() {
        return picture;
    }


    //Main Constructor
    Person(String firstname, String lastname, String birthdate, Drawable picture) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.picture = picture;
    }

    //Override the toString to make a full name
    @Override
    public String toString() {
        return firstname+" "+lastname;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {

    }

    @Override
    public void setAlpha(@IntRange(from = 0, to = 255) int alpha) {

    }
    
    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }
}
