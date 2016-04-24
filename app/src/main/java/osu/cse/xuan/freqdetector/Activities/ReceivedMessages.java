package osu.cse.xuan.freqdetector.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import osu.cse.xuan.freqdetector.R;


public class ReceivedMessages extends AppCompatActivity {

    public static ArrayList<String> myList = new ArrayList<>();
    private String decrypt;
    private static final String MyPrefs = "myprefs", pk = "pkey";
    SharedPreferences sharedPreferences;
    byte[] convert, decodedBytes, decodedMessage;
    ListView recList;
    private String selected, newMessage;
    SecretKeySpec sks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_messages);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);


        recList = (ListView) findViewById(R.id.receivedList);


        recList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                decrypt = sharedPreferences.getString(pk, null);
                selected = ((TextView)view).getText().toString();
                decodedMessage = Base64.decode(selected, Base64.DEFAULT);
                convert = new byte[decrypt.length() / 2];
                for (int i = 0; i < decrypt.length(); i += 2) {
                    convert[i / 2] = (byte) ((Character.digit(decrypt.charAt(i), 16) << 4)
                            + Character.digit(decrypt.charAt(i + 1), 16));
                }
                sks = new SecretKeySpec(convert, "AES");
                try {
                    Cipher c = Cipher.getInstance("AES");
                    c.init(Cipher.DECRYPT_MODE, sks);
                    decodedBytes = c.doFinal(decodedMessage);
                    newMessage = new String(decodedBytes, StandardCharsets.UTF_8);
                    ((TextView)view).setText(newMessage);
                    selected = "";
                    decodedMessage = null;
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
            }
        });
//            @Override
//            public boolean onItemLongClick(AdapterView<?> arg0, View view, int index, long arg3){
//                selected = recList.getItemAtPosition(index).toString();
//                convert = new byte[decrypt.length() / 2];
//                for (int i = 0; i < decrypt.length(); i += 2) {
//                    convert[i / 2] = (byte) ((Character.digit(decrypt.charAt(i), 16) << 4)
//                            + Character.digit(decrypt.charAt(i + 1), 16));
//                }
//                sks = new SecretKeySpec(convert, "AES");
//                try {
//                    Cipher c = Cipher.getInstance("AES");
//                    c.init(Cipher.DECRYPT_MODE, sks);
//                    decodedBytes = c.doFinal(selected.getBytes());
//                    newMessage = Base64.encodeToString(decodedBytes, Base64.DEFAULT);
//                    ((TextView)view).setText(newMessage);
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                } catch (InvalidKeyException e) {
//                    e.printStackTrace();
//                } catch (NoSuchPaddingException e) {
//                    e.printStackTrace();
//                } catch (BadPaddingException e) {
//                    e.printStackTrace();
//                } catch (IllegalBlockSizeException e) {
//                    e.printStackTrace();
//                }
//                return true;
//            }
//        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myList);
        recList.setAdapter(adapter);

        registerForContextMenu(recList);
        adapter.notifyDataSetChanged();


    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//
//        menu.add("Decrypt");
//    }
//
//    @Override
//    public boolean onContextItemSelected(MenuItem item) {
//        super.onContextItemSelected(item);
//
//        if (item.getTitle() == "Decrypt") {
//            int position = recList.getSelectedItemPosition();
//            selected = recList.getItemAtPosition(position).toString();
//            convert = new byte[decrypt.length() / 2];
//            for (int i = 0; i < decrypt.length(); i += 2) {
//                convert[i / 2] = (byte) ((Character.digit(decrypt.charAt(i), 16) << 4)
//                        + Character.digit(decrypt.charAt(i + 1), 16));
//            }
//            sks = new SecretKeySpec(convert, "AES");
//            try {
//                Cipher c = Cipher.getInstance("AES");
//                c.init(Cipher.DECRYPT_MODE, sks);
//                decodedBytes = c.doFinal(selected.getBytes());
//                newMessage = Base64.encodeToString(decodedBytes, Base64.DEFAULT);
//
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//            } catch (InvalidKeyException e) {
//                e.printStackTrace();
//            } catch (NoSuchPaddingException e) {
//                e.printStackTrace();
//            } catch (BadPaddingException e) {
//                e.printStackTrace();
//            } catch (IllegalBlockSizeException e) {
//                e.printStackTrace();
//            }
//
//        }
//        return true;
//    }
}
