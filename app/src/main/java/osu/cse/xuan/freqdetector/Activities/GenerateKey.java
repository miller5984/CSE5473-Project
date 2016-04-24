package osu.cse.xuan.freqdetector.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import osu.cse.xuan.freqdetector.R;

public class GenerateKey extends AppCompatActivity implements View.OnClickListener{

    byte[] key;
    char[] hex;
    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    Button keyGen;
    Button sendKey;
    public static final String MyPrefs = "myprefs";
    public static final String pk = "pkey";
    SharedPreferences sharedPreferences;
    TextView keyText;
    String checkprefs, keySend, keySend1, first = "25fc941a", second = "50d2ab2e", third = "e8517afd",
    fourth = "c2492baf";
    HashMap<String,String> stringMap = new HashMap<>();

    public ArrayList<String> stringArray = new ArrayList<>();
    public ArrayList<String> idArray = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_key);
        keyText = (TextView)findViewById(R.id.keyText);
        keyGen = (Button)findViewById(R.id.genkey);
        sendKey = (Button)findViewById(R.id.sendKey);
        sharedPreferences = getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);
        key = new byte[16];
        checkprefs = sharedPreferences.getString(pk, null);
        //get list of names from server
        requestNames getNames = new requestNames();
        getNames.execute();

        //keyText.setText(checkprefs);
//        if(checkprefs.equals(null)){
//            sendKey.setEnabled(false);
//        }
//        else{
//            keyGen.setEnabled(false);
//            sendKey.setEnabled(true);
//        }

        keyGen.setOnClickListener(this);
        sendKey.setOnClickListener(this);

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.sendKey:
                keySend = sharedPreferences.getString("pkey", null);
                keySend1 = keySend.substring(0,1) + first + keySend.substring(1,5) + second
                        + keySend.substring(5, 17) + third + keySend.substring(17, 30) + fourth
                        + keySend.substring(30, 32);

                //Send logic here

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
                        sendSync.execute(keySend,nameOfRecipient,idOfRec);


                    }
                });



                break;
            case R.id.genkey:
                try{
                    KeyGenerator kg = KeyGenerator.getInstance("AES");
                    kg.init(128, new SecureRandom());
                    SecretKey keyGen = kg.generateKey();
                    key = keyGen.getEncoded();
                    hex = new char[key.length * 2];
                    for ( int j = 0; j < key.length; j++ ) {
                        int i = key[j] & 0xFF;
                        hex[j * 2] = hexArray[i >>> 4];
                        hex[j * 2 + 1] = hexArray[i & 0x0F];
                    }
                    String keyValue = String.valueOf(hex);
                    keyText.setText(keyValue);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("pkey", keyValue);
                    editor.apply();
                    Toast.makeText(GenerateKey.this, "Key generated", Toast.LENGTH_SHORT).show();

                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                keyGen.setEnabled(false);
                break;

        }
    }


    public class requestNames extends AsyncTask<String, String, String> {

        HttpURLConnection urlConnection;

        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            dialog = ProgressDialog.show(GenerateKey.this, "", "Loading Users...");
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
            sendDialog = ProgressDialog.show(GenerateKey.this, "", "Sending Key...");
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
                JSONEnteredMessage.put("type","KEY");
                //set sender ID
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(GenerateKey.this);
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
