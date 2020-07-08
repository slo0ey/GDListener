package com.github.DenFade.gdlistener;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.DenFade.gdlistener.utils.FileStream;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView title;
    private EditText cmd;
    private Button run;

    Messenger mOutputMessenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = (TextView) findViewById(R.id.apptitle);
        cmd = (EditText) findViewById(R.id.cmd);
        run = (Button) findViewById(R.id.run);

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
                mOutputMessenger = new Messenger(iBinder);
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mOutputMessenger = null;
            }
        };

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String command = cmd.getText().toString();
                    Intent intent;
                    if (!command.startsWith("/")) return;
                    switch (command) {
                        case "/help":
                            setAlertDialog("Command List", "working..");
                            break;
                        case "/start":
                            Toast.makeText(getApplicationContext(), "MainActivity: Wait a second..", Toast.LENGTH_SHORT).show();
                            startService(new Intent(MainActivity.this, EventLoopService.class));
                            break;
                        case "/start at FOREGROUND":
                            Toast.makeText(getApplicationContext(), "MainActivity: Wait a second..", Toast.LENGTH_SHORT).show();
                            startForegroundService(new Intent(MainActivity.this, EventLoopService.class));
                            break;
                        case "/withToast true":
                            sendToService(1, 1);
                            break;
                        case "/withToast false":
                            sendToService(1, 0);
                            break;
                        case "/kill":
                            Toast.makeText(getApplicationContext(), "MainActivity: Wait a second..", Toast.LENGTH_SHORT).show();
                            stopService(new Intent(MainActivity.this, EventLoopService.class));
                            break;
                        case "/remove list1":
                            FileStream.remove(FileStream.ROOT_DIR + "awardedList.json");
                            Toast.makeText(getApplicationContext(), "File: awardedList.json deleted!", Toast.LENGTH_SHORT).show();
                            break;
                        case "/remove .loop":
                            FileStream.remove(FileStream.ROOT_DIR + "loop.properties");
                            Toast.makeText(getApplicationContext(), "File: loop.properties deleted!", Toast.LENGTH_SHORT).show();
                            break;
                        case "/view list1":
                            try {
                                String res = FileStream.read(FileStream.ROOT_DIR + "awardedList.json");
                                setAlertDialog("awardedList.json", res);
                            } catch (IOException e) {
                                Toast.makeText(getApplicationContext(), "Oops: Failed to read awardedList.json", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "/view .loop":
                            try {
                                String res = FileStream.read(FileStream.ROOT_DIR + "loop.properties");
                                setAlertDialog("loop.properties", res);
                            } catch (IOException e) {
                                Toast.makeText(getApplicationContext(), "Oops: Failed to read loop.properties", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        default:
                    }
                } catch (Exception e){
                    e.printStackTrace();
                    setAlertDialog("Oops! error occurred", e.toString());
                }
            }
        });
    }

    private boolean sendToService(int... args){
        Message msg = new Message();
        msg.what = args[0];
        msg.arg1 = args[1];
        msg.arg2 = args[2];
        try {
            mOutputMessenger.send(msg);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void setAlertDialog(String title, String content){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title)
                .setMessage(content)
                .show();
    }
}