package osu.cse.xuan.freqdetector;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class retrievedKeys extends AppCompatActivity {

    ListView recKeysList;
    public static ArrayList<String> myKeysList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieved_keys);

        recKeysList = (ListView) findViewById(R.id.listViewKeys);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, myKeysList);
        recKeysList.setAdapter(adapter);

        registerForContextMenu(recKeysList);
        adapter.notifyDataSetChanged();
    }
}
