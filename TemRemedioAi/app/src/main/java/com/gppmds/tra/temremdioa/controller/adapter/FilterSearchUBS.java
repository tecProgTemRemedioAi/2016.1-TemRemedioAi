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

        this.adapter = (CardListAdapterUBS) adapter;
        this.filterList = (List<UBS>) filterList;

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

            List<UBS> filteredUBSs = (List<UBS>) createFilteredList(constraint);

            final int listUBSSize = filteredUBSs.size();

            // checking list numeric interval
            if((listUBSSize >= 0) && (listUBSSize <= filterList.size())){

                results.count = (int) filteredUBSs.size();
                results.values = (Object) filteredUBSs;

            }else{
                Exception eventException = new Exception("Invalid numbers to size of filtered List.");
                try {
                    throw eventException;
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        } else {
            results.count = filterList.size();
            results.values = filterList;
        }

        if(results == null){
            Exception eventException = new Exception("Error! Impossible on find results for perform filtering on UBS class.");
            try {
                throw eventException;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } else {
            // nothing to do
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

        constraint = (CharSequence) constraint.toString().toUpperCase();
        
        List<UBS> filteredUBSs = new ArrayList<UBS>();

        // this paragraph should add UBS objects of a complete list of UBSs into a filtered list, to result the current search
        for (int i = 0; i < filterList.size(); i++) {

            final boolean findContains = (boolean) filterList.get(i).getUbsName().toUpperCase().contains(constraint);

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
