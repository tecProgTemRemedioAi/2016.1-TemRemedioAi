/**
 * File: ViewHolderMedicine.java
 * Purpose: this file set and treats things referents to medicine information on cards of view.
 */

package com.gppmds.tra.temremdioa.controller.adapter.holder;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.gppmds.tra.temremdioa.controller.Inform;
import com.gppmds.tra.temremdioa.controller.SelectUBSActivity;
import com.gppmds.tra.temremdioa.controller.adapter.CardListAdapterMedicine;
import com.gppmds.tra.temremdioa.model.Medicine;
import com.gppmds.tra.temremdioa.model.Notification;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.tra.gppmds.temremdioa.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static junit.framework.Assert.assertFalse;

/**
 * Class: ViewHolderMedicine
 * Purpose: this class set all things about medicine cards.
 */

public class ViewHolderMedicine extends RecyclerView.ViewHolder {

    private static TextView textViewMedicineName = null;                  // This variable refers to the name of the drug, the same existing in the system.
    private static TextView textViewLatestInformation =  null;             // This variable refers to latest information on availability of the drug, made by a users.
    private static TextView textViewPenultimateInformation = null;        // This variable refers to the penultimate information on availability of the drug, made by a users.
    private static TextView textViewAntepenultimateInformation = null;    // This variable refers to the antepenultimate information on availability of the drug, made by a users.
    private static TextView textViewLatestInformationTitle = null;        // This variable refers to the title of the latest information, made by users.
    private static TextView textViewWithoutNotification = null;           // This variable refers to the text that appears when there are no notifications made by users.
    private static TextView textViewMedicineUnit = null;                  // This variable refers to the type of unit that the drug is distributed.
    private static TextView textViewMedicineDosage = null;                // This variable refers to what kind of dosage that the drug is distributed.

    private static RelativeLayout headerLayout = null;                    // This layout refers to the standard structure of the card when it is collapsed.
    private static RelativeLayout expandLayout = null;                    // This layout refers to the standard structure of the card when it is expanded.
    private static ValueAnimator cardAnimation = null;                    // This animator refers to the animation that occurs on the card when it is clicked.
    private static Button buttonSelectUbs = null;                         // This button is for the user to select the desired UBS.
    private static ImageView imageViewArrow = null;                       // This image refers to where the chart will be set.
    private static PieChart pieChart = null;                              // This chart is completed by information provided by users.
    public static Button buttonMedicineInform = null;                     // This button allows users to inform met any medication or not.
    public static String ubsSelectedName = null;                          // This variable says refers to the UBS name that was searched by the user.



