/**
 * File: FilterSearchMedicine.java
 * Purpose: this file purpose is to filter search of medicines.
 */

package com.gppmds.tra.temremdioa.controller.adapter;

import android.util.Log;
import android.widget.Filter;

import com.gppmds.tra.temremdioa.model.Medicine;

import java.util.ArrayList;
import java.util.List;

public class FilterSearchMedicine extends Filter{

    private CardListAdapterMedicine adapter; // adapt card list by search result
    private List<Medicine> filterList; // a filtered list for search result

    /**
     * Method: FilterSearchMedicine.
     * Purpose: this method is used to create a medicine list filtered by name.
     * @param filterList
     * @param adapter
     */
    public FilterSearchMedicine(final List<Medicine> filterList, CardListAdapterMedicine adapter) {

        this.adapter = (CardListAdapterMedicine) adapter;
        this.filterList = (List<Medicine>) filterList;

    }

    /**
     * Method: performFiltering.
     * Purpose: this method is used to perform filtered results.
     * @param constraint
     * @return FilterResutls
     */
    @Override
    public FilterResults performFiltering(CharSequence constraint) {

        Log.i("LOG", "\n" + "Starting to filtering results of search medicine");

        FilterResults results = new FilterResults(); // filtered list for return the result of search

        //This control structure is used to check the parameter exist, to be user after this.
        if(constraint != null && constraint.length() > 0) {

            List<Medicine> filteredMedicines = (List<Medicine>) createFilteredMedicineList(constraint);

            final int listMedicinesSize = filteredMedicines.size();

            // checking list numeric interval
            if((listMedicinesSize >= 0) && (listMedicinesSize <= filterList.size())){

                results.count = (int) filteredMedicines.size();
                results.values = (Object) filteredMedicines;

            }else{
                Exception eventException = new Exception("Invalid numbers to size of filtered List.");
                try {
                    throw eventException;
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }

        } else {
            results.count = (int) filterList.size();
            results.values = (Object) filterList;
        }

        if(results == null){
            Exception eventException = new Exception("Error on find results for perform filtering.");
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
     * Method: publishResults.
     * Purpose: this method is used to publish results of filtering medicines.
     * @param constraint
     * @param results
     */
    @Override
    public void publishResults(final CharSequence constraint, final FilterResults results) {

        Log.i("LOG", "\n" + "CardListAdapter attribute filled by medicine lists");

        adapter.dataMedicine = (List<Medicine>) results.values;
        adapter.notifyDataSetChanged();

        Log.d("LOG", "\n" + "Returning from publishResults method");


    }

    /**
     * Name:
     * Purpose:
     * @param constraint
     * @return filteredMedicines
     */
    private List<Medicine> createFilteredMedicineList(CharSequence constraint) {

        constraint = (CharSequence) constraint.toString().toUpperCase();
        List<Medicine> filteredMedicines = new ArrayList<Medicine>();

        // this paragraph should add medicine objects of a complete list of medicines into a filtered list, to result the current search
        for (int i = 0; i < filterList.size(); i++) {
            final boolean checkingContains = (boolean) filterList.get(i).getMedicineDescription().toUpperCase().contains(constraint); // checks the contains of constraints into filtered list
            //This control structure is used to check if current filterList element contains the constrains.
            if(checkingContains) {
                try {
                    filteredMedicines.add(filterList.get(i));
                }catch(Exception exception){
                    exception.notify();
                    System.out.println("Impossible to add element of medicines into filtered list.");
                }
            } else {
                    /* Nothing to do */
            }
        }

        return filteredMedicines;
    }
}
