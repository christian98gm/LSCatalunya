package edu.salleurl.lscatalunya.repositories.impl;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import edu.salleurl.lscatalunya.model.Center;
import edu.salleurl.lscatalunya.repositories.AsyncCenterRepo;
import edu.salleurl.lscatalunya.repositories.json.JsonException;
import edu.salleurl.lscatalunya.repositories.json.JsonManager;

public class CenterWebService implements AsyncCenterRepo {

    //Custom codes
    public final static int OK = -1;
    public final static int HTTP_ERROR = -2;

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
    private final RequestQueue requestQueue;
    private final Callback callback;

    public CenterWebService(Context context, AsyncCenterRepo.Callback callback) {
        requestQueue = Volley.newRequestQueue(context);
        this.callback = callback;
    }

    @Override
    public void getCenters() {
        String url = URL + '?' + METHOD_PARAM + '=' + GET_SCHOOLS_METHOD;
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JsonManager jsonManager = new JsonManager(fixEncoding(response));
                            int totalCenters = jsonManager.getTotalCenters();
                            for(int i = 0; i < jsonManager.getTotalCenters(); i++) {
                                callback.onResponse(jsonManager.getCenter(i), OK,
                                        i == (totalCenters - 1));
                            }
                        } catch(JsonException e) {
                            callback.onResponse(null, e.getErrorCode(), true);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onResponse(null, HTTP_ERROR, true);
                    }
                });
        requestQueue.add(request);
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

}