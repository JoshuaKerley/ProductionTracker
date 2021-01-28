package com.newimagethrift.joshua.productiontracker;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.Normalizer;
import java.util.ArrayList;

public class Submit extends AppCompatActivity {

    public static final MediaType FORM_DATA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    private Totals t;

    String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DataStore data = DataStore.getCurrent();
        ArrayList<Item> list = data.getDataList();

//        racks1_name = sharedpreferences.getString("NAME_RACKS_1", null);
//        racks2_name = sharedpreferences.getString("NAME_RACKS_2", null);
//        bins_name = sharedpreferences.getString("NAME_BINS", null);
//        furniture_name = sharedpreferences.getString("NAME_FURNITURE", null);
//        electrical_name = sharedpreferences.getString("NAME_ELECTRONICS", null);
//        bricbrac_name = sharedpreferences.getString("NAME_BRICBRAC", null);
//        bins_acc_name = sharedpreferences.getString("NAME_BINS_ACCESSORIES", null);
//        shoes_name = sharedpreferences.getString("NAME_SHOES", null);

        t = new Totals();

        TextView racks1 = (TextView)findViewById(R.id.textView12);
        TextView racks2 = (TextView)findViewById(R.id.textView13);
        TextView racks_total = (TextView)findViewById(R.id.textView20);
        TextView bins_hanging = (TextView)findViewById(R.id.textView14);
        TextView bins_hanging_total = (TextView)findViewById(R.id.textView21);
        TextView furniture = (TextView)findViewById(R.id.textView15);
        TextView electrical = (TextView)findViewById(R.id.textView16);
        TextView bricbrac = (TextView)findViewById(R.id.textView17);
        TextView bins_acc = (TextView)findViewById(R.id.textView18);
        TextView shoes = (TextView)findViewById(R.id.textView19);
        TextView baskets_total = (TextView)findViewById(R.id.textView22);
        TextView sweeps_total = (TextView)findViewById(R.id.totalFloorSweeps);

        racks1.setText("Racks 1: " + Name.RACKS_1 + " - " + t.racks1.items_total + " Items");
        racks2.setText("Racks 2: " + Name.RACKS_2 + " - " + t.racks2.items_total + " Items");

        SpannableString racks_total_span = new SpannableString("Total Racks Items: " + (t.racks_total_items));
        racks_total_span.setSpan(new UnderlineSpan(), 0, racks_total_span.length(), 0);
        racks_total_span.setSpan(new StyleSpan(Typeface.BOLD), 0, racks_total_span.length(), 0);
        racks_total.setText(racks_total_span);

        bins_hanging.setText("Bins-Hanging: " + Name.BINS + " - " + t.bins.items_total + " Items");

        SpannableString bins_total_span = new SpannableString("Total Bins-Hanging Items: " + t.bins.items_total);
        bins_total_span.setSpan(new UnderlineSpan(), 0, bins_total_span.length(), 0);
        bins_total_span.setSpan(new StyleSpan(Typeface.BOLD), 0, bins_total_span.length(), 0);
        bins_hanging_total.setText(bins_total_span);

        furniture.setText("Furniture: " + Name.FURNITURE + " - " + t.furniture.baskets + " Baskets");
        electrical.setText("Electrical: " + Name.ELECTRICAL + " - " + t.electrical.baskets + " Baskets");
        bricbrac.setText("Bric-Brac: " + Name.BRICBRAC + " - " + t.bricbrac.baskets + " Baskets");
        bins_acc.setText("Bins-Acc.: " + Name.BINS_ACC + " - " + t.bins_acc.baskets + " Baskets");
        shoes.setText("Shoes: " + Name.SHOES + " - " + t.shoes.baskets + " Baskets");

        SpannableString baskets_total_span = new SpannableString("Total Baskets: " + t.baskets.baskets);
        baskets_total_span.setSpan(new UnderlineSpan(), 0, baskets_total_span.length(), 0);
        baskets_total_span.setSpan(new StyleSpan(Typeface.BOLD), 0, baskets_total_span.length(), 0);
        baskets_total.setText(baskets_total_span);

