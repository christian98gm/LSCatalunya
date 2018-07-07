package edu.salleurl.lscatalunya.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.activities.CenterListActivity;
import edu.salleurl.lscatalunya.adapters.CenterSelectionAdapter;
import edu.salleurl.lscatalunya.listeners.RefreshListener;
import edu.salleurl.lscatalunya.model.Center;

public class RecyclerViewFragment extends Fragment {

    private final static String ADAPTER_ARG = "adapterArg";

    public static RecyclerViewFragment newInstance(CenterListActivity activity,
                                                   ArrayList<Center> centers) {
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

        //Link views and layout manager
        View view = inflater.inflate(R.layout.activity_center_list_items, container,
                false);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.centerListRefresh);
        RecyclerView recyclerView = view.findViewById(R.id.centerListItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Get args
        Bundle args = getArguments();
        CenterSelectionAdapter centerSelectionAdapter = args.getParcelable(ADAPTER_ARG);
        CenterListActivity activity = centerSelectionAdapter.getActivity();

        //Link adapter to recycler view
        recyclerView.setAdapter(centerSelectionAdapter);

        //Link listener to swipe layout
        swipeRefreshLayout.setOnRefreshListener(new RefreshListener(activity, swipeRefreshLayout));

        return view;

    }

    public void updatedCenters() {
        CenterSelectionAdapter adapter = getArguments().getParcelable(ADAPTER_ARG);
        adapter.notifyDataSetChanged();
    }

}