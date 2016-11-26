package com.gppmds.tra.temremdioa.controller.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.RelativeLayout;

import com.gppmds.tra.temremdioa.controller.adapter.holder.ViewHolderMedicine;
import com.gppmds.tra.temremdioa.model.Medicine;
import com.tra.gppmds.temremdioa.R;

import java.util.List;

public class CardListAdapterMedicine extends RecyclerView.Adapter<ViewHolderMedicine> implements Filterable{
    public static List<Medicine> dataMedicine;  // This list refers to stores data to fill in the cards.
    List<Medicine> filterDataMedicine;          // This list refers to stores filtered data to fill in the cards.
    Context contextOpen;                        //
    FilterSearchMedicine filter;                // This filter refers to medicines searched by users.
    private static Boolean showButtonUBSs;             // This variable refers to UBS button visibility.
    private static Boolean showButtonInform;           // This variable refers to Inform button visibility.
    private static String ubsName;                     // This varaible refers to UBS name.

    public CardListAdapterMedicine(Context context, List<Medicine> dataMedicine) {
        this.contextOpen = context;
        this.dataMedicine = dataMedicine;
        this.filterDataMedicine = dataMedicine;
        setShowButtonUBSs(true);
        setShowButtonInform(false);
        setUbsName("");
    }

    /**
     * Method: onCreateViewHolder.
     * Purpose:
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolderMedicine onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        CardView view = (CardView) inflater.inflate(R.layout.card_list_medicine, parent, false);
        Log.i("LOG", "\n" + "CardView was inflated on onCreateViewHolder, line 48 on CardListAdapterMedicine");

        if (inflater == null){
            Exception eventException = new Exception("Error on inflater of View Holder Medicine.");
            try{
                throw eventException;
            } catch(Exception exception){
                exception.printStackTrace();
            }
        } else {
            //nothing to do
        }
        return new ViewHolderMedicine(view);
    }

    /**
     * Method: onBindViewHolder.
     * Purpose:
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolderMedicine holder, int position) {
        createMedicineRowData(holder, position);
        validatingMedicineRowData(holder);

    }

    /**
     * Method: getFilter.
     * Purpose: this is a get method for the filter used to medicine research.
     * @return
     */
    @Override
    public FilterSearchMedicine getFilter() {
        // this control structure check the if the filter exist, if not a filter is create.
        if(filter == null) {
            filter = new FilterSearchMedicine(filterDataMedicine, this );
            Log.i("LOG", "\n" + "Set a new filter because doens't exist a filter,on FilterSearchMedicine , line 98 on CardListAdapterMedicine");
        } else {
            // Nothing to do
        }

        return filter;
    }

    /**
     * Method: getItemCount.
     * Purpose: this method refers to size of a List with medicine data.
     * @return
     */
    @Override
    public int getItemCount() {
        return dataMedicine.size();
    }

    /**
     * Method: setShowButtonUBSs.
     * Purpose: this method set the boolean varaible about the status of Button Ubs.
     * @param showButtonUBSs
     */
    public void setShowButtonUBSs(Boolean showButtonUBSs) {
        this.showButtonUBSs = showButtonUBSs;
    }

    /**
     * Method: setShowButtonInform.
     * Purpose: this method set the boolean varaible about the status of Button Inform.
     * @param showButtonInform
     */
    public void setShowButtonInform(Boolean showButtonInform){
        this.showButtonInform = showButtonInform;
    }

    /**
     * Method: getUbsName.
     * Purpose: this method get the Ubs name.
     * @return
     */
    public String getUbsName(){
        return this.ubsName;
    }

    /**
     * Method: setUbsName.
     * Purpose: this method set the name of Ubs.
     * @param ubsName
     */
    public void setUbsName(String ubsName){
        this.ubsName = ubsName;
    }

    /**
     * Method: createFilter.
     * Purpose: this method create the filter to search a medicine.
     */
    public void createFilter() {
        filter = new FilterSearchMedicine(filterDataMedicine, this);
        Log.i("LOG", "\n" + "Create a new filter, on createFilter, line 176 on CardListAdapterMedicine");
        Boolean test = getShowButtonUBSs();
    }

    /**
     * Method: getShowButtonUBSs.
     * Purpose: this method get the boolean varaible that refers the status of Ubs.
     * @return
     */
    private Boolean getShowButtonUBSs() {
        return this.showButtonUBSs;
    }

    /**
     * Method: getShowButtonInform.
     * Purpose: this method get the boolean varaible that refers the status of Button Inform.
     * @return
     */
    private Boolean getShowButtonInform(){
        return this.showButtonInform;
    }

    private void createMedicineRowData(ViewHolderMedicine holder, int position){
        Medicine rowData = this.dataMedicine.get(position);
        holder.getTextViewMedicineName().setText(rowData.getMedicineDescription());
        holder.getTextViewMedicineUnit().setText(rowData.getUnityMedicineFormatted());
        holder.getTextViewMedicineDosage().setText(rowData.getMedicineDosage());
    }

    private void validatingMedicineRowData(ViewHolderMedicine holder){
        // this control structure refers to the  visibility and params of Button UBS.
        if (!getShowButtonUBSs()) {
            holder.getButtonSelectUbs().setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.getButtonMedicineInform().getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_END);
            holder.getButtonMedicineInform().setLayoutParams(params);
        } else {
            // Nothing to do
        }

        // this control structures refers verification of the UBS name to be saved.
        if (!getUbsName().equalsIgnoreCase("")) {
            holder.ubsSelectedName = getUbsName();
            Log.i("LOG", "\n" + "Set the Ubs name on holder if exists, on onBindViewHolder, line  77 on CardListAdapterMedicine");
        } else {
            holder.ubsSelectedName = "";
            Log.i("LOG", "\n" + "Set the Ubs name on holder if its empty, on onBindViewHolder, line 80 on CardListAdapterMedicine");
        }

        // this control structure refers to the visibility of Button Inform according to the state returned by the function getShowButtonInform().
        if (!getShowButtonInform()) {
            holder.buttonMedicineInform.setVisibility(View.GONE);
            Log.i("LOG", "\n" + "Set the off visilibity of Button Medicine Inform ,on onBindViewHolder, line 85 on CardListAdapterMedicine");
        } else {
            // Nothing to do
        }
    }
}