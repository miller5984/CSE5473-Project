package osu.cse.xuan.freqdetector.Core;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ted Zhu on 4/23/2016.
 */
public class JSONHelper {

    public static JSONArray fetchArray(String url) {
        String result = HttpRequestClient.get(url);

        try {
            return new JSONArray(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static JSONObject fetchObject(String url) {
        String result = HttpRequestClient.get(url);

        try {
            return new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
