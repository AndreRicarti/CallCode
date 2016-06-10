package com.example.bcf_andre.callcode;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int speakerPhone = 0;
    int volumeMaximum = 0;
    int minimizeActivity = 0;
    //
    Button btnEmergency, btnEmergency2, btnContactCustom;
    //
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);
        //
        btnEmergency = (Button) findViewById(R.id.btnEmergency);
        btnEmergency2 = (Button) findViewById(R.id.btnEmergency2);
        btnContactCustom = (Button) findViewById(R.id.btnContactCustom);
        //
        context = getBaseContext();
        //
        TelephonyManager TelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        TelephonyMgr.listen(new TeleListener(), PhoneStateListener.LISTEN_CALL_STATE);
        //
        inicializaAcao();
    }

    private void inicializaAcao() {
        btnEmergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call("190");
                volumeMute(context);
                volumeMaximum = 1;
                minimizeActivity = 1;
            }
        });
        //
        btnEmergency2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call("+55 11 97202-3780");
                volumeMute(context);
                volumeMaximum = 1;
                minimizeActivity = 1;
            }
        });
        //
        btnContactCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call("+55 11 94312-2070");
                speakerPhone = 1;
            }
        });
    }

    class TeleListener extends PhoneStateListener {
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //
                    if (volumeMaximum == 1){
                        volumeMaximum(context);
                        volumeMaximum = 0;
                    }
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    try {
                        Thread.sleep(250);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    //
                    if (speakerPhone == 1) {
                        speakerPhone(context);
                        speakerPhone = 0;
                    }
                    //
                    if (minimizeActivity == 1){
                        minimizeActivity();
                        minimizeActivity = 0;
                    }
                    break;
            }
        }
    }

    private void call(String telephone) {
        Intent in=new Intent(Intent.ACTION_CALL);
        try {
            in.setData(Uri.parse("tel:" + telephone));
            startActivity(in);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(),"yourActivity is not founded",Toast.LENGTH_SHORT).show();
        }
    }

    public void speakerPhone(Context context){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setMode(AudioManager.MODE_IN_CALL);
        audioManager.setSpeakerphoneOn(true);
    }

    public void volumeMute(Context context){
        AudioManager am =(AudioManager) context.getSystemService(AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_LOWER, 0);
    }

    public void volumeMaximum(Context context){
        AudioManager am =(AudioManager) context.getSystemService(AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_UNMUTE, 0);
    }

    public void minimizeActivity(){
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        this.startActivity(startMain);
    }
}
