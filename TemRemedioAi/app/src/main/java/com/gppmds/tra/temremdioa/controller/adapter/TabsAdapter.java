/**
 * File: TabsAdapter.java
 * Purpose: this file set all paramters about the existing tabs on app.
 */

package com.gppmds.tra.temremdioa.controller.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.gppmds.tra.temremdioa.controller.fragment.MedicineFragment;
import com.gppmds.tra.temremdioa.controller.fragment.UBSFragment;

public class TabsAdapter extends FragmentPagerAdapter{

    private static final int tabsQuantity = 2;  // This variable refers to the number of tabs present in the application.

    public TabsAdapter(FragmentManager fm){
        super(fm);
    }

    /**
     * Method: getItem
     * Purpose: this method instance the medicine or the UBS according to position of item.
     * @param position
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        // this control structure check the position and instance a medicne fragment or a UBS  fragment.
        switch (position){
            case 0:
                return MedicineFragment.newInstance();
            case 1:
                return UBSFragment.newInstance();
            default:
                /* Nothing to do */
                break;
        }
        return null;
    }

    /**
     *
     * @return
     */
    @Override
    public int getCount() {
        return tabsQuantity;
    }

    /**
     *
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        final int DOWN_LIMIT = 0;   // this variable refers to the down limit positon to set name of tab.
        final int UP_LIMIT = 1;     // this variable refers to the up limit position to set name of tab.

        if (DOWN_LIMIT != 0){
            Exception eventException = new Exception ("Error on Down Limit value.");
            try{
                throw eventException;
            } catch(Exception exception){
                exception.printStackTrace();
            }
        } else {
            //nothing to do
        }

        if (UP_LIMIT != 1){
            Exception eventException = new Exception ("Error on Up Limit value.");
            try{
                throw eventException;
            } catch(Exception exception){
                exception.printStackTrace();
            }
        } else {
            //nothing to do
        }

        assert(position >= DOWN_LIMIT && position <= UP_LIMIT);

        try {
            switch (position) {
                case 0:
                    return "Remédio";
                case 1:
                    return "UBS";
                default:
                    /* Nothing to do */
                    break;
            }
        }catch (Throwable exception){
            exception.printStackTrace();
        }
        return null;
    }
}