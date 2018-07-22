package edu.salleurl.lscatalunya.listeners;

import android.support.v4.widget.SwipeRefreshLayout;

import edu.salleurl.lscatalunya.fragments.RecyclerRefreshManager;

public class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerRefreshManager recyclerRefreshManager;

    public RefreshListener(RecyclerRefreshManager recyclerRefreshManager) {
        this.recyclerRefreshManager = recyclerRefreshManager;
    }

    @Override
    public void onRefresh() {
        recyclerRefreshManager.refreshFragment();
    }

}