/**
 * File: UBS.java
 * Purpose: this file has methods get and set about ubs.
 */
package com.gppmds.tra.temremdioa.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@ParseClassName("UBS")
public class UBS extends ParseObject {

    /**
     * Method: getUbsLatitude()
     * Purpose: Returns the latitude of the establishment.
     * @return getUbsLatitudeTitle()
     */
    public Double getUbsLatitude(){
        return getDouble(getUbsLatitudeTitle());
    }

    /**
     * Method: setUbsLatitude(D)
     * Purpose: Sets a latitude to the establishment.
     */
    public void setUbsLatitude(Double ubsLatitude){
        put(getUbsLatitudeTitle(), ubsLatitude);
    }

    /**
     * Method: getUbsLongitude()
     * Purpose: Returns the longitude of the establishment.
     * @return getUbsLongitudeTitle()
     */
    public Double getUbsLongitude(){
        return getDouble(getUbsLongitudeTitle());
    }

    /**
     * Method: setUbsLongitude()
     * Purpose: Sets the longitude to the establishment.
     */
    public void setUbsLongitude(Double ubsLongitude){
        put(getUbsLongitudeTitle(), ubsLongitude);
    }

    /**
     * Method: getUbsName()
     * Purpose: Returns the name of the establishment.
     * @return getUbsNameTitle()
     */
    public String getUbsName(){
        return getString(getUbsNameTitle());
    }

    /**
     * Method: setUbsName()
     * Purpose: Sets a name to the establishment.
     */
    public void setUbsName(String ubsName){
        put(getUbsNameTitle(), ubsName);
    }

    /**
     * Method: getUbsAddress()
     * Purpose: Returns the address of the establishment.
     * @return getUbsAddressTitle()
     */
    public String getUbsAddress(){
        return getString(getUbsAddressTitle());
    }

    /**
     * Method: setUbsAddress()
     * Purpose: Sets an address to the establishment.
     */
    public void setUbsAddress(String ubsAddress){
        put(getUbsAddressTitle(), ubsAddress);
    }

    /**
     * Method: getUbsNeighborhood()
     * Purpose: Returns the neighborhood of the establishment.
     * @return getUbsNeighborhoodTitle()
     */
    public String getUbsNeighborhood(){
        return getString(getUbsNeighborhoodTitle());
    }

    /**
     * Method: setUbsNeighborhood()
     * Purpose: Sets a neighborhood to the establishment.
     */
    public void setUbsNeighborhood(String ubsNeighborhood){
        put(getUbsNeighborhoodTitle(), ubsNeighborhood);
    }

    /**
     * Method: getUbsCity()
     * Purpose: Returns a city of the establishment.
     * @return getUbsCityTitle()
     */
    public String getUbsCity(){
        return getString(getUbsCityTitle());
    }

    /**
     * Method: setUbsCity()
     * Purpose: Sets a city to the establishment.
     */
    public void setUbsCity(String ubsCity){
        put(getUbsCityTitle(), ubsCity);
    }

    /**
     * Method: getUbsAttentionLevel()
     * Purpose: Returns the attention level of a establishment.
     * @return getUbsAttentionLevelTitle()
     */
    public String getUbsAttentionLevel(){
        return getString(getUbsAttentionLevelTitle());
    }

    /**
     * Method: setUbsAttentionLevel()
     * Purpose: Sets an attention level to a establishment.
     */
    public void setUbsAttentionLevel(String ubsAttentionLevel){
        put(getUbsAttentionLevelTitle(), ubsAttentionLevel);
    }

    /**
     * Method: getUbsPhone()
     * Purpose: Returns the phone of a establishment.
     * @return getUbsPhoneTitle()
     */
    public String getUbsPhone(){
        return getString(getUbsPhoneTitle());
    }

    /**
     * Method: setUbsPhone()
     * Purpose: Sets the phone of the establishment.
     */
    public void setUbsPhone(String ubsPhone){
        put(getUbsPhoneTitle(), ubsPhone);
    }

    /**
     * Method: getQuery()
     * Purpose: Query UBS data from parse.
     * @return ParseQuery
     */
    public static ParseQuery<UBS> getQuery() {
        return ParseQuery.getQuery(UBS.class);
    }

    /**
     * Method: getUbsLongitudeTitle()
     * Purpose: Returns the longitude title of a establishment.
     * @return vlr_longitude
     */
    public static String getUbsLongitudeTitle(){
        return "vlr_longitude";
    }

    /**
     * Method: getUbsLatitudeTitle()
     * Purpose: Returns the latitude title of a establishment.
     * @return vlr_latitude
     */
    public static String getUbsLatitudeTitle(){
        return "vlr_latitude";
    }

    /**
     * Method: getUbsNameTitle()
     * Purpose: Returns the name title of a establishment.
     * @return nom_estab
     */
    public static String getUbsNameTitle(){
        return "nom_estab";
    }

    /**
     * Method: getUbsAddressTitle()
     * Purpose: Returns the address title of a establishment.
     * @return dsc_endereco
     */
    public static String getUbsAddressTitle(){
        return "dsc_endereco";
    }

    /**
     * Method: getUbsNeighborhoodTitle()
     * Purpose: Returns the neighborhood title of a establishment.
     * @return dsc_bairro
     */
    public static String getUbsNeighborhoodTitle(){
        return "dsc_bairro";
    }

    /**
     * Method: getUbsCityTitle()
     * Purpose: Returns the city title of a establishment.
     * @return dsc_cidade
     */
    public static String getUbsCityTitle(){ return "dsc_cidade"; }

    /**
     * Method: getUbsAttentionLevelTitle()
     * Purpose: Returns the attention level title of a establishment.
     * @return nivel_at
     */
    public static String getUbsAttentionLevelTitle(){
        return "nivel_at";
    }

    /**
     * Method: getUbsPhoneTitle()
     * Purpose: Returns the phone of a establishment.
     * @return telefone
     */
    public static String getUbsPhoneTitle() {
        return "telefone";
    }

    /**
     * Method: toString()
     * Purpose: Overrides the method to returns the description title of a establishment.
     * @return getUbsNameTitle()
     */
    @Override
    public String toString(){
        return getString(getUbsNameTitle());
    }
}