    /**
     * Method: ViewHolderMedicine.
     * Purpose: this method set all things that will be showed in a card of medicine.
     * @param card
     */
    public ViewHolderMedicine(CardView card) {
        super(card);
        this.textViewMedicineName = (TextView) card.findViewById(R.id.textViewMedicineName);
        this.textViewLatestInformation = (TextView) card.findViewById(R.id.textViewLatestInformation);
        this.textViewPenultimateInformation = (TextView) card.findViewById(R.id.textViewPenultimateInformation);
        this.textViewAntepenultimateInformation = (TextView) card.findViewById(R.id.textViewAntepenultimateInformation);
        this.textViewLatestInformationTitle = (TextView) card.findViewById(R.id.textViewLatestInformationTitle );
        this.textViewWithoutNotification = (TextView) card.findViewById(R.id.textViewWithoutNotification);
        this.textViewMedicineDosage = (TextView) card.findViewById(R.id.textViewMedicineDosage);
        this.textViewMedicineUnit = (TextView) card.findViewById(R.id.textViewMedicineUnit);
        this.imageViewArrow = (ImageView) card.findViewById(R.id.imageViewArrow);
        this.buttonSelectUbs = (Button) card.findViewById(R.id.buttonSelectUbs);
        this.buttonMedicineInform = (Button) card.findViewById(R.id.buttonInformRemedio);
        this.expandLayout = (RelativeLayout) card.findViewById(R.id.expandable);
        this.headerLayout = (RelativeLayout) card.findViewById(R.id.header);
        this.pieChart = (PieChart) card.findViewById(R.id.pie_chart_medicine);

        // Here the card is not expanded, it is collapsed.
        this.expandLayout.setVisibility(View.GONE);

        // getViewTreeObserver returns the notifications about global events, in this case
        // returns information about the card state, if it is expand or collapse.
        this.expandLayout.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        expandLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                        expandLayout.setVisibility(View.GONE); // Defined visibility.

                        // Size of the layout width when the card is expanded.
                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

                        // Size of the layout height when the card is expanded.
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                        expandLayout.measure(widthSpec, heightSpec);

                        cardAnimation = slideAnimator(0, expandLayout.getMeasuredHeight());
                        return true;
                    }
                });

        // Here we check the card visibility and set users notifications about the last time they get
        // medicine in UBS.
        this.headerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Integer LATESTINFORMATION =  1;
                final Integer PENULTIMATEINFORMATION = 2;
                final Integer ANTEPENULTIMATEINFORMATION = 3;

                if (LATESTINFORMATION != 1){
                    Exception eventException = new Exception ("Error on Latest Information value.");
                    try{
                        throw eventException;
                    } catch(Exception exception){
                        exception.printStackTrace();
                    }
                } else {
                    //nothing to do
                }

                if (PENULTIMATEINFORMATION != 2){
                    Exception eventException = new Exception ("Error on Penultimate Information value.");
                    try{
                        throw eventException;
                    } catch(Exception exception){
                        exception.printStackTrace();
                    }
                } else {
                    //nothing to do
                }

                if (ANTEPENULTIMATEINFORMATION != 3){
                    Exception eventException = new Exception ("Error on Antepenultimate Information value.");
                    try{
                        throw eventException;
                    } catch(Exception exception){
                        exception.printStackTrace();
                    }
                } else {
                    //nothing to do
                }

                Log.i("LOG", "\n" + "Clicked header");

                // This control structure refers to visibility of card when it expanded.
                if (expandLayout.getVisibility() == View.GONE) {
                    Log.i("LOG", "\n" + "Card expanded");
                    Medicine selectItem = (Medicine) CardListAdapterMedicine.dataMedicine.get(ViewHolderMedicine.this.getAdapterPosition());

                    List<Notification> notificationList = null;
                    notificationList = (List<Notification>)getNotifications(selectItem); // Get a selected item and set on new list.

                    Boolean haveNotification = true;                        // This variable tells whether or not notification made by users.
                    haveNotification = false;

                    // This control structure refers to one or more notifications sent by users.
                    if (notificationList.size() >= LATESTINFORMATION) {
                        haveNotification = true;
                        // Get the first information about medicine made by users.
                        getTextViewLatestInformation().setText("1. " + generateTextNotification(notificationList.get(0)));
                        Log.i("LOG", "\n" + "Latest information was found");
                    } else {
                        getTextViewLatestInformation().setText("");
                        Log.i("LOG", "\n" + "Latest information does not exist");
                    }

                    // This control structure refers to two or more notifications sent by users.
                    if (notificationList.size() >= PENULTIMATEINFORMATION) {
                        // Get the second information about medicine made by users.
                        getTextViewPenultimateInformation().setText("2. " + generateTextNotification(notificationList.get(1)));
                        Log.i("LOG", "\n" + "Penultimate information was found");
                    } else {
                        getTextViewPenultimateInformation().setText("");
                        Log.i("LOG", "\n" + "Penultimate information does not exist");
                    }

                    // This control structure refers to three or more notifications sent by users.
                    if (notificationList.size() >= ANTEPENULTIMATEINFORMATION) {
                        // Get the third information about medicine made by users.
                        getTextViewAntepenultimateInformation().setText("3. " + generateTextNotification(notificationList.get(2)));
                        Log.i("LOG", "\n" + "Antepenultimate information was found");
                    } else {
                        getTextViewAntepenultimateInformation().setText("");
                        Log.i("LOG", "\n" + "Antepenultimate information does not exist");
                    }

                    // This control structure refers to existêmcia or not notified and the card of the population with this information.
                    if (haveNotification) {
                        Log.i("LOG", "\n" + "\n" + "\n" + "Medicine has notifications");

                        setInformationOfChart(selectItem);
                        Log.i("LOG", "\n" + "\n" + "Informations are setted in pie chart ");

                        getTextViewWithoutNotification().setVisibility(View.GONE);
                        Log.i("LOG", "\n" + "Have notifications");
                    } else {
                        Log.i("LOG", "\n" + "\n" + "\n" + "Medicine dos not have notifications");

                        getTextViewLatestInformationTitle().setText("");
                        Log.i("LOG", "\n" + "Does not have any notifications");

                        setInformationOfChartWithoutNotification();
                        Log.i("LOG", "\n" + "\n" + "Graphic setted as empty");
                    }

                    ParseUser getCurrentUser = ParseUser.getCurrentUser();

                    // This control structure checks whether or not logged in user, according to the response
                    // shows the button to inform drug availability.
                    if (getCurrentUser != null && getButtonMedicineInform().getVisibility() == View.VISIBLE) {
                        getButtonMedicineInform().setVisibility(View.VISIBLE);
                        Log.i("LOG", "\n" + "\n" + "\n" + "Button to inform medicine is visible");
                    } else {
                        getButtonMedicineInform().setVisibility(View.GONE);
                        Log.i("LOG", "\n" + "\n" + "Button to inform medicine is not visible");
                    }

                    expand();
                } else {
                    Log.i("LOG", "Collapse Click");
                    collapse();
                }
            }
        });

        // Here we set the itens that will be showed on medicine card when user click on card.
        this.buttonSelectUbs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SelectUBSActivity.class);
                boolean running =  true;

                try {
                    createMedicineSelectedItemToShow(intent);
                    Exception eventException = new Exception ("Error on selected item from CardListAdapert.");

                    v.getContext().startActivity(intent);
                    Log.i("LOG", "\n" + "\n" + "Informations on expanded medicine card are setted");

                    if(intent == null){
                        throw eventException;
                    } else {
                        // Nothing to do.
                    }
                } catch  (Exception exception){
                    running = false;
                }
                assertFalse(running);

            }
        });

        // Here we set the itens that will be showed when user decide to inform about a medicine in
        // a Ubs.
        this.buttonMedicineInform.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(view.getContext(), Inform.class);
                boolean running =  true;

                try {
                    createMedicineSelectedItemToInform(intent);
                    Exception eventException = new Exception ("Error on selected item to inform from CardListAdapert.");

                    view.getContext().startActivity(intent);
                    Log.i("LOG", "\n" + "\n" + "Informations about searched medicine are setted");

                    if(intent == null){
                        throw eventException;
                    } else {
                        // Nothing to do.
                    }
                } catch  (Exception exception){
                    running = false;
                }
                assertFalse(running);
            }
        });
    }


    /**
     * Method: getDataPie.
     * Purpose: this method get data informations made user to next step set in a pie chart.
     * @param medicine
     * @return
     */
    public PieData getDataPie(Medicine medicine) {
        PieData pieData = null;

        Integer countNotificationAvailable = 0;
        Integer countNotificationNotAvailable = 0;

        ParseQuery<Notification> queryNotificationAvailable = Notification.getQuery();
        queryNotificationAvailable.fromLocalDatastore();
        queryNotificationAvailable.whereEqualTo(Notification.getTitleMedicineName(), medicine.getMedicineDescription());
        queryNotificationAvailable.whereEqualTo(Notification.getTitleMedicineDosage(), medicine.getMedicineDosage());
        queryNotificationAvailable.whereEqualTo(Notification.getTitleAvailable(), true);

        // This control structure refers to check if the name of Ubs was empty or not in the search.
        if (ubsSelectedName != "") {
            // Compares if the search name exists on data base of medicines.
            queryNotificationAvailable.whereEqualTo(Notification.getTitleUBSName(), ubsSelectedName);
            Log.i("LOG", "\n" + "\n" + "UBS name select is not empty");
        } else {
            // Nothing to do.
            Log.i("LOG", "\n" + "\n" + "UBS name select is empty");
        }

        try {
            countNotificationAvailable = queryNotificationAvailable.count();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParseQuery<Notification> queryNotificationNotAvailable = Notification.getQuery();
        queryNotificationNotAvailable.fromLocalDatastore();
        queryNotificationNotAvailable.whereEqualTo(Notification.getTitleMedicineName(), medicine.getMedicineDescription());
        queryNotificationNotAvailable.whereEqualTo(Notification.getTitleMedicineDosage(), medicine.getMedicineDosage());
        queryNotificationNotAvailable.whereEqualTo(Notification.getTitleAvailable(), false);

        // This control structure checks that the research of ubs done by the user is empty  or not.
        if (ubsSelectedName != "") {
            queryNotificationNotAvailable.whereEqualTo(Notification.getTitleUBSName(), ubsSelectedName);
            Log.i("LOG", "\n" + "\n" + "UBS name select is not empty");
        } else {
            // Nothing to do.
            Log.i("LOG", "\n" + "\n" + "UBS name select is empty");
        }

        try {
            countNotificationNotAvailable = queryNotificationNotAvailable.count();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ArrayList<Entry> valuesAvailable = new ArrayList<Entry>();
        ArrayList<String> valuesLegend = new ArrayList<String>();

        valuesLegend.add("Sim");
        valuesLegend.add("Não");

        valuesAvailable.add(new Entry((float) countNotificationAvailable, 0));
        valuesAvailable.add(new Entry((float) countNotificationNotAvailable, 1));

        PieDataSet pieDataSet = new PieDataSet(valuesAvailable, "");
        int color [] = {Color.parseColor("#00BEED"), Color.parseColor("#FFED4F")};
        pieDataSet.setColors(color);
        pieDataSet.setSliceSpace(5);
        pieDataSet.setValueTextSize(10);

        pieData = new PieData(valuesLegend, pieDataSet);

        return pieData;
    }

    /**
     * Method: getTextViewWithoutNotification
     * Purpose: this method get the text view when there is no notification made by users.
     * @return textViewWithoutNotification.
     */
    public TextView getTextViewWithoutNotification(){
        TextView newViewNotification = (TextView) this.textViewWithoutNotification;
        return newViewNotification;
    }

    /**
     * Method: getTextViewMedicineName.
     * Purpose: this method get the text view about the medicine name.
     * @return textViewMedicineName.
     */
    public TextView getTextViewMedicineName(){
        TextView newViewMedicineName = (TextView) this.textViewMedicineName;
        return newViewMedicineName;
    }

    /**
     * Method: getTextViewMedicineUnit.
     * Purpose: this method get the text view about medicine unit.
     * @return textViewMedicineUnit.
     */
    public TextView getTextViewMedicineUnit() {
        TextView newViewMedicineUnit = (TextView) this.textViewMedicineUnit;
        return newViewMedicineUnit;
    }

    /**
     * Method: getTextViewLatestInformationTitle.
     * Purpose: this method get the text view about the latest information title.
     * @return textViewLatestInformationTitle.
     */
    public TextView getTextViewLatestInformationTitle() {
        TextView newViewLatestInformationTitle = (TextView) this.textViewLatestInformationTitle;
        return newViewLatestInformationTitle;
    }

    /**
     * Method: getTextViewLatestInformation.
     * Purpose: this method get the text view about the latest information made by users.
     * @return extViewLatestInformation.
     */
    public TextView getTextViewLatestInformation() {
        TextView newViewLatestInformation = (TextView) this.textViewLatestInformation;
        return newViewLatestInformation;
    }

    /**
     * Method: getTextViewPenultimateInformation.
     * Purpose: this method get the text view about the penultimate information made by users.
     * @return textViewPenultimateInformation.
     */
    public TextView getTextViewPenultimateInformation() {
        TextView newViewPenultimateInformation = (TextView) this.textViewPenultimateInformation;
        return newViewPenultimateInformation;
    }

    /**
     * Method: getTextViewAntepenultimateInformation.
     * Purpose: this method get the text view about the antepenultimate information made by users.
     * @return textViewAntepenultimateInformation.
     */
    public TextView getTextViewAntepenultimateInformation() {
        TextView newViewAntepenultimateInformation = (TextView) this.textViewAntepenultimateInformation;
        return newViewAntepenultimateInformation;
    }

    /**
     * Method: getTextViewMedicineDosage.
     * Purpose: this method get the text view about the medicine dosage.
     * @return textViewMedicineDosage.
     */
    public TextView getTextViewMedicineDosage() {
        TextView newViewMedicineDosage = (TextView) this.textViewMedicineDosage;
        return newViewMedicineDosage;
    }

    /**
     * Method: getButtonMedicineInform.
     * Purpose: this method get the button MedicineInform.
     * @return buttonMedicineInform.
     */
    public Button getButtonMedicineInform() {
        Button newButtonMedicineInform = (Button) this.buttonMedicineInform;
        return newButtonMedicineInform;
    }

    /**
     * Method: getButtonSelectUbs;
     * Purpose: this method get the button SelectUbs.
     * @return buttonSelectUbs.
     */
    public Button getButtonSelectUbs() {
        Button newButtonSelectUbs = (Button) this.buttonSelectUbs;
        return newButtonSelectUbs;
    }

    /**
     * Method: expand.
     * Purpose: this method controls when card is expanded, set the visibility.
     */
    private void expand() {
        Log.i("LOG", "Expand enter, View.VISIBLE");
        expandLayout.setVisibility(View.VISIBLE);
        cardAnimation.start();
        imageViewArrow.setBackgroundResource(R.drawable.ic_keyboard_arrow_up);
    }

    /**
     * Method: collapse.
     * Purpose: this method controls when card is collapsed, when user click in card.
     */
    private void collapse() {
        int finalHeight = expandLayout.getHeight();

        ValueAnimator cardAnimationCollapse = slideAnimator(finalHeight, 0);
        cardAnimationCollapse.addListener(new Animator.AnimatorListener(){
            @Override
            public void onAnimationEnd(Animator animator) {
                Log.i("LOG", "collapse onAnimationEnd enter, View.GONE");
                expandLayout.setVisibility(View.GONE);
            }

            // Method declared empty because the override is mandatory.
            @Override
            public void onAnimationStart(Animator animator) {
            }

            // Method declared empty because the override is mandatory.
            @Override
            public void onAnimationCancel(Animator animator) {
            }

            // Method declared empty because the override is mandatory.
            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
        cardAnimationCollapse.start();
        imageViewArrow.setBackgroundResource(R.drawable.ic_keyboard_arrow_down);
    }

    /**
     * Method: slideAnimator.
     * Purpose: this method set params about layout animator of card.
     * @param start
     * @param end
     * @return animator.
     */
    private ValueAnimator slideAnimator(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        // Here we update the height value of card when it expand.
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = expandLayout.getLayoutParams();
                layoutParams.height = value;
                expandLayout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    /**
     * Method: generateTextNotification.
     * Purpose: this method check the notification about medicine availabity and set the information
     *          about which ubs the medicine is available.
     * @param notification
     * @return String
     */
    private String generateTextNotification(Notification notification) {
        String textOfNotification = "";

        // This control structure set as information availability and location unavailability of
        // medicine.
        if (notification.getAvailable()) {
            textOfNotification = "Disponível em ";
            Log.i("LOG", "\n" + "\n" + "Location information availability is visible");
        } else {
            textOfNotification = "Indisponível em ";
            Log.i("LOG", "\n" + "\n" + "The unavailability of information is visible");
        }

        Calendar dayCalendar = Calendar.getInstance(new Locale("pt", "BR"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");

        dayCalendar.setTime(notification.getDateInform());
        textOfNotification = textOfNotification + simpleDateFormat.format(dayCalendar.getTime());

        return textOfNotification;
    }

    /**
     * Method: getNotifications.
     * Purpose: this method get the notifications made by users about medicines in ubs.
     * @param medicine
     * @return List<Notification>
     */
    private List<Notification> getNotifications(Medicine medicine) {

        List<Notification> listNotification = null;

        ParseQuery<Notification> queryNotification = Notification.getQuery();
        queryNotification.whereEqualTo(Notification.getTitleMedicineName(), medicine.getMedicineDescription());
        queryNotification.whereEqualTo(Notification.getTitleMedicineDosage(), medicine.getMedicineDosage());

        // This control structure checks that the research done by the user is not empty.
        if (!ubsSelectedName.isEmpty()) {
            queryNotification.whereEqualTo(Notification.getTitleUBSName(), ubsSelectedName);
            Log.i("LOG", "\n" + "\n" + "UBS empty search");
        } else {
            // Nothing to do.
        }

        queryNotification.orderByDescending(Notification.getTitleDateInform());
        queryNotification.setLimit(3);

        try {
            listNotification = queryNotification.find();
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }

        return listNotification;
    }

    /**
     * Method: setInformationOfChartWithoutNotification.
     * Purpose: this method set default information in chart because the medicine doesn't have notifications.
     */
    private void setInformationOfChartWithoutNotification() {
        pieChart.setDescription("");
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(0);
        pieChart.setTransparentCircleRadius(40);
        pieChart.setDrawSliceText(false);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.animateY(1000);

        pieChart.getLegend().setEnabled(true);
        pieChart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        pieChart.getLegend().setTextSize(12);

        pieChart.setNoDataTextDescription("Sem notificações encontradas.");

        ArrayList<Entry> valuesAvailable = new ArrayList<Entry>();
        ArrayList<String> valuesLegend = new ArrayList<String>();

        valuesLegend.add("Sem Notificação");

        valuesAvailable.add(new Entry((float) 1, 0));

        PieDataSet pieDataSet = new PieDataSet(valuesAvailable, "");
        final int color [] = {Color.parseColor("#F0F0F0")};
        pieDataSet.setColors(color);
        pieDataSet.setSliceSpace(5);

        PieData pieData = new PieData(valuesLegend, pieDataSet);
        for (IDataSet<?> set : pieData.getDataSets()) {
            set.setDrawValues(!set.isDrawValuesEnabled());
        }


        pieChart.setData(pieData);
    }

    /**
     * Method: set informationOfChart.
     * Purpose: this method set all inofrmations about notifications made by users in a chart.
     * @param medicine
     */
    private void setInformationOfChart(Medicine medicine) {
        pieChart.setDescription("");
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleRadius(0);
        pieChart.setTransparentCircleRadius(40);
        pieChart.setDrawSliceText(false);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(true);
        pieChart.animateY(1000);

        pieChart.getLegend().setEnabled(true);
        pieChart.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        pieChart.getLegend().setTextSize(12);

        pieChart.setNoDataTextDescription("Sem notificações encontradas.");
        pieChart.setData(getDataPie(medicine));
    }

    private void createMedicineSelectedItemToInform(Intent intent){
        Medicine selectedItem = CardListAdapterMedicine.dataMedicine.get(ViewHolderMedicine.this.getAdapterPosition());
        intent.putExtra("medicineName", selectedItem.getMedicineDescription());
        intent.putExtra("medicineDosage", selectedItem.getMedicineDosage());
        intent.putExtra("ubsName",ubsSelectedName);

    }

    private void createMedicineSelectedItemToShow(Intent intent) {
        Medicine selectItem = CardListAdapterMedicine.dataMedicine.get(ViewHolderMedicine.this.getAdapterPosition());
        intent.putExtra("medicineName", selectItem.getMedicineDescription());
        intent.putExtra("medicineLevelAttention", selectItem.getMedicineAttentionLevel());
        intent.putExtra("medicineDosage", selectItem.getMedicineDosage());
    }
}