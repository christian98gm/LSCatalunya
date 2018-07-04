package edu.salleurl.lscatalunya.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.adapters.TabAdapter;
import edu.salleurl.lscatalunya.fragment.RecyclerViewFragment;
import edu.salleurl.lscatalunya.model.Center;
import edu.salleurl.lscatalunya.repositories.AsyncCenterRepo;
import edu.salleurl.lscatalunya.repositories.impl.CenterWebService;
import edu.salleurl.lscatalunya.repositories.json.JsonException;

public class CenterSelectionActivity extends AppCompatActivity implements AsyncCenterRepo.Callback {

    private final static int TOTAL_TABS = 3;

    //Save instance keys
    private final static String CENTERS_KEY = "centersKey";
    private final static String ALL_KEY = "allKey";
    private final static String SCHOOLS_KEY = "schoolsKey";
    private final static String OTHERS_KEY = "othersKey";

    //Centers list
    private ArrayList<Center> centers;
    private ArrayList<Center> all;
    private ArrayList<Center> schools;
    private ArrayList<Center> others;

    //Attributes
    private Spinner provincesSpinner;
    private RecyclerViewFragment[] fragments;
    private TabAdapter tabAdapter;
    private android.widget.ProgressBar progressBar;
    private CenterWebService centerWebService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center_selection);

        //Link adapter to provinces spinner
        ArrayAdapter<CharSequence> provincesAdapter = ArrayAdapter.createFromResource(this,
                R.array.provinces, android.R.layout.simple_spinner_item);
        provincesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        provincesSpinner = findViewById(R.id.centerSelectionProvinces);
        provincesSpinner.setAdapter(provincesAdapter);

        //Link listener
        provincesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                updateCenters(String.valueOf(provincesSpinner.getSelectedItem()));
                updateTabs();
            }
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        //Create tabs and adapter
        if(savedInstanceState != null) {
            centers = savedInstanceState.getParcelableArrayList(CENTERS_KEY);
            all = savedInstanceState.getParcelableArrayList(ALL_KEY);
            schools = savedInstanceState.getParcelableArrayList(SCHOOLS_KEY);
            others = savedInstanceState.getParcelableArrayList(OTHERS_KEY);
        } else {
            centers = new ArrayList<>();
            all = new ArrayList<>();
            schools = new ArrayList<>();
            others = new ArrayList<>();
        }

        //Link fragments
        fragments = new RecyclerViewFragment[TOTAL_TABS];
        fragments[0] = RecyclerViewFragment.newInstance(all, this);
        fragments[1] = RecyclerViewFragment.newInstance(schools, this);
        fragments[2] = RecyclerViewFragment.newInstance(others,this);
        String[] tabs = getResources().getStringArray(R.array.centers);
        tabAdapter = new TabAdapter(getSupportFragmentManager(), fragments, tabs);

        //Link adapter to views
        ViewPager viewPager = findViewById(R.id.centerSelectionViewPager);
        TabLayout tabLayout = findViewById(R.id.centerSelectionTab);

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        //Get progressbar
        progressBar = findViewById(R.id.centerSelectionProgressBar);

        //Get center data
        if(savedInstanceState == null) {
            progressBar.setVisibility(View.VISIBLE);
            centerWebService = new CenterWebService(this, this);
            getCentersData();
        }

    }

    public void getCentersData() {
        if(progressBar.getVisibility() == View.GONE) {
            centers.clear();
            centerWebService.getCenters();
        }
    }

    private void updateCenters(String province) {

        all.clear();
        schools.clear();
        others.clear();

        for(Center center : centers) {
            if(center.getAddress().contains(province)) {
                all.add(center);
                if(center.hasChildren() || center.hasPrimary() || center.hasSecondary()) {
                    schools.add(center);
                }
                if(center.hasHighSchool() || center.hasVocationalTraining() ||
                        center.hasUniversity()) {
                    others.add(center);
                }
            }
        }

    }

    private void updateTabs() {
        for(int i = 0; i < TOTAL_TABS; i++) {
            fragments[i].updatedCenters();
        }
        tabAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResponse(Center center, int errorCode, boolean endInformation) {
        switch(errorCode) {
            case CenterWebService.OK:
                //Update data and views
                centers.add(center);
                updateCenters(String.valueOf(provincesSpinner.getSelectedItem()));
                updateTabs();
                break;
            case CenterWebService.HTTP_ERROR:
                Toast.makeText(this, getString(R.string.connection_error), Toast.LENGTH_LONG)
                        .show();
                break;
            case JsonException.FORMAT_ERROR:
                Toast.makeText(this, getString(R.string.format_error), Toast.LENGTH_LONG)
                        .show();
                break;
            case JsonException.READ_ERROR:
                Toast.makeText(this, getString(R.string.read_error), Toast.LENGTH_LONG)
                        .show();
                break;
        }
        if(endInformation) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(CENTERS_KEY, centers);
        outState.putParcelableArrayList(ALL_KEY, all);
        outState.putParcelableArrayList(SCHOOLS_KEY, schools);
        outState.putParcelableArrayList(OTHERS_KEY, others);
        super.onSaveInstanceState(outState);
    }

}
