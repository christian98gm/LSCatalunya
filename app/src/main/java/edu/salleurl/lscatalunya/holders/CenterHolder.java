package edu.salleurl.lscatalunya.holders;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.salleurl.lscatalunya.R;

public class CenterHolder extends RecyclerView.ViewHolder {

    //Attributes
    private ImageView schoolImage;
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
        schoolName = itemView.findViewById(R.id.centerSchoolName);
        schoolAddress = itemView.findViewById(R.id.centerSchoolAddress);
        children = itemView.findViewById(R.id.centerChildren);
        primary = itemView.findViewById(R.id.centerPrimary);
        secondary = itemView.findViewById(R.id.centerSecondary);
        highSchool = itemView.findViewById(R.id.centerHighSchool);
        vocationalTraining = itemView.findViewById(R.id.centerVocationalTraining);
        university = itemView.findViewById(R.id.centerUniversity);
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