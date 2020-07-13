package com.github.DenFade.gdlistener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.DenFade.gdlistener.utils.FileStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ConstraintLayout background;
    private TextView title;
    private EditText cmd;
    private Button run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        createNotificationChannel();

        background = (ConstraintLayout) findViewById(R.id.main_background);
        title = (TextView) findViewById(R.id.apptitle);
        cmd = (EditText) findViewById(R.id.cmd);
        run = (Button) findViewById(R.id.run);

        FileStream.ROOT_DIR = getFilesDir().getAbsolutePath() + "/";

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
                        case "/title":
                            String r = String.format("%02X", (int) (Math.random() * 255));
                            String g = String.format("%02X", (int) (Math.random() * 255));
                            String b = String.format("%02X", (int) (Math.random() * 255));
                            title.setTextColor(Color.parseColor("#" + r + g + b));
                            Toast.makeText(getApplicationContext(), "Splash! #" + r + g + b, Toast.LENGTH_SHORT).show();
                            break;
                        case "/bg":
                            String r2 = String.format("%02X", (int) (Math.random() * 255));
                            String g2 = String.format("%02X", (int) (Math.random() * 255));
                            String b2 = String.format("%02X", (int) (Math.random() * 255));
                            background.setBackgroundColor(Color.parseColor("#" + r2 + g2 + b2));
                            Toast.makeText(getApplicationContext(), "Splash! #" + r2 + g2 + b2, Toast.LENGTH_SHORT).show();
                            break;
                        case "/remove old":
                            FileStream.remove(FileStream.ROOT_DIR + "awardedList.json");
                            FileStream.remove(FileStream.ROOT_DIR + "loop.properties");
                            Toast.makeText(getApplicationContext(), "File: old data deleted!", Toast.LENGTH_SHORT).show();
                            break;
                        case "/start":
                            Toast.makeText(getApplicationContext(), "MainActivity: Wait a second..", Toast.LENGTH_SHORT).show();
                            startForegroundService(new Intent(MainActivity.this, EventLoopService.class));
                            break;
                        case "/setting":
                            Toast.makeText(getApplicationContext(), "MainActivity: Goto setting", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, AppSettingActivity.class);
                            startActivity(intent);
                            break;
                        case "/kill":
                            Toast.makeText(getApplicationContext(), "MainActivity: Wait a second..", Toast.LENGTH_SHORT).show();
                            stopService(new Intent(MainActivity.this, EventLoopService.class));
                            break;
                        case "/remove awarded":
                            FileStream.remove(FileStream.ROOT_DIR + "awardedList.txt");
                            Toast.makeText(getApplicationContext(), "File: awardedList deleted!", Toast.LENGTH_SHORT).show();
                            break;
                        case "/remove followed":
                            FileStream.remove(FileStream.ROOT_DIR + "followedList.txt");
                            Toast.makeText(getApplicationContext(), "File: followedList deleted!", Toast.LENGTH_SHORT).show();
                            break;
                        case "/view awarded":
                            try {
                                String res = FileStream.read(FileStream.ROOT_DIR + "awardedList.txt");
                                setAlertDialog("awardedList", res);
                            } catch (IOException e) {
                                Toast.makeText(getApplicationContext(), "Oops: Failed to read awardedList", Toast.LENGTH_SHORT).show();
                            }
                            break;
                        case "/view followed":
                            try {
                                String res = FileStream.read(FileStream.ROOT_DIR + "followedList.txt");
                                setAlertDialog("followedList", res);
                            } catch (IOException e) {
                                Toast.makeText(getApplicationContext(), "Oops: Failed to read followedList", Toast.LENGTH_SHORT).show();
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

        @NonNull
        @Override
        public String toString(){
            return "[id="+this.id+", name="+this.name+", description="+this.description+"]";
        }
    }
}