package com.newimagethrift.joshua.productiontracker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;

public class InputInitialValues extends AppCompatActivity
{
    public InputInitialValues(){}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_initial_values);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(final View view)
            {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run()
                    {
                        if(isEmpty(R.id.personName1) || isEmpty(R.id.personName2) || isEmpty(R.id.personName3) || isEmpty(R.id.personName4) || isEmpty(R.id.personName5) || isEmpty(R.id.personName6) || isEmpty(R.id.personName7) || isEmpty(R.id.personName8))
                        {
                            Snackbar.make(view, "All fields must be completed before submitting", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                        else
                        {
                            updateList(getFieldText(R.id.personName1));
                            updateList(getFieldText(R.id.personName2));
                            updateList(getFieldText(R.id.personName3));
                            updateList(getFieldText(R.id.personName4));
                            updateList(getFieldText(R.id.personName5));
                            updateList(getFieldText(R.id.personName6));
                            updateList(getFieldText(R.id.personName7));
                            updateList(getFieldText(R.id.personName8));

                            DataStore data = DataStore.getCurrent();
                            data.setName(Name.RACKS_1, getFieldText(R.id.personName1));
                            data.setName(Name.RACKS_2, getFieldText(R.id.personName2));
                            data.setName(Name.RACKS_CURRENT, getFieldText(R.id.personName1));
                            data.setName(Name.BINS, getFieldText(R.id.personName3));
                            data.setName(Name.FURNITURE, getFieldText(R.id.personName4));
                            data.setName(Name.ELECTRICAL, getFieldText(R.id.personName5));
                            data.setName(Name.BRICBRAC, getFieldText(R.id.personName6));
                            data.setName(Name.BINS_ACC, getFieldText(R.id.personName7));
                            data.setName(Name.SHOES, getFieldText(R.id.personName8));

                            finish();
                        }
                    }
                });
                t.start();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(InputInitialValues.this, android.R.layout.simple_list_item_1, new ArrayList<>(DataStore.getCurrent().getNameList()));

        initTextField(R.id.personName1, adapter);
        initTextField(R.id.personName2, adapter);
        initTextField(R.id.personName3, adapter);
        initTextField(R.id.personName4, adapter);
        initTextField(R.id.personName5, adapter);
        initTextField(R.id.personName6, adapter);
        initTextField(R.id.personName7, adapter);
        initTextField(R.id.personName8, adapter);

    }

    public void initTextField(int field, ArrayAdapter  adapter)
    {
        AutoCompleteTextView ACTV = (AutoCompleteTextView)findViewById(field);
        ACTV.setAdapter(adapter);
        ACTV.setThreshold(1);
    }
    public boolean isEmpty(int textField)
    {
        AutoCompleteTextView ACTV = (AutoCompleteTextView)findViewById(textField);
        return ACTV.getText().toString().equals("");
    }

    public String getFieldText(int textField)
    {
        AutoCompleteTextView ACTV = (AutoCompleteTextView)findViewById(textField);

        return ACTV.getText().toString();
    }

    public void updateList(String input)
    {
        DataStore.getCurrent().updateNameList(input);
    }
}
