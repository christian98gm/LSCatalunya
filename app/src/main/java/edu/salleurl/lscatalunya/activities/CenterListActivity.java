package edu.salleurl.lscatalunya.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.adapters.TabAdapter;
import edu.salleurl.lscatalunya.fragment.RecyclerViewFragment;
import edu.salleurl.lscatalunya.listeners.RefreshListener;
import edu.salleurl.lscatalunya.model.Center;
import edu.salleurl.lscatalunya.model.CenterManager;
import edu.salleurl.lscatalunya.repositories.AsyncCenterRepo;
import edu.salleurl.lscatalunya.repositories.impl.CenterWebService;
import edu.salleurl.lscatalunya.repositories.json.JsonException;

public class CenterListActivity extends AppCompatActivity implements AsyncCenterRepo.Callback {

    //Number of tabs
    private final static int TOTAL_TABS = 3;

    //Save instance key
    private final static String FIRST_TIME_KEY = "firstTimeKey";

    //Switch keys
    private final static int START_WEB_SERVICE = 1;
    private final static int STOP_WEB_SERVICE = 2;
    private final static int UPDATE_CENTERS = 3;
    private final static int STOP_REFRESH_STATE = 4;

    //Data managers
    private CenterManager centerManager;
    private CenterWebService centerWebService;

    //Views
    private Spinner provincesSpinner;

    //Fragments
    private RecyclerViewFragment[] fragments;

    //Adapters
    private TabAdapter tabAdapter;
    private RefreshListener refreshListener;

    //Load states
    private boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_list);

        //Get center manager and repository
        centerManager = CenterManager.getInstance();
        centerWebService = CenterWebService.getInstance(this, this);

        //Link views
        provincesSpinner = findViewById(R.id.centerListProvinces);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.centerListRefresh);
        ViewPager viewPager = findViewById(R.id.centerListViewPager);
        TabLayout tabLayout = findViewById(R.id.centerListTab);

        //Link provinces adapter
        ArrayAdapter<CharSequence> provincesAdapter = ArrayAdapter.createFromResource(this,
                R.array.provinces, android.R.layout.simple_spinner_item);
        provincesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        provincesSpinner.setAdapter(provincesAdapter);

        //Link provinces listener
        provincesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                centerManager.setProvince(String.valueOf(provincesSpinner.getSelectedItem()));
                updateFragmentsState(UPDATE_CENTERS);
            }
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        //Link fragments
        fragments = new RecyclerViewFragment[TOTAL_TABS];
        fragments[0] = RecyclerViewFragment.newInstance(this, centerManager.getCentersIn());
        fragments[1] = RecyclerViewFragment.newInstance(this, centerManager.getSchoolsIn());
        fragments[2] = RecyclerViewFragment.newInstance(this, centerManager.getOthersIn());

        //Link tab adapter
        String[] tabs = getResources().getStringArray(R.array.center_types);
        tabAdapter = new TabAdapter(getSupportFragmentManager(), fragments, tabs);
        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        //Link swipe listener
        refreshListener = new RefreshListener(this, swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);

        //Get center data
        if(savedInstanceState == null) {
            Toast.makeText(this, getString(R.string.wait_loading), Toast.LENGTH_SHORT).show();
            firstTime = true;
            getCentersData();
        } else {
            firstTime = savedInstanceState.getBoolean(FIRST_TIME_KEY);
        }

    }

    public void getCentersData() {
        if(!centerWebService.isWorking()) {
            firstTime = false;
            centerWebService.getCenters();
        }
    }

    @Override
    synchronized public void onResponse(Center center, int errorCode, boolean endInformation) {

        //Check error code
        switch(errorCode) {
            case CenterWebService.OK:           //Update centers data
                centerManager.addCenter(center);
                updateFragmentsState(UPDATE_CENTERS);
                break;
            case CenterWebService.HTTP_ERROR:   //Connection error
                Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT)
                        .show();
                break;
            case JsonException.FORMAT_ERROR:    //Error from web service
                Toast.makeText(this, getString(R.string.format_error), Toast.LENGTH_SHORT)
                        .show();
                break;
            case JsonException.READ_ERROR:      //Error reading data
                Toast.makeText(this, getString(R.string.read_error), Toast.LENGTH_SHORT)
                        .show();
                break;
        }

        //Check if data is complete
        if(endInformation) {
            updateFragmentsState(STOP_REFRESH_STATE);
        }

    }

    private void updateFragmentsState(int mode) {
        switch(mode) {
            case UPDATE_CENTERS:        //Notify centers have been updated
                for(int i = 0; i < TOTAL_TABS; i++) {
                    fragments[i].updatedCenters();
                }
                tabAdapter.notifyDataSetChanged();
                break;
            case STOP_REFRESH_STATE:    //Notify refresh has finished
                refreshListener.refreshedList();
                break;
        }
    }

    private void updateWebServiceState(int mode) {
        switch(mode) {
            case START_WEB_SERVICE: //Start searching data if data search is paused
                if(centerWebService.isPaused()) {
                    centerWebService.startRequest();
                }
                break;
            case STOP_WEB_SERVICE:  //Stop searching data if data search is working
                if(centerWebService.isWorking()) {
                    centerWebService.stopRequest();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        updateWebServiceState(START_WEB_SERVICE);
        super.onStart();
    }

    @Override
    protected void onPause() {
        updateWebServiceState(STOP_WEB_SERVICE);
        super.onPause();
    }

    public boolean isLoading() {
        return centerWebService.isWorking();
    }

    public void showMap(View view) {
        if(!centerWebService.isWorking() && !centerManager.getCenters().isEmpty()) {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.wait_loading), Toast.LENGTH_SHORT).show();
        }
    }

    public void showLogin(View view) {
        if(!centerWebService.isWorking()) {
            Toast.makeText(this, "In progress...", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.wait_loading), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(FIRST_TIME_KEY, firstTime);
        super.onSaveInstanceState(outState);
    }

}
