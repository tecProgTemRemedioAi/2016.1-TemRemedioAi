package com.gppmds.tra.temremdioa.controller.adapter;

import android.widget.Filter;

import com.gppmds.tra.temremdioa.model.UBS;

import java.util.ArrayList;
import java.util.List;

public class FilterSearchUBS extends Filter {
    CardListAdapterUBS adapter;
    List<UBS> filterList;


    public FilterSearchUBS(List<UBS> filterList, CardListAdapterUBS adapter)
    {
        this.adapter = adapter;
        this.filterList = filterList;

    }
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();

        if(constraint != null && constraint.length() > 0)
        {
            constraint = constraint.toString().toUpperCase();
            List<UBS> filteredUBSs = new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                if(filterList.get(i).getEstablishmentName().toUpperCase().contains(constraint))
                {
                    filteredUBSs.add(filterList.get(i));
                }
            }

            results.count = filteredUBSs.size();
            results.values = filteredUBSs;
        }else
        {
            results.count = filterList.size();
            results.values = filterList;

        }


        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.dataUBS = ( List<UBS>) results.values;

        adapter.notifyDataSetChanged();
    }
}