package com.server.cab.server;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Home extends Fragment implements View.OnClickListener {

    private Button add;
    private Button more;
    private LinearLayout templates;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        final View v=inflater.inflate(R.layout.home, container, false);

        add = (Button) v.findViewById(R.id.button);
        add.setOnClickListener(this);

        templates = (LinearLayout) v.findViewById(R.id.templates);
        loadData();
        return v;
    }

    public void loadData(){
        File dir = new File(Environment.getExternalStorageDirectory()+ File.separator+"Templates");
        File[] f = dir.listFiles();

        for(int i=0 ; i<f.length ; i++) {
            View space = new View(getContext());
            space.setLayoutParams(new LinearLayout.LayoutParams(20,20));
            templates.addView(space);
            String contents = "";
            try {
                contents = new Scanner(f[i]).useDelimiter("\\A").next();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            TextView t = new TextView(getContext());
            t.setPadding(10,10,10,10);
            t.setBackgroundColor(Color.rgb(64,224,208));
            t.setText( i+1+". "+f[i].getName()+"\n--------------------\n"+contents);
            templates.addView(t);
        }
    }

    @Override
    public void onClick(View view) {
        final Context context = getActivity().getBaseContext();
        if(view==add){

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Add new Template");
            builder.setCancelable(false);
            FrameLayout frameView = new FrameLayout(getContext());
            builder.setView(frameView);

            final AlertDialog alertDialog = builder.create();
            LayoutInflater inflater = alertDialog.getLayoutInflater();
            final View dialoglayout = inflater.inflate(R.layout.dialog, frameView);

            final EditText title = (EditText) dialoglayout.findViewById(R.id.title_text);
            final EditText code = (EditText) dialoglayout.findViewById(R.id.code_text);
            final EditText data = (EditText) dialoglayout.findViewById(R.id.field1);

            final LinearLayout fields = (LinearLayout) dialoglayout.findViewById(R.id.fields);

            more = (Button) dialoglayout.findViewById(R.id.more);
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

                    EditText text = new EditText(alertDialog.getContext());
                    text.setSingleLine();
                    text.setLayoutParams(lp);
                    text.setHint("Enter Field Name");

                    //TextInputLayout textInputLayout = new TextInputLayout(getContext());
                    //textInputLayout.setLayoutParams(lp);
                    //textInputLayout.addView(text);

                    fields.addView(text);
                }
            });

            Button cancel = (Button) dialoglayout.findViewById(R.id.cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                }
            });

            Button add = (Button) dialoglayout.findViewById(R.id.add);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(title.getText().toString().equals("")||code.getText().toString().equals("")||data.getText().toString().equals("")) {
                        showToast("Some field is empty");
                    }else {
                        String allData = "";

                        ViewGroup viewGroup = (ViewGroup) dialoglayout.findViewById(R.id.fields);
                        showLog(viewGroup.getChildCount()+"");

                        EditText[] allFields = new EditText[viewGroup.getChildCount()];
                        int count = viewGroup.getChildCount();
                        for (int i = 0; i < count; i++) {
                            if (viewGroup.getChildAt(i) instanceof EditText) {
                                EditText edittext = (EditText) viewGroup.getChildAt(i);
                                allFields[i]=edittext;
                            }
                        }
                        for(int i = 0; i < allFields.length; i++){
                            allData += "\n"+allFields[i].getText().toString()+":";
                        }

                        writeFile(title.getText().toString(), code.getText().toString(), allData);
                        alertDialog.dismiss();
                    }
                }
            });
            alertDialog.show();
        }
    }

    public void writeFile(String title,String code,String data){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            showToast("No SD card Present, No data Saved");
        } else {
            String dir = Environment.getExternalStorageDirectory()+ File.separator+"Templates";
            File folder = new File(dir);
            folder.mkdirs();

            File file = new File(dir, title+"."+code);
            try {
                FileOutputStream stream = new FileOutputStream(file);
                try{
                    String save = "Template"+data;
                    stream.write(save.getBytes());
                    showToast("Template Saved");
                    templates.removeAllViews();
                    loadData();
                }catch (IOException e) {
                    showToast("Can't write in file, Permission Denied, No Data Save");
                }finally {
                    try {
                        stream.close();
                    } catch (IOException e) {}
                }
            } catch (FileNotFoundException e) {
                showToast("File Not Found, No Data Save");
            }
        }
    }
    public void showToast(String s){
        Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
    }
    public void showLog(String s){
        Log.e("Some Info" , s);
    }

}