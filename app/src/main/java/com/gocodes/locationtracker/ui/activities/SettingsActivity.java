package com.gocodes.locationtracker.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gocodes.locationtracker.R;
import com.gocodes.locationtracker.network.API;
import com.gocodes.locationtracker.network.requests.TestRequest;
import com.gocodes.locationtracker.utils.HttpsTrustManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SettingsActivity extends AppCompatActivity {
    private SeekBar seekBar;
    private TextView tvFrequency;

    private String[] frequencies = {"1", "2", "4","8", "12", "24", "48"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvFrequency = (TextView) findViewById(R.id.tvFrequency);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvFrequency.setText(frequencies[i]);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

      //  sendComment();

        String url = "https://55z4akhs63.execute-api.us-west-2.amazonaws.com/UpdateAssetLocation2";
/*
        HurlStack hurlStack = new HurlStack() {
            @Override
            protected HttpURLConnection createConnection(URL url) throws IOException {
                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
                try {
                    httpsURLConnection.setSSLSocketFactory(getSSLSocketFactory());
                    httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return httpsURLConnection;
            }
        }; */

        Map<String, String> params = new HashMap<String, String>();

         params.put("email", "service@gocodes.com");
         params.put("password", "gocodes111");
        params.put("assetId", "22PHNP89");
        params.put("latitude", "48.386402");
        params.put("longitude", "22.718983");
        params.put("enableHistory", "true");
        params.put("customValue1", "934");

      /*  TestRequest request = new TestRequest(params, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                Log.d("myLogs", result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myLogs", error + " " + error.getMessage());
            }
        });

        final RequestQueue requestQueue = Volley.newRequestQueue(this, hurlStack);

        requestQueue.add(request); */

      //  HttpsTrustManager.allowAllSSL();
//        String  tag_string_req = "string_req";
//        StringRequest strReq = new StringRequest(Request.Method.GET,
//                url, new Response.Listener<String>() {
//            @Override
//            public void onResponse(String response) {
//                Log.d("myLogs", "response :"+response);
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Log.d("myLogs", "Error: " + error.getMessage());
//            }
//        }){
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("email", "service@gocodes.com");
//                params.put("password", "gocodes111");
//                params.put("assetId", "22PH-NP89");
//                params.put("latitude", "3.005385");
//                params.put("longitude", "93.508629");
//                params.put("enableHistory", "true");
//                params.put("customValue1", "934");
//                return params;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("email", "service@gocodes.com");
//                params.put("password", "gocodes111");
//                params.put("assetId", "22PH-NP89");
//                params.put("latitude", "3.005385");
//                params.put("longitude", "93.508629");
//                params.put("enableHistory", "true");
//                params.put("customValue1", "934");
//
//                return params;
//            }
//        };
//        API.getInstance(this).addToRequestQueue(strReq);


            JSONObject param = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, param,
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


        Log.d("myLogs", param + "");
//            JsonObjectRequest req = new JsonObjectRequest(
//                    Request.Method.POST,
//                    url,
//                    param,
//                   new Response.Listener<JSONObject>() {
//                       @Override
//                       public void onResponse(JSONObject response) {
//                           Log.d("myLogs", "response :"+response);
//                       }
//                   }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.d("myLogs", "Error: " + error.toString());
//                }
//
//
//            });
        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(jsonObjectRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void sendComment() {
        Map<String, String> params = new HashMap<String, String>();

       // params.put("smsID", String.valueOf(sms.getId()));
       // params.put("userID", GlobalVariables.getEmail(this));
       /// params.put("password", GlobalVariables.getPassword(this));


        TestRequest request = new TestRequest(params, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                Log.d("myLogs", result);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("myLogs", error + " " + error.getMessage());
            }
        });

        API.getInstance(this).addToRequestQueue(request);
    }

    // Let's assume your server app is hosting inside a server machine
    // which has a server certificate in which "Issued to" is "localhost",for example.
    // Then, inside verify method you can verify "localhost".
    // If not, you can temporarily return true
  /*  private HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //return true; // verify always returns true, which could cause insecure network traffic due to trusting TLS/SSL server certificates for wrong hostnames
                HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();
                return hv.verify("localhost", session);
            }
        };
    }

    private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {
        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];
        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return originalTrustManager.getAcceptedIssuers();
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        try {
                            if (certs != null && certs.length > 0){
                                certs[0].checkValidity();
                            } else {
                                originalTrustManager.checkClientTrusted(certs, authType);
                            }
                        } catch (CertificateException e) {
                            Log.w("checkClientTrusted", e.toString());
                        }
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        try {
                            if (certs != null && certs.length > 0){
                                certs[0].checkValidity();
                            } else {
                                originalTrustManager.checkServerTrusted(certs, authType);
                            }
                        } catch (CertificateException e) {
                            Log.w("checkServerTrusted", e.toString());
                        }
                    }
                }
        };
    }

    private SSLSocketFactory getSSLSocketFactory()
            throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        InputStream caInput = getResources().openRawResource(R.raw.mystore); // this cert file stored in \app\src\main\res\raw folder path

        Certificate ca = cf.generateCertificate(caInput);
        caInput.close();

        KeyStore keyStore = KeyStore.getInstance("BKS");
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(tmf.getTrustManagers());

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, wrappedTrustManagers, null);

        return sslContext.getSocketFactory();
    } */
}
