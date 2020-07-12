package com.github.DenFade.gdlistener;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.DenFade.gdlistener.utils.FileStream;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class MainActivity extends AppCompatActivity {

    private TextView title;
    private EditText cmd;
    private Button run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();
        title = (TextView) findViewById(R.id.apptitle);
        cmd = (EditText) findViewById(R.id.cmd);
        run = (Button) findViewById(R.id.run);

        FileStream.ROOT_DIR = getFilesDir().getAbsolutePath() + "/";

        if(!new File(getFilesDir()+"/loop.properties").exists()){
            String resOption = "delay=30000\n"+
                                "awarded=1\n"+
                                "withToast=0";
            try {
                FileStream.write(getFilesDir()+"/loop.properties", resOption);
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String command = cmd.getText().toString();
                    if (!command.startsWith("/")) return;
                    if(command.endsWith(" ")) command = command.substring(0, command.length()-1);
                    switch (command) {
                        case "/help":
                            setAlertDialog("Command List", String.join("\n\n", getResources().getStringArray(R.array.cmd_help)));
                            break;
                        case "/start at BACKGROUND":
                            Toast.makeText(getApplicationContext(), "MainActivity: Wait a second..", Toast.LENGTH_SHORT).show();
                            startService(new Intent(MainActivity.this, EventLoopService.class));
                            break;
                        case "/start":
                            Toast.makeText(getApplicationContext(), "MainActivity: Wait a second..", Toast.LENGTH_SHORT).show();
                            startForegroundService(new Intent(MainActivity.this, EventLoopService.class));
                            break;
                        case "/setting":
                            Toast.makeText(getApplicationContext(), "MainActivity: Goto setting", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, AppSettingActivity.class);
                            startActivity(intent);
                        case "/withToast true":
                            try{
                                Properties setting = new Properties();
                                setting.load(Files.newBufferedReader(Paths.get(FileStream.ROOT_DIR + "loop.properties")));
                                setting.setProperty("withToast", "1");
                                FileStream.writeAsProperties(FileStream.ROOT_DIR + "loop.properties", setting);
                                Toast.makeText(getApplicationContext(), "Event: withToast=true", Toast.LENGTH_SHORT).show();
                            } catch (IOException e){
                                Toast.makeText(getApplicationContext(), "Event: Failed to change", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "/withToast false":
                            try{
                                Properties setting = new Properties();
                                setting.load(Files.newBufferedReader(Paths.get(FileStream.ROOT_DIR + "loop.properties")));
                                setting.setProperty("withToast", "0");
                                FileStream.writeAsProperties(FileStream.ROOT_DIR + "loop.properties", setting);
                                Toast.makeText(getApplicationContext(), "Event: withToast=false", Toast.LENGTH_SHORT).show();
                            } catch (IOException e){
                                Toast.makeText(getApplicationContext(), "Event: Failed to change", Toast.LENGTH_SHORT).show();
                            }
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

    private void setAlertDialog(String title, String content){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title)
                .setMessage(content)
                .show();
    }
    private void createNotificationChannel(){
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        NotificationChannel channelForService = new NotificationChannel(
                getString(R.string.service_channel_id),
                getString(R.string.service_channel_name),
                NotificationManager.IMPORTANCE_DEFAULT
        );
        channelForService.setDescription(getString(R.string.service_channel_desc));
        notificationManager.createNotificationChannel(channelForService);
        List<NotificationChannelInfo> channelInfos = new ArrayList<>();
        channelInfos.add(new NotificationChannelInfo(
                getString(R.string.awarded_event_channel_id),
                getString(R.string.awarded_event_channel_name)
        ));
        channelInfos.add(new NotificationChannelInfo(
                getString(R.string.followed_event_channel_id),
                getString(R.string.followed_event_channel_name)
        ));
        channelInfos.add(new NotificationChannelInfo(
                getString(R.string.timely_event_channel_id),
                getString(R.string.timely_event_channel_name)
        ));
        for(NotificationChannelInfo info : channelInfos) {
            Log.d("NCI", info.toString());
            NotificationChannel channel = new NotificationChannel(
                    info.getId(),
                    info.getName(),
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            String desc = info.getDescription();
            if(desc!=null){
                channel.setDescription(desc);
            }
            notificationManager.createNotificationChannel(channel);
        }


    }
    private class NotificationChannelInfo {
        private String id;
        private String name;
        private String description = null;
        NotificationChannelInfo(String id, String name, String description){
            this.id = id;
            this.name = name;
            this.description = description;
        }
        NotificationChannelInfo(String id, String name){
            this.id = id;
            this.name = name;
        }
        public String getId(){
            return id;
        }
        public String getName(){
            return name;
        }
        public String getDescription(){
            return description;
        }
        @NotNull
        @Override
        public String toString(){
            return "{id="+this.id+", name="+this.name+", description="+this.description+"}";
        }
    }
}