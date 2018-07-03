package edu.salleurl.lscatalunya.listeners;

import android.view.View;

import java.util.ArrayList;

import edu.salleurl.lscatalunya.adapters.CenterAdapter;
import edu.salleurl.lscatalunya.model.Center;

public class CenterListener implements View.OnClickListener {

    private ArrayList<Center> centers;
    private CenterAdapter centerAdapter;

    public CenterListener(ArrayList<Center> centers, CenterAdapter centerAdapter) {
        this.centers = centers;
        this.centerAdapter = centerAdapter;
    }

    @Override
    public void onClick(View view) {
        //Do something
    }

}