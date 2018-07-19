package edu.salleurl.lscatalunya.repositories.impl;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;

import edu.salleurl.lscatalunya.model.Center;
import edu.salleurl.lscatalunya.repositories.AsyncCenterRepo;
import edu.salleurl.lscatalunya.repositories.json.JsonException;
import edu.salleurl.lscatalunya.repositories.json.JsonManager;

public class CenterWebService implements AsyncCenterRepo {

    //Custom codes
    public final static int OK = -1;
    public final static int HTTP_ERROR = -2;

    //Address help
    private final static String CATALONIA_LOCALE = "ca_ES";

    //Url
    private final static String URL = "https://testapi-pprog2.azurewebsites.net/api/schools.php";

    //Url params
    private final static String GET_SCHOOLS_METHOD = "getSchools";
    private final static String ADD_SCHOOL_METHOD = "addSchool";
    private final static String DELETE_MESSAGE_METHOD = "deleteSchool";
    private final static String METHOD_PARAM = "method";
    private final static String DESCRIPTION_PARAM = "description";
    private final static String NAME_PARAM = "name";
    private final static String ADDRESS_PARAM = "address";
    private final static String PROVINCE_PARAM = "province";
    private final static String TYPE_PARAM = "type";

    //Attributes
    private RequestQueue requestQueue;
    private Callback callback;
    private Context context;

    private boolean isPaused;
    private boolean isWorking;
    private boolean isFirstTime;

    //Instance
    private static CenterWebService instance;

    private CenterWebService(Context context, AsyncCenterRepo.Callback callback) {
        isFirstTime = true;
        requestQueue = Volley.newRequestQueue(context);
        this.context = context;
        this.callback = callback;
    }

    public static CenterWebService getInstance(Context context, AsyncCenterRepo.Callback callback) {
        if(instance == null) {
            instance = new CenterWebService(context, callback);
        }
        instance.callback = callback;
        return instance;
    }

    @Override
    public void getCenters() {
        if(!isWorking) {
            isWorking = true;
            isPaused = false;
            String url = URL + '?' + METHOD_PARAM + '=' + GET_SCHOOLS_METHOD;
            StringRequest request = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JsonManager jsonManager = new JsonManager(fixEncoding(response));
                                int totalCenters = jsonManager.getTotalCenters();
                                for (int i = 0; i < jsonManager.getTotalCenters(); i++) {
                                    Center center = jsonManager.getCenter(i);
                                    center.setLocation(getLocationFromAddress(center.getAddress()));
                                    if (i == (totalCenters - 1)) {
                                        isFirstTime = false;
                                        isWorking = false;
                                    }
                                    callback.onResponse(center, OK, i == (totalCenters - 1));
                                }
                            } catch (JsonException e) {
                                callback.onResponse(null, e.getErrorCode(), true);
                                isWorking = false;
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    isFirstTime = false;
                    isWorking = false;
                    callback.onResponse(null, HTTP_ERROR, true);
                }
            });
            requestQueue.add(request);
        }
    }

    public void stopRequest() {
        isPaused = true;
        requestQueue.stop();
    }

    public boolean isPaused() {
        return isPaused;
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void startRequest() {
        requestQueue.start();
        isPaused = false;
    }

    public boolean isFirstTime() {
        return isFirstTime;
    }

    private String fixEncoding(String s) {
        try {
            byte[] u = s.toString().getBytes(
                    "ISO-8859-1");
            s = new String(u, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return s;
    }

    private LatLng getLocationFromAddress(String address) {
        LatLng location = null;
        try {
            Geocoder geocoder = new Geocoder(context, new Locale(CATALONIA_LOCALE));
            List<Address> addresses = geocoder.getFromLocationName(address, 1);
            if(!addresses.isEmpty()) {
                location = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
            }
        } catch(IOException e) {
            //Address not found
        }
        return location;
    }

}