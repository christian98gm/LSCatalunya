package edu.salleurl.lscatalunya.adapters;

import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.activities.CenterActivity;
import edu.salleurl.lscatalunya.activities.CenterSelectionActivity;
import edu.salleurl.lscatalunya.holders.CenterHolder;
import edu.salleurl.lscatalunya.listeners.CenterSelectionListener;
import edu.salleurl.lscatalunya.model.Center;

public class CenterSelectionAdapter extends RecyclerView.Adapter<CenterHolder> implements Parcelable {

    private CenterSelectionActivity activity;
    private ArrayList<Center> centers;
    private CenterSelectionListener listener;

    public CenterSelectionAdapter(CenterSelectionActivity activity, ArrayList<Center> centers) {
        this.activity = activity;
        this.centers = centers;
        listener = new CenterSelectionListener(this);
    }

    protected CenterSelectionAdapter(Parcel in) {
        centers = in.createTypedArrayList(Center.CREATOR);
    }

    public static final Creator<CenterSelectionAdapter> CREATOR = new Creator<CenterSelectionAdapter>() {
        @Override
        public CenterSelectionAdapter createFromParcel(Parcel in) {
            return new CenterSelectionAdapter(in);
        }
        @Override
        public CenterSelectionAdapter[] newArray(int size) {
            return new CenterSelectionAdapter[size];
        }
    };

    @NonNull
    @Override
    public CenterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Create layout and link listener
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.activity_center_selection_list_item, parent, false);
        view.setOnClickListener(listener);
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(centers);
    }

    public void onClick(View view) {
        if(!activity.isLoading()) {
            Intent intent = new Intent(activity, CenterActivity.class);
            RecyclerView rView = (RecyclerView) view.getParent();
            intent.putExtra(CenterActivity.CENTER_EXTRA, centers.get(rView.getChildLayoutPosition(view)));
            activity.startActivity(intent);
        } else {
            Toast.makeText(activity, activity.getString(R.string.wait_refresh), Toast.LENGTH_SHORT).
                    show();
        }
    }

}