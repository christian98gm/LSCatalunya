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
import edu.salleurl.lscatalunya.activities.ListActivity;
import edu.salleurl.lscatalunya.activities.RefreshActivity;
import edu.salleurl.lscatalunya.model.Center;
import edu.salleurl.lscatalunya.adapters.ListAdapter;
import edu.salleurl.lscatalunya.listeners.RefreshListener;

public class RecyclerFragment extends Fragment implements RecyclerRefreshManager, RecyclerClickManager {

    //Extra key
    private final static String CENTERS_EXTRA = "centersExtra";

    public static RecyclerFragment newInstance(ArrayList<Center> centers) {
        RecyclerFragment recyclerFragment = new RecyclerFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(CENTERS_EXTRA, centers);
        recyclerFragment.setArguments(args);
        return recyclerFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //Link views
        View view = inflater.inflate(R.layout.activity_center_list_items, container, false);
        final RecyclerView recyclerView = view.findViewById(R.id.centerListItems);
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.centerListRefresh);

        //Link layout manager
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Get arguments
        Bundle args = getArguments();
        final ArrayList<Center> centers = args.getParcelableArrayList(CENTERS_EXTRA);

        //Link list adapter
        ListAdapter listAdapter = new ListAdapter(centers, recyclerView, this);
        recyclerView.setAdapter(listAdapter);

        //Link refresh adapter
        RefreshListener refreshListener = new RefreshListener(this);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);

        return view;

    }

    public void notifyDataSetChanged() {
        if(getView() != null) {
            RecyclerView recyclerView = getView().findViewById(R.id.centerListItems);
            recyclerView.getAdapter().notifyDataSetChanged();
            SwipeRefreshLayout swipeRefreshLayout = getView().findViewById(R.id.centerListRefresh);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void endRefreshing() {
        if(getView() != null) {
            SwipeRefreshLayout swipeRefreshLayout = getView().findViewById(R.id.centerListRefresh);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void recyclerClick(Center center) {
        if(getActivity() != null) {
            ListActivity listActivity = (ListActivity) getActivity();
            listActivity.showCenterContent(center);
        }
    }

    @Override
    public void refreshFragment() {
        if(getActivity() != null) {
            RefreshActivity refreshActivity = (RefreshActivity) getActivity();
            if(getView() != null) {
                SwipeRefreshLayout swipeRefreshLayout = getView().findViewById(R.id.centerListRefresh);
                swipeRefreshLayout.setRefreshing(refreshActivity.refreshActivity());
            }
        }
    }

}