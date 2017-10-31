package com.gocodes.locationtracker.network.requests;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.Map;

/**
 * Created by vit-vetal- on 30.10.17.
 */

public class TestRequest extends StringRequest {
    private static final String PATH = "https://55z4akhs63.execute-api.us-west-2.amazonaws.com/UpdateAssetLocation2/";
    private Map<String, String> params;

    public TestRequest(Map<String, String> params,
                       Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, PATH,  listener, errorListener);

        this.params = params;
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {

        return params;
    }
}