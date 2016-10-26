/**
 * File: UbsMapsActivity.java
 * Purpose: this file has methods about maps of ubs.
 */
package com.gppmds.tra.temremdioa.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.tra.gppmds.temremdioa.R;

public class UbsMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;     // This variable refers to the ubs map.
    private Double latitude;    // This variable refers to the ubs latitude.
    private Double longitude;   // This variable refers to the ubs longitude.
    private String ubsName;     // This variable refers to the ubs name.

    private static final int LATLNGZOOM = 13;   // This variable refers to the map zoom that is showed on the screen.

    /**
     * Method: onCreate()
     * Purpose:
     * @return
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubs_maps);
        Log.i("LOG", "\n" + "onCreate() activity ubs maps. Status: view. Line 46");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        obtainSupportMapFragment();

        gettingLatitudeAndLongitude();

        // Getting UBS descriptions from UBS Holder class
        gettingUbsFromHolder();

        // Ubs trajectory
        generateTrajectory();
    }

    /**
     * Method: obtainSupportMapFragment()
     * Purpose:
     * @return
     */
    private void obtainSupportMapFragment() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Log.i("LOG", "\n" + "obtainSupportMapFragment(). Status: Ubs map visible. Line 68");
    };

    /**
     * Method: gettingLatitudeAndLongitude()
     * Purpose:
     * @return
     */
    private void gettingLatitudeAndLongitude() {
        latitude = getIntent().getDoubleExtra("latitude", 0);
        longitude = getIntent().getDoubleExtra("longitude", 0);
        Log.i("LOG", "\n" + "gettingLatitudeAndLongitude(). Status: Ubs latitude and longitude visible. Line 79");
    }

    /**
     * Method: gettingUbsFromHolder()
     * Purpose:
     * @return
     */
    private void gettingUbsFromHolder() {
        gettingUbsName();
        gettingUbsAddress();
        gettingUbsNeighborhood();
        gettingUbsCity();
        gettingUbsPhone();
    }

    /**
     * Method: gettingUbsName()
     * Purpose:
     * @return
     */
    private void gettingUbsName() {
        ubsName = getIntent().getStringExtra("nomeUBS");
        TextView editName = (TextView) findViewById(R.id.textViewUbsName);
        editName.setText(ubsName);
        Log.i("LOG", "\n" + "gettingUbsName(). Status: Ubs name that was selected. Line 104");
    }

    /**
     * Method: gettingUbsAddress ()
     * Purpose:
     * @return
     */
    private void gettingUbsAddress () {
        String descUbsAddress = getIntent().getStringExtra("descEnderecoUBS");
        TextView editDscAddress = (TextView) findViewById(R.id.textViewUbsAddress);
        editDscAddress.setText(descUbsAddress);
        Log.i("LOG", "\n" + "gettingUbsAddress(). Status: Ubs address that was selected. Line 116");
    }

    /**
     * Method: gettingUbsNeighborhood()
     * Purpose:
     * @return
     */
    private void gettingUbsNeighborhood() {
        String descUbsNeighborhood = getIntent().getStringExtra("descBairroUBS");
        TextView editDscNeighborhood = (TextView) findViewById(R.id.textViewUbsNeighborhood);
        editDscNeighborhood.setText(descUbsNeighborhood);
        Log.i("LOG", "\n" + "gettingUbsNeighborhood(). Status: Ubs neighborhood that was selected. Line 128");
    }

    /**
     * Method: gettingUbsCity()
     * Purpose:
     * @return
     */
    private void gettingUbsCity() {
        String descUbsCity = getIntent().getStringExtra("descCidadeUBS");
        TextView editDscCity = (TextView) findViewById(R.id.textViewCityUbs);
        editDscCity.setText(descUbsCity);
        Log.i("LOG", "\n" + "gettingUbsCity(). Status: Ubs city that was selected. Line 140");
    }

    /**
     * Method: gettingUbsPhone()
     * Purpose:
     * @return
     */
    private void gettingUbsPhone() {
        String descUbsPhone = getIntent().getStringExtra("telefoneUBS");
        TextView editDscPhone = (TextView) findViewById(R.id.textViewPhoneUbs);
        editDscPhone.setText(descUbsPhone);
        Log.i("LOG", "\n" + "gettingUbsPhone(). Status: Ubs phone that was selected. Line 152");
    }

    /**
     * Method: generateTrajectory()
     * Purpose:
     * @return
     */
    private void generateTrajectory() {
        FloatingActionButton generateTrajectory;
        generateTrajectory = (FloatingActionButton) findViewById(R.id.direction);
        generateTrajectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUpMapIfNeeded();
            }
        });
        Log.i("LOG", "\n" + "generateTrajectory() enter. Status: Clicked generate trajetory button. Line 169");
    }

    /**
     * Method: setUpMapIfNeeded(
     * Purpose:
     * @return
     */
    private void setUpMapIfNeeded() {
        if(isGoogleMapsInstalled()) {
            // Get latitude and longitude from ubs Holder and open with GMaps
            String uri = "http://maps.google.com/maps?saddr="+"&daddr="+latitude+","+longitude;
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setClassName("com.google.android.apps.maps",
                    "com.google.android.maps.MapsActivity");
            startActivity(intent);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Maps");
            builder.setIcon(R.drawable.google_maps_icon);
            builder.setMessage("Instalar Google Maps");
            builder.setCancelable(true);
            builder.setNegativeButton("Cancelar",null);
            builder.setPositiveButton("Instalar", getGoogleMapsListener());
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    /**
     * Method: isGoogleMapsInstalled()
     * Purpose:
     * @return true or false
     */
    public boolean isGoogleMapsInstalled() {
        try {
            ApplicationInfo info = getPackageManager()
                    .getApplicationInfo("com.google.android.apps.maps", 0);
            return true;
        } catch(PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Method: getGoogleMapsListener()
     * Purpose:
     * @return
     */
    public DialogInterface.OnClickListener getGoogleMapsListener() {
        return new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=com.google.android.apps.maps"));
                startActivity(intent);

                // Finish the activity so they can't circumvent the check
                finish();
            }
        };
    }

    /**
     * Method: onMapReady(GoogleMap googleMap)
     * Purpose:
     * @return
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Get latitude and longitude to create a marker on map
        LatLng latLngValues = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(latLngValues).title(ubsName));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngValues, LATLNGZOOM));
    }
}
