package edu.salleurl.lscatalunya.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.model.Center;
import edu.salleurl.lscatalunya.model.CenterManager;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    public final static String ADDRESS_EXTRA = "addressExtra";
    private final static String POSITION_KEY = "positionKey";

    private final static int LOCATION_PERMISSION = 4718;

    //Map utils
    private final static String CATALONIA_LOCALE = "ca_ES";
    private final static double CATALONIA_LAT = 41.81;
    private final static double CATALONIA_LON = 1.47;
    private final static float CATALONIA_ZOOM = 6.8f;
    private final static float ADDRESS_ZOOM = 15.0f;

    //Data
    private GoogleMap map;
    private CameraPosition cameraPosition;

    //Views
    private Spinner centerTypes;

    //Center manager
    private CenterManager centerManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Get data
        centerManager = CenterManager.getInstance();
        if(savedInstanceState != null) {
            cameraPosition = savedInstanceState.getParcelable(POSITION_KEY);
        } else {

            Intent intent = getIntent();
            cameraPosition = new CameraPosition.Builder().target(new LatLng(CATALONIA_LAT,
                    CATALONIA_LON)).zoom(CATALONIA_ZOOM).build();

            if(intent.hasExtra(ADDRESS_EXTRA)) {
                String address = intent.getStringExtra(ADDRESS_EXTRA);
                try {
                    Geocoder geocoder = new Geocoder(this, new Locale(CATALONIA_LOCALE));
                    List<Address> addresses = geocoder.getFromLocationName(address, 1);
                    cameraPosition = new CameraPosition.Builder().
                            target(new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude())).
                            zoom(ADDRESS_ZOOM).build();
                } catch(IOException e) {
                    Toast.makeText(this, getString(R.string.address_not_found), Toast.LENGTH_SHORT).
                            show();
                }
            }

        }

        //Set center type list and link listener
        ArrayAdapter<CharSequence> centerTypesAdapter = ArrayAdapter.createFromResource(this,
                R.array.center_types, android.R.layout.simple_spinner_item);
        centerTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        centerTypes = findViewById(R.id.mapCenterTypes);
        centerTypes.setAdapter(centerTypesAdapter);
        centerTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                updateMapMarkers();
            }
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        //Link map fragment
        MapFragment mapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapFragment, mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);

    }

    private void updateMapMarkers() {
        ArrayList<Center> centers = new ArrayList<>();
        if(String.valueOf(centerTypes.getSelectedItem()).equals(getString(R.string.all))) {
            centers = centerManager.getCenters();
        } else if(String.valueOf(centerTypes.getSelectedItem()).equals(getString(R.string.schools))) {
            centers = centerManager.getSchools();
        } else if(String.valueOf(centerTypes.getSelectedItem()).equals(getString(R.string.others))) {
            centers = centerManager.getOthers();
        }
        setMarkers(centers);
    }

    private void setMarkers(ArrayList<Center> centers) {
        //Custom marker
        //MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_location));
        map.clear();
        boolean error = false;
        for(Center c : centers) {
            try {
                //Get location and add marker to map if possible
                Geocoder geocoder = new Geocoder(this, new Locale(CATALONIA_LOCALE));
                List<Address> addresses = geocoder.getFromLocationName(c.getAddress(), 1);
                MarkerOptions marker = new MarkerOptions();
                marker.position(new LatLng(addresses.get(0).getLatitude(),
                        addresses.get(0).getLongitude()));
                map.addMarker(marker);
            } catch(IOException e) {
                error = true;
            }
        }
        if(error) {
            Toast.makeText(this, getString(R.string.centers_not_found), Toast.LENGTH_SHORT).
                    show();
        }
    }

    public void showCentersList(View view) {
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        //Update map position and UI
        map = googleMap;
        map.getUiSettings().setMapToolbarEnabled(false);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(cameraPosition.target,
                cameraPosition.zoom);
        map.animateCamera(cameraUpdate);

        //Show markers
        updateMapMarkers();

        //Check permissions to self-position
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_PERMISSION);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION);
            return;
        }

        map.setMyLocationEnabled(true);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    map.setMyLocationEnabled(true);
                }
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(POSITION_KEY, map.getCameraPosition());
        super.onSaveInstanceState(outState);
    }

}