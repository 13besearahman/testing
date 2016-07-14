package services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Message extends Service {

    public Message() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle data = intent.getExtras();
        String number = data.getString("number");
        String sms = data.getString("sms");
        sms = sms.trim();

        // "Help" Message
        if(sms.equalsIgnoreCase("help")){
            sendSMS(number, "Reply us with the Code or Name Below:\n"+searchTemplatesNames());
        }

        // "Help Name" Message
        else if (!sms.equalsIgnoreCase("") && sms.length() > 5){
            if(sms.substring(0,4).equalsIgnoreCase("help")) {
                sms = sms.substring(5);
                sendSMS(number, openTemplate(sms));
            }
        }

        // "Template" Message
        else if(!sms.equalsIgnoreCase("") && sms.length() > 6){
            if(sms.substring(0,5).equalsIgnoreCase("reply")) {
                showToast("Reply From Client");
                String[] lines = sms.split("\n");
                JSONObject tempData = new JSONObject();
                for(int i=1 ; i<lines.length ; i++){
                    try {
                        tempData.put(lines[i].split(":")[0],lines[i].split(":")[1]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Iterator<String> keys = tempData.keys();
                while( keys.hasNext() ) {
                    String key = (String)keys.next();
                    try {
                        Log.e(key," :::: "+tempData.getString(key));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        onDestroy();
        return START_STICKY;
    }

    public String openTemplate(String name){
        String result = "";
        File dir = new File(Environment.getExternalStorageDirectory()+ File.separator+"Templates");
        File[] f = dir.listFiles();

        for(int i=0 ; i<f.length ; i++) {
            String naam = f[i].getName();

            if( name.equalsIgnoreCase(naam.split("\\.")[0]) || name.equalsIgnoreCase(naam.split("\\.")[1]) ){
                String contents = "";
                try {
                    contents = new Scanner(f[i]).useDelimiter("\\A").next();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                return contents;
            }
        }
        return "No Such Template Present";
    }

    public String searchTemplatesNames(){
        String result = "";
        File dir = new File(Environment.getExternalStorageDirectory()+ File.separator+"Templates");
        File[] f = dir.listFiles();

        for(int i=0 ; i<f.length ; i++) {
            String name = f[i].getName();
            result += name.substring(0,name.indexOf('.')) +"\n"+ name.substring(name.indexOf('.')+1,name.length())+"\n";
        }
        Log.e("String ",result);
        return result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void sendSMS(String number, String message){
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(message);
        smsManager.sendMultipartTextMessage(number, null, parts, null, null);
    }

    public void showToast(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}
