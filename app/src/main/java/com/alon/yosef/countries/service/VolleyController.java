package com.alon.yosef.countries.service;

import android.content.Context;

import com.alon.yosef.countries.ui.interfaces.ResponseListener;
import com.alon.yosef.countries.service.model.Country;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by alony on 18/07/2019.
 */

public class VolleyController {

    private static final String BASE_URL = "https://restcountries.eu/rest/v2/";
    private static final String ALL = "all";

    private static VolleyController instance;

    private Context context;

    private static RequestQueue requestQueue;

    private VolleyController(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public static VolleyController getInstance(Context context) {
        return instance == null ? instance = new VolleyController(context): instance;
    }

    public void getAllCountries(final ResponseListener<ArrayList<Country>> listener) {
        JsonArrayRequest jor = new JsonArrayRequest(Request.Method.GET, BASE_URL + ALL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                ArrayList<Country> countries = new ArrayList<>();
                for (int i=0; i < response.length(); i++) {
                    try {
                        JSONObject jo = response.getJSONObject(i);
                        Country country = Country.parse(jo);
                        countries.add(country);

                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
                listener.onSuccess(countries);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error);
                error.printStackTrace();
            }
        });
        requestQueue.add(jor);
    }
}
