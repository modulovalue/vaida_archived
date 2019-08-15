package com.modestasvalauskas.vaida.Tabs.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.modestasvalauskas.vaida.KBFileSharedPreference;
import com.sromku.simple.storage.SimpleStorage;
import com.sromku.simple.storage.Storage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by Modestas Valauskas on 16.09.2016.
 */
public class KBFile {

    private static KBFile loadedKBFile;

    public static KBFile getLoadedKBFile(Context c) {
        if(loadedKBFile == null) {

            KBFileError kbFileError = KBFile.loadKBFile(c,
                    new KBItem(
                            KBFileSharedPreference.getKBFileName(c),
                            KBFileSharedPreference.getKBFileFolder(c),
                            false,
                            KBFileSharedPreference.getKBFileAssetOrInternalStorage(c)));

            if(kbFileError.getError() == KBFileError.FILENOTFOUND) {
                KBFileSharedPreference.resetPreferences(c);
            } else {
                loadedKBFile = kbFileError.getKbFile();
            }

        }

        return loadedKBFile;

    }

    private static KBFile getTemplateKBFile(Context c) {
        return KBFile.loadKBFile(c,
                new KBItem(
                        KBFileSharedPreference.getKBFileName(c),
                        KBFileSharedPreference.getKBFileFolder(c),
                        false,
                        KBFileSharedPreference.getKBFileAssetOrInternalStorage(c))).getKbFile();
    }

    public static void setKBFile(KBFile kbFile) {
        loadedKBFile = kbFile;
    }

    private ArrayList<String> constants;
    private ArrayList<String> predicates;
    private ArrayList<String> facts;
    private ArrayList<String> functions;
    private KBItem kbItem;

    public KBFile(ArrayList<String> constants, ArrayList<String> predicates, ArrayList<String> facts, ArrayList<String> functions, KBItem kbItem) {
        this.constants = constants;
        this.predicates = predicates;
        this.facts = facts;
        this.functions = functions;
        this.kbItem = kbItem;
    }

    public ArrayList<String> getFacts() {
        return facts;
    }

    public void setFacts(ArrayList<String> facts) {
        this.facts = facts;
    }

    public ArrayList<String> getPredicates() {
        return predicates;
    }

    public void setPredicates(ArrayList<String> predicates) {
        this.predicates = predicates;
    }

    public ArrayList<String> getConstants() {
        return constants;
    }

    public void setConstants(ArrayList<String> constants) {
        this.constants = constants;
    }

    public ArrayList<String> getFunctions() {
        return functions;
    }

    public void setFunctions(ArrayList<String> functions) {
        this.functions = functions;
    }

    public KBItem getKbItem() {
        return kbItem;
    }

    public void setKbItem(KBItem kbItem) {
        this.kbItem = kbItem;
    }

