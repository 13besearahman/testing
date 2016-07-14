package com.client.cab.clientapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class Main extends AppCompatActivity implements View.OnClickListener {

    Button help;
    ListView help_data;
    LinearLayout templateFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        help = (Button) findViewById(R.id.help);
        help.setOnClickListener(this);

        templateFields = (LinearLayout) findViewById(R.id.templateFields);

        help_data = (ListView) findViewById(R.id.help_data);
        help_data.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView t = (TextView) view.findViewById(R.id.textView);
                String text = t.getText().toString();
                showToast(text);
                sendSMS("help "+text);
            }
        });

        BroadcastReceiver broadcastReceiver = null;
        broadcastReceiver =  new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle b = intent.getExtras();
                String message = b.getString("sms");

                String[] namecodes = message.split("\n");
                final ArrayList<String> list = new ArrayList<String>();
                list.addAll(Arrays.asList(namecodes));


                if(list.get(0).equalsIgnoreCase("template")){
                    help_data.setAdapter(null);
                    templateFields.removeAllViews();

                    //Dynamically Generated Fields
                    final TextView[] tagView = new TextView[list.size()-1];
                    final EditText[] tagValue = new EditText[list.size()-1];

                    //Adding Dynamic Fields of Template
                    for(int i=1 ; i<list.size() ; i++){
                        String tag = list.get(i).substring(0,list.get(i).indexOf(':'));
                        if(tag!="" || tag!=null)
                            tag = tag.substring(0,1).toUpperCase() + tag.substring(1);

                        tagView[i-1] = new TextView(getBaseContext());
                        tagView[i-1].setTextColor(Color.BLACK);
                        tagView[i-1].setTextSize(25);
                        tagView[i-1].setText(tag);

                        tagValue[i-1] = new EditText(getBaseContext());
                        tagValue[i-1].setSingleLine();
                        tagValue[i-1].setTextColor(Color.BLACK);
                        tagValue[i-1].setHint(tag);

                        templateFields.addView(tagView[i-1]);
                        templateFields.addView(tagValue[i-1]);
                    }

                    Button send = new Button(getBaseContext());
                    send.setText("Send");
                    send.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String reply = "Reply";
                            for(int i=0 ; i<list.size()-1;i++){
                                reply += "\n"+tagView[i].getText()+":";
                                reply += tagValue[i].getText();
                            }
                            Log.e("Data",reply);
                            sendSMS(reply);
                        }
                    });
                    templateFields.addView(send);
                }
                else if(list.get(0).equalsIgnoreCase("reply")){
                    showToast("Own Reply");
                }
                else {
                    if (list.size() > 1)
                        list.remove(0);
                    ArrayAdapter adapter = new ArrayAdapter<String>(context, R.layout.listview, R.id.textView, list);
                    templateFields.removeAllViews();
                    help_data.setAdapter(adapter);
                }
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("smsReceive"));
    }

    @Override
    public void onClick(View e) {
        if(e == help){
            sendSMS("help");
        }
    }

    private void sendSMS( String message){
        String number = "03330627462" ;
        SmsManager smsManager = SmsManager.getDefault();
        ArrayList<String> parts = smsManager.divideMessage(message);
        smsManager.sendMultipartTextMessage(number, null, parts, null, null);
        Toast.makeText(this,"Sending SMS",Toast.LENGTH_SHORT).show();
    }

    public void showToast(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
}