        sweeps_total.setText("Total Floor Sweeps: " + t.sweeps.number + " ");
    }

    public void buttonPressed(View view)
    {
        Utilities util = new Utilities();

        if(t.racks_total_items < 1400)
        {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.custom_dialog_bookkeeping_note);

            Button sendButton=(Button)dialog.findViewById(R.id.buttonDialog_Send);
            sendButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    TextView text = (TextView) dialog.findViewById(R.id.editText2);

                    if(text.getText().toString().equals(""))
                    {
                        Toast toast = Toast.makeText(getApplicationContext(), "Reason Is Required", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                    else
                    {
                        message = text.getText().toString();

                        if(isInternetConnected())
                        {
                            dialog.dismiss();

                            new Download().execute();
                        }
                        else
                        {
                            Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "Please Connect to the Internet", Snackbar.LENGTH_LONG).setAction("Action", null);

                            View snackView = snack.getView();
                            TextView tv = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
                            tv.setTextSize(25);
                            snack.show();
                        }
                    }
                }
            });

            dialog.show();
        }
        else
        {
            if(isInternetConnected())
            {
                new Download().execute();
            }
            else
            {
                Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "Please Connect to the Internet", Snackbar.LENGTH_LONG).setAction("Action", null);

                View snackView = snack.getView();
                TextView tv = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextSize(25);
                snack.show();
            }
        }
    }

    public void submitValues()
    {
        StringBuilder dataBuilder = new StringBuilder();

        dataBuilder.append(FormEntries.COMBINED_TOTAL_HUNG + URLEncoder.encode((t.racks_total_items + t.bins.items_total) + ""));                 //combined total hung
        dataBuilder.append(FormEntries.LK_HUNG + URLEncoder.encode((t.lk_total_racks) + "\n" + (t.lk_total_items)));                 //L/K racks/hung
        dataBuilder.append(FormEntries.MENS_HUNG + URLEncoder.encode((t.mens_total_racks) + "\n" + (t.mens_total_items)));                 //Mens racks/hung
        dataBuilder.append(FormEntries.TOTAL_RACKS_HUNG + URLEncoder.encode((t.racks_total_racks) + "\n" + t.racks_total_items));                 //L/K + Mens racks/hung
        dataBuilder.append(FormEntries.BINS_HUNG + URLEncoder.encode(t.bins.items_total + ""));                 //Bins-Hanging hung
        dataBuilder.append(FormEntries.HUNG_MESSAGE + URLEncoder.encode(message));                   //reason if hung < 1400
        dataBuilder.append(FormEntries.RACKS1_NAME + URLEncoder.encode(Name.RACKS_1.toString()));
        dataBuilder.append(FormEntries.RACKS1_TOTAL_HUNG + URLEncoder.encode(t.racks1.racks_total + "\n" + t.racks1.items_total));
        dataBuilder.append(FormEntries.RACKS1_LK_HUNG + URLEncoder.encode(t.racks1.lk.racks + "\n" + t.racks1.lk.items));
        dataBuilder.append(FormEntries.RACKS1_MENS_HUNG + URLEncoder.encode(t.racks1.mens.racks + "\n" + t.racks1.mens.items));
        dataBuilder.append(FormEntries.RACKS1_LK_TIMES + URLEncoder.encode(t.racks1.lk.times));
        dataBuilder.append(FormEntries.RACKS1_MENS_TIMES + URLEncoder.encode(t.racks1.mens.times));
        dataBuilder.append(FormEntries.RACKS2_NAME + URLEncoder.encode(Name.RACKS_2.toString()));
        dataBuilder.append(FormEntries.RACKS2_TOTAL_HUNG + URLEncoder.encode(t.racks2.racks_total + "\n" + t.racks2.items_total));
        dataBuilder.append(FormEntries.RACKS2_LK_HUNG + URLEncoder.encode(t.racks2.lk.racks + "\n" + t.racks2.lk.items));
        dataBuilder.append(FormEntries.RACKS2_MENS_HUNG + URLEncoder.encode(t.racks2.mens.racks + "\n" + t.racks2.mens.items));
        dataBuilder.append(FormEntries.RACKS2_LK_TIMES + URLEncoder.encode(t.racks2.lk.times));
        dataBuilder.append(FormEntries.RACKS2_MENS_TIMES + URLEncoder.encode(t.racks2.mens.times));
        dataBuilder.append(FormEntries.BINS_NAME + URLEncoder.encode(Name.BINS.toString()));
        dataBuilder.append(FormEntries.BINS_ITEMS + URLEncoder.encode(t.bins.items_total + ""));
        dataBuilder.append(FormEntries.BINS_TIMES + URLEncoder.encode(t.bins.times));
        dataBuilder.append(FormEntries.FURNITURE_NAME + URLEncoder.encode(Name.FURNITURE.toString()));
        dataBuilder.append(FormEntries.FURNITURE_BASKETS + URLEncoder.encode(t.furniture.baskets + ""));
        dataBuilder.append(FormEntries.FURNITURE_TIMES + URLEncoder.encode(t.furniture.times));
        dataBuilder.append(FormEntries.ELECTRICAL_NAME + URLEncoder.encode(Name.ELECTRICAL.toString()));
        dataBuilder.append(FormEntries.ELECTRICAL_BASKETS + URLEncoder.encode(t.electrical.baskets + ""));
        dataBuilder.append(FormEntries.ELECTRICAL_TIMES + URLEncoder.encode(t.electrical.times));
        dataBuilder.append(FormEntries.BRICBRAC_NAME + URLEncoder.encode(Name.BRICBRAC.toString()));
        dataBuilder.append(FormEntries.BRICBRAC_BASKETS + URLEncoder.encode(t.bricbrac.baskets + ""));
        dataBuilder.append(FormEntries.BRICBRAC_TIMES + URLEncoder.encode(t.bricbrac.times));
        dataBuilder.append(FormEntries.BINS_ACC_NAME + URLEncoder.encode(Name.BINS_ACC.toString()));
        dataBuilder.append(FormEntries.BINS_ACC_BASKETS + URLEncoder.encode(t.bins_acc.baskets + ""));
        dataBuilder.append(FormEntries.BINS_ACC_TIMES + URLEncoder.encode(t.bins_acc.times));
        dataBuilder.append(FormEntries.SHOES_NAME + URLEncoder.encode(Name.SHOES.toString()));
        dataBuilder.append(FormEntries.SHOES_BASKETS + URLEncoder.encode(t.shoes.baskets + ""));
        dataBuilder.append(FormEntries.SHOES_TIMES + URLEncoder.encode(t.shoes.times));
        dataBuilder.append(FormEntries.SWEEPS_TIMES + URLEncoder.encode(t.sweeps.times));

        executePost(FormEntries.FORM_URL, dataBuilder.toString());

        DataStore.getCurrent().resetData();
    }

    public static void executePost(String targetURL, String data)
    {
        try{
            //Create OkHttpClient for sending request
            OkHttpClient client = new OkHttpClient();
            //Create the request body with the help of Media Type
            RequestBody body = RequestBody.create(FORM_DATA_TYPE, data);
            Request request = new Request.Builder()
                    .url(targetURL)
                    .post(body)
                    .build();
            //Send the request
            Response response = client.newCall(request).execute();
        }catch (IOException exception)
        {

        }
    }

    public String getTimes(ItemType type, Name name)
    {
        ArrayList<Item> list = DataStore.getCurrent().getDataList();

        StringBuilder builder = new StringBuilder();

        for(int x = 0; x < list.size(); x++)
        {
            if(list.get(x).getName().equals(name) && list.get(x).getType().equals(type))
            {
                if(builder.toString().equals(""))
                {
                    builder.append(list.get(x).getTime());
                }
                else
                {
                    builder.append(" - " + list.get(x).getTime());
                }
            }
        }
        return builder.toString();
    }

    private class Download extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("Hi", "Upload Commencing");

            pDialog = new ProgressDialog(Submit.this);
            pDialog.setMessage("Uploading Database...");


            String message= "Uploading Data";

            SpannableString ss2 =  new SpannableString(message);
            ss2.setSpan(new RelativeSizeSpan(2f), 0, ss2.length(), 0);
            ss2.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ss2.length(), 0);

            pDialog.setMessage(ss2);

            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            submitValues();

            return "Executed!";

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("Hi", "Done Uploading.");

            pDialog.dismiss();
            finish();
        }
    }

    public boolean isInternetConnected()
    {
        ConnectivityManager check = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = check.getAllNetworkInfo();
        for (int i = 0; i<info.length; i++){
            if (info[i].getState() == NetworkInfo.State.CONNECTED){
                return true;
            }
        }
        return false;
    }
}


