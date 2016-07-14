package com.server.cab.cargerserver;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import code.AddNewTemplate;


public class Tab1 extends Fragment implements View.OnClickListener{

    private Button add;
    private LinearLayout templates;
    private AddNewTemplate addNewTemplate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View tab = inflater.inflate(R.layout.tab1, container, false);

        add = (Button) tab.findViewById(R.id.button);
        add.setOnClickListener(this);

        templates = (LinearLayout) tab.findViewById(R.id.templates);

        addNewTemplate = new AddNewTemplate();
        addNewTemplate.setContext(getContext());
        addNewTemplate.setTemplates(templates);
        addNewTemplate.loadData();

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewTemplate.addNewTemplate();
            }
        });

        return tab;
    }

    @Override
    public void onClick(View view) {
        if(view==add){
            addNewTemplate.addNewTemplate();
        }
    }

}

//Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();