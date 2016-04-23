package osu.cse.xuan.freqdetector;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SyncAdapterType;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MessengerMain extends AppCompatActivity {

    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger_main);

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
        final Button newUser = (Button) findViewById(R.id.addUser);
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
                            //addNewUser newUserSync = new addNewUser();
                            //newUserSync.execute(myInput);
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
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

    }





}
