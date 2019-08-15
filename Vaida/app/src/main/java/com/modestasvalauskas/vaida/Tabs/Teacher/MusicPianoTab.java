package com.modestasvalauskas.vaida.Tabs.Teacher;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.modestasvalauskas.vaida.R;
import com.modestasvalauskas.vaida.pianoview.Piano;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Modestas Valauskas on 13.09.2016.
 */

public class MusicPianoTab extends TeacherClass {

    public Piano piano;
    public List<Piano> pianos = new ArrayList<Piano>();

    public static Map<Integer, String> idToNote = new HashMap<Integer, String>() {{
        put(0 ,"C" );
        put(1 ,"Cs");
        put(2 ,"D" );
        put(3 ,"Ds");
        put(4 ,"E" );
        put(5 ,"F" );
        put(6 ,"Fs");
        put(7 ,"G" );
        put(8 ,"Gs");
        put(9 ,"A" );
        put(10,"As");
        put(11,"B" );
    }};
    public LinearLayout pianoLinLayout;

    private Piano.PianoKeyListener onPianoKeyPress=
            new Piano.PianoKeyListener() {

                @Override
                public void keyPressed(int id, int action) {

                    FormQuery formQuery = new FormQuery();

                    for(int i = 0; i < piano.key_count; i++){
                        if(piano.black_key_indexes.contains(i % 12)){

                            if(piano.keymap_black.get(i).isPressed()) {
                                formQuery.args.add(idToNote.get(i % 12));
                            };
                        } else {
                            if(piano.keymap_white.get(i).isPressed()) {
                                formQuery.args.add(idToNote.get(i % 12));
                            };
                        }
                    }

                    formQuery.putPred("Two",1,3,0);
                    formQuery.putPred("Two",2,3,0);
                    formQuery.putPred("Three",3,4,0);
                    formQuery.putPred("Four",4,5,0);
                    String query = formQuery.getQuery();

                    long timeA = System.currentTimeMillis();

                    if(!query.equals("") ) {
                        if(getPredicates().contains(formQuery.getPred())) {

                            for (Piano piano: pianos) {
                                piano.resetKeysToNotPressed();
                            }

                            ArrayList<String> varsbefore = new ArrayList<String>();
                            for(String arg: formQuery.args) {
                                varsbefore.add(arg);
                            }


                            ArrayList<ArrayList<String>> queryVars = formQuery.getVars(MusicPianoTab.this, query);

                            ArrayList<ArrayList<String>> solutionArrays = new ArrayList<ArrayList<String>>();

                            for(ArrayList<String> arrayList: queryVars) {

                                ArrayList<String> temp = new ArrayList<String>(varsbefore);

                                for(String str: arrayList) {
                                    temp.add(str);
                                }

                                solutionArrays.add(temp);
                            }

                            ArrayList<ArrayList<Integer>> integersSolutionArrays = new ArrayList<ArrayList<Integer>>();

                            for(ArrayList<String> arrayList: solutionArrays) {
                                ArrayList<Integer> temp = new ArrayList<Integer>();
                                for(String string: arrayList) {

                                    for (Map.Entry<Integer, String> entry : idToNote.entrySet()) {
                                        if(entry.getValue().equals(string)) {
                                            temp.add(entry.getKey());

                                        }
                                    }
                                }
                                integersSolutionArrays.add(temp);
                            }

                            int smaller = 0;
                            if(integersSolutionArrays.size() < pianos.size()) {
                                smaller = integersSolutionArrays.size();
                            } else {
                                smaller = pianos.size();
                            }

                            for(int i = 0; i < smaller; i++) {
                                pianos.get(i).setKeysToPressed(integersSolutionArrays.get(i));
                            }


                            System.out.println(solutionArrays.toString());
                            System.out.println(integersSolutionArrays.toString());



                         //   Toast.makeText(getContext(), solutionArrays.toString(), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getContext(), "Predicate "+ formQuery.getPred() +" Not In KB", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        for (Piano piano: pianos) {
                            piano.resetKeysToNotPressed();
                        }
                    }
                    long timeB = System.currentTimeMillis();
                    //Toast.makeText(getContext(), "Elapsed time: " + (timeB - timeA), Toast.LENGTH_SHORT).show();

                }

            };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.piano_tab, container, false);


        pianoLinLayout = (LinearLayout) view.findViewById(R.id.pianoviewbottom);
        piano = new Piano(getContext(), null, 0, 48);
        piano.setPianoKeyListener(onPianoKeyPress);
        pianoLinLayout.addView(piano);

        if(pianos.size() == 0) {
            pianos.add(new Piano(getContext(), null, 0, 48));
            pianos.add(new Piano(getContext(), null, 0, 48));
            pianos.add(new Piano(getContext(), null, 0, 48));
            pianos.add(new Piano(getContext(), null, 0, 48));
        } else {
            for (Piano piano: pianos) {
                ((LinearLayout) piano.getParent()).removeView(piano);
            }
        }
        ((LinearLayout) view.findViewById(R.id.pianoview1)).addView(pianos.get(0));
        ((LinearLayout) view.findViewById(R.id.pianoview2)).addView(pianos.get(1));
        ((LinearLayout) view.findViewById(R.id.pianoview3)).addView(pianos.get(2));
        ((LinearLayout) view.findViewById(R.id.pianoview4)).addView(pianos.get(3));

        Button button = (Button) view.findViewById(R.id.button);

        return view;

    }



}