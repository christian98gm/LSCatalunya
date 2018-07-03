package edu.salleurl.lscatalunya.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

public class Center implements Parcelable {

    private long id;
    private String name;
    private String address;
    private boolean hasChildren;
    private boolean hasPrimary;
    private boolean hasSecondary;
    private boolean hasHighSchool;
    private boolean hasVocationalTraining;
    private boolean hasUniversity;
    private String description;

    public Center() {}

    protected Center(Parcel in) {
        id = in.readLong();
        name = in.readString();
        address = in.readString();
        hasChildren = in.readByte() != 0;
        hasPrimary = in.readByte() != 0;
        hasSecondary = in.readByte() != 0;
        hasHighSchool = in.readByte() != 0;
        hasVocationalTraining = in.readByte() != 0;
        hasUniversity = in.readByte() != 0;
        description = in.readString();
    }

    public static final Creator<Center> CREATOR = new Creator<Center>() {
        @Override
        public Center createFromParcel(Parcel in) {
            return new Center(in);
        }

        @Override
        public Center[] newArray(int size) {
            return new Center[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public boolean hasUniversity() {
        return hasUniversity;
    }

    public void setUniversity(boolean hasUniversity) {
        this.hasUniversity = hasUniversity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Center center = (Center) o;
        return id == center.id &&
                hasChildren == center.hasChildren &&
                hasPrimary == center.hasPrimary &&
                hasSecondary == center.hasSecondary &&
                hasHighSchool == center.hasHighSchool &&
                hasVocationalTraining == center.hasVocationalTraining &&
                hasUniversity == center.hasUniversity &&
                Objects.equals(name, center.name) &&
                Objects.equals(address, center.address) &&
                Objects.equals(description, center.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, hasChildren, hasPrimary, hasSecondary, hasHighSchool, hasVocationalTraining, hasUniversity, description);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeByte((byte) (hasChildren ? 1 : 0));
        parcel.writeByte((byte) (hasPrimary ? 1 : 0));
        parcel.writeByte((byte) (hasSecondary ? 1 : 0));
        parcel.writeByte((byte) (hasHighSchool ? 1 : 0));
        parcel.writeByte((byte) (hasVocationalTraining ? 1 : 0));
        parcel.writeByte((byte) (hasUniversity ? 1 : 0));
        parcel.writeString(description);
    }

}