package com.newimagethrift.joshua.productiontracker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Item
{
    private String time;
    private ItemType type;
    private int number;
    private Name name;

    public Item(ItemType type, int number, Name name)
    {
        time = new SimpleDateFormat("hh:mm aa").format(Calendar.getInstance().getTime());
        this.type = type;
        this.number = number;
        this.name = name;
    }

    public String getTime()
    {
        return time;
    }

    public ItemType getType()
    {
        return type;
    }

    public int getNumber()
    {
        return number;
    }

    public Name getName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        return getTime() + "     " + getType() + "     " + getName();
    }
}
