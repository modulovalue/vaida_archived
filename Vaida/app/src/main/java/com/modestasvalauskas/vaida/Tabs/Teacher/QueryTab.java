package com.modestasvalauskas.vaida.Tabs.Teacher;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.modestasvalauskas.vaida.Fassade.Machine;
import com.modestasvalauskas.vaida.MainActivity;
import com.modestasvalauskas.vaida.R;
import com.modestasvalauskas.vaida.Tabs.adapter.KBFile;

/**
 * Created by Modestas Valauskas on 13.09.2016.
 */

public class QueryTab extends TeacherClass {

    public Button clearBtn, goBtn;
    public TextView showTextView;
    public EditText editTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.query_tab, container, false);

        clearBtn = (Button) view.findViewById(R.id.clearBtn);
        goBtn = (Button) view.findViewById(R.id.goBtn);
        showTextView = (TextView) view.findViewById(R.id.textView);
        editTextView = (EditText) view.findViewById(R.id.editText);

        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTextView.setText("");
            }
        });

        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    MainActivity.hideSoftKeyboard(getActivity());
                } catch (Exception ignored) {}
                try {
                    new Machine(KBFile.getLoadedKBFile(getContext())).ask(editTextView.getText().toString(), showTextView);
                } catch (Exception e) {
                    showTextView.append(e.getLocalizedMessage());
                }

            }
        });

        return view;
    }

}