package com.gocodes.locationtracker.network.requests;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gocodes.locationtracker.network.API;
import com.gocodes.locationtracker.utils.GlobalVariables;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vit-vetal- on 01.11.17.
 */

public class SendLocationRequest {
    public static void send(Context context, JSONObject param) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, GlobalVariables.SERVER_URL, param,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("myLogs", "response :"+response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myLogs", "Error: " + error.toString());
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        API.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
