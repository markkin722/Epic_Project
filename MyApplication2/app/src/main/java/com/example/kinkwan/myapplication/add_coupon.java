package com.example.kinkwan.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.concurrent.ExecutionException;

public class add_coupon extends AppCompatActivity {
    IntentIntegrator integrator = new IntentIntegrator(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon);

        //phone no
        SharedPreferences sharedpreferences = getSharedPreferences(sms_verify.MyPREFERENCES, Context.MODE_PRIVATE);
        String phoneKey = sharedpreferences.getString("phoneKey", "");
        TextView add1 = (TextView) findViewById(R.id.add1);
        add1.setText(phoneKey);



        new IntentIntegrator(this).initiateScan();

        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan a barcode or QR code");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(true);

        integrator.initiateScan();
    }

    public void submit_coupon(View view){
        //send check existence request
        TextView add1 = (TextView) findViewById(R.id.add1);
        TextView add6 = (TextView) findViewById(R.id.add6);
        String method2 = "add_coupon";
        if (add6.getText().equals("")){
            Toast.makeText(this,"No coupon detected!",Toast.LENGTH_SHORT).show();
        } else {
            backgroundTask backgroundTask2= new backgroundTask(this);
            backgroundTask2.execute(method2, add1.getText().toString(), add6.getText().toString());
            finish();
        }
    }

    public void cancel_coupon(View view){
        finish();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        String coupons = "";

        if (result != null) {
            String contents = result.getContents();
            if (contents != null) {
                //contents = contents.trim();
                Toast.makeText(add_coupon.this, contents, Toast.LENGTH_SHORT).show();
                TextView add2 = (TextView) findViewById(R.id.add2);
                TextView add3 = (TextView) findViewById(R.id.add3);
                TextView add4 = (TextView) findViewById(R.id.add4);
                TextView add5 = (TextView) findViewById(R.id.add5);
                TextView add6 = (TextView) findViewById(R.id.add6);
                add6.setText(contents);
                Button submit = (Button) findViewById(R.id.button6);
                //if (add2.getText().toString().isEmpty()){



                //send check detail request
                String method1 = "add_coupon_detail";
                backgroundTask backgroundTask= new backgroundTask(this);
                try {
                    coupons = backgroundTask.execute(method1, contents).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                String[] parts = coupons.split("\\.");
                //for (int i=0; i<parts.length; i++)
                //Toast.makeText(getApplicationContext(), parts[i], Toast.LENGTH_LONG).show();
                //Toast.makeText(add_coupon.this, parts.length, Toast.LENGTH_SHORT).show();

                add2.setText(parts[0]);
                add3.setText(parts[1]);
                add4.setText(parts[2]);
                add5.setText(parts[3]);

                //startActivity(new Intent(this, home_page.class));
                //add_coupon.this.finish();
            }
        } else {
            add_coupon.this.finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
