package osu.cse.xuan.freqdetector;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;

import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


public class Pop extends Activity implements View.OnClickListener {


    Button record, send, play, stop, close;
    TextView boxMessage;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;
    SecretKeySpec sks = null;
    private GoogleApiClient client;
    private String key;
    SharedPreferences sharedPreferences;
    public static final String MyPrefs = "myprefs";
    public static final String pkey = "pkey";
    byte[] convert;
    byte[] encodedBytes = null;
    byte[] decodedBytes = null;
    byte[] decode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);


        sharedPreferences = getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);
        key = sharedPreferences.getString("pkey", null);
        record = (Button) findViewById(R.id.record);
        stop = (Button) findViewById(R.id.stop);
        send = (Button) findViewById(R.id.send);
        play = (Button) findViewById(R.id.play);
        close = (Button) findViewById(R.id.close);
        boxMessage = (TextView) findViewById(R.id.boxmessage);

        stop.setEnabled(false);
        play.setEnabled(false);

//        final File folder = new File(getApplicationContext().getExternalFilesDir(null) + "/AudioFiles");
//        System.out.println(folder.toString());
//        //check if directory exists
//        boolean val = false;
//        if (!folder.exists()) {
//            val = folder.mkdir();
//        }
//
//        String fileName =  "recording.3gp";




        record.setOnClickListener(this);
        stop.setOnClickListener(this);
        play.setOnClickListener(this);
        send.setOnClickListener(this);
        close.setOnClickListener(this);

        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);

        //outputFile = folder + "/" + fileName;
        outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
        //System.out.println(outputFile);
        myAudioRecorder.setOutputFile(outputFile);
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
                Toast.makeText(getApplicationContext(), "You are recording!",
                        Toast.LENGTH_SHORT).show();
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                }

                record.setEnabled(false);
                stop.setEnabled(true);
                break;
            case R.id.stop:
                Toast.makeText(getApplicationContext(), "You have made your recording!",
                        Toast.LENGTH_SHORT).show();
                myAudioRecorder.stop();
                myAudioRecorder.release();
                myAudioRecorder = null;

                stop.setEnabled(false);
                play.setEnabled(true);

                break;

            case R.id.send:
                Toast.makeText(getApplicationContext(), "You are sending your message!",
                        Toast.LENGTH_SHORT).show();
                WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                WifiInfo info = manager.getConnectionInfo();

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
                    encodedBytes = c.doFinal(outputFile.getBytes());
                    FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/encrypted.3gp"));
                    fos.write(encodedBytes);
                    fos.close();

                    File part = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/encrypted.3gp");
                    BufferedInputStream buf = new BufferedInputStream(new FileInputStream(part));
                    decode = new byte[(int) part.length()];
                    buf.read(decode);
                    c = Cipher.getInstance("AES");
                    c.init(Cipher.DECRYPT_MODE, sks);
                    decodedBytes = c.doFinal(decode);
                    FileOutputStream fos1 = new FileOutputStream(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/fixed.3gp"));

                    fos1.write(decodedBytes);
                    fos1.close();


                } catch (Exception e) {
                    e.printStackTrace();
                }

                /* Write in send message code */


                String address = "Received from MAC: " + info.getMacAddress();
                ReceivedMessages.myList.add(address);

                break;

            case R.id.play:
                MediaPlayer m = new MediaPlayer();
                try {
                    m.setDataSource(outputFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    m.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                m.start();
                Toast.makeText(getApplicationContext(), "Playing Audio",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.close:

                MediaPlayer mediaPlayer = new MediaPlayer();
                String fpath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/fixed.3gp";
                try {
                    mediaPlayer.setDataSource(fpath);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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

}
