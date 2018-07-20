package edu.salleurl.lscatalunya.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.Objects;

public class Center implements Parcelable, Comparable {

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
    private LatLng location;
    private String province;

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
        location = in.readParcelable(LatLng.class.getClassLoader());
        province = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeByte((byte) (hasChildren ? 1 : 0));
        dest.writeByte((byte) (hasPrimary ? 1 : 0));
        dest.writeByte((byte) (hasSecondary ? 1 : 0));
        dest.writeByte((byte) (hasHighSchool ? 1 : 0));
        dest.writeByte((byte) (hasVocationalTraining ? 1 : 0));
        dest.writeByte((byte) (hasUniversity ? 1 : 0));
        dest.writeString(description);
        dest.writeParcelable(location, flags);
        dest.writeString(province);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Center center = (Center) o;
        return /*id == center.id &&*/
                hasChildren == center.hasChildren &&
                hasPrimary == center.hasPrimary &&
                hasSecondary == center.hasSecondary &&
                hasHighSchool == center.hasHighSchool &&
                hasVocationalTraining == center.hasVocationalTraining &&
                hasUniversity == center.hasUniversity &&
                name.equals(center.name);
                /*Objects.equals(address, center.address) &&*/
               /* Objects.equals(description, center.description);*/
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, address, hasChildren, hasPrimary, hasSecondary, hasHighSchool,
                hasVocationalTraining, hasUniversity, description, location);
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Center c = (Center)o;
        return this.getName().toLowerCase().compareTo(c.getName().toLowerCase());
    }
}