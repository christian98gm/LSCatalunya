package edu.salleurl.lscatalunya.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.model.Center;
import edu.salleurl.lscatalunya.model.CenterManager;
import edu.salleurl.lscatalunya.repositories.impl.CenterWebService;
import edu.salleurl.lscatalunya.repositories.json.JsonException;
import edu.salleurl.lscatalunya.adapters.PagerAdapter;

public class CenterListActivity extends FragmentActivity implements CenterWebService.Callback, RefreshActivity,
        ListActivity {

    //Instance keys
    private final static String TIME_KEY = "loadKey";

    //Views
    private Spinner spinner;
    private ProgressBar progressBar;

    //Adapters
    private PagerAdapter pagerAdapter;

    //Center manager
    private CenterManager centerManager;

    //Web service
    private CenterWebService centerWebService;

    //Aux
    private boolean isCreated;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_list);

        //Get center manager
        centerManager = CenterManager.getInstance();

        //Get center web service
        centerWebService = CenterWebService.getInstance(this, this);

        //Link views
        spinner = findViewById(R.id.centerListProvinces);
        progressBar = findViewById(R.id.centerListProgressBar);
        TabLayout tabLayout = findViewById(R.id.centerListTab);
        ViewPager viewPager = findViewById(R.id.centerListViewPager);

        //Link view pager adapter
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), centerManager.getCentersIn(),
                centerManager.getSchoolsIn(), centerManager.getOthersIn(), this);
        viewPager.setAdapter(pagerAdapter);

        //Link view pager with tab layout
        tabLayout.setupWithViewPager(viewPager);

        //Link spinner adapter
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.provinces, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);

        //Link spinner listener
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                centerManager.setProvince(String.valueOf(spinner.getSelectedItem()));
                pagerAdapter.notifyDataSetChanged();
            }
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        if(savedInstanceState == null) {
            centerWebService.getCenters();
            Toast.makeText(this, getString(R.string.wait_loading), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(centerWebService.isWorking() && centerWebService.isPaused()) {
            centerWebService.startRequest();
        }
        if(centerWebService.isFirstTime()) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        centerWebService.stopRequest();
        pagerAdapter.endRefreshing();
    }

    @Override
    public void onResponse(Center center, int errorCode, boolean endInformation) {

        //Manage error codes
        switch(errorCode) {
            case CenterWebService.OK:           //Update centers data
                centerManager.addCenter(center);
                pagerAdapter.notifyDataSetChanged();
                if(endInformation) {
                    Toast.makeText(this, getString(R.string.load_complete), Toast.LENGTH_SHORT)
                            .show();
                }
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

        if(endInformation) {
            progressBar.setVisibility(View.GONE);
            pagerAdapter.endRefreshing();
        }

    }

    @Override
    public boolean refreshActivity() {
        if(centerWebService.isWorking()) {
            return false;
        } else {
            centerWebService.getCenters();
            return true;
        }
    }

    @Override
    public void showCenterContent(Center center) {
        if(!centerWebService.isWorking()) {
            Intent intent = new Intent(this, CenterActivity.class);
            intent.putExtra(CenterActivity.CENTER_EXTRA, center);
            startActivity(intent);
        } else {
            if(centerWebService.isFirstTime()) {
                Toast.makeText(this, getString(R.string.wait_loading), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.wait_refresh), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showLogin(View view) {
        if(!centerWebService.isWorking()) {
            Toast.makeText(this, "In progress...", Toast.LENGTH_SHORT).show();
        } else {
            if(centerWebService.isFirstTime()) {
                Toast.makeText(this, getString(R.string.wait_loading), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.wait_refresh), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showMap(View view) {
        //&& !centerManager.getCenters().isEmpty()
        if(!centerWebService.isWorking()) {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        } else {
            if(centerWebService.isFirstTime()) {
                Toast.makeText(this, getString(R.string.wait_loading), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.wait_refresh), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(TIME_KEY, centerWebService.isFirstTime());
    }

}