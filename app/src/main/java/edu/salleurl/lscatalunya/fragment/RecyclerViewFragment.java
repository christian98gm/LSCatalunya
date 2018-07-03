package edu.salleurl.lscatalunya.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.adapters.CenterSelectionAdapter;
import edu.salleurl.lscatalunya.model.Center;

public class RecyclerViewFragment extends Fragment {

    private final static String ADAPTER_ARG = "adapterArg";

    public static RecyclerViewFragment newInstance(ArrayList<Center> centers, Activity activity) {
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        Bundle args = new Bundle();
        args.putParcelable(ADAPTER_ARG, new CenterSelectionAdapter(activity, centers));
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_center_selection_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.centerSelectionList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Create center list adapter and link it
        Bundle args = getArguments();
        CenterSelectionAdapter centerSelectionAdapter = args.getParcelable(ADAPTER_ARG);
        recyclerView.setAdapter(centerSelectionAdapter);

        return view;

    }

    public void updatedCenters() {
        CenterSelectionAdapter adapter = getArguments().getParcelable(ADAPTER_ARG);
        adapter.notifyDataSetChanged();
    }

}