package osu.cse.xuan.freqdetector.Core;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import osu.cse.xuan.freqdetector.Utility.Dbug;

/**
 * Created by Ted Zhu on 4/23/2016.
 */
public class Device {

    private static Map<Integer, Device> cachedDevices = new HashMap<>();
    private static String collectionURL = "http://tanapp.tedzhu.org/devices.php";

    public int id;
    public String name;

    static {
    }

    private Device(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static void reloadCache(final Runnable cacheReloadedHandler) {

        Dbug.log("refreshing devices list cache.");

        new Thread(new Runnable() {

                @Override
                public void run() {

                    try {
                        //Dbug.log("in try...");
                        JSONArray jsonData = JSONHelper.fetchArray(collectionURL);
                        //Dbug.log("Fetched data: " + jsonData.toString(5));

                        for(int i = 0; i < jsonData.length(); i++) {

                            int id = jsonData.getJSONObject(i).getInt("id");
                            String name = jsonData.getJSONObject(i).getString("name");
                            Device device = new Device(id, name);

                            if(!cachedDevices.containsKey(id))  {
                                cachedDevices.put(id, device);
                            }
                        }
                        cacheReloadedHandler.run();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
        }).start();
    }

    public static Device findById(int id) {
        Device result = cachedDevices.get(id);
        if (result == null) {
            Dbug.log("NULL DEVICE FOUND for ID " + id);
            try {
                throw new Error("Device not found. Maybe your code needs to call Device.reloadCache to fetch latest list? -Ted ");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cachedDevices.get(id);
    }


}
