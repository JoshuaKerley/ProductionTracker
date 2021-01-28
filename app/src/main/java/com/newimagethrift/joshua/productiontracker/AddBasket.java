package com.newimagethrift.joshua.productiontracker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class AddBasket extends AppCompatActivity implements AdapterView.OnItemSelectedListener
{

    int numberToAdd = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_basket);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void addBasket(final ItemType type, final Name name)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_add_basket);

        Spinner dropdown = (Spinner)dialog.findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, new String[]{"1", "2", "3", "4"});
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);

        ((TextView)dialog.findViewById(R.id.textView9)).setText(" Basket(s) " + type + " for " + name + "?");

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
                for(int x = 0; x < numberToAdd; x++)
                    DataStore.getCurrent().recordAddition(type, name, 0);

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
        dialog.show();
    }

    public void addFurniture(View view)
    {
        addBasket(ItemType.FURNITURE, Name.FURNITURE);
    }
    public void addElectrical(View view)
    {
        addBasket(ItemType.ELECTRICAL, Name.ELECTRICAL);
    }
    public void addBricBrac(View view)
    {
        addBasket(ItemType.BRICBRAC, Name.BRICBRAC);
    }
    public void addBinsAccessories(View view)
    {
        addBasket(ItemType.BINS_ACC, Name.BINS_ACC);
    }
    public void addShoes(View view)
    {
        addBasket(ItemType.SHOES, Name.SHOES);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
    {
        numberToAdd = pos+1;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

}
