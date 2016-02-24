
package com.example.kinkwan.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class new_user extends AppCompatActivity {

    TextView phone_no;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        phone_no = (TextView) findViewById(R.id.new_phone_no);
    }

    public void next_sms(View view){
        String phone = phone_no.getText().toString();
        String code = Integer.toString((int) (Math.random() * 8999 + 1000));

        //send SMS
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, "Epicoupon: " + code, null, null);
            Toast.makeText(getApplicationContext(), "SMS Sent!",
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "SMS faild, please try again later!",
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }


        //SMS verification
        Intent intent = new Intent(this, sms_verify.class);
        intent.putExtra("phoneKey", phone);
        intent.putExtra("codeKey", code);
        startActivity(intent);

        new_user.this.finish();
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
