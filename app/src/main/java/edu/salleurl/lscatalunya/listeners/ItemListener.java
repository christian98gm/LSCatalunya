package edu.salleurl.lscatalunya.listeners;

import android.view.View;

import edu.salleurl.lscatalunya.adapters.CenterSelectionAdapter;

public class ItemListener implements View.OnClickListener {

    private CenterSelectionAdapter adapter;

    public ItemListener(CenterSelectionAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onClick(View view) {
        adapter.onClick(view);
    }

}