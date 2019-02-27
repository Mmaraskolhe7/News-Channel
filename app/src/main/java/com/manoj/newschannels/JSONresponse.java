package com.manoj.newschannels;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONresponse {
    private List<Sources> sources = new ArrayList<>();
    JSONObject parts;

    public List<Sources> response(String result) {
        try {
            JSONObject object = new JSONObject(result);
            JSONArray sourceArray = object.getJSONArray("sources");
            if (sourceArray != null) {
                for (int i = 0; i < sourceArray.length(); i++) {
                    parts = sourceArray.getJSONObject(i);
                    sources.add(new Sources(parts.getString("name"), parts.getString("description"), parts.getString("url"), parts.getString("category"), parts.getString("language")));
                }
            }
            return sources;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }


}
