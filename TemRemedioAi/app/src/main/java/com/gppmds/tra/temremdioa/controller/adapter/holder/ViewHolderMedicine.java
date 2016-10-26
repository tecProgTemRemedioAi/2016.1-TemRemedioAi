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

/**
 * Class: ViewHolderMedicine
 * Purpose: this class set all things about medicine cards.
 */

public class ViewHolderMedicine extends RecyclerView.ViewHolder {
    private TextView textViewMedicineName;                  // This variable refers to the name of the drug, the same existing in the system.
    private TextView textViewLatestInformation;             // This variable refers to latest information on availability of the drug, made by a users.
    private TextView textViewPenultimateInformation;        // This variable refers to the penultimate information on availability of the drug, made by a users.
    private TextView textViewAntepenultimateInformation;    // This variable refers to the antepenultimate information on availability of the drug, made by a users.
    private TextView textViewLatestInformationTitle;        // This variable refers to the title of the latest information, made by users.
    private TextView textViewWithoutNotification;           // This variable refers to the text that appears when there are no notifications made by users.
    private TextView textViewMedicineUnit;                  // This variable refers to the type of unit that the drug is distributed.
    private TextView textViewMedicineDosage;                // This variable refers to what kind of dosage that the drug is distributed.
    private RelativeLayout headerLayout;                    // This layout refers to the standard structure of the card when it is collapsed.
    private RelativeLayout expandLayout;                    // This layout refers to the standard structure of the card when it is expanded.
    private ValueAnimator cardAnimation;                    // This animator refers to the animation that occurs on the card when it is clicked.
    private Button buttonSelectUbs;                         // This button is for the user to select the desired UBS.
    private ImageView imageViewArrow;                       // This image refers to where the chart will be set.
    private PieChart pieChart;                              // This chart is completed by information provided by users.
    public Button buttonMedicineInform;                     // This button allows users to inform met any medication or not.
    public String ubsSelectedName;                          // This variable says refers to the UBS name that was searched by the user.
    public Boolean haveNotification;                        // This variable tells whether or not notification made by users.


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
                        expandLayout.setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
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
                Log.i("LOG", "\n" + "Clicked header");

