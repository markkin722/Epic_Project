package com.example.kinkwan.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class home_page extends AppCompatActivity  implements SensorEventListener {
    Sensor accelerometer;
    SensorManager sm;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        SharedPreferences sharedpreferences = getSharedPreferences(sms_verify.MyPREFERENCES, Context.MODE_PRIVATE);
        String phoneKey = sharedpreferences.getString("phoneKey", "");
        TextView user_telephone = (TextView) findViewById(R.id.user_telephone);
        user_telephone.setText(phoneKey);
        //Toast.makeText(this, user_telephone.getText().toString(), Toast.LENGTH_SHORT).show();

        String coupons="";
        GetCouponList g= new GetCouponList(this);
        try {
            coupons = g.execute(phoneKey).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        //finish();
        //Toast.makeText(getApplicationContext(), coupons, Toast.LENGTH_LONG).show();
        String[] parts = coupons.split("\\.");
        //for (int i=0;i<parts.length;i++)
            //Toast.makeText(getApplicationContext(), parts[i], Toast.LENGTH_LONG).show();


        //coupon list
        listView = (ListView) findViewById(R.id.list);
        final List<String[]> coupon = new LinkedList<String[]>();
        for (int i=0;i<parts.length/2;i++)
            coupon.add(new String[] {parts[i*2],parts[i*2+1]});
        /*coupon.add(new String[] {"Pizzahut","3 Coupons"});
        coupon.add(new String[] {"KFC","5 Coupons"});
        coupon.add(new String[]{"Starbucks", "1 Coupon"});
        coupon.add(new String[]{"McDonald's", "8 Coupons"});
        coupon.add(new String[] {"PizzaBox","3 Coupons"});
        coupon.add(new String[] {"Wellcome","1 Coupon"});
        coupon.add(new String[] {"7-Eleven","2 Coupons"});
        coupon.add(new String[] {"OK Store","1 Coupon"});*/
        ArrayAdapter<String[]> adapter = new ArrayAdapter<String[]>(this, android.R.layout.simple_list_item_2, android.R.id.text1, coupon){

            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position,convertView,parent);

                String[] entry = coupon.get(position);
                TextView text1 = (TextView) view.findViewById(android.R.id.text1);
                TextView text2 = (TextView) view.findViewById(android.R.id.text2);
                text1.setText(entry[0]);
                text2.setText(entry[1]);
                return view;
            }
        };

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view.setSelected(true);
                Toast.makeText(home_page.this,view+"is selected!",Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void check_points(View view){
        String points="";
        GetRewardPoint g= new GetRewardPoint(this);
        try {
            SharedPreferences sharedpreferences = getSharedPreferences(sms_verify.MyPREFERENCES, Context.MODE_PRIVATE);
            String phoneKey = sharedpreferences.getString("phoneKey", "");
            points = g.execute("get",phoneKey).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, reward_point.class);
        intent.putExtra("points", points);
        startActivity(intent);
        home_page.this.recreate();
        //home_page.this.finish();
    }

    public void add_coupon(View view){
        startActivity(new Intent(this, add_coupon.class));
        home_page.this.recreate();
        //StartPage.this.finish();
    }

    public void add_coupon_nfc(View view){
        startActivity(new Intent(this, add_coupon_nfc.class));
        home_page.this.recreate();
    }

    public void use_coupon_QRcode(View view){
        startActivity(new Intent(this, CrunchifyQRCode.class));
        home_page.this.recreate();
    }

    public void refresh_home(View view){
        startActivity(new Intent(this, home_page.class));
        home_page.this.finish();
    }

    public void logout(View view){
        SharedPreferences sharedpreferences = getSharedPreferences(sms_verify.MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
        finish();
    }

    //public void close(View view){
    //    finishAffinity();
    //}

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

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.values[0] >= 11 || event.values[0] <=-11){
            Toast.makeText(this, "Shaking", Toast.LENGTH_LONG).show();
            GetRewardPoint g= new GetRewardPoint(this);
            try {
                SharedPreferences sharedpreferences = getSharedPreferences(sms_verify.MyPREFERENCES, Context.MODE_PRIVATE);
                String phoneKey = sharedpreferences.getString("phoneKey", "");
                String result = g.execute("add", phoneKey).get();
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
            }
            catch (Exception e){

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
