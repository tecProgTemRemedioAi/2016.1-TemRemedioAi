/**
 * File: MedicineFragment.java
 * Purpose: this file set all medicine in fragment.
 */
package com.gppmds.tra.temremdioa.controller.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gppmds.tra.temremdioa.controller.adapter.CardListAdapterMedicine;
import com.gppmds.tra.temremdioa.model.Medicine;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tra.gppmds.temremdioa.R;

import java.util.ArrayList;
import java.util.List;

public class MedicineFragment extends Fragment{

    private RecyclerView medicineRecyclerView;              // This component contains a list of medicine cards.
    public static CardListAdapterMedicine medicineAdapter;  // This class makes the management of medicine cards.

    public MedicineFragment(){
    }

    /**
     * Method: onCreateView()
     * Purpose:
     * @return rootView
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) throws AssertionError{
        View rootView = inflater.inflate(R.layout.fragment_medicine, container, false);

        medicineRecyclerView = (RecyclerView) rootView.findViewById(R.id.medicine_recycler_view);
        medicineRecyclerView.setHasFixedSize(true);
        medicineRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        medicineRecyclerView.setAdapter(medicineAdapter);

        ArrayList medicineFragments = new <String> ArrayList();

        medicineAdapter = new CardListAdapterMedicine(MedicineFragment.this.getContext(), getListOfMedicines(medicineFragments));
        medicineAdapter.setShowButtonInform(false);
        medicineAdapter.setUbsName("");

        /* Checking return value */
        assert(rootView != null);
        if (rootView == null) {
            throw new AssertionError("The View rootView can't be null");
        } else {
            // Nothing to do.
        }

        return rootView;
    }

    /**
     * Method: getListOfMedicines()
     * Purpose: query medicine data from parse
     * @return medicines
     */
    public List<Medicine> getListOfMedicines(ArrayList<String> medicineFragments) throws AssertionError{
        /* Verify the integrity of data structure ArrayList */
        assert(medicineFragments != null): "The ArrayList medicineFragments can't be null";
        if (medicineFragments == null) {
            throw new AssertionError("The ArrayList medicineFragments can't be null");
        } else {
            // Nothing to do.
        }

        /* Query ubs data from parse */
        ParseQuery<Medicine> queryMedicine = Medicine.getQuery();
        queryMedicine.fromLocalDatastore();
        queryMedicine.setLimit(1000);

        List<Medicine> medicines = new <Medicine> ArrayList();

        try {
            medicines = queryMedicine.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /* Checking return value */
        assert(medicines != null);
        if (medicines == null) {
            throw new AssertionError("The ArrayList medicines can't be null");
        } else {
            // Nothing to do.
        }

        return medicines;
    }

    public static MedicineFragment newInstance(){
        return new MedicineFragment();
    }

    public static CardListAdapterMedicine getMedicineAdapter() {
        return medicineAdapter;
    }
}