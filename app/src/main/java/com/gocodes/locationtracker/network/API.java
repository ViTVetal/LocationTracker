package com.gocodes.locationtracker.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by vit-vetal- on 30.10.17.
 */

public class API {
    private volatile static API apiInstance;
    private RequestQueue requestQueue;
    private Context context;

    private API(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public static API getInstance(Context context) {
        if(apiInstance == null) {
            synchronized (API.class) {
                if(apiInstance == null) {
                    apiInstance = new API(context);
                }
            }
        }

        return apiInstance;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}