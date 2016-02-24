package com.example.kinkwan.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class existed_user extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existed_user);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        //Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Log.i(TAG, "onStop");
    }
}
