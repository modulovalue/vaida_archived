package com.modestasvalauskas.vaida.Tabs.FourTabs;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.modestasvalauskas.vaida.R;
import com.modestasvalauskas.vaida.Tabs.adapter.KBFile;

import java.util.ArrayList;

/**
 * Created by Modestas Valauskas on 18.09.2016.
 */
public class EditTabListFragment extends ListFragment implements AdapterView.OnItemClickListener  {

    ArrayAdapter adapter;
    protected ArrayList<String> list;

    public void setupFab(View view, final ArrayList<String> list) {
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("New");
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        list.add(input.getText().toString());

                        setAdapter(getActivity(), list);

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
    }

    public void tabListClick(final Context context, final String itemName, final ArrayList<String> listItems, final int position, final ArrayAdapter adapter) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(itemName);
        builder.setItems(new CharSequence[]
                        {"Copy", "Edit", "Remove"},
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                                ClipData clip = ClipData.newPlainText("label", itemName);
                                clipboard.setPrimaryClip(clip);
                                Toast.makeText(context, "Copied to Clipboard", Toast.LENGTH_SHORT).show();
                                break;
                            case 1:
                                // TODO NEW EDITING PAGE

                                Toast.makeText(context, "TODO NEW EDITING PAGE and new thing", Toast.LENGTH_SHORT).show();
                                break;
                            case 2:

                                Toast.makeText(context, "Removed", Toast.LENGTH_SHORT).show();
                                listItems.remove(position);

                                System.out.println("KBFILE THINGS C  " + KBFile.getLoadedKBFile(getActivity()).getConstants());
                                System.out.println("KBFILE THINGS FU " + KBFile.getLoadedKBFile(getActivity()).getFunctions());
                                System.out.println("KBFILE THINGS FA " + KBFile.getLoadedKBFile(getActivity()).getFacts());
                                System.out.println("KBFILE THINGS PR " + KBFile.getLoadedKBFile(getActivity()).getPredicates());

                                System.out.println("LISTITEMS funcs  " + listItems);
                                break;
                        }

                        setAdapter(getActivity(), listItems);
                    }
                });
        builder.create().show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        tabListClick(getContext(), list.get(position), list, position, adapter);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    public void setAdapter(Context c, ArrayList<String> list) {
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, list);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
