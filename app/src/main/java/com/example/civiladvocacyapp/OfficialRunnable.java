package com.example.civiladvocacyapp;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class OfficialRunnable implements Runnable{

    private final String location;
    private MainActivity mAct;
    /////////////////////////////////////////////////////////
    private static final String civicURL = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyAZR89q5oXtnQuzu_b5sMK9S4-M6Xh870I&address";
    /////////////////////////////////////////////////////////

    public OfficialRunnable(MainActivity mAct, String location){
        this.location = location;
        this.mAct = mAct;
    }

    @Override
    public void run() {
        String url = civicURL + location;
        RequestQueue reqQueue = Volley.newRequestQueue(mAct);
        Uri.Builder builder = Uri.parse(url).buildUpon();
        String urlToUse = builder.build().toString();

        Response.Listener<JSONObject> listener = this::parseJSON;

        Response.ErrorListener error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try{
                    JSONObject jo = new JSONObject(new String(error.networkResponse.data));
                    parseJSON(null);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        JsonObjectRequest jor =
                new JsonObjectRequest(Request.Method.GET, urlToUse,
                        null, listener, error);

        reqQueue.add(jor);
    }

    public void parseJSON(JSONObject jsonObj){
        try{
            JSONObject nInput = jsonObj.getJSONObject("normalizedInput");
            String street = nInput.getString("line1");
            String city = nInput.getString("city");
            String state = nInput.getString("state");
            String zip = nInput.getString("zip");

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
