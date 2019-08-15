package com.modestasvalauskas.vaida.Tabs.adapter;

/**
 * Created by Modestas Valauskas on 17.09.2016.
 */
public class KBFileError {

    public static int OK = 0;
    public static int SOMEERROR = 1;
    public static int FILENOTFOUND = 2;

    private KBFile kbFile;
    //error == 1 => there was an error, error = 0 => everything is ok , 2 => File not found
    private int error;

    public KBFileError(KBFile kbFile, int error) {
        this.kbFile = kbFile;
        this.error = error;
    }

    public KBFile getKbFile() {
        return kbFile;
    }

    public int getError() {
        return error;
    }
}
