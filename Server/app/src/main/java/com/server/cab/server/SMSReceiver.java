package com.server.cab.server;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import services.Message;

public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras();
        SmsMessage[] sms_m = null;
        String sms_str = "";

        if (bundle != null) {

            // Get the SMS message
            Object[] pdus = (Object[]) bundle.get("pdus");
            sms_m = new SmsMessage[pdus.length];

            String number = "";
            String sms = "";
            for (int i = 0; i < sms_m.length; i++) {
                sms_m[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                number = sms_m[i].getOriginatingAddress();
                sms += sms_m[i].getMessageBody().toString();
            }

            // Start Application's  Service
            Intent service = new Intent(context, Message.class);
            service.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            service.putExtra("number", number);
            service.putExtra("sms",sms);
            context.startService(service);

            /*
            // Start Application's  MainActivty activity
            Intent smsIntent = new Intent(context, Main.class);
            smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            smsIntent.putExtra("number", number);
            smsIntent.putExtra("sms", sms);
            context.startActivity(smsIntent);
            */

        }
    }
}
