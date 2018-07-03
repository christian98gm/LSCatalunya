package edu.salleurl.lscatalunya.adapters;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.holders.CenterHolder;
import edu.salleurl.lscatalunya.listeners.CenterListener;
import edu.salleurl.lscatalunya.model.Center;

public class CenterAdapter extends RecyclerView.Adapter<CenterHolder> implements Parcelable {

    private ArrayList<Center> centers;

    public CenterAdapter(ArrayList<Center> centers) {
        this.centers = centers;
    }

    protected CenterAdapter(Parcel in) {
        centers = in.createTypedArrayList(Center.CREATOR);
    }

    public static final Creator<CenterAdapter> CREATOR = new Creator<CenterAdapter>() {
        @Override
        public CenterAdapter createFromParcel(Parcel in) {
            return new CenterAdapter(in);
        }

        @Override
        public CenterAdapter[] newArray(int size) {
            return new CenterAdapter[size];
        }
    };

    @NonNull
    @Override
    public CenterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Create layout and link listener
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.activity_center_list_item, parent, false);
        CenterListener itemListener = new CenterListener(centers, this);
        view.setOnClickListener(itemListener);
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

    public void setCenters(ArrayList<Center> centers) {
        this.centers = centers;
        notifyDataSetChanged();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(centers);
    }

}