package osu.cse.xuan.freqdetector;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

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
}
