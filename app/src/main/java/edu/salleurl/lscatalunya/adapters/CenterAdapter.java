package edu.salleurl.lscatalunya.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.holders.CenterHolder;
import edu.salleurl.lscatalunya.listeners.CenterListener;
import edu.salleurl.lscatalunya.model.Center;

public class CenterAdapter extends RecyclerView.Adapter<CenterHolder> {

    private RecyclerView recyclerView;
    private ArrayList<Center> centers;

    public CenterAdapter(RecyclerView recyclerView, ArrayList<Center> centers) {
        this.recyclerView = recyclerView;
        this.centers = centers;
    }

    @NonNull
    @Override
    public CenterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Create layout and link listener
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.activity_center_list_item, parent, false);
        CenterListener itemListener = new CenterListener(recyclerView, centers, this);
        view.setOnClickListener(itemListener);
        return new CenterHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CenterHolder holder, int position) {
        Center center = centers.get(position);
        holder.setSchoolImage(center.getPhoto());
        holder.setSchoolName(center.getName());
        holder.showChildren(center.hasChildren());
        holder.showPrimary(center.hasPrimary());
        holder.showSecondary(center.hasSecondary());
        holder.showHighSchool(center.hasHighSchool());
        holder.showVocationalTraining(center.hasVocationalTraining());
        holder.showUniversity(center.hasUniveristy());
    }

    @Override
    public int getItemCount() {
        return centers.size();
    }

}