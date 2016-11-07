/**
 * File: SelectMedicineActivity.java
 * Purpose: this file set all medicine information that was selected.
 */
package com.gppmds.tra.temremdioa.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.gppmds.tra.temremdioa.controller.adapter.CardListAdapterMedicine;
import com.gppmds.tra.temremdioa.model.Medicine;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tra.gppmds.temremdioa.R;

import java.util.ArrayList;
import java.util.List;

public class SelectMedicineActivity extends AppCompatActivity {

    private ArrayList<String> filterAttentionLevel;     // This variable refers to the filter list
                                                        // of medicine.
    private String ubsName;                             // This variable refers to the ubs name
                                                        // that contains the medicine.
    private String ubsAttentionLevel;                   // This variiable refers to the ubs
                                                        // attention level.

    /**
     * Method: onCreate()
     * Purpose: create the activity on the screen.
     * @return
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_medicine);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setInformationOfUBS();

        try {
            setTextViewSelectedUBS(getUbsName());
        } catch (Exception e) {
            // Exception was caught.
        }

        CardListAdapterMedicine claMedicine;
        claMedicine = new CardListAdapterMedicine(SelectMedicineActivity.this,
                getListOfMedicine(getFilterAttentionLevel()));
        claMedicine.setShowButtonUBSs(false);
        claMedicine.setShowButtonInform(true);
        claMedicine.setUbsName(getUbsName());

        try {
            createRecyclerView(claMedicine);
        } catch (Exception e) {
            // Exception was caught.
        }

        try {
            setTextViewMedicineQuantityFound(claMedicine.getItemCount());
        } catch (Exception e) {
            // Exception was caught.
        }
    }

    /**
     * Method: createNewLinearLayoutManager()
     * Purpose:
     * @return linearLayoutManager
     */
    public LinearLayoutManager createNewLinearLayoutManager() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());

        return linearLayoutManager;
    }

    /**
     * Method: createRecyclerView()
     * Purpose:
     * @param cardListAdapterMedicine
     */
    public void createRecyclerView(CardListAdapterMedicine cardListAdapterMedicine) throws Exception {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.medicine_recycler_view);

        if (recyclerView != null) {
            recyclerView.setLayoutManager(createNewLinearLayoutManager());
            recyclerView.setAdapter(cardListAdapterMedicine);
            recyclerView.setHasFixedSize(true);
        } else {
            Exception textUBSException = new Exception("Fail to found medicine_recycler_view");

            throw textUBSException;
        }
    }

    /**
     * Method: getListOfMedicine()
     * Purpose:
     * @return medicines
     */
    public List<Medicine> getListOfMedicine(ArrayList<String> filterAttentionLevel) {
        ParseQuery<Medicine> queryMedicine = Medicine.getQuery();
        queryMedicine.setLimit(1000);
        queryMedicine.whereContainedIn(Medicine.getMedicineAttentionLevelTitle(), filterAttentionLevel);
        queryMedicine.orderByAscending(Medicine.getMedicineDescriptionTitle());
        queryMedicine.fromLocalDatastore();
        List<Medicine> medicines = null;

        try {
            medicines = queryMedicine.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return medicines;
    }

    /**
     * Method: setTextViewSelectedUBS()
     * Purpose:
     * @param ubsSelected
     */
    private void setTextViewSelectedUBS(String ubsSelected) throws Exception {
        TextView textViewSelectedUBS = (TextView) findViewById(R.id.textViewSelectedUBS);

        if (textViewSelectedUBS != null) {
            textViewSelectedUBS.setText(ubsSelected);
        } else {
            Exception textUBSException = new Exception("Fail to found textViewSelectedUBS");

            throw textUBSException;
        }
    }

    /**
     * Method: setTextViewMedicineQuantityFound()
     * Purpose:
     * @param quantityFound
     */
    private void setTextViewMedicineQuantityFound(int quantityFound) throws Exception {
        TextView textViewMedicineQuantity = (TextView) findViewById(R.id.textViewMedicineQuantity);

        if (textViewMedicineQuantity != null) {
            textViewMedicineQuantity.setText("Encontrado(s): " + quantityFound);
        } else{
            Exception textUBSException = new Exception("Fail to found textViewMedicineQuantity");

            throw textUBSException;
        }
    }

    /**
     * Method: setInformationOfUBS()
     * Purpose:
     */
    private void setInformationOfUBS() {
        setUbsName(getIntent().getStringExtra("nomeUBS"));
        setUbsAttentionLevel(getIntent().getStringExtra("nivelAtencao"));
        setFilterAttentionLevel(getUbsAttentionLevel());
    }

    /**
     * Method: setFilterAttentionLevel()
     * Purpose:
     * @param ubsAttentionLevel
     */
    public void setFilterAttentionLevel(String ubsAttentionLevel) {
        String [] attentionLevelFilters = ubsAttentionLevel.split(",");
        filterAttentionLevel = new ArrayList<String>();

        for(int i = 0; i < attentionLevelFilters.length; i++) {
            Log.i("CLAUS WHERE", "Nível de atenção da UBS " + i + ": " + attentionLevelFilters[i]);
            filterAttentionLevel.add(attentionLevelFilters[i]);
        }
    }

    /**
     * Method: getFilterAttentionLevel()
     * Purpose:
     * @return filterAttentionLevel
     */
    public ArrayList<String> getFilterAttentionLevel() {
        return filterAttentionLevel;
    }

    /**
     * Method: setUbsName()
     * Purpose:
     * @param ubsName
     */
    public void setUbsName(String ubsName) {
        this.ubsName = ubsName;
    }

    /**
     * Method: setUbsAttentionLevel()
     * Purpose:
     * @param ubsAttentionLevel
     */
    public void setUbsAttentionLevel(String ubsAttentionLevel) {
        this.ubsAttentionLevel = ubsAttentionLevel;
    }

    /**
     * Method: getUbsAttentionLevel()
     * Purpose:
     * @return ubsAttentionLevel
     */
    public String getUbsAttentionLevel() {
        return this.ubsAttentionLevel;
    }

    /**
     * Method: getUbsName()
     * Purpose:
     * @return ubsName
     */
    public String getUbsName() {
        return this.ubsName;
    }
}
