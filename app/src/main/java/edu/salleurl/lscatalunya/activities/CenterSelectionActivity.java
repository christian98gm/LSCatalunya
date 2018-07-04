package edu.salleurl.lscatalunya.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
import edu.salleurl.lscatalunya.model.Center;
import edu.salleurl.lscatalunya.model.CenterManager;
import edu.salleurl.lscatalunya.repositories.AsyncCenterRepo;
import edu.salleurl.lscatalunya.repositories.impl.CenterWebService;
import edu.salleurl.lscatalunya.repositories.json.JsonException;

public class CenterSelectionActivity extends AppCompatActivity implements AsyncCenterRepo.Callback {

    private final static int TOTAL_TABS = 3;

    //Save instance keys
    private final static String LOADING_KEY = "loadingKey";

    //Managers
    private CenterManager centerManager;
    private CenterWebService centerWebService;

    //Views
    private Spinner provincesSpinner;

    //Others
    private RecyclerViewFragment[] fragments;
    private TabAdapter tabAdapter;
    private boolean isLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_selection);

        //Get center manager
        centerManager = CenterManager.getInstance();

        //Link adapter to provinces spinner
        ArrayAdapter<CharSequence> provincesAdapter = ArrayAdapter.createFromResource(this,
                R.array.provinces, android.R.layout.simple_spinner_item);
        provincesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        provincesSpinner = findViewById(R.id.centerSelectionProvinces);
        provincesSpinner.setAdapter(provincesAdapter);

        //Link listener
        provincesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                centerManager.setProvince(String.valueOf(provincesSpinner.getSelectedItem()));
                updateTabs();
            }
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        //Create tabs and adapter
        isLoading = savedInstanceState != null && savedInstanceState.getBoolean(LOADING_KEY);

        //Link fragments
        fragments = new RecyclerViewFragment[TOTAL_TABS];
        fragments[0] = RecyclerViewFragment.newInstance(this, centerManager.getCentersIn());
        fragments[1] = RecyclerViewFragment.newInstance(this, centerManager.getSchoolsIn());
        fragments[2] = RecyclerViewFragment.newInstance(this, centerManager.getOthersIn());
        String[] tabs = getResources().getStringArray(R.array.center_types);
        tabAdapter = new TabAdapter(getSupportFragmentManager(), fragments, tabs);

        //Link adapter to views
        ViewPager viewPager = findViewById(R.id.centerSelectionViewPager);
        TabLayout tabLayout = findViewById(R.id.centerSelectionTab);

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        //Get center data
        if(savedInstanceState == null) {
            Toast.makeText(this, getString(R.string.loading_centers), Toast.LENGTH_SHORT).
                    show();
            centerWebService = new CenterWebService(this, this);
            getCentersData();
        }

    }

    public boolean isLoading() {
        return isLoading;
    }

    public void getCentersData() {
        if(!isLoading) {
            isLoading = true;
            centerManager.clear();  //TODO: Check if clear is ok
            centerWebService.getCenters();
        }
    }

    private void updateTabs() {
        fragments[0].updatedCenters();
        fragments[1].updatedCenters();
        fragments[2].updatedCenters();
        tabAdapter.notifyDataSetChanged();
    }

    public void showMap(View view) {
        if(!isLoading) {
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this, getString(R.string.wait_loading), Toast.LENGTH_SHORT).show();
        }
    }

    public void showLogin(View view) {
        Toast.makeText(this, "In progress...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(Center center, int errorCode, boolean endInformation) {
        switch(errorCode) {
            case CenterWebService.OK:
                //Update data and views
                centerManager.addCenter(center);
                updateTabs();
                break;
            case CenterWebService.HTTP_ERROR:
                Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_SHORT)
                        .show();
                break;
            case JsonException.FORMAT_ERROR:
                Toast.makeText(this, getString(R.string.format_error), Toast.LENGTH_SHORT)
                        .show();
                break;
            case JsonException.READ_ERROR:
                Toast.makeText(this, getString(R.string.read_error), Toast.LENGTH_SHORT)
                        .show();
                break;
        }
        if(endInformation) {
            isLoading = false;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(LOADING_KEY, isLoading);
        super.onSaveInstanceState(outState);
    }

}
