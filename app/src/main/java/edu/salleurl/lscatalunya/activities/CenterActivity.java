package edu.salleurl.lscatalunya.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.adapters.TabAdapter;
import edu.salleurl.lscatalunya.fragment.RecyclerViewFragment;

public class CenterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);

        //Link adapter to provinces spinner
        ArrayAdapter<CharSequence> provincesAdapter = ArrayAdapter.createFromResource(this,
                R.array.provinces, android.R.layout.simple_spinner_item);
        provincesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner provincesSpinner = findViewById(R.id.centerProvinces);
        provincesSpinner.setAdapter(provincesAdapter);

        //Link listener
        provincesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //Province selected
            }
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        //Create tabs and adapter
        ArrayList<TabAdapter.TabEntry> tabEntries = new ArrayList<>();
        tabEntries.add(new TabAdapter.TabEntry(new RecyclerViewFragment(),
                getString(R.string.all)));
        tabEntries.add(new TabAdapter.TabEntry(new RecyclerViewFragment(),
                getString(R.string.schools)));
        tabEntries.add(new TabAdapter.TabEntry(new RecyclerViewFragment(),
                getString(R.string.others)));

        TabAdapter tabAdapter = new TabAdapter(getSupportFragmentManager(), tabEntries);

        //Link adapter to views
        ViewPager viewPager = findViewById(R.id.centerViewPager);
        TabLayout tabLayout = findViewById(R.id.centerTab);

        viewPager.setAdapter(tabAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

}
