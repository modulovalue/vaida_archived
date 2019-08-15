
package com.modestasvalauskas.vaida.Tabs.ChordProgressionTransposer;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.modestasvalauskas.vaida.R;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;


public class ChordProgressionChooserTab extends ListFragment implements AdapterView.OnItemClickListener {

    public static ArrayList<CPTFileData> itemList = new ArrayList<>();

    ArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cptchooser_tab, container, false);
        setupFabsView(view);
        return view;
    }

    public void setupFabsView(View view) {
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);

                new AlertDialog.Builder(getContext())
                        .setTitle("New Chord Progression")
                        .setView(input)
                        .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = input.getText().toString();
                                // check for legal characters
                                if(Pattern.compile("[^a-zA-Z0-9 ]").matcher(str).find()) {
                                    Toast.makeText(getContext(), "Please only alphanumerical characters", Toast.LENGTH_SHORT).show();
                                } else {
                                    if(!CPTSharedPreference.fileExists(getContext(), str+".json")) {
                                        CPTSharedPreference.createNewFile(str, getContext());
                                        populateListAndReset();
                                    } else {
                                        Toast.makeText(getContext(), "File already exists", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) view.findViewById(R.id.fabsave);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                input.setText(CPTSharedPreference.getLoadedFile(getActivity()).getNameWithoutExtension());

                new AlertDialog.Builder(getContext())
                        .setTitle("Save your Chord Progression")
                        .setView(input)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                CPTSharedPreference.saveFile(
                                        getContext(),
                                        CPTSharedPreference.getLoadedFile(getContext()),
                                        input.getText().toString()
                                );
                                populateListAndReset();
                                Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        populateListAndReset();
        getListView().setOnItemClickListener(this);
    }

    public void populateListAndReset() {
        itemList = new ArrayList<>();
        try {
            for (File file : CPTSharedPreference.getFiles(getContext())) {
                String filenameSplitToExtensionAndName[] = file.getName().split("\\.");
                String extension = filenameSplitToExtensionAndName[1];
                String filename = filenameSplitToExtensionAndName[0];
                if(extension.equals("json"))  {
                    itemList.add(CPTSharedPreference.loadCPTFile(file.getName(), getContext()));
                }
            }
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }

        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }

        adapter = new CPTTabListAdapter(getActivity(), R.layout.kbrowlist, itemList);
        setListAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(itemList.get(position).getNameWithoutExtension());
        builder.setItems(new CharSequence[]
                        {"Load", "Delete", "Edit Description"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                CPTSharedPreference.putCPTFileName(itemList.get(position).getFilename(), getContext());
                                System.out.println(CPTSharedPreference.getLoadedFile(getContext()).getChords());
                                populateListAndReset();
                                break;
                            case 1:
                                CPTSharedPreference.deleteFile(itemList.get(position).getFilename(), getContext());
                                populateListAndReset();
                                break;
                            case 2:
                                final EditText input = new EditText(getContext());
                                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                                input.setText("");
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Write a Description")
                                        .setView(input)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String filename = itemList.get(position).getNameWithoutExtension();

                                                CPTSharedPreference.saveFile(getContext(),
                                                        CPTSharedPreference.loadCPTFile(
                                                                filename + ".json",
                                                                getContext()
                                                        ).putDescription(input.getText().toString()),
                                                        filename);

                                                populateListAndReset();
                                            }
                                        })
                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        })
                                        .show();
                                break;
                        }
                    }
                });
        builder.create().show();

        adapter.notifyDataSetChanged();
    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {
            if(adapter != null) {
                adapter.notifyDataSetChanged();
                populateListAndReset();
            }
        }

    }


}