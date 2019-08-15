package com.modestasvalauskas.vaida.Tabs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.modestasvalauskas.vaida.R;

import java.util.List;

/**
 * Created by Modestas Valauskas on 15.09.2016.
 */
public class KBTabListAdapter extends ArrayAdapter<KBItem> {

    public KBTabListAdapter(Context context, int resource, List<KBItem> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.kbrowlist, null);
        }

        KBItem p = getItem(position);

        if (p != null) {
            TextView text1 = (TextView) v.findViewById(R.id.kbrowtitle);
            TextView text2 = (TextView) v.findViewById(R.id.infoEditable);

            if (text1 != null) {
                text1.setText(p.getItemTitle());
            }

            if (text2 != null) {
                if(p.getReadOnly()) {
                    text2.setText("Vaida");
                    text2.setTextColor(getContext().getResources().getColor(R.color.infokbnoteditable));
                } else {
                    text2.setText("Custom");
                    text2.setTextColor(getContext().getResources().getColor(R.color.infokbeditable));
                }
            }

        }

        return v;
    }

}

