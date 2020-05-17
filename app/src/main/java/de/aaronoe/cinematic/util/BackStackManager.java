package de.aaronoe.cinematic.util;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.LinkedList;

public class BackStackManager {

    private static BackStackManager instance;
    private static final String TAG = "BackStackManager";
    private static final int MAX_STACK_SIZE = 10;
    LinkedList<AppCompatActivity> activityQueue;

    public BackStackManager() {
        activityQueue = new LinkedList<>();
    }

    public static BackStackManager getInstance(){
        if(instance == null){
            instance = new BackStackManager();
        }
        return instance;
    }

    public void pushActivity(AppCompatActivity act) {
        activityQueue.add(act);
        Log.d(TAG, "Push activity: " + activityQueue.size());
        if(activityQueue.size() > MAX_STACK_SIZE){
            Log.e(TAG, "Removed Activity");
            activityQueue.poll().finish();
        }
    }

    public void popActivity(AppCompatActivity act) {
        activityQueue.remove(act);
        Log.d(TAG, "Pop Activity");
    }


}
