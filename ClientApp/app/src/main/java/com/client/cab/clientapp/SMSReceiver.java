package com.client.cab.clientapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

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

            if(number.equals("03330627462")||number.equals("+923330627462")||number.equals("923330627462")) {
                Intent i = new Intent("smsReceive");
                i.putExtra("sms", sms);
                context.sendBroadcast(i);
            }
        }
    }
}

