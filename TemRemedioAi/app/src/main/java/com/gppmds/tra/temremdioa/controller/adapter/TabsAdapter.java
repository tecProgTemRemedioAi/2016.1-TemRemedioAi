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

    private static final int tabsQuantity = 2;

    public TabsAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
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

    @Override
    public int getCount() {
        return tabsQuantity;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        final int DOWN_LIMIT = 0;
        final int UP_LIMIT = 1;

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