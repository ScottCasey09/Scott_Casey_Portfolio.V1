// Casey Scott
// FINAL PROJECT - 1712
// Day.java

package com.fullsail.caseyscott.ontime.objects;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

public class Day implements Serializable {

    private long dateTime;
    private String hours;
    private double clockInLongitude;
    private double clockInLatitude;
    private double clockOutLongitude;
    private double clockOutLatitude;
    private long clockInTime;
    private long clockOutTime;

    public Day(long dateTime, String hours, double clockInLongitude, double clockInLatitude, double clockOutLongitude, double clockOutLatitude, long clockInTime, long clockOutTime, long breakStartTime, long breakStopTime, boolean wasDefaulted, String clockedByWho, String dayKey) {
        this.dateTime = dateTime;
        this.hours = hours;
        this.clockInLongitude = clockInLongitude;
        this.clockInLatitude = clockInLatitude;
        this.clockOutLongitude = clockOutLongitude;
        this.clockOutLatitude = clockOutLatitude;
        this.clockInTime = clockInTime;
        this.clockOutTime = clockOutTime;
        this.breakStartTime = breakStartTime;
        this.breakStopTime = breakStopTime;
        this.wasDefaulted = wasDefaulted;
        this.clockedByWho.set(clockedByWho);
        this.dayKey = dayKey;
    }

    private long breakStartTime;
    private long breakStopTime;
    private boolean wasDefaulted;

    public Day() {
    }

    private final AtomicReference<String> clockedByWho = new AtomicReference<String>();

    public void setDayKey(String dayKey) {
        this.dayKey = dayKey;
    }

    private String dayKey;

    public Day(long dateTime, String hours, double clockInLongitude, double clockInLatitude, double clockOutLongitude, double clockOutLatitude, long clockInTime, long clockOutTime, long breakStartTime, long breakStopTime, String clockedByWho, String dayKey) {
        this.dateTime = dateTime;
        this.hours = hours;
        this.clockInLongitude = clockInLongitude;
        this.clockInLatitude = clockInLatitude;
        this.clockOutLongitude = clockOutLongitude;
        this.clockOutLatitude = clockOutLatitude;
        this.clockInTime = clockInTime;
        this.clockOutTime = clockOutTime;
        this.breakStartTime = breakStartTime;
        this.breakStopTime = breakStopTime;
        this.clockedByWho.set(clockedByWho);
        this.dayKey = dayKey;
    }

    public Day(long dateTime, double clockInLongitude, double clockInLatitude, long clockInTime, String dayKey) {
        this.dateTime = dateTime;
        this.clockInLongitude = clockInLongitude;
        this.clockInLatitude = clockInLatitude;
        this.clockInTime = clockInTime;
        this.dayKey = dayKey;
    }

//    public Day(long dateTime, String dayKey) {
//        this.dateTime = dateTime;
//        this.dayKey = dayKey;
//    }

// --Commented out by Inspection START (12/13/17, 12:39 PM):
//    public long getDateTime() {
//
//        return dateTime;
//    }
// --Commented out by Inspection STOP (12/13/17, 12:39 PM)

// --Commented out by Inspection START (12/13/17, 12:40 PM):
//    public void setDateTime(long dateTime) {
//        this.dateTime = dateTime;
//    }
// --Commented out by Inspection STOP (12/13/17, 12:40 PM)

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public double getClockInLongitude() {
        return clockInLongitude;
    }

    public void setClockInLongitude(double clockInLongitude) {
        this.clockInLongitude = clockInLongitude;
    }

    public double getClockInLatitude() {
        return clockInLatitude;
    }

    public void setClockInLatitude(double clockInLatitude) {
        this.clockInLatitude = clockInLatitude;
    }

    public double getClockOutLongitude() {
        return clockOutLongitude;
    }

    public void setClockOutLongitude(double clockOutLongitude) {
        this.clockOutLongitude = clockOutLongitude;
    }

    public double getClockOutLatitude() {
        return clockOutLatitude;
    }

    public void setClockOutLatitude(double clockOutLatitude) {
        this.clockOutLatitude = clockOutLatitude;
    }

    public long getClockInTime() {
        return clockInTime;
    }

    public void setClockInTime(long clockInTime) {
        this.clockInTime = clockInTime;
    }

    public long getClockOutTime() {
        return clockOutTime;
    }

    public void setClockOutTime(long clockOutTime) {
        this.clockOutTime = clockOutTime;
    }

    public long getBreakStartTime() {
        return breakStartTime;
    }

    public void setBreakStartTime(long breakStartTime) {
        this.breakStartTime = breakStartTime;
    }

    public long getBreakStopTime() {
        return breakStopTime;
    }

    public void setBreakStopTime(long breakStopTime) {
        this.breakStopTime = breakStopTime;
    }

// --Commented out by Inspection START (12/13/17, 12:40 PM):
//    public String getClockedByWho() {
//        return clockedByWho;
//    }
// --Commented out by Inspection STOP (12/13/17, 12:40 PM)

// --Commented out by Inspection START (12/13/17, 12:40 PM):
//    public void setClockedByWho(String clockedByWho) {
//        this.clockedByWho = clockedByWho;
//    }
// --Commented out by Inspection STOP (12/13/17, 12:40 PM)

    public String getDayKey() {
        return dayKey;
    }

//    public boolean isWasDefaulted() {
//        return wasDefaulted;
//    }

    public void setWasDefaulted() {
        this.wasDefaulted = true;
    }
}
