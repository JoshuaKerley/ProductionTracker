package com.newimagethrift.joshua.productiontracker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Set;

public class MainActivity extends AppCompatActivity
{
    DataStore data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Production Tracker - v2.0");

        DataStore.updateActivity(this);
        data = DataStore.init(MainActivity.this.getApplicationContext());

        this.inputValues();
    }

    @Override
    protected void onRestart()
    {
        super.onRestart();

        this.inputValues();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_manage_names)
        {
            startActivity(new Intent(MainActivity.this, ManageSavedNames.class));
        }
        else if (id == R.id.action_reset)
        {
            DataStore.getCurrent().resetDataDialog();
        }
        else if (id == R.id.action_edit_names)
        {
            editNames();
        }
        else if (id == R.id.action_about)
        {
            startActivity(new Intent(MainActivity.this, About.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void inputValues()
    {
        if(data.isEmpty())
        {
            startActivity(new Intent(MainActivity.this, InputInitialValues.class));
        }
        else
        {
            updateTextViews();
        }

    }

    public void updateTextViews()
    {
        TextView racks = (TextView)findViewById(R.id.racksNumber);
        TextView bins = (TextView)findViewById(R.id.binsNumber);
        TextView baskets = (TextView)findViewById(R.id.basketsNumber);
        TextView lastSweep = (TextView)findViewById(R.id.sweepNumber);

        String name1 = Name.RACKS_1.toString();
        String currentName = Name.RACKS_CURRENT.toString();

        racks.setText("Number of Items: " + data.getTotalItems(Name.RACKS_CURRENT)*88 + " - " + currentName);

        bins.setText("Number of Items: " + data.getTotalItems(ItemType.BINS) + " - " + Name.BINS.toString());

        int totalBaskets = data.getTotalItems(ItemType.FURNITURE)
                + data.getTotalItems(ItemType.ELECTRICAL)
                + data.getTotalItems(ItemType.BRICBRAC)
                + data.getTotalItems(ItemType.BINS_ACC)
                + data.getTotalItems(ItemType.SHOES);
        baskets.setText("Number of Baskets: " + totalBaskets);

        lastSweep.setText("last: " + Utilities.getLastSweep());
    }

    public void toggleRack(View view)
    {
        Utilities.toggleRack();

        updateTextViews();
    }

    public void addBin(final View view)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_bins_hanging);

        TextView name = (TextView) dialog.findViewById(R.id.textView9);
        name.setText("Add Bin for " + data.getName(Name.BINS) + "?");

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
                TextView number = (TextView) dialog.findViewById(R.id.editText);

                if(!number.getText().toString().equals(""))
                {
                    data.recordAddition(ItemType.BINS, Name.BINS, Integer.parseInt(number.getText().toString()));

                    dialog.dismiss();

                    Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "Bin Added", Snackbar.LENGTH_LONG).setAction("Action", null);

                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextSize(30);
                    snack.show();

                    updateTextViews();
                }
                else
                {
                    Toast toast = Toast.makeText(getApplicationContext(), "Number of Items Is Required", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

        dialog.show();
    }

    public void addSweep(final View view)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_sweep);

        TextView name = (TextView) dialog.findViewById(R.id.sweepDialogText);
        name.setText("Add floor sweep at " + new SimpleDateFormat("hh:mm aa").format(Calendar.getInstance().getTime()) + "?");

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
                data.recordAddition(ItemType.SWEEP, Name.SWEEP, 0);

                dialog.dismiss();

                Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "Floor Sweep Added", Snackbar.LENGTH_LONG).setAction("Action", null);

                View view = snack.getView();
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextSize(30);
                snack.show();

                updateTextViews();
            }
        });

        dialog.show();
    }



    public void editNames()
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_edit_names);

        Set<String> names = data.getNameList();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, new ArrayList<>(names));
        final AutoCompleteTextView ACTV = (AutoCompleteTextView)dialog.findViewById(R.id.newName);
        ACTV.setAdapter(adapter);
        ACTV.setThreshold(1);

        RadioButton button1 = (RadioButton)dialog.findViewById(R.id.radioRacks1);
        button1.setText("Racks 1 - " + data.getName(Name.RACKS_1));
        RadioButton button2 = (RadioButton)dialog.findViewById(R.id.radioRacks2);
        button2.setText("Racks 2 - " + data.getName(Name.RACKS_2));
        RadioButton button3 = (RadioButton)dialog.findViewById(R.id.radioBins);
        button3.setText("Bins - " + data.getName(Name.BINS));
        RadioButton button4 = (RadioButton)dialog.findViewById(R.id.radioFurniture);
        button4.setText("Furniture - " + data.getName(Name.FURNITURE));
        RadioButton button5 = (RadioButton)dialog.findViewById(R.id.radioElectronics);
        button5.setText("Electronics - " + data.getName(Name.ELECTRICAL));
        RadioButton button6 = (RadioButton)dialog.findViewById(R.id.radioBricBrac);
        button6.setText("Bric-Brac - " + data.getName(Name.BRICBRAC));
        RadioButton button7 = (RadioButton)dialog.findViewById(R.id.radioBinsAcc);
        button7.setText("Bins-Acc - " + data.getName(Name.BINS_ACC));
        RadioButton button8 = (RadioButton)dialog.findViewById(R.id.radioShoes);
        button8.setText("Shoes - " + data.getName(Name.SHOES));

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
                String input = ACTV.getText().toString();

                if(!input.equals(""))
                {
                    RadioGroup radioGroup = (RadioGroup) dialog.findViewById(R.id.radioGroup);
                    switch(radioGroup.getCheckedRadioButtonId())
                    {
                        case R.id.radioRacks1:
                            String currentName = data.getName(Name.RACKS_1);
                            String currentRacks = data.getName(Name.RACKS_CURRENT);
                            if(currentName.equals(currentRacks))
                            {
                                data.setName(Name.RACKS_CURRENT, input);
                            }
                            data.setName(Name.RACKS_1, input);
                            break;
                        case R.id.radioRacks2:
                            String currentName2 = data.getName(Name.RACKS_2);
                            currentRacks = data.getName(Name.RACKS_CURRENT);
                            if(currentName2.equals(currentRacks))
                            {
                                data.setName(Name.RACKS_CURRENT, input);
                            }
                            data.setName(Name.RACKS_2, input);
                            break;
                        case R.id.radioBins:
                            data.setName(Name.BINS, input);
                            break;
                        case R.id.radioFurniture:
                            data.setName(Name.FURNITURE, input);
                            break;
                        case R.id.radioElectronics:
                            data.setName(Name.ELECTRICAL, input);
                            break;
                        case R.id.radioBricBrac:
                            data.setName(Name.BRICBRAC, input);
                            break;
                        case R.id.radioBinsAcc:
                            data.setName(Name.BINS_ACC, input);
                            break;
                        case R.id.radioShoes:
                            data.setName(Name.SHOES, input);
                            break;
                    }
                    data.updateNameList(input);

                    dialog.dismiss();

                    Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "Name Changed Successfully", Snackbar.LENGTH_LONG).setAction("Action", null);

                    View view = snack.getView();
                    TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                    tv.setTextSize(30);
                    snack.show();

                    updateTextViews();
                }
            }
        });
        dialog.show();
    }

    public void gotoCorrections(View view)
    {
        startActivity(new Intent(MainActivity.this, Corrections.class));
    }
    public void gotoAddRack(View view)
    {
        startActivityForResult(new Intent(MainActivity.this, AddRack.class), 1);
    }

    public void gotoAddBasket(View view)
    {
        startActivityForResult(new Intent(MainActivity.this, AddBasket.class), 2);
    }

    public void gotoSubmit(View view)
    {
        startActivityForResult(new Intent(MainActivity.this, Submit.class), 3);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 1) {
            if(resultCode == Activity.RESULT_OK)
            {
                Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "Rack Added", Snackbar.LENGTH_LONG).setAction("Action", null);

                View view = snack.getView();
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextSize(30);
                snack.show();
            }
        }
        else if(requestCode == 2)
        {
            if(resultCode == Activity.RESULT_OK)
            {
                Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "Basket Added", Snackbar.LENGTH_LONG).setAction("Action", null);

                View view = snack.getView();
                TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
                tv.setTextSize(30);
                snack.show();
            }
        }
    }
}
