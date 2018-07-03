package edu.salleurl.lscatalunya.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import edu.salleurl.lscatalunya.R;

public class CenterHolder extends RecyclerView.ViewHolder {

    //Attributes
    private TextView schoolName;
    private TextView schoolAddress;
    private TextView children;
    private TextView primary;
    private TextView secondary;
    private TextView highSchool;
    private TextView vocationalTraining;
    private TextView university;

    public CenterHolder(View itemView) {
        super(itemView);
        schoolName = itemView.findViewById(R.id.centerSelectionSchoolName);
        schoolAddress = itemView.findViewById(R.id.centerSelectionSchoolAddress);
        children = itemView.findViewById(R.id.centerSelectionChildren);
        primary = itemView.findViewById(R.id.centerSelectionPrimary);
        secondary = itemView.findViewById(R.id.centerSelectionSecondary);
        highSchool = itemView.findViewById(R.id.centerSelectionHighSchool);
        vocationalTraining = itemView.findViewById(R.id.centerSelectionVocationalTraining);
        university = itemView.findViewById(R.id.centerSelectionUniversity);
    }

    public void setSchoolName(String name) {
        schoolName.setText(name);
    }

    public void setSchoolAddress(String address) {
        schoolAddress.setText(address);
    }

    public void showChildren(boolean show) {
        children.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void showPrimary(boolean show) {
        primary.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void showSecondary(boolean show) {
        secondary.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void showHighSchool(boolean show) {
        highSchool.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void showVocationalTraining(boolean show) {
        vocationalTraining.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void showUniversity(boolean show) {
        university.setVisibility(show ? View.VISIBLE : View.GONE);
    }

}