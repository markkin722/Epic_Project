package com.example.kinkwan.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.text.ClipboardManager;
//import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class CrunchifyQRCode extends AppCompatActivity {

    String BASE_QR_URL = "http://chart.apis.google.com/chart?cht=qr&chs=400x400&chld=M&choe=UTF-8&chl=";
    String fullUrl = BASE_QR_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crunchify_qrcode);
        WebView web = (WebView)findViewById(R.id.webView);
        web.setVisibility(View.INVISIBLE);
        web.loadUrl(BASE_QR_URL);
        web.layout(0, 0, 1200, 1200);
    }

    public void generate_QR(View view){
        WebView web = (WebView)findViewById(R.id.webView);
        TextView selected_coupon = (TextView)findViewById(R.id.select_coupon);
        fullUrl = BASE_QR_URL + selected_coupon.getText().toString();
        web.setVisibility(View.VISIBLE);
        web.loadUrl(fullUrl);
        web.layout(0, 0, 1200, 1200);
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
