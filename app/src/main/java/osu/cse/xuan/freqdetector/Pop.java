package osu.cse.xuan.freqdetector;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class Pop extends Activity implements View.OnClickListener{


    Button record, send, restart, pause;
    TextView boxmessage;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);


        record = (Button)findViewById(R.id.record);
        pause = (Button)findViewById(R.id.pause);
        send = (Button)findViewById(R.id.send);
        restart = (Button)findViewById(R.id.restart);
        boxmessage = (TextView)findViewById(R.id.boxmessage);


        record.setOnClickListener(this);
        pause.setOnClickListener(this);
        restart.setOnClickListener(this);
        send.setOnClickListener(this);


        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.25));


    }

    @Override
    public void onClick(View v){

        switch(v.getId()){
            case R.id.record:
                Toast.makeText(getApplicationContext(), "You are recording!",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.pause:
                Toast.makeText(getApplicationContext(), "You have paused your recording!",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.send:
                Toast.makeText(getApplicationContext(), "You are sending your message!",
                        Toast.LENGTH_SHORT).show();
                break;

            case R.id.restart:
                Toast.makeText(getApplicationContext(), "You are restarting your recording!",
                        Toast.LENGTH_SHORT).show();

                break;
        }
    }
}