                // This control structure refers to visibility of card when it expanded.
                if (expandLayout.getVisibility() == View.GONE) {
                    Log.i("LOG", "\n" + "Card expanded");
                    Medicine selectItem = CardListAdapterMedicine.dataMedicine.get(ViewHolderMedicine.this.getAdapterPosition());

                    List<Notification> notificationList = null;
                    notificationList = getNotifications(selectItem);

                    haveNotification = false;

                    // This control structure refers to one or more notifications sent by users.
                    if (notificationList.size() >= 1) {
                        haveNotification = true;
                        getTextViewLatestInformation().setText("1. " + generateTextNotification(notificationList.get(0)));
                        Log.i("LOG", "\n" + "Latest information was found");
                    } else {
                        getTextViewLatestInformation().setText("");
                        Log.i("LOG", "\n" + "Latest information does not exist");
                    }

                    // This control structure refers to two or more notifications sent by users.
                    if (notificationList.size() >= 2) {
                        getTextViewPenultimateInformation().setText("2. " + generateTextNotification(notificationList.get(1)));
                        Log.i("LOG", "\n" + "Penultimate information was found");
                    } else {
                        getTextViewPenultimateInformation().setText("");
                        Log.i("LOG", "\n" + "Penultimate information does not exist");
                    }

                    // This control structure refers to three or more notifications sent by users.
                    if (notificationList.size() >= 3) {
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

                Medicine selectItem = CardListAdapterMedicine.dataMedicine.get(ViewHolderMedicine.this.getAdapterPosition());
                intent.putExtra("medicineName", selectItem.getMedicineDescription());
                intent.putExtra("medicineLevelAttention", selectItem.getMedicineAttentionLevel());
                intent.putExtra("medicineDosage", selectItem.getMedicineDosage());

                v.getContext().startActivity(intent);
                Log.i("LOG", "\n" + "\n" + "Informations on expanded medicine card are setted");
            }
        });

        // Here we set the itens that will be showed when user decide to inform about a medicine in
        // a Ubs.
        this.buttonMedicineInform.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Inform.class);

                Medicine selectedItem = CardListAdapterMedicine.dataMedicine.get(ViewHolderMedicine.this.getAdapterPosition());
                intent.putExtra("medicineName", selectedItem.getMedicineDescription());
                intent.putExtra("medicineDosage", selectedItem.getMedicineDosage());
                intent.putExtra("ubsName",ubsSelectedName);

                view.getContext().startActivity(intent);
                Log.i("LOG", "\n" + "\n" + "Informations about searched medicine are setted");
            }
        });
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
        }

        queryNotification.orderByDescending(Notification.getTitleDateInform());
        queryNotification.setLimit(3);

        try {
            listNotification = queryNotification.find();
        } catch (ParseException e) {
            e.printStackTrace();
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
        int color [] = {Color.parseColor("#F0F0F0")};
        pieDataSet.setColors(color);
        pieDataSet.setSliceSpace(5);

        PieData pieData = new PieData(valuesLegend, pieDataSet);
        for (IDataSet<?> set : pieData.getDataSets())
            set.setDrawValues(!set.isDrawValuesEnabled());

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

        //
        if (ubsSelectedName != "") {
            queryNotificationAvailable.whereEqualTo(Notification.getTitleUBSName(), ubsSelectedName);
            Log.i("LOG", "\n" + "\n" + "UBS name select is not empty");
        } else {
            // Nothing to do
            Log.i("LOG", "\n" + "\n" + "UBS name select is empty");
        }

        //
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
            // Nothing to do
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
     * Purpose:
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
     * Method: getTextViewWithoutNotification
     * Purpose:
     * @return textViewWithoutNotification.
     */
    public TextView getTextViewWithoutNotification(){
        return this.textViewWithoutNotification;
    }

    /**
     * Method: getTextViewMedicineName.
     * Purpose:
     * @return textViewMedicineName.
     */
    public TextView getTextViewMedicineName(){
        return this.textViewMedicineName;
    }

    /**
     * Method: getTextViewMedicineUnit.
     * Purpose:
     * @return textViewMedicineUnit.
     */
    public TextView getTextViewMedicineUnit() {
        return this.textViewMedicineUnit;
    }

    /**
     * Method: getTextViewLatestInformationTitle.
     * Purpose:
     * @return textViewLatestInformationTitle.
     */
    public TextView getTextViewLatestInformationTitle() {
        return this.textViewLatestInformationTitle;
    }

    /**
     * Method: getTextViewLatestInformation.
     * Purpose:
     * @return extViewLatestInformation.
     */
    public TextView getTextViewLatestInformation() {
        return this.textViewLatestInformation;
    }

    /**
     * Method: getTextViewPenultimateInformation.
     * Purpose:
     * @return textViewPenultimateInformation.
     */
    public TextView getTextViewPenultimateInformation() {
        return this.textViewPenultimateInformation;
    }

    /**
     * Method: getTextViewAntepenultimateInformation.
     * Purpose:
     * @return textViewAntepenultimateInformation.
     */
    public TextView getTextViewAntepenultimateInformation() {
        return this.textViewAntepenultimateInformation;
    }

    /**
     * Method: getTextViewMedicineDosage.
     * Purpose:
     * @return textViewMedicineDosage.
     */
    public TextView getTextViewMedicineDosage() {
        return this.textViewMedicineDosage;
    }

    /**
     * Method: getButtonMedicineInform.
     * Purpose:
     * @return buttonMedicineInform.
     */
    public Button getButtonMedicineInform() {
        return this.buttonMedicineInform;
    }

    /**
     * Method: getButtonSelectUbs;
     * Purpose:
     * @return buttonSelectUbs.
     */
    public Button getButtonSelectUbs() {
        return this.buttonSelectUbs;
    }
}