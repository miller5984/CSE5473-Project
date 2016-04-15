package osu.cse.xuan.freqdetector;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class SentMessages extends AppCompatActivity {
public static ArrayList<String> sentMessages = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sent_messages);
        ListView listOfSent = (ListView) findViewById(R.id.sentList);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.row_sent_list, sentMessages);
        listOfSent.setAdapter(adapter);
    }

}
