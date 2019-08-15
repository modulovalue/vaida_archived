package com.modestasvalauskas.vaida.Tabs.ChordProgressionTransposer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Modestas Valauskas on 26.09.2016.
 */
public class CPTFileData {

    private String filename;
    private String writable;
    private String description;
    private ArrayList<ArrayList<Integer>> chords;

    public CPTFileData(String filename, String writable, String description, ArrayList<ArrayList<Integer>> chords) {
        this.filename = filename;
        this.writable = writable;
        this.description = description;
        this.chords = chords;
    }

    public String getFilename() {
        return filename;
    }

    public ArrayList<ArrayList<Integer>> getChords() {
        return chords;
    }

    public String getDescription() {
        return description;
    }

    public static List<CPTFileData> generateData(List<String> jsonList) {
        List<CPTFileData> data = new ArrayList<>();
        for(String json: jsonList) {
            data.add(jsonStringToCPTFileData("test", json));
        }

        return data;
    }

    public static CPTFileData jsonStringToCPTFileData(String filename, String json) {
        ArrayList<ArrayList<Integer>> chords = new ArrayList<>();

        JsonObject fileStringToJson = new JsonParser().parse(json).getAsJsonObject();

        String description = fileStringToJson.getAsJsonObject().getAsJsonPrimitive("description").getAsString();
        String writable = fileStringToJson.getAsJsonObject().getAsJsonPrimitive("writable").getAsString();

        for (JsonElement str : fileStringToJson.getAsJsonObject().getAsJsonArray("chords")) {
            ArrayList<Integer> arrayList = new ArrayList<>();
            for(JsonElement i: str.getAsJsonObject().getAsJsonArray("notes")) {
                arrayList.add(i.getAsInt());
            }
            chords.add(arrayList);
        }

        CPTFileData data = new CPTFileData(filename, writable, description, chords);

        return data;
    }

    @Override
    public String toString() {
        return filename + " " + writable + " " + description + " " + chords;
    }

    public String getWritable() {
        return writable;
    }

    public String getNameWithoutExtension() {
        String filenameSplitToExtensionAndName[] = getFilename().split("\\.");
        String filename = filenameSplitToExtensionAndName[0];
        return filename;
    }

    public void setChords(ArrayList<ArrayList<Integer>> chords) {
        this.chords = chords;
    }

    public CPTFileData putDescription(String s) {
        this.description = s;
        return this;
    }
}
