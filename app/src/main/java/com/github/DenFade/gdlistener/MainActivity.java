package com.github.DenFade.gdlistener;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.DenFade.gdlistener.utils.FileStream;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView title;
    private Button start;
    private Button kill;
    private Button setting;

    private IEventLoopService binder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = (TextView) findViewById(R.id.apptitle);
        start = (Button) findViewById(R.id.loop_start);
        kill = (Button) findViewById(R.id.loop_kill);
        setting = (Button) findViewById(R.id.setting);

        FileStream.ROOT_DIR = getFilesDir().getAbsolutePath() + "/";

        if(!new File(getFilesDir()+"/loop.properties").exists()){
            String resOption = "delay=30000\n"+
                                "awarded=1";
            try {
                FileStream.write(getFilesDir()+"/loop.properties", resOption);
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        ServiceConnection connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                binder = IEventLoopService.Stub.asInterface(iBinder);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {

            }
        };

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "MainActivity: Loop start!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, EventLoopService.class);
                startService(intent);
            }
        });

        kill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "MainActivity: Loop kill!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(MainActivity.this, EventLoopService.class);
                stopService(intent);
            }
        });

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String json = FileStream.read(FileStream.ROOT_DIR+"awardedList.json");
                    Log.d("JSON TEST", json);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}