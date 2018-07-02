package edu.salleurl.lscatalunya.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.adapters.CenterAdapter;
import edu.salleurl.lscatalunya.model.Center;

public class RecyclerViewFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //Get view
        View view = inflater.inflate(R.layout.activity_center_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.centerList);

        //Get centers
        ArrayList<Center> centers = new ArrayList<>();

        //Create center list adapter and link it
        CenterAdapter centerAdapter = new CenterAdapter(recyclerView, centers);
        recyclerView.setAdapter(centerAdapter);

        return view;

    }

}