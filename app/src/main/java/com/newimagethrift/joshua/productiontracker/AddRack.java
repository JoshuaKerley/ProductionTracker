package com.newimagethrift.joshua.productiontracker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class AddRack extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rack);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ((TextView)findViewById(R.id.textView)).setText("Add Rack for " + Name.RACKS_CURRENT);
    }

    public void addRack(final ItemType type)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog);

        TextView name = (TextView) dialog.findViewById(R.id.textView9);
        name.setText("Add Rack " + type + " for " + Name.RACKS_CURRENT + "?");

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
                DataStore.getCurrent().recordAddition(type, Name.RACKS_CURRENT, 0);

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        dialog.show();
    }

    public void addL_K(final View view)
    {
        addRack(ItemType.RACK_LADIESKIDS);
    }

    public void addMens(View view)
    {
        addRack(ItemType.RACK_MENS);
    }
}
