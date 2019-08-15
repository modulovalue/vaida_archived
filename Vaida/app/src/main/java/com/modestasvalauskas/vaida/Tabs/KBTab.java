package com.modestasvalauskas.vaida.Tabs;


import android.content.DialogInterface;
import android.content.res.AssetManager;
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

import com.modestasvalauskas.vaida.KBFileSharedPreference;
import com.modestasvalauskas.vaida.R;
import com.modestasvalauskas.vaida.Tabs.adapter.KBFile;
import com.modestasvalauskas.vaida.Tabs.adapter.KBFileError;
import com.modestasvalauskas.vaida.Tabs.adapter.KBItem;
import com.modestasvalauskas.vaida.Tabs.adapter.KBTabListAdapter;
import com.sromku.simple.storage.helpers.OrderType;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Modestas Valauskas on 13.09.2016.
 */

public class KBTab extends ListFragment implements AdapterView.OnItemClickListener {

    public static ArrayList<KBItem> itemList = new ArrayList<KBItem>();

    ArrayAdapter adapter;

    public KBTab() {
        itemList = new ArrayList<KBItem>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kb_tab, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Name your new KB");

                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                builder.setView(input);

                builder.setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = input.getText().toString();
                        Pattern p = Pattern.compile("[^a-zA-Z0-9 ]");
                        boolean hasSpecialChar = p.matcher(str).find();
                        if(hasSpecialChar) {
                            Toast.makeText(getContext(), "Please only alphanumerical characters", Toast.LENGTH_SHORT).show();
                        } else {
                            try {
                                KBFile.getInternalStorage(getContext()).createFile("kbs",str+".json",KBFile.getTemplateStringFromTemplateFile(getContext()));
                            } catch (Exception e) {
                                System.err.println(e.getLocalizedMessage());
                            }
                            System.out.println(str);
                            populateKbTabListAndReset();
                        }
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });

        FloatingActionButton fab2 = (FloatingActionButton) view.findViewById(R.id.fabsave);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Save your KB");

                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                builder.setView(input);
                input.setText(KBFile.getLoadedKBFile(getActivity()).getKbItem().getItemTitle());

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        KBFile.saveKBFile(getContext(), KBFile.getLoadedKBFile(getActivity()), input.getText().toString());
                        populateKbTabListAndReset();
                        Toast.makeText(getContext(), "Saved successfully", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        populateKbTabListAndReset();

        getListView().setOnItemClickListener(this);
    }

    public void populateKbTabListAndReset() {

        itemList = new ArrayList<>();
        try {
            AssetManager am = getActivity().getAssets();
            for (String str : am.list("kbs")) {
                String filenameArray[] = str.split("\\.");
                String extension = filenameArray[1];
                if(extension.equals("json"))  {
                    itemList.add(new KBItem(str, "kbs", true, true));
                }
            }

            List<File> files = KBFile.getInternalStorage(getContext()).getFiles("kbs", OrderType.NAME);


            for (File file : files) {
                String filenameArray[] = file.getName().split("\\.");
                String extension = filenameArray[1];
                if(extension.equals("json"))  {
                    itemList.add(new KBItem(file.getName(), "kbs", false, false));
                }
            }

        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }

        if(adapter != null) {
            adapter.notifyDataSetChanged();
        }

        adapter = new KBTabListAdapter(getActivity(), R.layout.kbrowlist,itemList);
        setListAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(itemList.get(position).getItemTitle());
        builder.setItems(new CharSequence[]
                        {"Load", "Delete"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                KBFileError kbFileError = KBFile.loadKBFile(getContext(), itemList.get(position));
                                if(kbFileError.getError() == 0) {
                                    KBFile.setKBFile(kbFileError.getKbFile());
                                    KBFileSharedPreference.openKBFile(kbFileError.getKbFile().getKbItem(),getActivity());
                                    Toast.makeText(getContext(), KBFile.getLoadedKBFile(getContext()).getKbItem().getItemTitle() + " loaded", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "Error while loading KB", Toast.LENGTH_SHORT).show();
                                }

                                break;
                            case 1:
                                if(!itemList.get(position).getReadOnly()) {
                                    String itemName = itemList.get(position).getItemTitle();
                                    KBFile.deleteKBFileInternalStorage(getContext(),itemList.get(position));
                                    Toast.makeText(getContext(), itemName + " deleted", Toast.LENGTH_SHORT).show();
                                    populateKbTabListAndReset();
                                } else {
                                    Toast.makeText(getContext(), "You can't delete this KB", Toast.LENGTH_SHORT).show();
                                }
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
            }
        }

    }
}