package osu.cse.networksecurity.tanapp.Core;

/**
 * Created by Ted Zhu on 4/23/2016.
 */
public class MessageTest {

    public static void runTest() {
//        new Thread(
//                new Runnable() {
//                    public void run() {
//                        Log.v("dbug","Starting task!");
//                        JSONArray jarray =  JSONHelper.fetchArray("http://tanapp.tedzhu.org/devices.php");
//                        try {
//                            Log.v("dbug", jarray.toString(5));
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//        ).start();

        Device.reloadCache(
            new Runnable() {
                @Override
                public void run() {
                    //Device one = Device.findById(1);
                    //Log.v("dbug", one.name);
                }
            }
        );


    }



}
