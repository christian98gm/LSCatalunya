package edu.salleurl.lscatalunya.model;

import android.graphics.Bitmap;

public class Center {

    private long id;
    private Bitmap photo;
    private String name;
    private String address;
    private boolean hasChildren;
    private boolean hasPrimary;
    private boolean hasSecondary;
    private boolean hasHighSchool;
    private boolean hasVocationalTraining;
    private boolean hasUniversity;

    public Center() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean hasChildren() {
        return hasChildren;
    }

    public void setChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public boolean hasPrimary() {
        return hasPrimary;
    }

    public void setPrimary(boolean hasPrimary) {
        this.hasPrimary = hasPrimary;
    }

    public boolean hasSecondary() {
        return hasSecondary;
    }

    public void setSecondary(boolean hasSecondary) {
        this.hasSecondary = hasSecondary;
    }

    public boolean hasHighSchool() {
        return hasHighSchool;
    }

    public void setHighSchool(boolean hasHighSchool) {
        this.hasHighSchool = hasHighSchool;
    }

    public boolean hasVocationalTraining() {
        return hasVocationalTraining;
    }

    public void setVocationalTraining(boolean hasVocationalTraining) {
        this.hasVocationalTraining = hasVocationalTraining;
    }

    public boolean hasUniveristy() {
        return hasUniversity;
    }

    public void setUniveristy(boolean hasUniversity) {
        this.hasUniversity = hasUniversity;
    }

}