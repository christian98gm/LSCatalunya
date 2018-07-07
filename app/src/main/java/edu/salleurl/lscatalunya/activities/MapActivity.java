package edu.salleurl.lscatalunya.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.model.Center;
import edu.salleurl.lscatalunya.model.CenterManager;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    public final static String CENTER_EXTRA = "addressExtra";

    //Saved instance keys
    private final static String POSITION_KEY = "positionKey";
    private final static String CENTER_KEY = "centerKey";

    //Permission id
    private final static int LOCATION_PERMISSION = 4718;

    //Map utils
    private final static double CATALONIA_LAT = 41.81;
    private final static double CATALONIA_LON = 1.47;
    private final static float CATALONIA_ZOOM = 6.8f;
    private final static float ADDRESS_ZOOM = 15.0f;

    //Data
    private GoogleMap map;
    private CameraPosition cameraPosition;
    private BottomSheetBehavior sheetBehavior;
    private Map<String, Center> hashMap;
    private Center sheetCenter;
    private boolean enabledSpinner;

    //Views
    private Spinner centerTypes;
    private TextView mapSchoolName;
    private TextView mapSchoolAddress;

    //Center manager
    private CenterManager centerManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        //Get sheet behavior
        sheetBehavior = BottomSheetBehavior.from(findViewById(R.id.mapBottomSheet));
        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if(newState == BottomSheetBehavior.STATE_HIDDEN) {
                    sheetCenter = null;
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {}
        });
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        //Get sheet views
        mapSchoolName = findViewById(R.id.mapName);
        mapSchoolAddress = findViewById(R.id.mapAddress);

        //Get data
        centerManager = CenterManager.getInstance();
        if(savedInstanceState != null) {
            cameraPosition = savedInstanceState.getParcelable(POSITION_KEY);
            sheetCenter = savedInstanceState.getParcelable(CENTER_KEY);
        } else {

            Intent intent = getIntent();
            cameraPosition = new CameraPosition.Builder().target(new LatLng(CATALONIA_LAT,
                    CATALONIA_LON)).zoom(CATALONIA_ZOOM).build();

            if(intent.hasExtra(CENTER_EXTRA)) {
                Center center = intent.getParcelableExtra(CENTER_EXTRA);
                if(center.getLocation() != null) {
                    cameraPosition = new CameraPosition.Builder().target(center.getLocation()).
                            zoom(ADDRESS_ZOOM).build();
                } else {
                    Toast.makeText(this, getString(R.string.address_not_found), Toast.LENGTH_SHORT).
                            show();
                }
            }

        }

        //Set center type list and link listener
        enabledSpinner = false;
        ArrayAdapter<CharSequence> centerTypesAdapter = ArrayAdapter.createFromResource(this,
                R.array.center_types, android.R.layout.simple_spinner_item);
        centerTypesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        centerTypes = findViewById(R.id.mapTypes);
        centerTypes.setAdapter(centerTypesAdapter);
        centerTypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(enabledSpinner) {
                    updateMapMarkers();
                } else {
                    enabledSpinner = true;
                }
            }
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        //Link map fragment
        MapFragment mapFragment = MapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapFrame, mapFragment);
        fragmentTransaction.commit();
        mapFragment.getMapAsync(this);

    }

    private void updateMapMarkers() {
        if(map != null) {
            ArrayList<Center> centers = new ArrayList<>();
            if (String.valueOf(centerTypes.getSelectedItem()).equals(getString(R.string.all))) {
                centers = centerManager.getCenters();
            } else if (String.valueOf(centerTypes.getSelectedItem()).equals(getString(R.string.schools))) {
                centers = centerManager.getSchools();
            } else if (String.valueOf(centerTypes.getSelectedItem()).equals(getString(R.string.others))) {
                centers = centerManager.getOthers();
            }
            setMarkers(centers);
        }
    }

    private void setMarkers(ArrayList<Center> centers) {
        map.clear();
        hashMap = new HashMap<>();
        boolean error = false;
        for(Center c : centers) {
            if(c.getLocation() != null) {
                //Get location and add marker to map if possible
                MarkerOptions marker = new MarkerOptions();
                marker.position(c.getLocation());
                marker.icon(BitmapDescriptorFactory.defaultMarker(getMarkerColor(c)));
                Marker mapMarker = map.addMarker(marker);
                hashMap.put(mapMarker.getId(), c);
            } else {
                error = true;
            }
        }
        if(error) {
            Toast.makeText(this, getString(R.string.centers_not_found), Toast.LENGTH_SHORT).
                    show();
        }
    }

    private float getMarkerColor(Center center) {
        if(center.hasUniversity()) {
            return BitmapDescriptorFactory.HUE_CYAN;
        } else if(center.hasVocationalTraining()) {
            return BitmapDescriptorFactory.HUE_GREEN;
        } else if(center.hasHighSchool()) {
            return BitmapDescriptorFactory.HUE_RED;
        } else if(center.hasSecondary()) {
            return BitmapDescriptorFactory.HUE_ORANGE;
        } else if(center.hasPrimary()) {
            return BitmapDescriptorFactory.HUE_YELLOW;
        } else if(center.hasChildren()) {
            return BitmapDescriptorFactory.HUE_VIOLET;
        } else {
            return BitmapDescriptorFactory.HUE_AZURE;
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
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                sheetCenter = hashMap.get(marker.getId());
                mapSchoolName.setText(sheetCenter.getName());
                mapSchoolAddress.setText(sheetCenter.getAddress());
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                return false;
            }
        });

        //Show markers
        updateMapMarkers();

        //Show sheet if there is content
        if(sheetCenter != null) {
            mapSchoolName.setText(sheetCenter.getName());
            mapSchoolAddress.setText(sheetCenter.getAddress());
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

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

        googleMap.setMyLocationEnabled(true);

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
        outState.putParcelable(CENTER_KEY, sheetCenter);
        super.onSaveInstanceState(outState);
    }

}