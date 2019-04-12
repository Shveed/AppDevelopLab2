package com.example.labwork2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Intro extends AppCompatActivity {

    static final String unpaused = "is resumed";
    static final String goBack = "is on back pressed";
    private boolean begin = false;
    private volatile boolean isGoBack = false;
    public ArrayList<HashMap<String, String>> arraylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        if(savedInstanceState != null) {
            begin = savedInstanceState.getBoolean(unpaused);
            isGoBack = savedInstanceState.getBoolean(goBack);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!begin) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        DownloadJSON();

                        if (!isGoBack) {
                            Intent intent = new Intent(Intro.this, MainActivity.class);
                            intent.putExtra("Downloaded", arraylist);
                            startActivity(intent);
                            finish();
                        }
                    } catch (
                            Exception e) {
                        e.printStackTrace();
                    }
                    begin = true;
                }
            });
            thread.start();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(unpaused, begin);
        savedInstanceState.putBoolean(goBack, isGoBack);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isGoBack = true;
    }

    void DownloadJSON() throws JSONException {
        // Create an array
        arraylist = new ArrayList<>();

        // Retrieve JSON Objects from the given URL address
        String jsonstring = JsonRelatives
                .getJSONStringfromURL("https://raw.githubusercontent.com/wesleywerner/ancient-tech/02decf875616dd9692b31658d92e64a20d99f816/src/data/techs.ruleset.json");
        JSONArray jsonarray;
        JSONObject jsonobject;
        jsonarray = new JSONArray(jsonstring);
        for (int i = 0; i < jsonarray.length(); i++) {
            HashMap<String, String> technologies = new HashMap<>();
            jsonobject = jsonarray.getJSONObject(i);
            // Retrieve JSON Objects
            if (jsonobject.has("name"))
                technologies.put("name", jsonobject.getString("name"));
            if (jsonobject.has("helptext"))
                technologies.put("helptext", jsonobject.getString("helptext"));
            if (jsonobject.has("graphic"))
                technologies.put("graphic", jsonobject.getString("graphic"));
            // Set the JSON Objects into the array
            if ((jsonobject != null) && jsonobject.has("name"))
                arraylist.add(technologies);
        }
    }
}