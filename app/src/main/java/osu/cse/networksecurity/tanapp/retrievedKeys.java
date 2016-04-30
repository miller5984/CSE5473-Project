package osu.cse.networksecurity.tanapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class retrievedKeys extends AppCompatActivity {

    ListView recKeysList;
    public static ArrayList<String> myKeysList = new ArrayList<>();
    private String key, keySave;
    SharedPreferences sharedPreferences;
    private static final String MyPrefs = "myprefs", pk = "pkey";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieved_keys);

        sharedPreferences = getSharedPreferences(MyPrefs, Context.MODE_PRIVATE);
        recKeysList = (ListView) findViewById(R.id.listViewKeys);

        recKeysList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                key = ((TextView)view).getText().toString();
                keySave = key.substring(0,1) + key.substring(9,13) + key.substring(21,33) + key.substring(41,54)+
                        key.substring(62,64);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(pk, keySave);
                editor.apply();
                Toast.makeText(retrievedKeys.this, "Key Saved", Toast.LENGTH_SHORT).show();
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myKeysList);
        recKeysList.setAdapter(adapter);

        //registerForContextMenu(recKeysList);
        adapter.notifyDataSetChanged();
    }
}
