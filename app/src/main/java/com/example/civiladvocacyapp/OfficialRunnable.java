package com.example.civiladvocacyapp;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

public class OfficialRunnable implements Runnable{

    private final String location;
    private MainActivity mAct;
    /////////////////////////////////////////////////////////
    private static final String civicURL = "https://www.googleapis.com/civicinfo/v2/representatives?key=AIzaSyAZR89q5oXtnQuzu_b5sMK9S4-M6Xh870I&address=";
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

    //public void parseJSON(String s){
    public void parseJSON(JSONObject jsonObj){
        try{
            Official newPerson;
            //JSONObject jsonObj = new JSONObject(s);
            JSONObject nInput = jsonObj.getJSONObject("normalizedInput");
            String street = nInput.getString("line1");
            String city = nInput.getString("city");
            String state = nInput.getString("state");
            String zip = nInput.getString("zip");
            //mAct.locationTV.setText(String.format("%s, %s, %s %s", street, city, state, zip));

            //iterate the rest of the JSON like "offices" and "officials"
            JSONArray offices = jsonObj.getJSONArray("offices");
            JSONArray officials = jsonObj.getJSONArray("officials");
            for (int i=0; i < offices.length(); i++){
                JSONObject office = (JSONObject) offices.get(i);
                //get the name ex. President of the United States
                String name = office.getString("name");

                JSONArray oIndices = office.getJSONArray("officialIndices");
                for(int j=0; j < oIndices.length(); j++){
                    //NOTE: There can be more than one index as time offices have multiple representatives.
                    int k = Integer.parseInt(oIndices.get(j).toString());
                    //access an officials info using the id
                    JSONObject officialInfo = (JSONObject) officials.get(k);
                    //this gets the persons name ex. 'DONALD J. TRUMP'
                    String person = officialInfo.getString("name");
                    newPerson = new Official(person, name);
                    String line1, line2, cityName, stateName, postal;
                    line1="";
                    line2="";
                    cityName="";
                    stateName="";
                    postal="";
                    //parsing data only IF it is present in the JSON else skip
                    //address
                    if (officialInfo.has("address")){
                        JSONArray addy = officialInfo.getJSONArray("address");
                        JSONObject fullAddy = (JSONObject) addy.get(0);
                        if(fullAddy.has("line1")){
                            line1 = fullAddy.getString("line1");
                        }
                        if(fullAddy.has("line2")){
                            line2 = fullAddy.getString("line2");
                        }
                        if(fullAddy.has("city")){
                            cityName = fullAddy.getString("city");
                        }
                        if(fullAddy.has("state")){
                            stateName = fullAddy.getString("state");
                        }
                        if(fullAddy.has("zip")){
                            postal = fullAddy.getString("zip");
                        }
                        String officeAddy = String.format(Locale.getDefault(),"%s %s, %s, %s %s", line1, line2, cityName, stateName, postal);
                        newPerson.setOfficeAddress(officeAddy);

                    }
                    //phone number
                    if (officialInfo.has("phones")){
                        //official may have more than one. just use the first one
                        JSONArray phoneNums = officialInfo.getJSONArray("phones");
                        newPerson.setPhoneNum((String) phoneNums.get(0));
                    }
                    //party
                    if (officialInfo.has("party")){
                        newPerson.setParty(officialInfo.getString("party"));
                    }
                    //email
                    if (officialInfo.has("emails")){
                        JSONArray emails = officialInfo.getJSONArray("emails");
                        newPerson.setEmail((String) emails.get(0));
                    }
                    //website
                    if (officialInfo.has("urls")){
                        JSONArray links = officialInfo.getJSONArray("urls");
                        newPerson.setWebsite((String) links.get(0));
                    }
                    //photo
                    if (officialInfo.has("photoUrl")){
                        newPerson.setPhotoLink(officialInfo.getString("photoUrl"));
                    }
                    //social
                    if (officialInfo.has("channels")){
                        JSONArray socialLink = officialInfo.getJSONArray("channels");//enters array of channels
                        for(int n = 0; n < socialLink.length(); n++){ //iterates over channels/socials
                            JSONObject social = (JSONObject) socialLink.get(n);
                            String socialApp = social.getString("type");
                            String socialID = social.getString("id");
                            if (socialApp.equals("Facebook")){
                                newPerson.setFbLink(socialID);
                            }
                            if (socialApp.equals("Twitter")){
                                newPerson.setTwitLink(socialID);
                            }
                            if (socialApp.equals("Youtube")){
                                newPerson.setYtLink(socialID);
                            }
                        }
                    }
                    Official nP = newPerson;
                    mAct.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAct.addNewOff(nP);
                        }
                    });
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
