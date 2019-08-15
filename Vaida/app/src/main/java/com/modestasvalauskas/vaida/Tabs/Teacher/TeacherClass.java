package com.modestasvalauskas.vaida.Tabs.Teacher;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.modestasvalauskas.vaida.Fassade.Machine;
import com.modestasvalauskas.vaida.R;
import com.modestasvalauskas.vaida.Tabs.adapter.KBFile;

import java.util.List;

import aima.core.logic.fol.inference.FOLBCAsk;
import aima.core.logic.fol.inference.InferenceResult;

/**
 * Created by Modestas Valauskas on 19.09.2016.
 */
public class TeacherClass extends Fragment {

    private KBFile kbFile;

    public KBFile getKbFile() {
        if(kbFile == null) {
            kbFile = KBFile.getLoadedKBFile(getContext());
        }
        return kbFile;
    }

    public List<String> getConstants() {
        return getKbFile().getConstants();
    }

    public List<String> getPredicates() {
        return getKbFile().getPredicates();
    }

    public InferenceResult ask(String query) {
        Machine machine = new Machine(getKbFile());
        return machine.ask(query, new FOLBCAsk());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.query_tab, container, false);

        return view;
    }



}
