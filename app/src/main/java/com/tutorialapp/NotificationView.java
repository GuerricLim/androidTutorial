package com.tutorialapp;

import android.os.Bundle;
import android.app.Activity;

import com.example.guerriclim.tutorialapp.R;

public class NotificationView extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);
    }
}