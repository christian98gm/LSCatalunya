package edu.salleurl.lscatalunya.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.fragments.RecyclerClickManager;
import edu.salleurl.lscatalunya.holders.CenterHolder;
import edu.salleurl.lscatalunya.model.Center;

public class ListAdapter extends RecyclerView.Adapter<CenterHolder> implements View.OnClickListener {

    private ArrayList<Center> centers;
    private RecyclerView recyclerView;
    private RecyclerClickManager recyclerClickManager;

    public ListAdapter(ArrayList<Center> centers, RecyclerView recyclerView,
                       RecyclerClickManager recyclerClickManager) {
        this.centers = centers;
        this.recyclerView = recyclerView;
        this.recyclerClickManager = recyclerClickManager;
    }

    @NonNull
    @Override
    public CenterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_center_list_center,
                parent, false);
        view.setOnClickListener(this);
        return new CenterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CenterHolder holder, int position) {
        Center center = centers.get(position);
        holder.setSchoolName(center.getName());
        holder.setSchoolAddress(center.getAddress());
        holder.showChildren(center.hasChildren());
        holder.showPrimary(center.hasPrimary());
        holder.showSecondary(center.hasSecondary());
        holder.showHighSchool(center.hasHighSchool());
        holder.showVocationalTraining(center.hasVocationalTraining());
        holder.showUniversity(center.hasUniversity());
    }

    @Override
    public int getItemCount() {
        return centers.size();
    }

    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildLayoutPosition(view);
        recyclerClickManager.recyclerClick(centers.get(position));
    }

}