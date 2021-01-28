package com.newimagethrift.joshua.productiontracker;

import java.util.ArrayList;

public class Totals
{
    public RackTotal racks1, racks2;
    public BinTotal bins;
    public BasketTotal furniture, electrical, bricbrac, bins_acc, shoes, baskets;
    public SweepTotal sweeps;

    public int lk_total_racks, mens_total_racks, lk_total_items, mens_total_items, racks_total_racks, racks_total_items;

    public Totals()
    {
        ArrayList<Item> list = DataStore.getCurrent().getDataList();

        racks1 = new RackTotal();
        racks2 = new RackTotal();
        bins = new BinTotal();
        furniture = new BasketTotal();
        electrical = new BasketTotal();
        bricbrac = new BasketTotal();
        bins_acc = new BasketTotal();
        shoes = new BasketTotal();
        baskets = new BasketTotal();
        sweeps = new SweepTotal();

        for(Item item : list)
        {
            switch(item.getType())
            {
                case RACK_LADIESKIDS:
                    if(item.getName().equals(Name.RACKS_1))
                        racks1.addLK(item);
                    else
                        racks2.addMens(item);
                    break;
                case RACK_MENS:
                    if(item.getName().equals(Name.RACKS_1))
                        racks1.addMens(item);
                    else
                        racks2.addMens(item);
                    break;
                case BINS:
                    bins.addItem(item);
                    break;
                case FURNITURE:
                    furniture.addItem(item);
                    baskets.addItem(item);
                    break;
                case ELECTRICAL:
                    electrical.addItem(item);
                    baskets.addItem(item);
                    break;
                case BRICBRAC:
                    bricbrac.addItem(item);
                    baskets.addItem(item);
                    break;
                case BINS_ACC:
                    bins_acc.addItem(item);
                    baskets.addItem(item);
                    break;
                case SHOES:
                    shoes.addItem(item);
                    baskets.addItem(item);
                    break;
                case SWEEP:
                    sweeps.addItem(item);
                    break;
            }
        }

        lk_total_racks = racks1.lk.racks + racks2.lk.racks;
        mens_total_racks = racks1.mens.racks + racks2.mens.racks;
        lk_total_items = lk_total_racks * 88;
        mens_total_items = mens_total_racks * 88;
        racks_total_racks = lk_total_racks + mens_total_racks;
        racks_total_items = racks_total_racks * 88;
    }

    public class ItemTotal
    {
        public String times;

        ItemTotal()
        {
            times = "";
        }

        private void addItem(Item item)
        {
            if(times.length() == 0)
                times += (item.getTime());
            else
                times += (" - " + item.getTime());
        }
    }

    public class RackTotal
    {
        public SingleRackTotal lk, mens;
        public int racks_total, items_total;

        RackTotal()
        {
            lk = new SingleRackTotal();
            mens = new SingleRackTotal();

            racks_total = 0;
            items_total = 0;
        }

        private void addLK(Item item)
        {
            lk.addItem(item);
            racks_total++;
            items_total += 88;
        }
        private void addMens(Item item)
        {
            mens.addItem(item);
            racks_total++;
            items_total += 88;
        }

        public class SingleRackTotal extends ItemTotal
        {
            int racks, items;

            SingleRackTotal()
            {
                racks = 0;
                items = 0;
            }
            private void addItem(Item item)
            {
                super.addItem(item);
                racks++;
                items += 88;
            }
        }
    }

    public class BinTotal extends ItemTotal
    {
        int items_total;

        BinTotal()
        {
            items_total = 0;
        }
        private void addItem(Item item)
        {
            super.addItem(item);
            items_total += item.getNumber();
        }
    }

    public class BasketTotal extends ItemTotal
    {
        int baskets;

        BasketTotal()
        {
            baskets = 0;
        }
        private void addItem(Item item)
        {
            super.addItem(item);
            baskets++;
        }
    }

    public class SweepTotal extends ItemTotal
    {
        int number;

        SweepTotal()
        {
            number = 0;
        }
        void addItem(Item item)
        {
            super.addItem(item);
            number++;
        }
    }

}
