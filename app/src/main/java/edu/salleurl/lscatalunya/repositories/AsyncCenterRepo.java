package edu.salleurl.lscatalunya.repositories;

import java.util.ArrayList;

import edu.salleurl.lscatalunya.model.Center;

public interface AsyncCenterRepo {

    interface Callback {
        void onResponse(ArrayList<Center> centers, int errorCode);
    }

    void getCenters();

}