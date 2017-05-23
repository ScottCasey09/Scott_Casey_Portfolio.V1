package com.fullsail.caseyscott.spinner_arrayadapty_practice;

/**
 * Created by Casey on 4/3/17.
 */

class AndroidVersion {

    private final String mName;
    private final double mVersion;

    AndroidVersion(String mName, double mVersion) {
        this.mName = mName;
        this.mVersion = mVersion;
    }

    @Override
    public String toString() {
        return  mName + " (" + mVersion + ')';
    }
}
