package com.example.kinkwan.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class sms_verify extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Phone = "phoneKey";
    public static final String Code = "codeKey";
    SharedPreferences sharedpreferences;
    String codeKey, phoneKey;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_verify);

        Intent intent = getIntent();
        phoneKey = intent.getStringExtra("phoneKey");
        codeKey = intent.getStringExtra("codeKey");
        Toast.makeText(this, "Phone:"+phoneKey+"\n code:"+codeKey, Toast.LENGTH_SHORT).show();


        //sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        //String name = sharedpreferences.getString("phoneKey", "");



    }

    public void check_sms(View view){
        TextView smscode = (TextView) findViewById(R.id.sms_code);
        if (smscode.getText().toString().equals(codeKey)){
            Toast.makeText(this,"Success!",Toast.LENGTH_SHORT).show();
            sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

            //remember the account
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.putString(Phone, phoneKey);
            editor.putString(Code, codeKey);
            editor.commit();

            //send register request
            String method = "register";
            backgroundTask backgroundTask= new backgroundTask(this);
            backgroundTask.execute(method,phoneKey, codeKey);
            finish();

            startActivity(new Intent(this, home_page.class));
            sms_verify.this.finish();
        } else {
            Toast.makeText(this,"Wrong code!",Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(this, new_user.class));
            //sms_verify.this.finish();
        }
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
