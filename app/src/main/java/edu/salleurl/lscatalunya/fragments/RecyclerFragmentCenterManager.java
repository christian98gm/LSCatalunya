package edu.salleurl.lscatalunya.fragments;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.activities.ListActivity;
import edu.salleurl.lscatalunya.activities.RefreshActivity;
import edu.salleurl.lscatalunya.adapters.ListAdapter;
import edu.salleurl.lscatalunya.listeners.RefreshListener;
import edu.salleurl.lscatalunya.model.Center;
import edu.salleurl.lscatalunya.adapters.RecyclerItemTouchHelper;

public class RecyclerFragmentCenterManager extends Fragment implements RecyclerRefreshManager, RecyclerClickManager {

    //Extra key
    private final static String CENTERS_EXTRA = "centersExtra";

    public static RecyclerFragmentCenterManager newInstance(ArrayList<Center> centers) {
        RecyclerFragmentCenterManager recyclerFragment = new RecyclerFragmentCenterManager();
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

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        //Link list adapter
        ListAdapter listAdapter = new ListAdapter(centers, recyclerView, this);
        recyclerView.setAdapter(listAdapter);

        //Link refresh adapter
        RefreshListener refreshListener = new RefreshListener(this);
        swipeRefreshLayout.setOnRefreshListener(refreshListener);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT,
                        (RecyclerItemTouchHelper.RecyclerItemTouchHelperListener) getActivity());
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback1 =
                new ItemTouchHelper.SimpleCallback(0,  ItemTouchHelper.LEFT ) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // Row is swiped from recycler view
                // remove it from adapter
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                    float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        // attaching the touch helper to recycler view
        new ItemTouchHelper(itemTouchHelperCallback1).attachToRecyclerView(recyclerView);

        return view;

    }

    public void notifyDataSetChanged() {
        if (getView() != null) {
            RecyclerView recyclerView = getView().findViewById(R.id.centerListItems);
            recyclerView.getAdapter().notifyDataSetChanged();
            SwipeRefreshLayout swipeRefreshLayout = getView().findViewById(R.id.centerListRefresh);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public void endRefreshing() {
        if (getView() != null) {
            SwipeRefreshLayout swipeRefreshLayout = getView().findViewById(R.id.centerListRefresh);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void recyclerClick(Center center) {
        if (getActivity() != null) {
            ListActivity listActivity = (ListActivity) getActivity();
            listActivity.showCenterContent(center);
        }
    }

    @Override
    public void refreshFragment() {
        if (getActivity() != null) {
            RefreshActivity refreshActivity = (RefreshActivity) getActivity();
            if (getView() != null) {
                SwipeRefreshLayout swipeRefreshLayout = getView().findViewById(R.id.centerListRefresh);
                swipeRefreshLayout.setRefreshing(refreshActivity.refreshActivity());
            }
        }
    }
}