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
    public static List<UBS> dataUBS;        // List of all ubs in database.
    List<UBS> filterDataUBS;                // List of ubs filtered.

    private static Context contextOpen;     // This context refers to activity that is open and calling this class.
    FilterSearchUBS filter;                 // This variable refers to class that make filter search on ubs.

    private Boolean showButtonMedicines;    // This button is for the user to select the desired medicine.
    private Boolean showButtonInform;       // This button allows users to inform medicine in a ubs or not.
    private String medicineName;            // This variable refers to the medicine name.
    private String medicineDosage;          // This variable refers to the medicine dosage.

    public CardListAdapterUBS(Context context, List<UBS> dataUBS) {
        this.contextOpen = context;
        this.dataUBS = dataUBS;
        this.filterDataUBS = dataUBS;
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
            // Nothing to do
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

        return new ViewHolderUBS(view);
    }

    /**
     * Method: onBindViewHolder()
     * Purpose:
     * @return
     */
    @Override
    public void onBindViewHolder(ViewHolderUBS holder, int position) {
        UBS rowData = this.dataUBS.get(position);
        holder.getTextViewUbsName().setText(rowData.getUbsName());
        holder.getTextViewUbsNeighborhood().setText(rowData.getUbsNeighborhood());

        if (!getShowButtonMedicines()) {
            holder.getButtonSelectMedicine().setVisibility(View.GONE);
            Log.i("LOG", "\n" + "Medicine button is not visible");
        } else {
            // Nothing to do
            Log.i("LOG", "\n" + "Medicine button is visible");
        }

        if(!getMedicineName().isEmpty()){
            holder.medicineSelectedName = getMedicineName();
            Log.i("LOG", "\n" + "Medicine selected name is not empty");
        } else {
            holder.medicineSelectedName = "";
            Log.i("LOG", "\n" + "Medicine selected name is empty");
        }

        if(!getMedicineDosage().isEmpty()){
            holder.medicineSelectedDosage = getMedicineDosage();
            Log.i("LOG", "\n" + "Medicine selected dosage is not empty");
        } else {
            holder.medicineSelectedDosage = "";
            Log.i("LOG", "\n" + "Medicine selected dosage is empty");
        }

        if(!getShowButtonInform()){
            holder.buttonUbsInform.setVisibility(View.GONE);
            Log.i("LOG", "\n" + "UBS inform button is not visible");
        } else {
            // Nothing to do
            Log.i("LOG", "\n" + "UBS inform button is visible");
        }
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
        this.showButtonMedicines = showButtonMedicines;
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
        this.showButtonInform = showButtonInform;
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
    public String getMedicineDosage(){
        return this.medicineDosage;
    }

}
