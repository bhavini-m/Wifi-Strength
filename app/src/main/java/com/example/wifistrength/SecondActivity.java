package com.example.wifistrength;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SecondActivity extends AppCompatActivity {


    String info;
    Button backButton;
    int flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        flag=0;
        backButton= findViewById(R.id.backBtn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SecondActivity.this, MainActivity.class));
            }
        });

        final Context context=this;
        final Handler handler=new Handler();
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                getWifiInfo(context);
                saveFile();
                Log.d("Handlers", "Called on main thread");
                // Repeat this the same runnable code block again another 2 seconds
                // 'this' is referencing the Runnable object
                handler.postDelayed(this, 60000);
            }
        };
// Start the initial runnable task by posting through the handler
        handler.post(runnableCode);
    }

    public void getWifiInfo(Context context) {
        WifiManager wifiManager = (WifiManager)
                getApplicationContext().getSystemService(getApplicationContext().WIFI_SERVICE);

        if(wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if(String.valueOf(wifiInfo.getSupplicantState()).equals("COMPLETED")){

                String ssid="SSID: "+wifiInfo.getSSID();

                int  rssid = wifiInfo.getRssi();
                String rssidval="RSSID: "+rssid+" db";

                String bssid = "BSSID: "+wifiInfo.getBSSID();

                int ip = wifiInfo.getIpAddress();
                String ipaddress = "Ip Address: "+ Formatter.formatIpAddress(ip);

                int linkspeed = wifiInfo.getLinkSpeed();
                String linksp = "Link Speed: "+linkspeed;

                String signal_strength;
                rssid = 2*(rssid + 100);
                signal_strength = "Signal Strength: " + rssid;

                info = ssid +"\n"+rssidval+"\n"+ signal_strength +"\n"+ bssid +"\n"+ ipaddress + "\n" +linksp;

                ((TextView) findViewById(R.id.Txt1)).setText(ssid);
                ((TextView) findViewById(R.id.Txt2)).setText(rssidval);
                ((TextView) findViewById(R.id.Txt3)).setText(signal_strength);
                ((TextView) findViewById(R.id.Txt4)).setText(bssid);
                ((TextView) findViewById(R.id.Txt5)).setText(linksp);

            } //if ends
        }

    } //getWifiInfo ends

    private void saveFile()
    {
        File file = new File(getExternalFilesDir(null), "timeStamp.txt");
        if(flag==0) {
            Toast.makeText(this, file.getAbsolutePath(), Toast.LENGTH_LONG).show();
            flag=1;
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        String currentTime = simpleDateFormat.format(c.getTime());
        try {
            // Creates a file in the primary external storage space of the current application.
            // If the file does not exists, it is created.
            File testFile = new File(this.getExternalFilesDir(null), "timeStamp.txt");
            if (!testFile.exists())
                testFile.createNewFile();
            // Adds a line to the file
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(testFile, true
                    /*append*/));
            bufferedWriter.write(currentTime + "\n" + info + "\n \n");
            bufferedWriter.close();

        } catch (IOException e) {
            Log.e("ReadWriteFile", "Unable to write to the TestFile.txt file.");
        }
    }
}
