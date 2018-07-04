package edu.salleurl.lscatalunya.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.model.Center;

public class CenterActivity extends AppCompatActivity {

    public final static String CENTER_EXTRA = "centerExtra";
    private final static String CENTER_KEY = "centerKey";

    public final static String CENTERS_EXTRA = "centersExtra";
    private final static String CENTERS_KEY = "centersKey";

    private ArrayList<Center> centers;
    private Center center;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_center);

        if(savedInstanceState == null) {
            Intent intent = getIntent();
            centers = intent.getParcelableArrayListExtra(CENTERS_EXTRA);
            center = intent.getParcelableExtra(CENTER_EXTRA);
        } else {
            centers = savedInstanceState.getParcelableArrayList(CENTERS_KEY);
            center = savedInstanceState.getParcelable(CENTER_KEY);
        }

        //Set school name and address
        TextView schoolName = findViewById(R.id.centerSchoolName);
        schoolName.setText(center.getName());

        TextView schoolAddress = findViewById(R.id.centerSchoolAddress);
        schoolAddress.setText(center.getAddress());

        //Set tags visibility
        TextView childrenTag = findViewById(R.id.centerChildren);
        childrenTag.setVisibility(center.hasChildren() ? View.VISIBLE : View.GONE);

        TextView primaryTag = findViewById(R.id.centerPrimary);
        primaryTag.setVisibility(center.hasPrimary() ? View.VISIBLE : View.GONE);

        TextView secondaryTag = findViewById(R.id.centerSecondary);
        secondaryTag.setVisibility(center.hasSecondary() ? View.VISIBLE : View.GONE);

        TextView highSchoolTag = findViewById(R.id.centerHighSchool);
        highSchoolTag.setVisibility(center.hasHighSchool() ? View.VISIBLE : View.GONE);

        TextView vocationalTrainingTag = findViewById(R.id.centerVocationalTraining);
        vocationalTrainingTag.setVisibility(center.hasVocationalTraining() ? View.VISIBLE : View.GONE);

        TextView universityTag = findViewById(R.id.centerUniversity);
        universityTag.setVisibility(center.hasUniversity() ? View.VISIBLE : View.GONE);

        //Set school description
        TextView schoolDescription = findViewById(R.id.centerDescription);
        schoolDescription.setText(center.getDescription());

    }

    public void showMap(View view) {
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra(MapActivity.CENTERS_EXTRA, centers);
        intent.putExtra(MapActivity.ADDRESS_EXTRA, center.getAddress());
        startActivity(intent);
        finish();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(CENTERS_KEY, centers);
        outState.putParcelable(CENTER_KEY, center);
        super.onSaveInstanceState(outState);
    }

}