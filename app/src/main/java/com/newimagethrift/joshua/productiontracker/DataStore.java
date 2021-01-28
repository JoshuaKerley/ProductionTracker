package com.newimagethrift.joshua.productiontracker;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DataStore
{
    private static DataStore store;
    private SharedPreferences preferences;

    private HashMap<Name, String> nameKey;

    private static WeakReference<Activity> mActivityRef;
    public static void updateActivity(Activity activity) {
        mActivityRef = new WeakReference<>(activity);
    }

    public static DataStore init(Context c)
    {
        if(store == null)
            store = new DataStore();
        return store;
    }
    public static DataStore getCurrent()
    {
        return store;
    }

    private DataStore()
    {
        preferences = mActivityRef.get().getSharedPreferences("MyPREFERENCES", Context.MODE_PRIVATE);

        nameKey = new HashMap<>();
        nameKey.put(Name.RACKS_1, "NAME_RACKS_1");
        nameKey.put(Name.RACKS_2, "NAME_RACKS_2");
        nameKey.put(Name.RACKS_CURRENT, "CURRENT_RACKS");
        nameKey.put(Name.BINS, "NAME_BINS");
        nameKey.put(Name.FURNITURE, "NAME_FURNITURE");
        nameKey.put(Name.ELECTRICAL, "NAME_ELECTRONICS");
        nameKey.put(Name.BRICBRAC, "NAME_BRICBRAC");
        nameKey.put(Name.BINS_ACC, "NAME_BINS_ACCESSORIES");
        nameKey.put(Name.SHOES, "NAME_SHOES");

        if(!preferences.contains("JSON_LIST"))
            writeList(new ArrayList<Item>());
    }

    public SharedPreferences getPreferences()
    {
        return preferences;
    }

    public ArrayList<Item> getDataList()
    {
        Gson gson = new Gson();
        String json_list = preferences.getString("JSON_LIST", null);
        return gson.fromJson(json_list, new TypeToken<ArrayList<Item>>() {}.getType());
    }
    public void writeList(ArrayList<Item> list)
    {
        Gson gson = new Gson();
        String json_list = gson.toJson(list);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("JSON_LIST", json_list);

        editor.apply();
    }

    public void recordAddition(ItemType type, Name name, int number)
    {
        String strTime = new SimpleDateFormat("hh:mm aa").format(Calendar.getInstance().getTime());

        if(name.equals(Name.RACKS_CURRENT))
            name = getName(Name.RACKS_CURRENT).equals(getName(Name.RACKS_1)) ? Name.RACKS_1 : Name.RACKS_2;

        ArrayList<Item> list = getDataList();
        list.add(new Item(type, number, name));
        writeList(list);
    }

    public int getTotalItems(Name name, ItemType type)
    {
        if(name != null && name.equals(Name.RACKS_CURRENT))
            name = getName(Name.RACKS_CURRENT).equals(getName(Name.RACKS_1)) ? Name.RACKS_1 : Name.RACKS_2;

        int total = 0;
        for(Item item : getDataList())
        {
            if(((name == null) || (item.getName().equals(name))) && ((type == null) || item.getType().equals(type)))
            {
                //System.out.println("MATCH: " + item.toString());
                if(item.getType().equals(ItemType.BINS))
                    total += item.getNumber();
                else
                    total++;
            }
        }
        return total;
    }
    public int getTotalItems(Name name)
    {
        return getTotalItems(name, null);
    }
    public int getTotalItems(ItemType type)
    {
        return getTotalItems(null, type);
    }

    public void resetData()
    {
        SharedPreferences.Editor editor = preferences.edit();

        for(Map.Entry name : nameKey.entrySet())
        {
            editor.remove((String)name.getValue());
        }
        editor.remove("JSON_LIST");

        editor.apply();

        Snackbar snack = Snackbar.make(mActivityRef.get().findViewById(android.R.id.content), "All Data Reset", Snackbar.LENGTH_LONG).setAction("Action", null);

        View view = snack.getView();
        TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextSize(30);
        snack.show();

        //mActivityRef.get().recreate();
    }

    public void resetDataDialog()
    {
        final Dialog dialog = new Dialog(mActivityRef.get());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_reset);

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

                resetData();
            }
        });

        dialog.show();
    }

    public boolean isEmpty()
    {
        return !preferences.contains(nameKey.get(Name.RACKS_1));
    }


    public String getName(Name name)
    {
        return preferences.getString(nameKey.get(name), null);
    }
    public void setName(Name nameType, String name)
    {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(nameKey.get(nameType), name);

//        if(Name.RACKS_CURRENT != null && nameType.toString().equals(Name.RACKS_CURRENT.toString()))
//            editor.putString(nameKey.get(Name.RACKS_CURRENT), name);

        editor.apply();
    }

    public Set<String> getNameList()
    {
        return preferences.getStringSet("NAMES", new HashSet<String>());
    }
    public void updateNameList(String name)
    {
        Set<String> names = getNameList();

        if(names.add(name.trim()))
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putStringSet("NAMES", names);
            editor.apply();
        }
    }
}
