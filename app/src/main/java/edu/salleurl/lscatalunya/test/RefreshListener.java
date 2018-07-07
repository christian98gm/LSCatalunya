package edu.salleurl.lscatalunya.test;

import android.support.v4.widget.SwipeRefreshLayout;

public class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {

    private RefreshFragment refreshFragment;

    public RefreshListener(RefreshFragment refreshFragment) {
        this.refreshFragment = refreshFragment;
    }

    @Override
    public void onRefresh() {
        refreshFragment.refreshFragment();
    }

}