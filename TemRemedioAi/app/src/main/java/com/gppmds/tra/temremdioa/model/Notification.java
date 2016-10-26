/**
 * Class: Notification
 * Purpose: this class set all things about medicine cards.
 */

package com.gppmds.tra.temremdioa.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;

@ParseClassName("Notification")
public class Notification extends ParseObject {

    /**
     * Method: getMedicineName()
     * Purpose:
     * @return medicineName
     */
    public String getMedicineName(){
        return getString(getTitleMedicineName());
    }

    /**
     * Method: setMedicineName
     * Purpose:
     * @param medicine_name
     */
    public void setMedicineName(String medicine_name){
        put(getTitleMedicineName(), medicine_name);
    }

    /**
     * Method: getMedicineDosage
     * Purpose:
     * @return medicineDosage
     */
    public String getMedicineDosage(){
        return getString(getTitleMedicineDosage());
    }

    /**
     * Method: setMedicineDosage
     * Purpose:
     * @param medicine_dosage
     */
    public void setMedicineDosage(String medicine_dosage){
        put(getTitleMedicineDosage(), medicine_dosage);
    }

    /**
     * Method: getUBSName
     * Purpose:
     * @return UBSName
     */
    public String getUBSName(){
        return getString(getTitleUBSName());
    }

    /**
     * Method: setUBSName
     * Purpose:
     * @param ubs_name
     */
    public void setUBSName(String ubs_name){
        put(getTitleUBSName(), ubs_name);
    }

    /**
     * Method: getDateInform
     * Purpose:
     * @return dateInform
     */
    public Date getDateInform(){
        return getDate(getTitleDateInform());
    }

    /**
     * Method: setDateInform
     * Purpose:
     * @param date_inform
     */
    public void setDateInform(Date date_inform){
        put(getTitleDateInform(), date_inform);
    }

    /**
     * Method: getAvailable
     * Purpose:
     * @return avaiable
     */
    public Boolean getAvailable(){
        return getBoolean(getTitleAvailable());
    }

    /**
     * Method: setAvailable
     * Purpose:
     * @param available
     */
    public void setAvailable(Boolean available){
        put(getTitleAvailable(), available);
    }

    /**
     * Method: getUserInform
     * Purpose:
     * @return userInform
     */
    public String getUserInform(){
        return getString(getTitleUser());
    }

    /**
     * Method: setUserInform
     * Purpose:
     * @param code
     */
    public void setUserInform(String code){
        put(getTitleUser(), code);
    }

    /**
     * Method: getQuery
     * Purpose:
     * @return ParseQuery
     */
    public static ParseQuery<Notification> getQuery() {
        return ParseQuery.getQuery(Notification.class);
    }

    /**
     * Method: getTitleMedicineName
     * Purpose:
     * @return medicine_name
     */
    public static String getTitleMedicineName(){
        return "medicine_name";
    }

    /**
     * Method: getTitleMedicineDosage
     * Purpose:
     * @return medicine_dosage
     */
    public static String getTitleMedicineDosage(){
        return "medicine_dosage";
    }

    /**
     * Method: getTitleUBSName
     * Purpose:
     * @return ubs_name
     */
    public static String getTitleUBSName(){
        return "ubs_name";
    }

    /**
     * Method: getTitleDateInform
     * Purpose:
     * @return data_inform
     */
    public static String getTitleDateInform(){
        return "data_inform";
    }

    /**
     * Method: getTitleAvailable
     * Purpose:
     * @return medicine_avaiable
     */
    public static String getTitleAvailable(){
        return "medicine_available";
    }

    /**
     * Method: getTitleUser
     * Purpose:
     * @return user_inform
     */
    public static String getTitleUser(){ return "user_inform"; }

    @Override
    public String toString(){
        return getString(getTitleMedicineName());
    }

}
