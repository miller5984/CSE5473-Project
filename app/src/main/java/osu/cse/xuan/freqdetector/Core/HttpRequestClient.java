package osu.cse.xuan.freqdetector.Core;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ted Zhu on 4/23/2016.
 */
public class HttpRequestClient {

    public static String get(String url) {
        StringBuilder result = new StringBuilder();
        HttpURLConnection urlConnection = null;

        Log.v("dbug", "starting GET request for " + url);

        try {
            URL urlObj = new URL(url);
            urlConnection = (HttpURLConnection) urlObj.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());


            BufferedReader reader = new BufferedReader(new InputStreamReader(in));

            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }

        }catch( Exception e) {
            e.printStackTrace();
        }
        finally {
            urlConnection.disconnect();
        }
        return result.toString();
    }

}