    public static String getTemplateStringFromTemplateFile(Context context) throws IOException {
        AssetManager am = context.getAssets();
        BufferedReader r = new BufferedReader(new InputStreamReader(am.open("templatekbs/template.json")));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line).append('\n');
        }
        return total.toString();
    }

    public void save(Context c) {
        saveKBFile(c, this, this.getKbItem().getFilename());
    }

    public static void saveKBFile(Context context, KBFile kbFile, String name) {

        try {

            JsonObject templateStringToJson = new JsonParser().parse(KBFile.getTemplateStringFromTemplateFile(context)).getAsJsonObject();

            JsonArray constantsJson  = (JsonArray) new Gson().toJsonTree(kbFile.getConstants(), new TypeToken<ArrayList<String>>(){}.getType());
            JsonArray predicatesJson = (JsonArray) new Gson().toJsonTree(kbFile.getPredicates(), new TypeToken<ArrayList<String>>(){}.getType());
            JsonArray factsJson      = (JsonArray) new Gson().toJsonTree(kbFile.getFacts(), new TypeToken<ArrayList<String>>(){}.getType());
            JsonArray functionsJson  = (JsonArray) new Gson().toJsonTree(kbFile.getFunctions(), new TypeToken<ArrayList<String>>(){}.getType());

            templateStringToJson.getAsJsonObject().getAsJsonObject("info").getAsJsonObject("constants").getAsJsonArray("item").addAll(constantsJson);
            templateStringToJson.getAsJsonObject().getAsJsonObject("info").getAsJsonObject("predicates").getAsJsonArray("item").addAll(predicatesJson);
            templateStringToJson.getAsJsonObject().getAsJsonObject("info").getAsJsonObject("facts").getAsJsonArray("item").addAll(factsJson);
            templateStringToJson.getAsJsonObject().getAsJsonObject("info").getAsJsonObject("functions").getAsJsonArray("item").addAll(functionsJson);


            KBFile.getInternalStorage(context).createFile(kbFile.kbItem.getFolderName(), name+".json", templateStringToJson.toString());

            if(kbFile.getKbItem().getAssetOrInternalStorage()) {
                Toast.makeText(context, "Can't edit internal KB. Saved as custom Knowledge Base", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(context, "Saved to " + kbFile.getKbItem().getItemTitle(), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
                Toast.makeText(context, "Error while saving KB", Toast.LENGTH_SHORT).show();
        }

    }

    public static KBFileError loadKBFile(Context context, KBItem kbItem) {

        KBFile kbFile;
        ArrayList<String> constants = new ArrayList<>();
        ArrayList<String> predicates = new ArrayList<>();
        ArrayList<String> facts = new ArrayList<>();
        ArrayList<String> functions = new ArrayList<>();

        String fileStringToParse = "" ;

        if(kbItem.getAssetOrInternalStorage()) {

            try {
                AssetManager am = context.getAssets();
                InputStream is = am.open(kbItem.getFolderName()+"/"+kbItem.getFilename());

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

        } else {
            if(KBFile.getInternalStorage(context).isFileExist(kbItem.getFolderName(), kbItem.getFilename())) {
                fileStringToParse = KBFile.getInternalStorage(context).readTextFile(kbItem.getFolderName(), kbItem.getFilename());
            } else {
                KBFileSharedPreference.resetPreferences(context);
                KBFile templateKbFile = KBFile.getTemplateKBFile(context);
                KBFile.setKBFile(templateKbFile);

                try {
                    AssetManager am = context.getAssets();
                    InputStream is = am.open(templateKbFile.getKbItem().getFolderName()+"/"+templateKbFile.getKbItem().getFilename());

                    BufferedReader r = new BufferedReader(new InputStreamReader(is));
                    StringBuilder total = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        total.append(line).append('\n');
                    }

                    fileStringToParse = total.toString();
                } catch (Exception ignored) {
                    System.err.println("11166666666666666666777");
                }

            }
        }

        if(!fileStringToParse.equals("")) {
            System.out.println(fileStringToParse);
            JsonObject fileStringToJson = new JsonParser().parse(fileStringToParse).getAsJsonObject();

            for (JsonElement str : fileStringToJson.getAsJsonObject().getAsJsonObject("info").getAsJsonObject("constants").getAsJsonArray("item")) {
                constants.add(str.getAsString());
            }
            for (JsonElement str : fileStringToJson.getAsJsonObject().getAsJsonObject("info").getAsJsonObject("predicates").getAsJsonArray("item")) {
                predicates.add(str.getAsString());
            }
            for (JsonElement str : fileStringToJson.getAsJsonObject().getAsJsonObject("info").getAsJsonObject("facts").getAsJsonArray("item")) {
                facts.add(str.getAsString());
            }
            for (JsonElement str : fileStringToJson.getAsJsonObject().getAsJsonObject("info").getAsJsonObject("functions").getAsJsonArray("item")) {
                functions.add(str.getAsString());
            }

            kbFile = new KBFile(constants, predicates, facts, functions, kbItem);
            return new KBFileError(kbFile, 0);
        } else {
            if (!KBFile.getInternalStorage(context).isFileExist(kbItem.getFolderName(), kbItem.getFilename())) {
                return new KBFileError(null, 2);
            } else {
                return new KBFileError(null, 1);
            }
        }
    }

    public static void deleteKBFileInternalStorage(Context context, KBItem kbItem) {
        KBFile.getInternalStorage(context).deleteFile(kbItem.getFolderName(), kbItem.getFilename());
    }

    public static Storage getInternalStorage(Context context) {
        Storage storage = SimpleStorage.getInternalStorage(context);
        if(!storage.isDirectoryExists("kbs")) {
            storage.createDirectory("kbs");
        }
        return storage;
    }

    public static ArrayList<String> addToList(ArrayList<String> list, String str) {
        ArrayList<String> retList = list;
        list.add(str);
        return retList;
    }

}
