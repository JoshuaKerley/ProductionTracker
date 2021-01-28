package com.newimagethrift.joshua.productiontracker;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class Corrections extends AppCompatActivity {

    ListView lv;
    HashMap<String, String> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_corrections);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        lv = (ListView) findViewById(R.id.listView1);
        final ArrayList<HashMap<String, String>> feedList= new ArrayList<>();

        final ArrayList<Item> list = DataStore.getCurrent().getDataList();

        for(int x = list.size()-1; x >= 0; x--)
        {
            map = new HashMap<>();
            map.put("time", "    " + list.get(x).getTime());
            if(list.get(x).getType().equals(ItemType.BINS))
                map.put("type", "bin - " + list.get(x).getNumber());
            else
                map.put("type", list.get(x).getType().toString());

            map.put("name", list.get(x).getName().toString());
            feedList.add(map);
        }

        final SimpleAdapter simpleAdapter = new SimpleAdapter(this, feedList, R.layout.custom_list_row, new String[]{"time", "type", "name"}, new int[]{R.id.time, R.id.type, R.id.name});
        lv.setAdapter(simpleAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id)
            {
                final int loc = list.size()-(position+1);

                final Dialog dialog = new Dialog(Corrections.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.custom_dialog);

                TextView name = (TextView) dialog.findViewById(R.id.textView9);
                name.setText("Are you sure you want to delete:\n\n" + list.get(loc).toString());
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

                        list.remove(loc);
                        DataStore.getCurrent().writeList(list);

                        Animation anim = AnimationUtils.loadAnimation(
                                Corrections.this, android.R.anim.slide_out_right
                        );
                        anim.setDuration(500);
                        lv.getChildAt(position).startAnimation(anim);

                        new Handler().postDelayed(new Runnable()
                        {
                            public void run()
                            {
                                feedList.remove(position);
                                simpleAdapter.notifyDataSetChanged();
                            }
                        }, anim.getDuration());

                        Snackbar snack = Snackbar.make(findViewById(android.R.id.content), "Item Deleted", Snackbar.LENGTH_LONG).setAction("Action", null);

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
}
