package com.newimagethrift.joshua.productiontracker;

enum ItemType
{
    FURNITURE("Furniture"),
    ELECTRICAL("Electrical"),
    BRICBRAC("Bric-Brac"),
    BINS_ACC("Bins-Accessories"),
    SHOES("Shoes"),
    RACK_LADIESKIDS("Ladies/Kids"),
    RACK_MENS("Mens"),
    BINS("Bin"),
    SWEEP("Sweep");

    private final String name;

    ItemType(String name)
    {
        this.name = name;
    }
    @Override
    public String toString()
    {
        return name;
    }
}

enum Name
{
    RACKS_1,
    RACKS_2,
    RACKS_CURRENT,
    BINS,
    FURNITURE,
    ELECTRICAL,
    BRICBRAC,
    BINS_ACC,
    SHOES,
    SWEEP;

    @Override
    public String toString()
    {
        return DataStore.getCurrent().getName(this);
    }
}