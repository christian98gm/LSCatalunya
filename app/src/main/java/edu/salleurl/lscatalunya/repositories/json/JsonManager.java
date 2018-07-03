package edu.salleurl.lscatalunya.repositories.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.salleurl.lscatalunya.model.Center;

public class JsonManager {

    //Params
    private final static String MESSAGE_PARAM = "msg";
    private final static String ID_PARAM = "id";
    private final static String SCHOOL_NAME_PARAM = "schoolName";
    private final static String SCHOOL_ADDRESS_PARAM = "schoolAddress";
    private final static String CHILDREN_PARAM = "isInfantil";
    private final static String PRIMARY_PARAM = "isPrimaria";
    private final static String SECONDARY_PARAM = "isEso";
    private final static String HIGH_SCHOOL_PARAM = "isBatxillerat";
    private final static String VOCATIONAL_TRAINING_PARAM = "isFP";
    private final static String UNIVERSITY_PARAM = "isUniversitat";
    private final static String DESCRIPTION_PARAM = "description";

    //Json data
    private JSONObject jsonObject;

    public JsonManager(String jsonString) throws JsonException {
        try {
            jsonObject = new JSONObject(jsonString);
        } catch(JSONException e) {
            throw new JsonException(JsonException.FORMAT_ERROR);
        }
    }

    public ArrayList<Center> getCenters() throws JsonException {

        ArrayList<Center> centers = new ArrayList<>();

        try {

            JSONArray jsonArray = jsonObject.getJSONArray(MESSAGE_PARAM);
            int totalCenters = jsonArray.length();

            for(int i = 0; i < totalCenters; i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Center center = new Center();
                center.setId(jsonObject.getLong(ID_PARAM));
                center.setName(jsonObject.getString(SCHOOL_NAME_PARAM));
                center.setAddress(jsonObject.getString(SCHOOL_ADDRESS_PARAM));
                center.setChildren(jsonObject.getInt(CHILDREN_PARAM) == 1);
                center.setPrimary(jsonObject.getInt(PRIMARY_PARAM) == 1);
                center.setSecondary(jsonObject.getInt(SECONDARY_PARAM) == 1);
                center.setHighSchool(jsonObject.getInt(HIGH_SCHOOL_PARAM) == 1);
                center.setVocationalTraining(jsonObject.getInt(VOCATIONAL_TRAINING_PARAM) == 1);
                center.setUniversity(jsonObject.getInt(UNIVERSITY_PARAM) == 1);
                center.setDescription(jsonObject.getString(DESCRIPTION_PARAM));
                centers.add(center);
            }

        } catch(JSONException e) {
            throw new JsonException(JsonException.READ_ERROR);
        }

        return centers;

    }

}