package com.example.kinkwan.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class StartPage extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;
    String phoneKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        sharedpreferences = getSharedPreferences(sms_verify.MyPREFERENCES, Context.MODE_PRIVATE);
        phoneKey = sharedpreferences.getString("phoneKey", "");


        Toast.makeText(this, phoneKey, Toast.LENGTH_SHORT).show();
        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        // your code here
                        check_sp();
                    }
                },
                1000
        );
    }

    //check sp
    public void check_sp(){
        //timer
        /*
        new CountDownTimer(30000,1000){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

            }
        }.start();
        */
        if (!phoneKey.isEmpty()){
            startActivity(new Intent(this, home_page.class));
            StartPage.this.finish();
        } else {
            startActivity(new Intent(this, new_user.class));
            StartPage.this.finish();
        }
        //sp not existed -> new
        //sp existed -> old
    }


}
