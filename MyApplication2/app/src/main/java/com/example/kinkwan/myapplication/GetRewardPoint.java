package com.example.kinkwan.myapplication;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Edison on 17/2/2016.
 */
public class GetRewardPoint extends AsyncTask<String,Void,String> {
    Context ctx;
    GetRewardPoint(Context ctx){
        this.ctx = ctx;
    }
    @Override
    protected String doInBackground(String... params) {
        String reg_url = "http://bookupon.co.nf/mobile/getRewardPoint.php";
        String add_url = "http://bookupon.co.nf/mobile/addRewardPoint.php";
        String ur="";
        String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()
        //String param1 = phoneKey;
        //StringBuffer sb = new StringBuffer();
        String result = "HI11";
        String method = params[0];
        String phone_no = params[1];
        StringBuffer sb = new StringBuffer();
        if (method.equals("get"))
            ur = reg_url;
        else if (method.equals("add"))
            ur = add_url;
        try {
            URL url =  new URL(ur);
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream OS =   httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
            String data = URLEncoder.encode("param1", "UTF-8")+ "=" + URLEncoder.encode(phone_no, "UTF-8");
            //"&" + URLEncoder.encode("user_pass", "UTF-8")+ "=" + URLEncoder.encode(user_pass, "UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();
            InputStream IS = httpURLConnection.getInputStream();
            IS = new BufferedInputStream(httpURLConnection.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(IS));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            result = sb.toString();
            IS.close();
        }
        catch(Exception e){

        }

        return result;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
       // Toast.makeText(ctx, result, Toast.LENGTH_LONG).show();
    }
}
