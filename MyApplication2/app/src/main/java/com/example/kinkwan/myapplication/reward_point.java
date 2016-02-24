package com.example.kinkwan.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Edison on 18/2/2016.
 */
public class reward_point extends AppCompatActivity implements SensorEventListener {
    Sensor accelerometer;
    SensorManager sm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reward_points);

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        Intent intent = getIntent();
        String points = intent.getStringExtra("points");
        TextView t = (TextView) findViewById(R.id.points);
        t.setText(points);

        ListView listView = (ListView) findViewById(R.id.list);
        final List<String[]> coupon = new LinkedList<String[]>();

        coupon.add(new String[] {"PizzaH $10 Coupon","100"});
        coupon.add(new String[] {"KFB $10 Coupon","200"});
        coupon.add(new String[]{"Starbuckz 1 Drink", "200"});
        coupon.add(new String[]{"McDonald'z 1 Burger", "200"});
        coupon.add(new String[] {"PizzaB 1 Small Pizza","200"});
        coupon.add(new String[] {"Wellcame $50 Coupon","300"});
        coupon.add(new String[] {"11-Seven $100 Coupon","500"});
        coupon.add(new String[] {"KO Store $200 Coupon","1000"});
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
    }

    public void refresh_home(View view){
        startActivity(new Intent(this, reward_point.class));
        reward_point.this.finish();
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
                TextView t = (TextView) findViewById(R.id.points);
                t.setText(result);
            }
            catch (Exception e){

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
