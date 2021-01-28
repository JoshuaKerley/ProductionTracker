package com.newimagethrift.joshua.productiontracker;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class Utilities
{
    private static DataStore data = DataStore.getCurrent();

    public static void toggleRack()
    {
        String name1 = data.getName(Name.RACKS_1);
        String name2 = data.getName(Name.RACKS_2);
        String currentName = data.getName(Name.RACKS_CURRENT);

        currentName = (currentName.equals(name1)) ? name2 : name1;
        data.setName(Name.RACKS_CURRENT, currentName);
    }

//    public int getTotalRacks()
//    {
//        return getTotalItems(Name.RACKS_1, ItemType.RACK_MENS) +
//                getTotalItems(Name.RACKS_1, ItemType.RACK_LADIESKIDS) +
//                getTotalItems(Name.RACKS_2, ItemType.RACK_MENS) +
//                getTotalItems(Name.RACKS_2, ItemType.RACK_LADIESKIDS);
//    }

//    public static int[] parseArraylist()
//    {
//        ArrayList<Item> list = data.getDataList();
//
//        HashMap<Item, Integer> info = new HashMap<>();
//
//        //int[] info = new int[11];
//
//        for(int x = 0; x < list.size(); x++)
//        {
//
//            switch(list.get(x).getType())
//            {
//                case RACK_LADIESKIDS:
//                    if(list.get(x).getName().equals(Name.RACKS_1))
//                    {
//                        info[0]++;
//                    }
//                    else
//                    {
//                        info[2]++;
//                    }
//                    break;
//                case RACK_MENS:
//                    if(list.get(x).getName().equals(Name.RACKS_1))
//                    {
//                        info[1]++;
//                    }
//                    else
//                    {
//                        info[3]++;
//                    }
//                    break;
//                case  BINS:
//                    info[4] += list.get(x).getNumber();
//                    break;
//                case FURNITURE:
//                    info[5]++;
//                    break;
//                case ELECTRICAL:
//                    info[6]++;
//                    break;
//                case BRICBRAC:
//                    info[7]++;
//                    break;
//                case BINS_ACC:
//                    info[8]++;
//                    break;
//                case SHOES:
//                    info[9]++;
//                    break;
//                case SWEEP:
//                    info[10]++;
//            }
//        }
//        return info;
//    }

    public static String getLastSweep()
    {
        ArrayList<Item> list = data.getDataList();

        for(int x = list.size()-1; x > -1; x--)
        {
            if(list.get(x).getType().equals(ItemType.SWEEP))
            {
                return list.get(x).getTime();
            }
        }
        return "N/A";
    }
}
