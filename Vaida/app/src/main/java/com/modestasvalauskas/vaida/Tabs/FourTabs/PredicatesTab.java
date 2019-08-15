package com.modestasvalauskas.vaida.Tabs.FourTabs;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.modestasvalauskas.vaida.R;
import com.modestasvalauskas.vaida.Tabs.adapter.KBFile;

/**
 * Created by Modestas Valauskas on 13.09.2016.
 */

public class PredicatesTab extends EditTabListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.objects_tab, container, false);
        setAdapter(getActivity(), KBFile.getLoadedKBFile(getActivity()).getPredicates());
        super.onCreateView(inflater,container, savedInstanceState);
        super.list = KBFile.getLoadedKBFile(getActivity()).getPredicates();
        super.setupFab(view, KBFile.getLoadedKBFile(getActivity()).getPredicates());
        return view;
    }

}