package com.alon.yosef.countries.service.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by alony on 17/07/2019.
 */

public class Country implements Serializable {
    private String name;
    private String nativeName;
    private String countryCode;
    private double area;
    private String[] borders;

    public static Country parse(JSONObject jo) {
        Country country = new Country();
        try {
            country.name = jo.getString("name");
            country.nativeName = jo.getString("nativeName");
            country.countryCode = jo.getString("alpha3Code");
            country.area = jo.optDouble("area", 0);
            JSONArray bordersJA = jo.getJSONArray("borders");
            if (bordersJA != null && bordersJA.length() > 0) {
                String[] borders = new String[bordersJA.length()];
                for (int j = 0; j < bordersJA.length(); j++)
                    borders[j] = bordersJA.getString(j);
                country.borders = borders;
            }
            //if (country.isFull());
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return country;
    }

    public String getName() {
        return name;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public String getNativeName() {
        return nativeName;
    }

    public double getArea() {
        return area;
    }

    public String[] getBorders() {
        return borders;
    }

    public static Country getCountryByCode(ArrayList<Country> countries, String countryCode) {
        Log.d("Alon", "countries.size="+countries.size()+", countryCode="+countryCode);
        for (Country c : countries) {
            if (countryCode.equals(c.getCountryCode()))
                return c;
        }
        return null;
    }

    @Override
    public String toString() {
        return "name="+name+", nativeName="+nativeName+", area="+area+", borders="+ Arrays.toString(borders);
    }
}
