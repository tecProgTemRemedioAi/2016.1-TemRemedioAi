package com.gppmds.tra.temremdioa.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.gppmds.tra.temremdioa.controller.adapter.CardListAdapterUBS;
import com.gppmds.tra.temremdioa.model.UBS;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.tra.gppmds.temremdioa.R;

import java.util.ArrayList;
import java.util.List;

public class SelectUBSActivity extends AppCompatActivity {

    private ArrayList<String> filterAttentionLevel;
    private String medicineName;            // This variable refers the name of medicine which will be shown on the card.
    private String medicineAttentionLevel;  // This variable refers the attention level of medicine which will be shown on the card.
    private String medicineDosage;          // This variable refers the dosage of medicine which will be shown on the card.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        
        // check the parameter value.
        assert(savedInstanceState != null);

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_select_ubs);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            setMedicineInfo();

            setTextViewSelectedMedicine(getMedicineName());

        } catch (Throwable e) {
            // exception was caught
            e.printStackTrace();
        }

        createCardListAdapterForUBS();

    }

    /**
     * Method: createLinearLayoutManager.
     * Purpose: this method create a linear layout.
     * @return
     */
    public LinearLayoutManager createLinearLayoutManager() {
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        return llm;
    }

    /**
     * Method: createRecyclerView.
     * Purpose: set all informations of cards.
     * @param cardListAdapterUBS
     * @throws Exception
     */
    public void createRecyclerView(CardListAdapterUBS cardListAdapterUBS) throws Exception {
        assert(cardListAdapterUBS != null);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.ubs_recycler_view);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(createLinearLayoutManager());
            recyclerView.setAdapter(cardListAdapterUBS);
            recyclerView.setHasFixedSize(true);
        } else {
            Exception exceptionUbsRecyclerView= new Exception("Fail to found ubs_recycler_view");

            throw exceptionUbsRecyclerView;
        }
    }

    /**
     * Method: getListOfUbs.
     * Purpose: this method get the list with all Ubs from parse.
     * @param filterAttentionLevel
     * @return
     */
    public List<UBS> getListOfUbs(ArrayList<String> filterAttentionLevel) throws AssertionError {

        /* Verify the integrity of data structure List */
        assert(filterAttentionLevel != null): "The ArrayList filter can't be null";

        if (filterAttentionLevel == null) {
            throw new AssertionError("The ArrayList filter can't be null");
        } else {
            // Nothing to do.
        }

        /* Query ubs data from parse */
        ParseQuery<UBS> queryUBS = UBS.getQuery();
        queryUBS.whereContainedIn(UBS.getUbsAttentionLevelTitle(), filterAttentionLevel);
        queryUBS.orderByAscending(UBS.getUbsNameTitle());
        queryUBS.fromLocalDatastore();

        /* Create a ubs list */
        List<UBS> ubsList = null;

        try {
            ubsList = queryUBS.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /* Checking return value */
        assert(ubsList != null);

        if (filterAttentionLevel == null) {
            throw new AssertionError("The List ubsList can't be null");
        } else {
            // Nothing to do.
        }

        return ubsList;

    }

    /**
     * Method: createCardListAdapterForUBS
     * Purpose: this method create an card list for ubs adapter
     */
    private void createCardListAdapterForUBS() {

        CardListAdapterUBS claUbs = new CardListAdapterUBS(SelectUBSActivity.this, getListOfUbs(getFilterAttentionLevel()));
        claUbs.setShowButtonMedicines(false);
        claUbs.setShowButtonInform(true);
        claUbs.setMedicineName(getMedicineName());
        claUbs.setMedicineDosage(getMedicineDosage());

        try {
            createRecyclerView(claUbs);
        } catch (Exception e) {
            // exception was caught
        }

        try {
            setTextViewUbsQuantityFound(claUbs.getItemCount());
        } catch (Exception e) {
            // exception was caught
        }

    }

    /**
     * Method: setTextViewSelectedMedicine.
     * Purpose: this method set name of selected medicine.
     * @param medicineSelected
     * @throws Exception
     */
    private void setTextViewSelectedMedicine(String medicineSelected) throws Exception {

        assert(medicineSelected != null);

        TextView textViewMedicineSelected = (TextView) findViewById(R.id.textViewMedicineSelected);

        if (textViewMedicineSelected != null) {
            textViewMedicineSelected.setText(medicineSelected);
        } else {
            Exception exceptionOnTextViewMedicineSlected = new Exception("Fail to found textViewMedicineSelected");
            throw exceptionOnTextViewMedicineSlected;
        }

    }

    /**
     * Method: setTextViewUbsQuantityFound.
     * Purpose: this method set in how many Ubs found through research.
     * @param quantityFound
     * @throws Exception
     */
    private void setTextViewUbsQuantityFound(int quantityFound) throws Exception {

        final int LIMIT = 0;

        assert(quantityFound <= Integer.MAX_VALUE);
        assert(quantityFound >= Integer.MIN_VALUE);
        assert(quantityFound >= LIMIT);

        TextView textViewUbsQuantity = (TextView) findViewById(R.id.textViewUbsQuantity);

        if (textViewUbsQuantity != null) {
            textViewUbsQuantity.setText("Encontrada(s): " + quantityFound);
        } else {
            Exception exceptionOntTextViewUbsQuantity = new Exception("Fail to found textViewUbsQuantity");
            throw exceptionOntTextViewUbsQuantity;
        }
    }

    /**
     * Method: setMedicineInfo.
     * Purpose: this method set all information about medicine.
     */
    public void setMedicineInfo() {

        setMedicineName(getIntent().getStringExtra("nomeRemedio"));
        setMedicineDosage(getIntent().getStringExtra("medicineDos"));
        setMedicineAttentionLevel(getIntent().getStringExtra("nivelAtencao"));
        setFilterAttentionLevel(getMedicineAttentionLevel());

    }

    /**
     * Method: setFilterAttentionLevel.
     * Purpose: set an Array with attention level of medicines.
     * @param medicineAttentionLevel
     */
    public void setFilterAttentionLevel(String medicineAttentionLevel) {

        assert(medicineAttentionLevel != null);

        try {

            String[] attentionLevelFilters = medicineAttentionLevel.split(",");

            /* Getting attention level count */
            filterAttentionLevel = new ArrayList<String>();

            for (int i = 0; i < attentionLevelFilters.length; i++) {
                if ("HO".equalsIgnoreCase(attentionLevelFilters[i])) {
                    attentionLevelFilters[i] = "HO,AB";
                }
                else {
                    // Nothing to do.
                }
                Log.i("CLAUS WHERE", "Nível de atenção do Remédio " + i + ": "
                        + attentionLevelFilters[i]);
                filterAttentionLevel.add(attentionLevelFilters[i]);
            }

        } catch (Throwable exception){
            exception.printStackTrace();
        }
    }

    /**
     * Method: getFilterAttentionLevel.
     * Purpose: this method return an ArrayList filtrated according to level attention.
     * @return
     */
    public ArrayList<String> getFilterAttentionLevel() {

        return filterAttentionLevel;

    }

    /**
     * Method: setMedicineName.
     * Purpose: set the medicine name.
     * @param medicineName
     */
    public void setMedicineName(String medicineName) {

        this.medicineName = medicineName;

    }

    /**
     * Method: setMedicineDosage.
     * Purpose: set the dosage of medicine.
     * @param medicineDosage
     */
    public void setMedicineDosage(String medicineDosage) {

        this.medicineDosage = medicineDosage;

    }

    /**
     * Method: setMedicineAttentionLevel.
     * Purpose: set the attention level of medicine.
     * @param medicineAttentionLevel
     */
    public void setMedicineAttentionLevel(String medicineAttentionLevel) {

        this.medicineAttentionLevel = medicineAttentionLevel;

    }

    /**
     * Method: getMedicineAttentionLevel.
     * Purpose: get the string that reffers the medicine level attention.
     * @return
     */
    public String getMedicineAttentionLevel() {

        return this.medicineAttentionLevel;

    }

    /**
     * Method: getMedicineName.
     * Purpose: get the medicine name.
     * @return
     */
    public String getMedicineName() {

        return this.medicineName;

    }

    /**
     * Method: getMedicineDosage.
     * Purpose: get the value of medicine dosage.
     * @return
     */
    public String getMedicineDosage() {

        return this.medicineDosage;

    }
}
