package com.sms.demo.antithreft;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.spark.submitbutton.SubmitButton;

import junit.runner.Version;

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
    ImageView imageView;
    boolean isClicked = false;
    private String TAG = "MyTag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //checkPermissions();

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M) {
            if(!Settings.System.canWrite(getApplicationContext())){
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + this.getPackageName()));
                startActivity(intent);
            }
        }
        else
            checkPermissions();


        submitButton = (SubmitButton)findViewById(R.id.submit);
        imageView = (ImageView)findViewById(R.id.imageView);
        XValue = false;
        YValue = false;
        ZValue = false;



        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        assert sensorManager != null;
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
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
                    imageView.setImageResource(R.drawable.y);

                    android.provider.Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT,5000);

                }else{
                    submitButton.setText("Disableing . . ");
                    imageView.setImageResource(R.drawable.n);
                    if(mediaPlayer!=null && mediaPlayer.isPlaying())
                             mediaPlayer.stop();
                    isClicked = false;

                    android.provider.Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT,50000*24);
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

    private boolean checkPermissions() {
        Log.i(TAG, "checkPermissions: "+"WORKIGN");
        if(ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_SETTINGS)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_SETTINGS},0);
        }else
           return true;
        return false;
    }

    //@Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//       if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
//           Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//           startActivity(intent);
//       }else{
//           checkPermissions();
//       }
//
//    }

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
                //mediaPlayer.setLooping(true);
                mediaPlayer.setVolume(100,100);
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
