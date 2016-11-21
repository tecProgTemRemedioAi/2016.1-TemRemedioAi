/**
 * File: FilterSearchUBS.java
 * Purpose: this file purpose is to filter search of UBS.
 */

package com.gppmds.tra.temremdioa.controller.adapter;

import android.util.Log;
import android.widget.Filter;

import com.gppmds.tra.temremdioa.model.UBS;

import java.util.ArrayList;
import java.util.List;

public class FilterSearchUBS extends Filter {

    private CardListAdapterUBS adapter;
    private List<UBS> filterList;

    /**
     * Method: FilterSearchUBS
     * Purpose:
     * @param filterList
     * @param adapter
     */
    public FilterSearchUBS(final List<UBS> filterList, CardListAdapterUBS adapter) {

        this.adapter = adapter;
        this.filterList = filterList;

    }

    /**
     * Name:
     * Purpose:
     * @param constraint
     * @return
     */
    @Override
    public FilterResults performFiltering(CharSequence constraint) {

        Log.i("LOG", "\n" + "Starting to filtering results of search UBS");

        FilterResults results = new FilterResults();

        // This method takes the user's search string and checks if there is any Ubs q contains these characters.
        if(constraint != null && constraint.length() > 0) {
            List<UBS> filteredUBSs = createFilteredList(constraint);

            results.count = filteredUBSs.size();
            results.values = filteredUBSs;
        } else {
            results.count = filterList.size();
            results.values = filterList;
        }

        Log.d("LOG", "\n" + "Returning from performFiltering method");
        return results;
    }

    /**
     * Name:
     * Purpose:
     * @param constraint
     * @param results
     */
    @Override
    public void publishResults(final CharSequence constraint, final FilterResults results) {

        Log.i("LOG", "\n" + "CardListAdapter attribute filled by UBS lists");

        adapter.dataUBS = (List<UBS>) results.values;
        adapter.notifyDataSetChanged();

        Log.d("LOG", "\n" + "Returning from publishResults method");

    }

    /**
     * Name:
     * Purpose:
     * @param constraint
     * @return filteredUBSs
     */
    private List<UBS> createFilteredList(CharSequence constraint){

        constraint = constraint.toString().toUpperCase();
        List<UBS> filteredUBSs = new ArrayList<>();

        // this paragraph should add UBS objects of a complete list of UBSs into a filtered list, to result the current search
        for (int i = 0; i < filterList.size(); i++) {
            boolean findContains = filterList.get(i).getUbsName().toUpperCase().contains(constraint);

            if(findContains) {
                try {
                    filteredUBSs.add(filterList.get(i));
                }catch(Exception exception){
                    exception.notify();
                    System.out.println("Impossible to add element on list.");
                }
            } else {
                    /* Nothing to do */
            }
        }
        return filteredUBSs;
    }

}
