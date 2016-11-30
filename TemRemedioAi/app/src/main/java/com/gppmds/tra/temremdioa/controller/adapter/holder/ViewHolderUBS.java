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
    public final ImageView IMAGE_VIEW_ARROW;               // This image refers to where the chart will be set.
    public static Button buttonUbsInform;                  // This button allows users to inform medicine in a ubs or not.
    public static String medicineSelectedName;             // This variable says refers to the medicine name that was searched by the user.
    public static String medicineSelectedDosage;           // his variable says refers to medicine dosage that was searched by the user.

    private static TextView textViewUbsName;               // This variable refers to the ubs name, the same existing in the system.
    private static TextView textViewUbsNeighborhood;       // This variable refers to ubs neighborhood.
    private static TextView textViewLastInformation1;      // This variable refers to latest information on availability of the drug, made by a users.
    private static TextView textViewLastInformation2;      // This variable refers to penultimate information on availability of the drug, made by a users.
    private static TextView textViewLastInformation3;      // This variable refers to antepenultimate information on availability of the drug, made by a users.
    private static TextView textViewLastInformationTitle;  // This variable refers to the title of the latest information, made by users.
    private static TextView textViewWithoutNotification;   // This variable refers to the text that appears when there are no notifications made by users.
    private static ValueAnimator cardAnimation;            // This animator refers to the animation that occurs on the card when it is clicked.

    private final Button BUTTON_VIEW_UBS_DESCRIPTION;      // This button is for the user to keep more information about one ubs.
    private final Button BUTTON_SELECT_MEDICINE;           // This button is for the user to select the desired medicine.
    private final RelativeLayout HEADER_LAYOUT;            // This layout refers to the standard structure of the card when it is collapsed.
    private final RelativeLayout EXPAND_LAYOUT;            // This layout refers to the standard structure of the card when it is expanded.
    private final PieChart PIE_CHART;                      // This chart is completed by information provided by users.

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
        this.IMAGE_VIEW_ARROW = (ImageView) card.findViewById(R.id.imageViewArrow);
        this.BUTTON_SELECT_MEDICINE = (Button) card.findViewById(R.id.buttonSelectMedicine);
        this.BUTTON_VIEW_UBS_DESCRIPTION = (Button) card.findViewById(R.id.buttonUbsDescription);
        this.buttonUbsInform = (Button) card.findViewById(R.id.buttonInformUbs);
        this.EXPAND_LAYOUT = (RelativeLayout) card.findViewById(R.id.expandable);
        this.HEADER_LAYOUT = (RelativeLayout) card.findViewById(R.id.header);
        this.PIE_CHART = (PieChart) card.findViewById(R.id.pie_chart_ubs);

        // Here the card is not expanded, it is collapsed.
        this.EXPAND_LAYOUT.setVisibility(View.GONE);

        // getViewTreeObserver returns the notifications about global events, in this case
        // returns information about the card state, if it is expand or collapse.
        this.EXPAND_LAYOUT.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {

                    @Override
                    public boolean onPreDraw() {
                        EXPAND_LAYOUT.getViewTreeObserver().removeOnPreDrawListener(this);
                        EXPAND_LAYOUT.setVisibility(View.GONE);

                        final int widthSpec = View.MeasureSpec.makeMeasureSpec(0,
                                View.MeasureSpec.UNSPECIFIED);
                        final int heightSpec = View.MeasureSpec.makeMeasureSpec(0,
                                View.MeasureSpec.UNSPECIFIED);
                        EXPAND_LAYOUT.measure(widthSpec, heightSpec);

                        cardAnimation = slideAnimator(0, EXPAND_LAYOUT.getMeasuredHeight());
                        return true;
                    }
                });

        // Here we check the card visibility and set users notifications about the last time they get
        // medicine in UBS.
        this.HEADER_LAYOUT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("LOG", "\n" + "onClick() listener call of HEADER_LAYOUT. Status: clicked. Line: 121");
                if (EXPAND_LAYOUT.getVisibility() == View.GONE) {
                    UBS selectItem = CardListAdapterUBS.dataUBS.get(ViewHolderUBS.this.getAdapterPosition());

                    List<Notification> notificationList = null;
                    notificationList = getNotifications(selectItem);

                    Boolean haveNotification;                // This variable tells whether or not notification made by users.
                    haveNotification = false;

                    if (notificationList.size() >= 1) {
                        haveNotification = true;
                        getTextViewLastInformation1().setText("1. " + generateTextNotification(notificationList.get(0)));
                        Log.i("LOG", "\n" + "getTextViewLastInformation1(). Latest information was found. Line 135");
                    } else {
                        getTextViewLastInformation1().setText("");
                        Log.i("LOG", "\n" + "getTextViewLastInformation1(). Latest information does not exist. Line 138");
                    }

                    if (notificationList.size() >= 2) {
                        getTextViewLastInformation2().setText("2. " + generateTextNotification(notificationList.get(1)));
                        Log.i("LOG", "\n" + "getTextViewLastInformation2(). Penultimate information was found. Line 143");
                    } else {
                        getTextViewLastInformation2().setText("");
                        Log.i("LOG", "\n" + "getTextViewLastInformation2(). Penultimate information does not exist. Line 146");
                    }

                    if (notificationList.size() >= 3) {
                        getTextViewLastInformation3().setText("3. " + generateTextNotification(notificationList.get(2)));
                        Log.i("LOG", "\n" + "getTextViewLastInformation3(). Antepenultimate information was found. Line 151");
                    } else {
                        getTextViewLastInformation3().setText("");
                        Log.i("LOG", "\n" + "getTextViewLastInformation3(). Antepenultimate information does not exist. Line 154");
                    }

                    if (haveNotification) {
                        Log.i("LOG", "\n" + "\n" + "\n" + "UBS has notifications in database. Line 158");

                        setInformationOfChart(selectItem);
                        Log.i("LOG", "\n" + "\n" + "Informations about UBS are setted in pie chart. Line 161");

                        getTextViewWithoutNotification().setVisibility(View.GONE);
                        Log.i("LOG", "\n" + "Have notifications, so this method getTextViewWithoutNotification() is not called. Line 164");
                    } else {
                        Log.i("LOG", "\n" + "\n" + "\n" + "UBS does not have notifications. Line 166");

                        getTextViewLastInformationTitle().setText("");
                        Log.i("LOG", "\n" + "Does not have any notifications. Line 169");

                        setInformationOfChartWithoutNotification();
                        Log.i("LOG", "\n" + "\n" + "Graphic setted as empty. Line 171");
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
                    Log.i("LOG", "\n" + "onClick() listener call of HEADER_LAYOUT. Status: collapse. Line: 188");
                    collapse();
                }
            }
        });

        // Here we set the itens that will be showed on ubs card when user click on card.
        this.BUTTON_SELECT_MEDICINE.setOnClickListener(new View.OnClickListener() {
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
        this.BUTTON_VIEW_UBS_DESCRIPTION.setOnClickListener(new View.OnClickListener() {
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
     * Method: getTextViewUbsName()
     * Purpose: set on view ubs name.
     * @return textViewUbsName
     */
    public TextView getTextViewUbsName(){
        TextView newViewUbsName = this.textViewUbsName;
        return newViewUbsName;
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
        TextView newViewNotification = this.textViewWithoutNotification;
        return newViewNotification;
    }

    /**
     * Method: getTextViewLastInformationTitle()
     * Purpose: set on view.
     * @return textViewLastInformationTitle
     */
    public TextView getTextViewLastInformationTitle() {
        TextView newViewLatestInformationTitle = this.textViewLastInformationTitle;
        return newViewLatestInformationTitle;
    }

    /**
     * Method: getTextViewLastInformation1()
     * Purpose: set on view.
     * @return textViewLastInformation1
     */
    public TextView getTextViewLastInformation1() {
        TextView newViewLastInformation1 = this.textViewLastInformation1;
        return newViewLastInformation1;
    }

    /**
     * Method: getTextViewLastInformation2()
     * Purpose: set on view.
     * @return textViewLastInformation2
     */
    public TextView getTextViewLastInformation2() {
        TextView newViewLastInformation2 = this.textViewLastInformation2;
        return newViewLastInformation2;
    }

    /**
     * Method: getTextViewLastInformation3()
     * Purpose: set on view.
     * @return textViewLastInformation3
     */
    public TextView getTextViewLastInformation3() {
        TextView newViewLastInformation3 = this.textViewLastInformation3;
        return newViewLastInformation3;
    }

    /**
     * Method: getBUTTON_SELECT_MEDICINE()
     * Purpose: set on view medicine button.
     * @return BUTTON_SELECT_MEDICINE
     */
    public Button getBUTTON_SELECT_MEDICINE(){
        Button newButtonSelectMedicine = this.BUTTON_SELECT_MEDICINE;
        return newButtonSelectMedicine;
    }

    /**
     * Method: getButtonUbsInform()
     * Purpose: set on view inform ubs button.
     * @return buttonUbsInform
     */
    public Button getButtonUbsInform() {
        Button newButtonUbsInform = this.buttonUbsInform;
        return newButtonUbsInform;
    }

    /**
     * Method: generateTextNotification
     * Purpose: this method check the notification about medicine availabity and set the information
     *          about which ubs the medicine is available.
     * @param notification
     * @return String
     */
    private String generateTextNotification(Notification notification) throws AssertionError{
        String textOfNotification = "";

        if (notification.getAvailable()) {
            textOfNotification = "Disponível em ";
            Log.i("LOG", "\n" + "\n" + "Location information availability is visible. Line 250");
        } else {
            textOfNotification = "Indisponível em ";
            Log.i("LOG", "\n" + "\n" + "The unavailability of information is visible. Line 253");
        }

        Calendar dayCalendar = Calendar.getInstance(new Locale("pt", "BR"));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy");

        dayCalendar.setTime(notification.getDateInform());
        textOfNotification = textOfNotification + simpleDateFormat.format(dayCalendar.getTime());

        // Verify numeric limits
        // The maximum length of a String is the same as an integer
        assert(textOfNotification.length() <= Integer.MAX_VALUE);

        //Verify return value
        assert(textOfNotification != null);
        if (textOfNotification == null) {
            throw new AssertionError("The String textOfNotification can't be null");
        } else {
            // Nothing to do.
        }

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
            Log.i("LOG", "\n" + "\n" + "Medicine select is not empty. Line 279");
        } else {
            // Nothing to do
            Log.i("LOG", "\n" + "\n" + "Medicine select is empty. Line 282");
        }

        if (!medicineSelectedName.isEmpty()) {
            queryNotification.whereEqualTo(Notification.getTitleMedicineName(), medicineSelectedName);
            Log.i("LOG", "\n" + "\n" + "Medicine select is not empty. Line 287");
        } else {
            // Nothing to do
            Log.i("LOG", "\n" + "\n" + "Medicine select is empty. Line 290");
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
        PIE_CHART.setDescription("");
        PIE_CHART.setDrawHoleEnabled(true);
        PIE_CHART.setHoleRadius(0);
        PIE_CHART.setTransparentCircleRadius(40);
        PIE_CHART.setDrawSliceText(false);
        PIE_CHART.setRotationAngle(0);
        PIE_CHART.setRotationEnabled(true);
        PIE_CHART.animateY(1000);

        PIE_CHART.getLegend().setEnabled(true);
        PIE_CHART.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        PIE_CHART.getLegend().setTextSize(12);

        PIE_CHART.setNoDataTextDescription("Sem notificações encontradas.");

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

        PIE_CHART.setData(pieData);
    }

    /**
     * Method: setInformationOfChart()
     * Purpose: this method set ubs information of chart.
     * @param ubs
     */
    private void setInformationOfChart(UBS ubs) {
        PIE_CHART.setDescription("");
        PIE_CHART.setDrawHoleEnabled(true);
        PIE_CHART.setHoleRadius(0);
        PIE_CHART.setTransparentCircleRadius(40);
        PIE_CHART.setDrawSliceText(false);
        PIE_CHART.setRotationAngle(0);
        PIE_CHART.setRotationEnabled(true);
        PIE_CHART.animateY(1000);

        PIE_CHART.getLegend().setEnabled(true);
        PIE_CHART.getLegend().setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);
        PIE_CHART.getLegend().setTextSize(12);

        PIE_CHART.setNoDataTextDescription("Sem notificações encontradas.");
        PIE_CHART.setData(getDataPie(ubs));
    }

    /**
     * Method: expand()
     * Purpose: this method set card visible.
     */
    private void expand() {
        Log.i("LOG", "expand() enter. Status: visible. Line: 440");
        EXPAND_LAYOUT.setVisibility(View.VISIBLE);
        cardAnimation.start();
        IMAGE_VIEW_ARROW.setBackgroundResource(R.drawable.ic_keyboard_arrow_up);
    }

    /**
     * Method: collapse()
     * Purpose: this method set card 'not' visible.
     */
    private void collapse() {
        int finalHeight = EXPAND_LAYOUT.getHeight();

        ValueAnimator cardAnimationCollapse = slideAnimator(finalHeight, 0);
        cardAnimationCollapse.addListener(new Animator.AnimatorListener(){
            @Override
            public void onAnimationEnd(Animator animator) {
                Log.i("LOG", "collapse onAnimationEnd() enter, Status: gone. Line 457");
                EXPAND_LAYOUT.setVisibility(View.GONE);
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
        IMAGE_VIEW_ARROW.setBackgroundResource(R.drawable.ic_keyboard_arrow_down);
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

                ViewGroup.LayoutParams layoutParams = EXPAND_LAYOUT.getLayoutParams();
                layoutParams.height = value;
                EXPAND_LAYOUT.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }

}