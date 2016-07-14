package com.server.cab.server;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

public class Main extends AppCompatActivity {

    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Fragment fm = new Home();
        FragmentManager fmManager=getSupportFragmentManager();
        FragmentTransaction transaction = fmManager.beginTransaction();
        transaction.replace(R.id.layout, fm);
        transaction.commit();
    }

}
