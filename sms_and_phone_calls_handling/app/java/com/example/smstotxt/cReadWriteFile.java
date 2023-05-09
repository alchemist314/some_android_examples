/**
 * Created by Grigoriy Golovanov (Alchemist) 2017-2023
 * magentrum@gmail.com
 */

package com.example.smstotxt;

import android.os.Environment;
import android.util.Log;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/*
 * Example class allows read and write data to a file
 */
public class cReadWriteFile {
    /*
     *  Create directory
     *  cGlobalData.sDirName variable must be not empty
     */
    public static void fCreateDirectory() {
        // Get path to storage
        File sDirectory = new File(Environment.getExternalStorageDirectory() +
                File.separator + cGlobalData.sDirName);

        boolean bSuccess = true;
        if (!sDirectory.exists()) {
            // Execute makedirs
            bSuccess = sDirectory.mkdirs();
        }

        if (bSuccess) {
            // Directory was created successfully
            if (cGlobalData.sLogShow) {
                Log.d("MY_APP_PHONE", "CREATE DIRECTORY: " + sDirectory);
            }
        } else {
            // Failed to create directory
            if (cGlobalData.sLogShow) {
                Log.d("MY_APP_PHONE", "CREATE DIRECTORY FAILED!: " + sDirectory);
            }
        }
    }

    /*
     * Write data to a file
     * cGlobalData.sDirName variable must be not empty
     */
    public static void fWriteFile(String sFileName, String sData) {
        FileOutputStream outputStream;
        File sDirectory = new File(Environment.getExternalStorageDirectory() +
                File.separator + cGlobalData.sDirName);
        if (cGlobalData.sLogShow) {
            Log.d("MY_APP_PHONE", "DIRECTORY: " + sDirectory);
        }
        try {
            FileOutputStream sOut = new FileOutputStream(sDirectory+File.separator + sFileName);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(sOut);
            Log.d("MY_APP_PHONE", "DATA TO SAVE: " + sData);
            myOutWriter.write(sData);
            myOutWriter.close();
            sOut.close();
        } catch (Exception e) {
            if (cGlobalData.sLogException) {
                e.printStackTrace();
            }
        }
    }
    /*
     * Read data from a file
     * cGlobalData.sDirName variable must be not empty
     */
    public static String fReadFile(String sFileName) {
        FileInputStream inputStream;
        String aBuffer = "";
        File sDirectory = new File(Environment.getExternalStorageDirectory() +
                File.separator + cGlobalData.sDirName);
        try {
            FileInputStream sIn = new FileInputStream(sDirectory+File.separator + sFileName);
            BufferedReader sReader = new BufferedReader(new InputStreamReader(sIn));
            String aDataRow = "";
            while ((aDataRow = sReader.readLine()) != null) {
                aBuffer += aDataRow;
            }
            sReader.close();
        } catch (Exception e) {
            if (cGlobalData.sLogException) {
                e.printStackTrace();
            }
        }
        return aBuffer;
    }
}