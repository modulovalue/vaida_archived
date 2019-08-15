package com.modestasvalauskas.vaida.Tabs.adapter;

/**
 * Created by Modestas Valauskas on 15.09.2016.
 */
public class KBItem {
    private String filename;
    private String folderName;
    private boolean readonly;
    //true = assets, false = internalStorage
    private boolean assetOrInternalStorage;

    public KBItem(String filename, String folderName, boolean readOnly, boolean assetOrInternalStorage){

        this.filename = filename;
        this.readonly = readOnly;
        this.assetOrInternalStorage = assetOrInternalStorage;
        this.folderName = folderName;
    }

    public boolean getReadOnly()  {
        return readonly;
    }
    public boolean getAssetOrInternalStorage()  {
        return assetOrInternalStorage;
    }
    public String getFilename() {
        return filename;
    }

    public String getItemTitle() {
        String filenameArray[] = filename.split("\\.");
        return filenameArray[0];
    }

    public String getFolderName() {
        return folderName;
    }
}
