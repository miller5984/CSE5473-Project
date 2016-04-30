package osu.cse.networksecurity.tanapp.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import javax.crypto.Cipher;

import javax.crypto.spec.SecretKeySpec;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import osu.cse.networksecurity.tanapp.R;


public class Pop extends Activity implements View.OnClickListener {


    Button record, send, play, stop, close;
    TextView boxMessage;
    private MediaRecorder myAudioRecorder;
    SecretKeySpec sks = null;
    private GoogleApiClient client;
    private String key;
    SharedPreferences sharedPreferences;
    public static final String MyPrefs = "myprefs";
    public static final String pk = "pkey";
    byte[] convert;
    byte[] encodedBytes = null;
    HashMap<String,String> stringMap = new HashMap<>();

    public ArrayList<String> stringArray = new ArrayList<>();
    public ArrayList<String> idArray = new ArrayList<>();

    String tempMessage, sendMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);

        //get list of names from server
        requestNames getNames = new requestNames();
        getNames.execute();


        sharedPreferences = getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);
        key = sharedPreferences.getString(pk, null);
        record = (Button) findViewById(R.id.record);
        send = (Button) findViewById(R.id.send);
        close = (Button) findViewById(R.id.close);
        boxMessage = (TextView) findViewById(R.id.boxmessage);

        record.setOnClickListener(this);

        send.setOnClickListener(this);
        close.setOnClickListener(this);

//        myAudioRecorder = new MediaRecorder();
//        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
//        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * .8), (int) (height * .25));


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.record:

                android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(Pop.this);
                alert.setTitle("Message");
                alert.setMessage("Enter your Message:");
                // Set an EditText view to get user input
                final EditText input = new EditText(Pop.this);
                alert.setView(input);

                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String message = input.getText().toString();
                        tempMessage = message;
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                alert.show();
                break;

            case R.id.send:

                key = sharedPreferences.getString("pkey", null);
                convert = new byte[key.length() / 2];
                for (int i = 0; i < key.length(); i += 2) {
                    convert[i / 2] = (byte) ((Character.digit(key.charAt(i), 16) << 4)
                            + Character.digit(key.charAt(i + 1), 16));
                }
                sks = new SecretKeySpec(convert, "AES");

                /* Encrypt portion */

                try {
                    Cipher c = Cipher.getInstance("AES");
                    c.init(Cipher.ENCRYPT_MODE, sks);
                    encodedBytes = c.doFinal(tempMessage.getBytes());
                    sendMessage = Base64.encodeToString(encodedBytes, Base64.DEFAULT);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Select Recipient");

                ListView myList = new ListView(this);

                ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, stringArray);
                myList.setAdapter(modeAdapter);

                builder.setView(myList);
                final Dialog dialog = builder.create();

                dialog.show();
                myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String nameOfRecipient = stringArray.get(position);

                        Toast.makeText(getApplicationContext(), "Sending message to: " + nameOfRecipient + "...",
                                Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        sendMessage sendSync = new sendMessage();
                        String idOfRec = idArray.get(position);
                        sendSync.execute(sendMessage,nameOfRecipient,idOfRec);
                    }
                });
                break;
            case R.id.close:
                finish();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Pop Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://osu.cse.xuan.freqdetector/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Pop Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://osu.cse.xuan.freqdetector/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    public class requestNames extends AsyncTask<String, String, String> {

        HttpURLConnection urlConnection;

        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(Pop.this, "", "Loading Users...");
        }

        @Override
        protected String doInBackground(String... args) {

            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL("http://tanapp.tedzhu.org/devices.php");
                urlConnection = (HttpURLConnection) url.openConnection();
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

        @Override
        protected void onPostExecute(String result) {

            try{
                JSONArray jsonData = new JSONArray(result);

               for(int i = 0; i < jsonData.length(); i++) {

                   String currentID = jsonData.getJSONObject(i).getString("id");
                   String currentName = jsonData.getJSONObject(i).getString("name");


                   if(!stringMap.containsKey(currentID))  {
                       stringMap.put(currentName,currentID);
                   }

                   if(!stringArray.contains(currentName)){
                       stringArray.add(currentName);
                       idArray.add(currentID);

                   }

                }

                dialog.dismiss();

            }catch (Throwable t){
                t.printStackTrace();
            }
        }
    }


    public class sendMessage extends AsyncTask<String,String,String>{

        ProgressDialog sendDialog;

        @Override
        protected void onPreExecute() {
            sendDialog = ProgressDialog.show(Pop.this, "", "Sending Message...");
        }

        @Override
        protected String doInBackground(String... params) {

            //send message
            String enteredMessage = params[0];
            String enteredName = params[1];
            String recipID = params[2];
            String JsonResponse;

            //form JSON
            JSONObject JSONEnteredMessage = new JSONObject();
            try{
                //set type
                JSONEnteredMessage.put("type","TEXT");
                //set sender ID
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(Pop.this);
                String myID = pref.getString("myDeviceID",null);
                JSONEnteredMessage.put("sender_device_id",myID);

                //set recipient ID
                JSONEnteredMessage.put("recipient_device_id",recipID);

                //set message
                JSONEnteredMessage.put("data_text",enteredMessage);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            String valueJSON = String.valueOf(JSONEnteredMessage);
            System.out.println(valueJSON);

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {

                //url to POST to
                URL url = new URL("http://tanapp.tedzhu.org/messages.php");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);


                // is output buffer
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setRequestProperty("Accept", "application/json");

                Writer writer = new BufferedWriter(new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8"));
                writer.write(valueJSON);

                writer.close();
                InputStream inputStream = urlConnection.getInputStream();

                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String inputLine;
                while ((inputLine = reader.readLine()) != null)
                    buffer.append(inputLine + "\n");
                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing.
                    return null;
                }
                JsonResponse = buffer.toString();
                System.out.println(JsonResponse);
                //response data

                return JsonResponse;

            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            sendDialog.dismiss();
        }
    }
}
