package com.gocodes.locationtracker.network.requests;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gocodes.locationtracker.utils.GlobalVariables;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by vit-vetal- on 01.11.17.
 */

public class SendLocationRequest extends JsonObjectRequest  {
    private final int REQUEST_TIMEOUT_LIMIT_SECONDS = 10; // Value in seconds

    public SendLocationRequest(JSONObject param, Response.Listener<JSONObject> listener,
                                                Response.ErrorListener errorListener) {
        super(Request.Method.POST, GlobalVariables.SERVER_URL, param, listener, errorListener);

        this.setRetryPolicy(new DefaultRetryPolicy(REQUEST_TIMEOUT_LIMIT_SECONDS * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Content-Type", "application/json");
        return params;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            //Log.d("myLogs", "status code " + response.statusCode);
            if(response.statusCode == 200) {
                return Response.success(
                        new JSONObject(),
                        HttpHeaderParser.parseCacheHeaders(response));
            } else {
                return Response.error(new ParseError());
            }
        } catch (Exception e) {
           // Log.d("myLogs", e.toString());
            return Response.error(new ParseError(e));
        }
    }
}