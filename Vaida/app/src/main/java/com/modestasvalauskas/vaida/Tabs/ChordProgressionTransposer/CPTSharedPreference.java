package com.modestasvalauskas.vaida.Tabs.ChordProgressionTransposer;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;
import com.sromku.simple.storage.helpers.OrderType;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Modestas Valauskas on 17.09.2016.
 */
public class CPTSharedPreference {


    private static CPTFileData loadedFile;

    public static CPTFileData getLoadedFile(Context c) {
        if(loadedFile == null) {

            System.out.println(CPTSharedPreference.getFiles(c));
            CPTSharedPreference.copyAssetToInternal(c);
            System.out.println(CPTSharedPreference.getFiles(c));

            if(getInternalStorage(c).isFileExist(CPTFolderName,get(c).getString(CPTFileNameKeyString, CPTDefaultFileName))) {
                loadedFile = loadCPTFile(get(c).getString(CPTFileNameKeyString, CPTDefaultFileName), c);
            } else {
                resetCPTPreferences(c);
                loadedFile = loadCPTFile(get(c).getString(CPTFileNameKeyString, CPTDefaultFileName), c);
            }
        }

        return loadedFile;
    }


    private static String CPTFileNameKeyString = "cptfilename";
    private static String CPTDefaultFileName = "template.json";
    public static String CPTFolderName = "cptlibrary";

    public static String AssetPresetFolderName = "cptlibrary";

    public static SharedPreferences get(Context c) {
       return PreferenceManager.getDefaultSharedPreferences(c);
    }

    public static void putCPTFileName(String filename, Context c) {
        get(c).edit().putString(CPTFileNameKeyString, filename).apply();
        loadedFile = null;
    }

    public static void resetCPTPreferences(Context c) {
        get(c).edit().putString(CPTFileNameKeyString, CPTDefaultFileName).apply();
    }

    public static List<File> getFiles(Context c) {
        return getInternalStorage(c).getFiles(CPTFolderName, OrderType.NAME);
    }
    public static void deleteFile(String name, Context c) {
        getInternalStorage(c).deleteFile(CPTFolderName, name);
        loadedFile = null;
    }

    public static CPTFileData loadCPTFile(String name, Context c) {
        return CPTFileData.jsonStringToCPTFileData(
                name,
                getInternalStorage(c).readTextFile(
                        CPTFolderName,
                        name
                )
        );
    }

    public static void copyAssetToInternal(Context c) {
        try {
            for(String string: c.getAssets().list(AssetPresetFolderName)) {
                copyAssetFileToInternal(c, AssetPresetFolderName, string, CPTFolderName);
            }
        } catch (Exception ignored) {
            System.err.println("1111111copyAssetToInternal1111");
        }
    }
    public static String getTemplateStringFromTemplateFile(Context context) throws IOException {
        AssetManager am = context.getAssets();
        BufferedReader r = new BufferedReader(new InputStreamReader(am.open("cpttemplate/template.json")));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line).append('\n');
        }
        return total.toString();
    }

    private static void copyAssetFileToInternal(Context c, String assetFolderName, String fileName, String internalFolderName) {
        String fileStringToParse = "" ;
        try {
            AssetManager am = c.getAssets();
            InputStream is = am.open(assetFolderName + "/" + fileName);
            BufferedReader r = new BufferedReader(new InputStreamReader(is));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line).append('\n');
            }
            fileStringToParse = total.toString();
        } catch (Exception ignored) {
            System.err.println("11111111111");
        }
        if(!fileStringToParse.equals("")) {
            getInternalStorage(c).createFile(internalFolderName, fileName, fileStringToParse);
        }
    }

    public static Storage getInternalStorage(Context context) {
        Storage storage = SimpleStorage.getInternalStorage(context);
        if(!storage.isDirectoryExists("kbs")) {
            storage.createDirectory("kbs");
        }
        return storage;
    }

    public static void createNewFile(String str, Context c) {
        try {
            String templateStr = getTemplateStringFromTemplateFile(c);
            String fileName = str+".json";
            getInternalStorage(c).createFile(CPTFolderName, fileName, templateStr);
        } catch (Exception e) {
            System.err.println(e.getLocalizedMessage());
        }
        System.out.println(str);
    }

    public static boolean fileExists(Context c, String filename) {
       return CPTSharedPreference.getInternalStorage(c).isFileExist(CPTFolderName, filename);
    }


    public static void saveFile(Context c, CPTFileData fileToSave, String name) {
        String toSave = CPTDataToString(c, fileToSave);
        if(!toSave.equals("")) {
            getInternalStorage(c).createFile(CPTFolderName, name + ".json", toSave);
            Toast.makeText(c, "Saved " + name, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(c, "Error while saving " + name, Toast.LENGTH_LONG).show();
        }
    }

    public static String CPTDataToString(Context c, CPTFileData data) {
        try {
            JsonObject templateStringToJson = new JsonObject();
            JsonArray chordsArray = new JsonArray();

            for(ArrayList<Integer> arrayList: data.getChords()) {
                System.out.println("loop " + arrayList);
                JsonObject jsonObject = new JsonObject();
                jsonObject.add("notes", new Gson().toJsonTree(arrayList, new TypeToken<ArrayList<Integer>>(){}.getType()));
                chordsArray.add(jsonObject);
            }

            templateStringToJson.addProperty("description"  , data.getDescription());
            templateStringToJson.addProperty("writable"     , "1");
            templateStringToJson.        add("chords"       , chordsArray);

            return templateStringToJson.toString();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "";
        }
    }

}

