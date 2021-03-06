/**
 * File: CardListAdapterUBS.java
 * Purpose: this file set which ubs will be showed according to the search made by user.
 */
package com.gppmds.tra.temremdioa.controller.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;

import com.gppmds.tra.temremdioa.controller.adapter.holder.ViewHolderUBS;
import com.gppmds.tra.temremdioa.model.UBS;
import com.tra.gppmds.temremdioa.R;

import java.util.List;

public class CardListAdapterUBS extends RecyclerView.Adapter<ViewHolderUBS> implements Filterable{
    private static List<UBS> dataUBS;                // List of all ubs in database.
    private static List<UBS> filterDataUBS = null;  // List of ubs filtered.
    private static Context contextOpen;             // This context refers to activity that is open and calling this class.
    private static FilterSearchUBS filter = null;   // This variable refers to class that make filter search on ubs.
    private static Boolean showButtonMedicines;    // This button is for the user to select the desired medicine.
    private static Boolean showButtonInform;       // This button allows users to inform medicine in a ubs or not.

    public CardListAdapterUBS(Context context, List<UBS> dataUBS) {

        this.contextOpen = (Context) context;
        this.dataUBS = (List<UBS>) dataUBS;
        this.filterDataUBS = (List<UBS>) dataUBS;
        this.contextOpen = (Context) context;

        setShowButtonMedicines(true);
        setShowButtonInform(false);
        setMedicineName("");
        setMedicineDosage("");

    }

    /**
     * Method: getFilter()
     * Purpose:
     * @return filter
     */
    @Override
    public FilterSearchUBS getFilter() {

        if(filter == null) {
            Log.i("LOG", "\n" + "UBS filter is null");
            filter = new FilterSearchUBS(filterDataUBS, this);
        } else {
            Log.i("LOG", "\n" + "UBS filter is not null");
        }

        return filter;

    }

    /**
     * Method: onCreateViewHolder()
     * Purpose:
     * @return new ViewHolderUBS
     */
    @Override
    public ViewHolderUBS onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CardView view = (CardView) inflater.inflate(R.layout.card_list_ubs, parent, false);
        Log.i("LOG", "\n" + "Event of view holder ubs creation");

        if (inflater == null){
            Exception eventException = new Exception("Error on inflater of View Holder UBS.");
            try{
                throw eventException;
            } catch(Exception exception){
                exception.printStackTrace();
            }
        } else {
            //nothing to do
        }

        return new ViewHolderUBS(view);

    }

    /**
     * Method: onBindViewHolder()
     * Purpose:
     * @return
     */
    @Override
    public void onBindViewHolder(ViewHolderUBS holder, int position) {

        createUbsRowData(holder, position);
        validatingUbsRowData(holder);

    }

    /**
     * Method: getItemCount()
     * Purpose:
     * @return dataUBS.size()
     */
    @Override
    public int getItemCount() {

        Log.i("LOG", "\n" + "Counting how many UBS are in database");
        return dataUBS.size();

    }

    /**
     * Method: setShowButtonMedicines()
     * Purpose:
     * @return
     */
    public void setShowButtonMedicines(Boolean showButtonMedicines) {

        this.showButtonMedicines = (Boolean) showButtonMedicines;

    }

    /**
     * Method: getShowButtonMedicines()
     * Purpose:
     * @return showButtonMedicines
     */
    private Boolean getShowButtonMedicines() {

        return this.showButtonMedicines;

    }

    /**
     * Method: createFilter()
     * Purpose:
     * @return
     */
    public void createFilter() {

        filter = new FilterSearchUBS(filterDataUBS, this);
        Boolean test = getShowButtonMedicines();

    }

    /**
     * Method: setShowButtonInform()
     * Purpose:
     * @return
     */
    public void setShowButtonInform(boolean showButtonInform) {

        this.showButtonInform = (Boolean) showButtonInform;

    }

    /**
     * Method: getShowButtonInform()
     * Purpose:
     * @return showButtonInform
     */
    public boolean getShowButtonInform(){

        return this.showButtonInform;

    }

    /**
     * Method: setMedicineName()
     * Purpose:
     * @return
     */
    public void setMedicineName(String medicineName) {

        this.medicineName = medicineName;

    }

    /**
     * Method: getMedicineName()
     * Purpose:
     * @return medicineName
     */
    private static String medicineName = null;            // This variable refers to the medicine name.
    public String getMedicineName(){

        return this.medicineName;

    }

    /**
     * Method: setMedicineDosage()
     * Purpose:
     * @return
     */
    public void setMedicineDosage(String medicineDosage) {

        this.medicineDosage = medicineDosage;

    }

    /**
     * Method: getMedicineDosage()
     * Purpose:
     * @return medicineDosage
     */
    private static String medicineDosage = null;          // This variable refers to the medicine dosage.
    public String getMedicineDosage(){
        return this.medicineDosage;
    }

    private void createUbsRowData(ViewHolderUBS holder, int position) {
        UBS rowData = this.dataUBS.get(position);
        holder.getTextViewUbsName().setText(rowData.getUbsName());
        holder.getTextViewUbsNeighborhood().setText(rowData.getUbsNeighborhood());

    }

    private void validatingUbsRowData(ViewHolderUBS holder) {
        // this control structure refers to the  visibility and params of Button Medicine.
        if (!getShowButtonMedicines()) {
            holder.getBUTTON_SELECT_MEDICINE().setVisibility(View.GONE);
            Log.i("LOG", "\n" + "Medicine button is not visible");
        } else {
            Log.i("LOG", "\n" + "Medicine button is visible");
        }

        boolean nameMedicineValid = true;
        // this control structures refers verification of the medicine name to be saved.
        if(getMedicineName().length() > 0){
            nameMedicineValid = true;
        }
        else{
            nameMedicineValid = false;
        }

        // this control if the medicine name is valid
        if(nameMedicineValid){
            holder.medicineSelectedName = getMedicineName();
            Log.i("LOG", "\n" + "Medicine selected name is not empty");
        } else {
            holder.medicineSelectedName = "";
            Log.i("LOG", "\n" + "Medicine selected name is empty");
        }

        boolean medicineDosageValid = true;
        // this control if the medicine dosage is bigger than zero
        if(getMedicineDosage().length() > 0){
            medicineDosageValid = true;
        }
        else{
            medicineDosageValid = false;
        }

        // this control if the medicine dosage is valid
        if(medicineDosageValid){
            holder.medicineSelectedDosage = getMedicineDosage();
            Log.i("LOG", "\n" + "Medicine selected dosage is not empty");
        } else {
            holder.medicineSelectedDosage = "";
            Log.i("LOG", "\n" + "Medicine selected dosage is empty");
        }

        // this control structure refers to the visibility of Button Inform according to the state returned by the function getShowButtonInform().
        if(!getShowButtonInform()){
            holder.buttonUbsInform.setVisibility(View.GONE);
            Log.i("LOG", "\n" + "UBS inform button is not visible");
        } else {
            Log.i("LOG", "\n" + "UBS inform button is visible");
        }

    }

}
