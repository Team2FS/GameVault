package com.example.gamevault;

/*
EXAMPLE USAGE
JSONObject jsonObject = JSONReader.readJSONFromAssets(this, "CamoData.json");
String path = "modes.mp[0].Assault Rifles[0].Skins.Splinter.counter";
Object value = JSONReader.getValueByPath(jsonObject, path);

if (value != null) {
    Log.d(value);
} else {
    Log.e(path);
}


 */



import android.content.Context;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;

public class JSONReader {

    private static final String TAG = "JSONReader";

    // Reads camodata
    public static JSONObject readJSONFromAssets(Context context, String fileName) {
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new JSONObject(new String(buffer, "UTF-8"));
        } catch (Exception e) {
            Log.e(TAG, "Error reading JSON file", e);
            return null;
        }
    }

    // use a dot to seperate parents "modes.mp[0].Assault Rifles.XM4.Splinter.counter")
    public static Object getValueByPath(JSONObject jsonObject, String path) {
        try {
            if (jsonObject == null || path == null || path.isEmpty()) return null;

            String[] keys = path.split("\\.");
            Object currentObject = jsonObject;

            for (String key : keys) {
                if (currentObject instanceof JSONObject) {
                    currentObject = ((JSONObject) currentObject).opt(key);
                }
                else if (currentObject instanceof JSONArray) {
                    // If it's an array, get the first thing
                    JSONArray jsonArray = (JSONArray) currentObject;
                    if (jsonArray.length() > 0) {
                        currentObject = jsonArray.opt(0);
                    } else {
                        return null;
                    }
                }
                else {
                    return null; // not found
                }
            }
            return currentObject;
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving value from JSON path: " + path, e);
            return null;
        }
    }
}