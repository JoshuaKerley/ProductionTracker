package com.newimagethrift.joshua.productiontracker;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class ManageSavedNames extends AppCompatActivity {

    ArrayList<String> names;
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_saved_names);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        final SharedPreferences sharedpreferences = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);

        listView = (ListView) findViewById(R.id.listView);

        names = new ArrayList<>(sharedpreferences.getStringSet("NAMES", null));

        Collections.sort(names);

        adapter = new ArrayAdapter<String>(this, R.layout.custom_list_row_names, R.id.textView1, names);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id)
            {

                final Dialog dialog = new Dialog(ManageSavedNames.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog);

                TextView name = (TextView) dialog.findViewById(R.id.textView9);
                name.setText("Are you sure you want to delete:\n\n" + names.get(position));
                name.setTextSize(25);

                Button cancelButton=(Button)dialog.findViewById(R.id.buttonDialog_Cancel);
                cancelButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();
                    }
                });

                Button yesButton=(Button)dialog.findViewById(R.id.buttonDialog_Yes);
                yesButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        dialog.dismiss();

                        names.remove(position);

                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putStringSet("NAMES", new HashSet<String>(names));
                        editor.commit();


                        Animation anim = AnimationUtils.loadAnimation(
                                ManageSavedNames.this, android.R.anim.slide_out_right
                        );
                        anim.setDuration(500);
                        listView.getChildAt(position).startAnimation(anim);

                        new Handler().postDelayed(new Runnable() {

                            public void run() {

                                adapter.notifyDataSetChanged();

                            }

                        }, anim.getDuration());

                        adapter.notifyDataSetChanged();

                        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "Name Deleted", Snackbar.LENGTH_LONG).setAction("Action", null);

                        View view = snack.getView();
                        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                        tv.setTextSize(30);
                        snack.show();
                    }
                });

                dialog.show();
            }
        });
    }

    public void addName(View view)
    {
        TextView name = (TextView) findViewById(R.id.name);

        if(!name.getText().toString().equals(""))
        {
            if(!names.contains(name.getText().toString()))
            {
                names.add(name.getText().toString().trim());

                Collections.sort(names);

                SharedPreferences.Editor editor = getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE).edit();

                editor.putStringSet("NAMES", new HashSet<String>(names));
                editor.commit();

                name.setText("");

                adapter.notifyDataSetChanged();
            }
            else
            {
                Toast toast = Toast.makeText(getApplicationContext(), "Name has already been added", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

}
