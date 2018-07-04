package edu.salleurl.lscatalunya.listeners;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;

import edu.salleurl.lscatalunya.activities.CenterSelectionActivity;
import edu.salleurl.lscatalunya.model.Center;
import edu.salleurl.lscatalunya.repositories.AsyncCenterRepo;

public class CenterSelectionRefresh implements SwipeRefreshLayout.OnRefreshListener, Parcelable,
        AsyncCenterRepo.Callback {

    private CenterSelectionActivity activity;
    private SwipeRefreshLayout view;

    public CenterSelectionRefresh(CenterSelectionActivity activity) {
        this.activity = activity;
    }

    protected CenterSelectionRefresh(Parcel in) {}

    @Override
    public void writeToParcel(Parcel dest, int flags) {}

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CenterSelectionRefresh> CREATOR = new Creator<CenterSelectionRefresh>() {
        @Override
        public CenterSelectionRefresh createFromParcel(Parcel in) {
            return new CenterSelectionRefresh(in);
        }

        @Override
        public CenterSelectionRefresh[] newArray(int size) {
            return new CenterSelectionRefresh[size];
        }
    };

    @Override
    public void onRefresh() {
        activity.getCentersData();
    }

    @Override
    public void onResponse(Center center, int errorCode, boolean endInformation) {
        if(endInformation) {
            view.setRefreshing(false);
        }
    }

    public void setView(SwipeRefreshLayout view) {
        this.view = view;
    }

}
