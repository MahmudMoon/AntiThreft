package com.sms.demo.antithreft;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.spark.submitbutton.SubmitButton;

import pl.bclogic.pulsator4droid.library.PulsatorLayout;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    TextView tv_X,tv_Y,tv_Z;
    Sensor accelerometer;
    SensorManager sensorManager;
    Button AlarmBtn,DisableBtn;

    double x = 0.0,y=0.0,z=0.0;
    double savedX,savedY,saveZ;
    boolean XValue,YValue,ZValue;
    MediaPlayer mediaPlayer;
    SubmitButton submitButton;
    boolean isClicked = false;
    PulsatorLayout pulsator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        submitButton = (SubmitButton)findViewById(R.id.submit);
        XValue = false;
        YValue = false;
        ZValue = false;


        pulsator = (PulsatorLayout) findViewById(R.id.pulsator);



        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER  );
        sensorManager.registerListener( this, accelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isClicked) {
                    XValue = checkX();
                    YValue = checkY();
                    ZValue = checkZ();
                    isClicked = true;
                    submitButton.setText("Enableing . . ");
                    pulsator.setCount(4);
                    pulsator.setDuration(7000);
                    pulsator.setInterpolator(PulsatorLayout.INTERP_LINEAR);
                    pulsator.start();

                    android.provider.Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT,5000);

                }else{
                    submitButton.setText("Disableing . . ");
                    if(mediaPlayer!=null && mediaPlayer.isPlaying())
                             mediaPlayer.stop();
                    isClicked = false;
                    pulsator.stop();
                }

//                Log.d("TAG","\nX : "+x+"\n SAVEDX :"+ savedX + "\nY : " +y  +"\nSavedY " + savedY + "\nZ : "+z+ "\nSAVEDZ " + saveZ);
            }
        });

//        submitButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(isClicked) {
//                    mediaPlayer.stop();
//                    isClicked = false;
//                }
//            }
//        });
    }

    private boolean checkX() {
        savedX = x<0.0?x*=-1:x;
        if(savedX!=0.0){
            return true;
        }else
            return false;
    }

    private boolean checkY() {
        savedY = y<0.0?y*=-1:y;
        if(savedY!=0.0){
            return true;
        }else
            return false;
    }

    private boolean checkZ() {
        saveZ = z<0.0?z*=-1:z;
        if(saveZ!=0.0){
            return true;
        }else
            return false;

    }




    public void onSensorChanged(SensorEvent event) {

        x = event.values[0];
        y = event.values[1];
        z = event.values[2];

        if(XValue || YValue || ZValue){

            if(x<0.0){
                x*=-1;
            }

            if(y<0.0){
                y*=-1;
            }

            if(z<0.0){
                z*=-1;
            }

            Log.d("TAG","\nX : "+x+"\n SAVEDX :"+ savedX + "\nY : " +y  +"\nSavedY " + savedY + "\nZ : "+z+ "\nSAVEDZ " + saveZ);

            if((Math.abs(savedX-x)>1.0) || (Math.abs(savedY-y)>1.0) || (Math.abs(saveZ-z)>1.0) ){
                mediaPlayer = MediaPlayer.create(this,R.raw.plice);
                mediaPlayer.start();

                XValue = false;
                YValue = false;
                ZValue = false;

                Log.d("TAG","\nX : "+x+"\n SAVEDX :"+ savedX + "\nY : " +y  +"\nSavedY " + savedY + "\nZ : "+z+ "\nSAVEDZ " + saveZ);

            }
        }





    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
