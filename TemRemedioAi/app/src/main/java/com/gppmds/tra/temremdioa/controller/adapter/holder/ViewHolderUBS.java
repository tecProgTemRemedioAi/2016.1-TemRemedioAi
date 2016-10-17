/**
 * File: ViewHolderUBS.java
 * Purpose: this file set and treats things referents to ubs information on cards of view.
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
import com.gppmds.tra.temremdioa.controller.SelectMedicineActivity;
import com.gppmds.tra.temremdioa.controller.UbsMapsActivity;
import com.gppmds.tra.temremdioa.controller.adapter.CardListAdapterUBS;
import com.gppmds.tra.temremdioa.model.Notification;
import com.gppmds.tra.temremdioa.model.UBS;
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
 * Class: ViewHolderUBS
 * Purpose: this class set all things about ubs cards;
 */
public class ViewHolderUBS extends RecyclerView.ViewHolder{
    private TextView textViewUbsName;
    private TextView textViewUbsNeighborhood;
    private TextView textViewLastInformation1;
    private TextView textViewLastInformation2;
    private TextView textViewLastInformation3;
    private TextView textViewLastInformationTitle;
    private TextView textViewWithoutNotification;
    private RelativeLayout headerLayout;
    private RelativeLayout expandLayout;
    private ValueAnimator cardAnimation;
    private Button buttonSelectMedicine;
    private Button buttonViewUbsDescription;
    public Button buttonUbsInform;
    public String medicineSelectedName;
    public String medicineSelectedDosage;
    public ImageView imageViewArrow;
    private PieChart pieChart;
    public Boolean haveNotification;

