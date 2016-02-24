package com.example.kinkwan.myapplication;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by Kinkwan on 5/10/2015.
 */
public class backgroundTask extends AsyncTask<String,Void,String> {
    Context ctx;
    backgroundTask(Context ctx){
        this.ctx = ctx;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        String reg_url = "http://bookupon.co.nf/mobile/register.php";
        String add_url = "http://bookupon.co.nf/mobile/add_coupon.php";
        String add_detail_url = "http://bookupon.co.nf/mobile/add_coupon_detail.php";
        String method = params[0];

        //String user_pass = params[3];
        if (method.equals("register")) {
            String phone_no = params[1];
            String code_no = params[2];
            try {
                URL url = new URL(reg_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data = URLEncoder.encode("phone_no", "UTF-8") + "=" + URLEncoder.encode(phone_no, "UTF-8") +
                        "&" + URLEncoder.encode("code_no", "UTF-8") + "=" + URLEncoder.encode(code_no, "UTF-8");
                //"&" + URLEncoder.encode("user_pass", "UTF-8")+ "=" + URLEncoder.encode(user_pass, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();

                IS.close();
                return "Registration Success...";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else

        if (method.equals("add_coupon_detail")){
            String coupon_code = params[1];
            String result = "HI11";
            StringBuffer sb = new StringBuffer();
            try{
                URL url =  new URL(add_detail_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data = URLEncoder.encode("coupon_code", "UTF-8")+ "=" + URLEncoder.encode(coupon_code, "UTF-8");
                //"&" + URLEncoder.encode("coupon_code", "UTF-8")+ "=" + URLEncoder.encode(coupon_code, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                InputStream IS;
                IS = new BufferedInputStream(httpURLConnection.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(IS));
                String inputLine = "";
                while ((inputLine = br.readLine()) != null) {
                    sb.append(inputLine);
                }
                result = sb.toString();
                IS.close();

            } catch(Exception e){

            }

            return result;
        } else

        if (method.equals("add_coupon")){
            String phone_no = params[1];
            String coupon_code = params[2];
            try{
                URL url =  new URL(add_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS =   httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data = URLEncoder.encode("phone_no", "UTF-8")+ "=" + URLEncoder.encode(phone_no, "UTF-8") +
                        "&" + URLEncoder.encode("coupon_code", "UTF-8")+ "=" + URLEncoder.encode(coupon_code, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();

                IS.close();
                return "Add Success...";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        //Toast.makeText(ctx,result,Toast.LENGTH_LONG).show();
    }
}
