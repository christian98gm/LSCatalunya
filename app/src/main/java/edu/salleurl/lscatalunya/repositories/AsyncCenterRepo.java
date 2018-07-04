package edu.salleurl.lscatalunya.repositories;

import edu.salleurl.lscatalunya.model.Center;

public interface AsyncCenterRepo {

    interface Callback {
        void onResponse(Center center, int errorCode, boolean endInformation);
    }

    void getCenters();

}