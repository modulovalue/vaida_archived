package com.modestasvalauskas.vaida.Tabs.ChordProgressionTransposer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.modestasvalauskas.vaida.R;
import com.modestasvalauskas.vaida.pianoview.Piano;

import java.util.ArrayList;
import java.util.List;

public class CPTMainTabFragment extends Fragment {

    public List<Piano> pianos = new ArrayList<Piano>();


    private Piano.PianoKeyListener onPianoKeyPress =
            new Piano.PianoKeyListener() {
                @Override
                public void keyPressed(int id, int action) {
                    pianoToFile();
                }
            };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cptpiano_tab, container, false);
        initPianoViews(view);
        initShiftButtonsListener(view);
        fileToPianos();
        return view;
    }

    public void initPianoViews(View view) {
        if(pianos.size() == 0) {
            for(int i = 0; i < 4; i++) {
                Piano piano = new Piano(getContext(), null, 0, 48);
                piano.setPianoKeyListener(onPianoKeyPress);
                pianos.add(piano);
            }
        } else {
            for (Piano piano: pianos) { ((LinearLayout) piano.getParent()).removeView(piano);}
        }
        ((LinearLayout) view.findViewById(R.id.pianoview1)).addView(pianos.get(0));
        ((LinearLayout) view.findViewById(R.id.pianoview2)).addView(pianos.get(1));
        ((LinearLayout) view.findViewById(R.id.pianoview3)).addView(pianos.get(2));
        ((LinearLayout) view.findViewById(R.id.pianoview4)).addView(pianos.get(3));
    }

    public void initShiftButtonsListener(View view) {
        (view.findViewById(R.id.buttonm7)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Piano.shiftPianosBySemitone(pianos, -7);
            }
        });
        (view.findViewById(R.id.buttonm5)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Piano.shiftPianosBySemitone(pianos, -5);
            }
        });
        (view.findViewById(R.id.buttonm3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Piano.shiftPianosBySemitone(pianos, -3);
            }
        });
        (view.findViewById(R.id.buttonm1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Piano.shiftPianosBySemitone(pianos, -1);
            }
        });
        (view.findViewById(R.id.buttonp1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Piano.shiftPianosBySemitone(pianos, 1);
            }
        });
        (view.findViewById(R.id.buttonp3)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Piano.shiftPianosBySemitone(pianos, 3);
            }
        });
        (view.findViewById(R.id.buttonp5)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Piano.shiftPianosBySemitone(pianos, 5);
            }
        });
        (view.findViewById(R.id.buttonp7)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Piano.shiftPianosBySemitone(pianos, 7);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            System.out.println("VISIBLE DING");
            if(getContext() != null) {
                fileToPianos();
            }
        }
        else {
        }
    }

    public void fileToPianos() {
        CPTFileData fileData = CPTSharedPreference.getLoadedFile(getContext());
        System.out.println("chords: " + fileData.getChords());

        for(int i = 0; i < pianos.size(); i++) {
            pianos.get(i).setKeysToPressed(fileData.getChords().get(i));
        }
    }

    public void pianoToFile() {
        ArrayList<ArrayList<Integer>> arrayLists = new ArrayList<>();
        for(int i = 0; i < pianos.size(); i++) {
            arrayLists.add(pianos.get(i).getPressedArray());
        }
        CPTSharedPreference.getLoadedFile(getContext()).setChords(arrayLists);
    }

}