    /**
     * Method: ViewHolderUBS
     * Purpose: this method set all things that will be showed in a card of ubs.
     * @param card
     */
    public ViewHolderUBS(CardView card) {
        super(card);
        this.textViewUbsName = (TextView) card.findViewById(R.id.textViewUbsName);
        this.textViewUbsNeighborhood = (TextView) card.findViewById(R.id.textViewUbsNeighborhood);
        this.textViewLastInformation1 = (TextView) card.findViewById(R.id.textViewLastInformation1);
        this.textViewLastInformation2 = (TextView) card.findViewById(R.id.textViewLastInformation2);
        this.textViewLastInformation3 = (TextView) card.findViewById(R.id.textViewLastInformation3);
        this.textViewLastInformationTitle = (TextView) card.findViewById(R.id.textViewLastInformationTitle);
        this.textViewWithoutNotification = (TextView) card.findViewById(R.id.textViewWithoutNotification);
        this.imageViewArrow = (ImageView) card.findViewById(R.id.imageViewArrow);
        this.buttonSelectMedicine = (Button) card.findViewById(R.id.buttonSelectMedicine);
        this.buttonViewUbsDescription = (Button) card.findViewById(R.id.buttonUbsDescription);
        this.buttonUbsInform = (Button) card.findViewById(R.id.buttonInformUbs);
        this.expandLayout = (RelativeLayout) card.findViewById(R.id.expandable);
        this.headerLayout = (RelativeLayout) card.findViewById(R.id.header);
        this.pieChart = (PieChart) card.findViewById(R.id.pie_chart_ubs);

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

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0,
                                View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0,
                                View.MeasureSpec.UNSPECIFIED);
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
                if (expandLayout.getVisibility() == View.GONE) {
                    Log.i("LOG", "\n" + "Card expanded");

                    UBS selectItem = CardListAdapterUBS.dataUBS.get(ViewHolderUBS.this.getAdapterPosition());

                    List<Notification> notificationList = null;
                    notificationList = getNotifications(selectItem);

                    haveNotification = false;

                    if (notificationList.size() >= 1) {
                        haveNotification = true;
                        getTextViewLastInformation1().setText("1. " + generateTextNotification(notificationList.get(0)));
                        Log.i("LOG", "\n" + "Latest information was found");
                    } else {
                        getTextViewLastInformation1().setText("");
                        Log.i("LOG", "\n" + "Latest information does not exist");
                    }

                    if (notificationList.size() >= 2) {
                        getTextViewLastInformation2().setText("2. " + generateTextNotification(notificationList.get(1)));
                        Log.i("LOG", "\n" + "Penultimate information was found");
                    } else {
                        getTextViewLastInformation2().setText("");
                        Log.i("LOG", "\n" + "Penultimate information does not exist");
                    }

                    if (notificationList.size() >= 3) {
                        getTextViewLastInformation3().setText("3. " + generateTextNotification(notificationList.get(2)));
                        Log.i("LOG", "\n" + "Antepenultimate information was found");
                    } else {
                        getTextViewLastInformation3().setText("");
                        Log.i("LOG", "\n" + "Antepenultimate information does not exist");
                    }

                    if (haveNotification) {
                        Log.i("LOG", "\n" + "\n" + "\n" + "UBS has notifications");

                        setInformationOfChart(selectItem);
                        Log.i("LOG", "\n" + "\n" + "Informations are setted in pie chart ");

                        getTextViewWithoutNotification().setVisibility(View.GONE);
                        Log.i("LOG", "\n" + "Have notifications");
                    } else {
                        Log.i("LOG", "\n" + "\n" + "\n" + "UBS does not have notifications");

                        getTextViewLastInformationTitle().setText("");
                        Log.i("LOG", "\n" + "Does not have any notifications");

                        setInformationOfChartWithoutNotification();
                        Log.i("LOG", "\n" + "\n" + "Graphic setted as empty");
                    }

                    ParseUser getCurrentUser = ParseUser.getCurrentUser();

                    if (getCurrentUser != null && getButtonUbsInform().getVisibility() == View.VISIBLE) {
                        getButtonUbsInform().setVisibility(View.VISIBLE);
                        Log.i("LOG", "\n" + "\n" + "\n" + "Button to inform ubs is visible");
                    } else {
                        getButtonUbsInform().setVisibility(View.GONE);
                        Log.i("LOG", "\n" + "\n" + "Button to inform ubs is not visible");
                    }

                    expand();

                } else {
                    Log.i("LOG", "\n" + "Card collapsed");
                    collapse();
                }
            }
        });

        // Here we set the itens that will be showed on ubs card when user click on card.
        this.buttonSelectMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SelectMedicineActivity.class);
                UBS selectItem = CardListAdapterUBS.dataUBS.get(ViewHolderUBS.this
                        .getAdapterPosition());
                intent.putExtra("UbsName", selectItem.getUbsName());
                intent.putExtra("UbsAttentionLevel", selectItem.getUbsAttentionLevel());
                v.getContext().startActivity(intent);
            }
        });

        // Here we set description about ubs.
        this.buttonViewUbsDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UbsMapsActivity.class);
                UBS selectItem = CardListAdapterUBS.dataUBS.get(ViewHolderUBS.this
                        .getAdapterPosition());
                intent.putExtra("latitude", selectItem.getUbsLatitude());
                intent.putExtra("longitude", selectItem.getUbsLongitude());
                intent.putExtra("UbsName", selectItem.getUbsName());
                intent.putExtra("UbsAddress", selectItem.getUbsAddress());
                intent.putExtra("UbsNeighborhood", selectItem.getUbsNeighborhood());
                intent.putExtra("UbsCity", selectItem.getUbsCity());
                intent.putExtra("UbsPhone", selectItem.getUbsPhone());
                v.getContext().startActivity(intent);
            }
        });

        // Here we set the itens that will be showed when user decide to inform about a medicine in a Ubs.
        this.buttonUbsInform.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), Inform.class);
                UBS selectItem = CardListAdapterUBS.dataUBS.get(ViewHolderUBS.this.getAdapterPosition());
                intent.putExtra("UbsName", selectItem.getUbsName());
                intent.putExtra("medicineName", medicineSelectedName);
                intent.putExtra("medicineDosage", medicineSelectedDosage);

                view.getContext().startActivity(intent);
            }
        });
    }

    /**
     * Method: generateTextNotification
     * Purpose: this method check the notification about medicine availabity and set the information
     *          about which ubs the medicine is available.
     * @param notification
     * @return String
     */
    private String generateTextNotification(Notification notification) {
        String textOfNotification = "";

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
     * Method: getNotifications
     * Purpose: this method get the notifications made by users about medicines in ubs.
     * @param ubs
     * @return listNotification
     */
    private List<Notification> getNotifications(UBS ubs) {
        List<Notification> listNotification = null;

        ParseQuery<Notification> queryNotification = Notification.getQuery();
        queryNotification.whereEqualTo(Notification.getTitleUBSName(), ubs.getUbsName());

        if (!medicineSelectedDosage.isEmpty()) {
            queryNotification.whereEqualTo(Notification.getTitleMedicineDosage(), medicineSelectedDosage);
            Log.i("LOG", "\n" + "\n" + "Medicine select is not empty");
        } else {
            // Nothing to do
            Log.i("LOG", "\n" + "\n" + "Medicine select is empty");
        }

        if (!medicineSelectedName.isEmpty()) {
            queryNotification.whereEqualTo(Notification.getTitleMedicineName(), medicineSelectedName);
            Log.i("LOG", "\n" + "\n" + "Medicine select is not empty");
        } else {
            // Nothing to do
            Log.i("LOG", "\n" + "\n" + "Medicine select is empty");
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
     * Method: setInformationOfChartWithoutNotification()
     * Purpose: this method get the notifications about ubs that doenst have medicines.
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
     * Method: setInformationOfChart()
     * Purpose: this method set ubs information of chart.
     * @param ubs
     */
    private void setInformationOfChart(UBS ubs) {
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
        pieChart.setData(getDataPie(ubs));
    }

    /**
     * Method: getDataPie()
     * Purpose: this method set ubs information in data pie (update).
     * @param ubs
     * @return pieData
     */
    public PieData getDataPie(UBS ubs) {
        PieData pieData = null;

        Integer countNotificationAvailable = 0;
        Integer countNotificationNotAvailable = 0;

        ParseQuery<Notification> queryNotificationAvailable = Notification.getQuery();
        queryNotificationAvailable.fromLocalDatastore();
        queryNotificationAvailable.whereEqualTo(Notification.getTitleUBSName(), ubs.getUbsName());
        queryNotificationAvailable.whereEqualTo(Notification.getTitleAvailable(), true);

        if (medicineSelectedName != "") {
            queryNotificationAvailable.whereEqualTo(Notification.getTitleMedicineDosage(), medicineSelectedDosage);
            queryNotificationAvailable.whereEqualTo(Notification.getTitleMedicineName(), medicineSelectedName);
        } else {
            // Nothing to do
        }

        try {
            countNotificationAvailable = queryNotificationAvailable.count();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParseQuery<Notification> queryNotificationNotAvailable = Notification.getQuery();
        queryNotificationNotAvailable.fromLocalDatastore();
        queryNotificationNotAvailable.whereEqualTo(Notification.getTitleUBSName(), ubs.getUbsName());
        queryNotificationNotAvailable.whereEqualTo(Notification.getTitleAvailable(), false);

        if (medicineSelectedName != "") {
            queryNotificationNotAvailable.whereEqualTo(Notification.getTitleMedicineDosage(), medicineSelectedDosage);
            queryNotificationNotAvailable.whereEqualTo(Notification.getTitleMedicineName(), medicineSelectedName);
        } else {
            // Nothing to do
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
     * Method: expand()
     * Purpose: this method set card visible.
     */
    private void expand() {
        Log.i("LOG", "Expand enter, View.VISIBLE");
        expandLayout.setVisibility(View.VISIBLE);
        cardAnimation.start();
        imageViewArrow.setBackgroundResource(R.drawable.ic_keyboard_arrow_up);
    }

    /**
     * Method: collapse()
     * Purpose: this method set card 'not' visible.
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

            @Override
            public void onAnimationStart(Animator animator) {
                // Nothing to do
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // Nothing to do
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                // Nothing to do
            }
        });
        cardAnimationCollapse.start();
        imageViewArrow.setBackgroundResource(R.drawable.ic_keyboard_arrow_down);
    }

    /**
     * Method: slideAnimator()
     * Purpose: this method controls the beginning and end of the layout animation.
     * @param start
     * @param end
     * @return animator
     */
    private ValueAnimator slideAnimator(int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                /* Update Height */
                int value = (Integer) valueAnimator.getAnimatedValue();

                ViewGroup.LayoutParams layoutParams = expandLayout.getLayoutParams();
                layoutParams.height = value;
                expandLayout.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

    /**
     * Method: getTextViewUbsName()
     * Purpose: set on view ubs name.
     * @return textViewUbsName
     */
    public TextView getTextViewUbsName(){
        return this.textViewUbsName;
    }

    /**
     * Method: getTextViewUbsNeighborhood()
     * Purpose: set on view ubs neighborhood.
     * @return textViewUbsNeighborhood
     */
    public TextView getTextViewUbsNeighborhood(){
        return this.textViewUbsNeighborhood;
    }

    /**
     * Method: getTextViewWithoutNotification()
     * Purpose: set on view.
     * @return textViewWithoutNotification
     */
    public TextView getTextViewWithoutNotification(){
        return this.textViewWithoutNotification;
    }

    /**
     * Method: getTextViewLastInformationTitle()
     * Purpose: set on view.
     * @return textViewLastInformationTitle
     */
    public TextView getTextViewLastInformationTitle() {
        return this.textViewLastInformationTitle;
    }

    /**
     * Method: getTextViewLastInformation1()
     * Purpose: set on view.
     * @return textViewLastInformation1
     */
    public TextView getTextViewLastInformation1() {
        return this.textViewLastInformation1;
    }

    /**
     * Method: getTextViewLastInformation2()
     * Purpose: set on view.
     * @return textViewLastInformation2
     */
    public TextView getTextViewLastInformation2() {
        return this.textViewLastInformation2;
    }

    /**
     * Method: getTextViewLastInformation3()
     * Purpose: set on view.
     * @return textViewLastInformation3
     */
    public TextView getTextViewLastInformation3() {
        return this.textViewLastInformation3;
    }

    /**
     * Method: getButtonSelectMedicine()
     * Purpose: set on view medicine button.
     * @return buttonSelectMedicine
     */
    public Button getButtonSelectMedicine(){
        return this.buttonSelectMedicine;
    }

    /**
     * Method: getButtonUbsInform()
     * Purpose: set on view inform ubs button.
     * @return buttonUbsInform
     */
    public Button getButtonUbsInform() {
        return this.buttonUbsInform;
    }
}