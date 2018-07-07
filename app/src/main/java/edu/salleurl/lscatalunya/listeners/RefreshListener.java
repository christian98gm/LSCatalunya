package edu.salleurl.lscatalunya.listeners;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

import edu.salleurl.lscatalunya.R;
import edu.salleurl.lscatalunya.activities.CenterListActivity;

public class RefreshListener implements SwipeRefreshLayout.OnRefreshListener, Parcelable,
        RefreshCallback {

    private CenterListActivity activity;
    private SwipeRefreshLayout view;
    private boolean isRefreshing;

    public RefreshListener(CenterListActivity activity, SwipeRefreshLayout view) {
        this.activity = activity;
        this.view = view;
    }

    protected RefreshListener(Parcel in) {
        isRefreshing = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isRefreshing ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<RefreshListener> CREATOR = new Creator<RefreshListener>() {
        @Override
        public RefreshListener createFromParcel(Parcel in) {
            return new RefreshListener(in);
        }

        @Override
        public RefreshListener[] newArray(int size) {
            return new RefreshListener[size];
        }
    };

    @Override
    public void onRefresh() {
        if(activity.isLoading()) {
            isRefreshing = false;
            view.setRefreshing(false);
            Toast.makeText(activity, activity.getString(R.string.wait_refresh), Toast.LENGTH_SHORT)
                    .show();
        } else {
            isRefreshing = true;
            activity.getCentersData();
        }
    }

    @Override
    public void refreshedList() {
        isRefreshing = false;
        if(view != null) {
            view.setRefreshing(false);
        }
    }

}
