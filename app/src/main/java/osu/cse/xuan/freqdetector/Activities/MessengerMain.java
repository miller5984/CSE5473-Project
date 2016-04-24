package osu.cse.xuan.freqdetector.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import osu.cse.xuan.freqdetector.Core.MessageTest;
import osu.cse.xuan.freqdetector.R;

public class MessengerMain extends AppCompatActivity {

     private int MAX_ID_REC_ID;

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger_main);

        MessageTest.runTest();

        Button received = (Button) findViewById(R.id.receivedmessages);
        if(received != null) {
            received.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MessengerMain.this, ReceivedMessages.class));
                }
            });
        }
        Button sent = (Button) findViewById(R.id.sentmessages);
        if(sent != null) {
            sent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MessengerMain.this, Pop.class));
                }
            });
        }

        Button keyGen = (Button)findViewById(R.id.keygen);
        if(keyGen != null){
            keyGen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MessengerMain.this, GenerateKey.class));
                }
            });
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        //New User Button
         Button newUser = (Button) findViewById(R.id.addUser);
        if(newUser != null){
            newUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    AlertDialog.Builder alert = new AlertDialog.Builder(MessengerMain.this);

                    alert.setTitle("Add a User");
                    alert.setMessage("Enter your Name:");

                    // Set an EditText view to get user input
                    final EditText input = new EditText(MessengerMain.this);
                    alert.setView(input);

                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            String myInput = input.getText().toString();
                            addNewUser newUserSync = new addNewUser();
                            newUserSync.execute(myInput);
                        }
                    });

                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            dialog.dismiss();
                        }
                    });

                    alert.show();








                }
            });
        }

        Button checkForMessage = (Button) findViewById(R.id.checkMessages);
        if(checkForMessage != null) {
            checkForMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(getApplicationContext(), "Checking for new messages...",
                            Toast.LENGTH_SHORT).show();

                    checkMessageTask myTask = new checkMessageTask();
                    myTask.execute();


                }
            });

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
                "MessengerMain Page", // TODO: Define a title for the content shown.
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
                "MessengerMain Page", // TODO: Define a title for the content shown.
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


    public class addNewUser extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String enteredName = params[0];
            String JsonResponse;

            //form JSON
            JSONObject JSONEnteredName = new JSONObject();
            try{
            JSONEnteredName.put("name",enteredName);
            JSONEnteredName.put("mac_address","");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String valueJSON = String.valueOf(JSONEnteredName);


            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            try {

                //url to POST to
                URL url = new URL("http://tanapp.tedzhu.org/devices.php");
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

            try{
                JSONObject jsonData = new JSONObject(s);
                String currentID = jsonData.getString("id");


                SharedPreferences sharedPreferences;
                sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MessengerMain.this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("myDeviceID", currentID);
                editor.apply();



            }catch (Throwable t){
                t.printStackTrace();
            }




        }

    }
    public class checkMessageTask extends AsyncTask<String, String, String> {

        HttpURLConnection urlConnection;

        @Override
        protected String doInBackground(String... params) {
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL("http://tanapp.tedzhu.org/messages.php");
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
        protected void onPostExecute(String s) {


            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MessengerMain.this);
            String myID = pref.getString("myDeviceID",null);

            try{
            JSONArray messages = new JSONArray(s);

                for(int i = 0; i < messages.length(); i++){
                    String currentRecID = messages.getJSONObject(i).getString("recipient_device_id");

                    if(currentRecID.equals(myID)){
                        String myMessage = messages.getJSONObject(i).getString("data_text");
                        String messageID = messages.getJSONObject(i).getString("id");

                        int messageIDint = Integer.parseInt(messageID);

                        if(messageIDint > MAX_ID_REC_ID) {
                            ReceivedMessages.myList.add(myMessage);
                            MAX_ID_REC_ID = messageIDint;
                            System.out.println("messageID: " + messageIDint + ", maxID: " + MAX_ID_REC_ID);
                        }
                    }
                }



        }catch (Throwable t){
            t.printStackTrace();
        }



    }
    }





}
