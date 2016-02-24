package com.example.kinkwan.myapplication;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class add_coupon_nfc extends AppCompatActivity {

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "NfcDemo";

    private TextView mTextView;
    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coupon_nfc);

        //phone no
        SharedPreferences sharedpreferences = getSharedPreferences(sms_verify.MyPREFERENCES, Context.MODE_PRIVATE);
        String phoneKey = sharedpreferences.getString("phoneKey", "");
        TextView phone_no_nfc = (TextView) findViewById(R.id.phone_no_nfc);
        phone_no_nfc.setText(phoneKey);

        mTextView = (TextView)findViewById(R.id.textView_explanation);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(this, "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(this,"NFC is disabled.",Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this,"Welcome to the page!",Toast.LENGTH_SHORT).show();
        }

        handleIntent(getIntent());
    }

    public void cancel_coupon(View view){
        finish();
    }

    public void submit_coupon_nfc(View view){
        //send check existence request
        TextView add1 = (TextView) findViewById(R.id.phone_no_nfc);
        TextView add6 = (TextView) findViewById(R.id.textView_explanation);
        String method2 = "add_coupon";

        if (add6.getText().equals("")){
            Toast.makeText(this,"No coupon detected!",Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(this,"coupon detected!"+add6.getText(),Toast.LENGTH_SHORT).show();
            backgroundTask backgroundTask2= new backgroundTask(add_coupon_nfc.this);
            backgroundTask2.execute(method2, add1.getText().toString(), add6.getText().toString());
            finish();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //Toast.makeText(this, "Detected", Toast.LENGTH_LONG).show();
        /**
         * It's important, that the activity is in the foreground (resumed). Otherwise
         * an IllegalStateException is thrown.
         */
        //setupForegroundDispatch(this, mNfcAdapter);
        Intent intent = new Intent(this, add_coupon_nfc.class);
        intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilters = new IntentFilter[]{};
        mNfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilters, null);

    }

    @Override
    protected void onPause() {
        /**
         * Call this before onPause, otherwise an IllegalArgumentException is thrown as well.
         */
        //stopForegroundDispatch(this, mNfcAdapter);

        super.onPause();
        mNfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        /**
         * This method gets called, when a new Intent gets associated with the current activity instance.
         * Instead of creating a new activity, onNewIntent will be called. For more information have a look
         * at the documentation.
         *
         * In our case this method gets called, when the user attaches a Tag to the device.
         */
        //Toast.makeText(this, "received",Toast.LENGTH_LONG).show();

        handleIntent(intent);
    }
    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Toast.makeText(this, "action:"+action,Toast.LENGTH_LONG).show();
            String type = intent.getType();
            Toast.makeText(this, "data string:",Toast.LENGTH_LONG).show();
            Toast.makeText(this,"type: "+ type,Toast.LENGTH_LONG).show();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                Toast.makeText(this,"tag: "+ tag, Toast.LENGTH_LONG).show();
                new NdefReaderTask().execute(tag);
                Toast.makeText(this, "ID:"+tag.getId().toString()+"\nContents:"+tag.describeContents()+"\n" +
                        "TechList:"+ tag.getTechList()+"\n Tag:" + tag.toString(),Toast.LENGTH_LONG).show();
            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
            Toast.makeText(this, type+": "+MIME_TEXT_PLAIN.equals(type), Toast.LENGTH_LONG).show();
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Toast.makeText(this, "TECH",Toast.LENGTH_LONG).show();
            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        } else {
            Toast.makeText(this, action, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public static void setupForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public static void stopForegroundDispatch(final Activity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
    }

    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.

                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }

        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = "UTF-8";

            // Get the Language Code
            int languageCodeLength = 0;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength, payload.length - languageCodeLength, textEncoding);
        }


        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                mTextView.setText(result);
            }

            String coupons = "";
            if (result != null) {
                String contents = result;
                if (contents != null) {
                    //contents = contents.trim();
                    Toast.makeText(add_coupon_nfc.this, contents, Toast.LENGTH_SHORT).show();
                    TextView add2 = (TextView) findViewById(R.id.ccom_nfc);
                    TextView add3 = (TextView) findViewById(R.id.cdiscount_nfc);
                    TextView add4 = (TextView) findViewById(R.id.cstart_nfc);
                    TextView add5 = (TextView) findViewById(R.id.cexpire_nfc);

                    Button submit = (Button) findViewById(R.id.button6);
                    //if (add2.getText().toString().isEmpty()){



                    //send check detail request
                    String method1 = "add_coupon_detail";
                    backgroundTask backgroundTask= new backgroundTask(add_coupon_nfc.this);
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
                add_coupon_nfc.this.finish();
            }
        }

    }
}
