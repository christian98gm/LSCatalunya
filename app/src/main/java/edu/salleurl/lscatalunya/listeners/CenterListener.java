package edu.salleurl.lscatalunya.listeners;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import edu.salleurl.lscatalunya.adapters.CenterAdapter;
import edu.salleurl.lscatalunya.model.Center;

public class CenterListener implements View.OnClickListener {

    private RecyclerView recyclerView;
    private ArrayList<Center> centers;
    private CenterAdapter centerAdapter;

    public CenterListener(RecyclerView recyclerView, ArrayList<Center> centers,
                          CenterAdapter centerAdapter) {
        this.recyclerView = recyclerView;
        this.centers = centers;
        this.centerAdapter = centerAdapter;
    }

    @Override
    public void onClick(View view) {
        //Do something
    }

}