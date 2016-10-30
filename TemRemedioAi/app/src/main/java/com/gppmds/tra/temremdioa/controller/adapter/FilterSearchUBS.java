package com.gppmds.tra.temremdioa.controller.adapter;

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

        FilterResults results = new FilterResults();

        // This method takes the user's search string and checks if there is any Ubs q contains these characters.
        if(constraint != null && constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();
            List<UBS> filteredUBSs = new ArrayList<>();

            for (int i = 0; i < filterList.size(); i++) {
                boolean findContains = filterList.get(i).getUbsName().toUpperCase().contains(constraint);

                if(findContains) {
                    filteredUBSs.add(filterList.get(i));
                } else {
                    /* Nothing to do */
                }
            }

            results.count = filteredUBSs.size();
            results.values = filteredUBSs;
        } else {
            results.count = filterList.size();
            results.values = filterList;
        }

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

        adapter.dataUBS = (List<UBS>) results.values;
        adapter.notifyDataSetChanged();

    }

}
