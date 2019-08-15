package com.modestasvalauskas.vaida;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.modestasvalauskas.vaida.Tabs.adapter.KBItem;

/**
 * Created by Modestas Valauskas on 17.09.2016.
 */
public class KBFileSharedPreference {

    private static String KBFileNameKeyString = "kbfilename";
    //true: stored in Assets, false: stored in Internal storage BOOLEAN
    private static String KBFileWhereKeyString = "kbwherename";
    private static String KBFileFolderKeyString = "kbfoldername";

    private static String defaultFileName = "template.json";
    private static String defaultFolderName = "templatekbs";
    private static boolean defaultAssetOrInternalStorage = true;

    public static SharedPreferences get(Context c) {
       return PreferenceManager.getDefaultSharedPreferences(c);
    }

    public static void openKBFile(KBItem kbItem, Context c) {
        get(c).edit().putString(KBFileNameKeyString, kbItem.getFilename()).apply();
        get(c).edit().putBoolean(KBFileWhereKeyString, kbItem.getAssetOrInternalStorage()).apply();
        get(c).edit().putString(KBFileFolderKeyString, kbItem.getFolderName()).apply();
    }

    public static String getKBFileName(Context c) {
        return get(c).getString(KBFileNameKeyString, defaultFileName);
    }

    public static boolean getKBFileAssetOrInternalStorage(Context c) {
        return get(c).getBoolean(KBFileWhereKeyString, defaultAssetOrInternalStorage);
    }

    public static String getKBFileFolder(Context c) {
        return get(c).getString(KBFileFolderKeyString, defaultFolderName);
    }

    public static void resetPreferences(Context c) {
        get(c).edit().putString(KBFileNameKeyString, defaultFileName).apply();
        get(c).edit().putBoolean(KBFileWhereKeyString, defaultAssetOrInternalStorage).apply();
        get(c).edit().putString(KBFileFolderKeyString, defaultFolderName).apply();
    }

}
