package com.gocodes.locationtracker.network.requests;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.Map;

/**
 * Created by vit-vetal- on 30.10.17.
 */

public class SendLocationRequest<T> extends BaseJsonRequest<T> {
    private static final String PATH = "https://55z4akhs63.execute-api.us-west-2.amazonaws.com/UpdateAssetLocation2";
    private Map<String, String> params;

    public SendLocationRequest(Map<String, String> params, Class<T> clazz,
                                Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, PATH, clazz, listener, errorListener);

        this.params = params;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {

        return params;
    }
}