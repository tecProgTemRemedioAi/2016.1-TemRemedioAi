/**
 * File: FilterSearchMedicine.java
 * Purpose: this file purpose is to filter search of medicines.
 */

package com.gppmds.tra.temremdioa.controller.adapter;

import android.widget.Filter;
import com.gppmds.tra.temremdioa.model.Medicine;
import java.util.ArrayList;
import java.util.List;

public class FilterSearchMedicine extends Filter{

    private CardListAdapterMedicine adapter;
    private List<Medicine> filterList;

    /**
     * Method: FilterSearchMedicine.
     * Purpose: this method is used to create a medicine list filtered by name.
     * @param filterList
     * @param adapter
     */
    public FilterSearchMedicine(List<Medicine> filterList, CardListAdapterMedicine adapter) {

        this.adapter = adapter;
        this.filterList = filterList;

    }

    /**
     * Method: performFiltering.
     * Purpose: this method is used to perform filtered results.
     * @param constraint
     * @return FilterResutls
     */
    @Override
    public FilterResults performFiltering(CharSequence constraint) {

        FilterResults results = new FilterResults();

        if(constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();
            List<Medicine> filteredMedicines = new ArrayList<>();

            for (int i = 0; i < filterList.size(); i++) {
                boolean checkingContains = filterList.get(i).getMedicineDescription().toUpperCase().contains(constraint);
                if(checkingContains) {
                    filteredMedicines.add(filterList.get(i));
                } else {
                    /* Nothing to do */
                }
            }
            results.count = filteredMedicines.size();
            results.values = filteredMedicines;

        } else {
            results.count = filterList.size();
            results.values = filterList;
        }

        return results;
    }

    /**
     * Method: publishResults.
     * Purpose: this method is used to publish results of filtering medicines.
     * @param constraint
     * @param results
     */
    @Override
    public void publishResults(CharSequence constraint, FilterResults results) {

        adapter.dataMedicine = (List<Medicine>) results.values;
        adapter.notifyDataSetChanged();

    }
}
