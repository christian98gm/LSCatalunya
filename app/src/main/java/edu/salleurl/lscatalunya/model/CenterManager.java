package edu.salleurl.lscatalunya.model;

import java.util.ArrayList;
import java.util.Collections;

public class CenterManager {

    private static CenterManager instance;

    //Attributes
    private ArrayList<Center> centers;
    private ArrayList<Center> centersIn;
    private ArrayList<Center> schools;
    private ArrayList<Center> schoolsIn;
    private ArrayList<Center> others;
    private ArrayList<Center> othersIn;
    private String province;


    private CenterManager() {
        centers = new ArrayList<>();
        centersIn = new ArrayList<>();
        schools = new ArrayList<>();
        schoolsIn = new ArrayList<>();
        others = new ArrayList<>();
        othersIn = new ArrayList<>();
    }

    public static CenterManager getInstance() {
        if(instance == null) {
            instance = new CenterManager();
        }
        return instance;
    }

    public void addCenter(Center center) {

        //Add center to global data
        if(!centers.contains(center)) {
            centers.add(center);
            if(center.hasChildren() || center.hasPrimary() || center.hasSecondary()) {
                schools.add(center);
            }
            if(center.hasHighSchool() || center.hasVocationalTraining() || center.hasUniversity()) {
                others.add(center);
            }

            //Add center to province data
            if(province != null && center.getAddress().contains(province)) {
                centersIn.add(center);
                if (center.hasChildren() || center.hasPrimary() || center.hasSecondary()) {
                    schoolsIn.add(center);
                }
                if (center.hasHighSchool() || center.hasVocationalTraining() || center.hasUniversity()) {
                    othersIn.add(center);
                }
            } else if(province == null) {
                centersIn.add(center);
                if (center.hasChildren() || center.hasPrimary() || center.hasSecondary()) {
                    schoolsIn.add(center);
                }
                if (center.hasHighSchool() || center.hasVocationalTraining() || center.hasUniversity()) {
                    othersIn.add(center);
                }
            }
        }

    }

    public void setProvince(String province) {

        this.province = province;

        //Centers
        centersIn.clear();
        for (int i = 0; i < centers.size(); i++) {
            if (centers.get(i).getAddress().contains(province) || centers.get(i).getProvince().toLowerCase().equals(province.toLowerCase())) {
                centersIn.add(centers.get(i));
            }
        }

        //Schools
        schoolsIn.clear();
        for (int i = 0; i < schools.size(); i++) {
            if (schools.get(i).getAddress().contains(province) || centers.get(i).getProvince().toLowerCase().equals(province.toLowerCase())) {
                schoolsIn.add(schools.get(i));
            }
        }

        //Others
        othersIn.clear();
        for (int i = 0; i < others.size(); i++) {
            if (others.get(i).getAddress().contains(province) || centers.get(i).getProvince().toLowerCase().equals(province.toLowerCase())) {
                othersIn.add(others.get(i));
            }
        }
    }
    public void orderCenters(int type){
        if(type == 1){
            Collections.sort(centers);
        }else{
            Collections.reverse(centers);
        }
    }

    public ArrayList<Center> getCenters() {
        return centers;
    }

    public ArrayList<Center> getCentersIn() {
        return centersIn;
    }

    public ArrayList<Center> getSchools() {
        return schools;
    }

    public ArrayList<Center> getSchoolsIn() {
        return schoolsIn;
    }

    public ArrayList<Center> getOthers() {
        return others;
    }

    public ArrayList<Center> getOthersIn() {
        return othersIn;
    }

}