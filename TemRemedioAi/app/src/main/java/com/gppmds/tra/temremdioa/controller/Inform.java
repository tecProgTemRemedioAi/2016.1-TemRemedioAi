/**
 * File: Inform.java
 * Purpose: this file purpose inform about medicines and ubs.
 */
package com.gppmds.tra.temremdioa.controller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.gppmds.tra.temremdioa.model.Notification;
import com.parse.ParseUser;
import com.tra.gppmds.temremdioa.R;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Inform extends AppCompatActivity {

    private static TextView textViewInformedMedicine;
    private static TextView textViewInformedUbs;
    private static TextView textViewAvailableError;
    private static RadioButton radioButtonAvailable;
    private static RadioButton radioButtonNotAvailable;
    private static Button informButton;
    private static Button cancelButton;
    private static DatePicker datePickerInform;

    private Boolean availability;                       // This variable refers to avaiability of medicines.
    private String ubsName;                             // This variable refers to ubs name.
    private String medicineName;                        // This variable refers to medicina name.
    private String medicineDosage;                      // This variable refers to medicine dosage.

    /**
     * Name: onCreate
     * Purpose: send informations on create a new medicine
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // check the parameter value.
        assert(savedInstanceState != null);

        final String SUCCESS_SEND_INFORMATION = (String) "Informação enviada com sucesso.";
        final String ERROR_SEND_COMPLETE_ACTION = (String) "Não foi possível completar a ação.";

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.inform);

            cancelButton = (Button) findViewById(R.id.cancel_button);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            informButton = (Button) findViewById(R.id.inform_button);
            informButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (validateInform()) {
                        // When information is sent successfully
                        attemptInform();
                        Toast.makeText(view.getContext(), SUCCESS_SEND_INFORMATION, Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        //When it is not possible to send the information
                        Toast.makeText(view.getContext(), ERROR_SEND_COMPLETE_ACTION, Toast.LENGTH_LONG).show();
                    }
                }
            });

            ubsName = getIntent().getStringExtra("UBSName");

            medicineName = (String) getIntent().getStringExtra("MedicineName");
            medicineDosage = (String) getIntent().getStringExtra("MedicineDosage");

            textViewInformedMedicine = (TextView) findViewById(R.id.informed_medicine);
            textViewInformedMedicine.setText(medicineName);
            textViewInformedUbs = (TextView) findViewById(R.id.informed_ubs);
            textViewInformedUbs.setText(ubsName);

            radioButtonAvailable = (RadioButton) findViewById(R.id.available);
            radioButtonNotAvailable = (RadioButton) findViewById(R.id.not_available);

            datePickerInform = (DatePicker) findViewById(R.id.date_picker_inform);

        } catch (Throwable e){
            // exception was caught.
            e.printStackTrace();
        }
    }

    /**
     * Name: attemptInform
     * Purpose:
     */
    private void attemptInform() {

        if (radioButtonAvailable.isChecked()) {
            availability = (boolean) true;
        } else if (radioButtonNotAvailable.isChecked()) {
            availability = (boolean) false;
        } else {
            availability = (boolean) false;
        }

        Integer selectedYear = (Integer) datePickerInform.getYear();
        Integer selectedMonth = (Integer) datePickerInform.getMonth();
        Integer selectedDay = (Integer) datePickerInform.getDayOfMonth();

        Calendar calendar = new GregorianCalendar();
        calendar.set(selectedYear, selectedMonth, selectedDay);

        ParseUser getCurrentUser = (ParseUser) ParseUser.getCurrentUser();

        Notification notification = new Notification();
        notification.setMedicineDosage(medicineDosage);
        notification.setMedicineName(medicineName);
        notification.setUBSName(ubsName);
        notification.setAvailable(availability);
        notification.setDateInform(calendar.getTime());
        notification.setUserInform(getCurrentUser.getUsername());
        notification.pinInBackground();
        notification.saveInBackground();

        calendar.finalize();

    }

    /**
     * Name: vaidateInform
     * Purpose: validate the information of medicine
     * @return boolean
     */
    private boolean validateInform() {

        final String NOT_SELECTED_AVALIABLE =  (String) "Disponibilidade não selecionada";

        boolean returnValidate = (boolean) true;

        if (!radioButtonAvailable.isChecked() && !radioButtonNotAvailable.isChecked()) {

            radioButtonAvailable.setError(NOT_SELECTED_AVALIABLE);

            returnValidate = (boolean) false;
        } else {
            /* nothing to do */
        }

        return returnValidate;

    }

}
