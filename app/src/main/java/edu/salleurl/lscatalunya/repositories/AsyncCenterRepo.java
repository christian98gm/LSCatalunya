package edu.salleurl.lscatalunya.repositories;

import edu.salleurl.lscatalunya.model.Center;

public interface AsyncCenterRepo {

    interface Callback {
        void onGetCentersResponse(Center center, int errorCode, boolean endInformation);
        void onAddCenterResponse(String msg, int type);
        void onDeleteCenterResponse(String msg, int type);
    }

    void getCenters();

    void addCenter(final Center center);
}