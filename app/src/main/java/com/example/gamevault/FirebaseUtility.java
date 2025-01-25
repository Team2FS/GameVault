package com.example.gamevault;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import android.content.Context;
import android.util.Log;
import com.google.firebase.database.FirebaseDatabase;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.InputStream;
import java.util.*;

public class FirebaseUtility {

    private static final String TAG = "FirebaseUtility";

    // Initialize Firebase Database reference
    private static final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    // Reads the JSON file from the "assets" folder.
    public static String readJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
            Log.e(TAG, "Error reading JSON file", e);
        }
        return json;
    }

    // Uploads JSON data to Firebase
    public static void uploadJSONToFirebase(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);

            // Convert JSON object to a Map compatible with Firebase
            Map<String, Object> firebaseData = convertJSONObjectToMap(jsonObject);

            // Write the data to Firebase
            mDatabase.setValue(firebaseData)
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Data uploaded successfully!"))
                    .addOnFailureListener(e -> Log.e(TAG, "Error uploading data", e));
        } catch (Exception e) {
            Log.e(TAG, "Error parsing JSON data", e);
        }
    }

    // Converts a JSONObject to a Map<String, Object> for Firebase compatibility.
    private static Map<String, Object> convertJSONObjectToMap(JSONObject jsonObject) {
        Map<String, Object> map = new HashMap<>();
        try {
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();

                // Sanitize key to ensure it's valid for Firebase
                String sanitizedKey = sanitizeKey(key);

                Object value = jsonObject.get(key);

                // Handle different types of JSON values
                if (value instanceof JSONObject) {
                    map.put(sanitizedKey, convertJSONObjectToMap((JSONObject) value));
                } else if (value instanceof JSONArray) {
                    map.put(sanitizedKey, convertJSONArrayToList((JSONArray) value));
                } else if (value instanceof Integer || value instanceof Double || value instanceof Boolean || value instanceof String) {
                    map.put(sanitizedKey, value); // Add primitive types directly
                } else {
                    Log.w(TAG, "Unsupported data type for key: " + sanitizedKey + " - Value: " + value);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error converting JSONObject to Map", e);
        }
        return map;
    }

    private static List<Object> convertJSONArrayToList(JSONArray jsonArray) {
        List<Object> list = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                Object value = jsonArray.get(i);

                // Handle different types of JSON values in the array
                if (value instanceof JSONObject) {
                    list.add(convertJSONObjectToMap((JSONObject) value));
                } else if (value instanceof JSONArray) {
                    list.add(convertJSONArrayToList((JSONArray) value));
                } else if (value instanceof Integer || value instanceof Double || value instanceof Boolean || value instanceof String) {
                    list.add(value); // Add primitive types directly
                } else {
                    Log.w(TAG, "Unsupported data type in JSONArray at index: " + i + " - Value: " + value);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error converting JSONArray to List", e);
        }
        return list;
    }

    private static String sanitizeKey(String key) {
        // Replace invalid characters in Firebase keys
        return key.replace("/", "_")
                .replace(".", "_")
                .replace("#", "_")
                .replace("$", "_")
                .replace("[", "_")
                .replace("]", "_");
    }

}