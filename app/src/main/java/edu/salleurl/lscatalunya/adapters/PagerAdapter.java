package edu.salleurl.lscatalunya.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.fragments.RecyclerFragmentCenterList;
import edu.salleurl.lscatalunya.model.Center;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private final static int TOTAL_PAGES = 3;

    private ArrayList<RecyclerFragmentCenterList> recyclerFragmentCenterLists;

    private ArrayList<Center> centers;
    private ArrayList<Center> schools;
    private ArrayList<Center> others;

    private Context context;

    public PagerAdapter(FragmentManager fm, ArrayList<Center> centers, ArrayList<Center> schools,
                        ArrayList<Center> others, Context context) {
        super(fm);
        this.centers = centers;
        this.schools = schools;
        this.others = others;
        this.context = context;
        createFragments();
    }

    private void createFragments() {
        recyclerFragmentCenterLists = new ArrayList<>(TOTAL_PAGES);
        recyclerFragmentCenterLists.add(RecyclerFragmentCenterList.newInstance(centers));
        recyclerFragmentCenterLists.add(RecyclerFragmentCenterList.newInstance(schools));
        recyclerFragmentCenterLists.add(RecyclerFragmentCenterList.newInstance(others));
    }

    @Override
    public Fragment getItem(int position) {
        return recyclerFragmentCenterLists.get(position);
    }

    @Override
    public int getCount() {
        return TOTAL_PAGES;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        switch(position) {
            case 0:
                title = context.getString(R.string.all);
                break;
            case 1:
                title = context.getString(R.string.schools);
                break;
            case 2:
                title = context.getString(R.string.others);
                break;
        }
        return title;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        for(int i = 0; i < TOTAL_PAGES; i++) {
            recyclerFragmentCenterLists.get(i).notifyDataSetChanged();
        }
    }

    public void endRefreshing() {
        for(int i = 0; i < TOTAL_PAGES; i++) {
            recyclerFragmentCenterLists.get(i).endRefreshing();
        }
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;   //Will recreate all pages if called
    }

}