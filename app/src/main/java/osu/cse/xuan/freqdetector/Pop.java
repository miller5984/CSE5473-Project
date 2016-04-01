package osu.cse.xuan.freqdetector;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;


public class Pop extends Activity implements View.OnClickListener{


    Button record, send, play, stop;
    TextView boxMessage;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popwindow);


        record = (Button)findViewById(R.id.record);
        stop = (Button)findViewById(R.id.stop);
        send = (Button)findViewById(R.id.send);
        play = (Button)findViewById(R.id.play);
        boxMessage = (TextView)findViewById(R.id.boxmessage);

        stop.setEnabled(false);
        play.setEnabled(false);

        record.setOnClickListener(this);
        stop.setOnClickListener(this);
        play.setOnClickListener(this);
        send.setOnClickListener(this);

        myAudioRecorder = new MediaRecorder();
        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myAudioRecorder.setOutputFile(outputFile);

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
                try {
                    myAudioRecorder.prepare();
                    myAudioRecorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch(IllegalStateException e){
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
                break;

            case R.id.play:
                MediaPlayer m = new MediaPlayer();
                try{
                    m.setDataSource(outputFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try{
                    m.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                m.start();
                Toast.makeText(getApplicationContext(), "Playing Audio",
                        Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
