/**
 * Created by Grigoriy Golovanov (Alchemist) 2023
 * magentrum@gmail.com
 */

package com.example.smstotxt

import android.icu.text.SimpleDateFormat
import android.provider.CallLog
import android.text.TextUtils
import android.util.Log
import java.sql.Date

/*
 * Example class how to read Calls from phone
 */
class cManageCALLS {
    /*
     * Read Calls and write JSON data to a file
     */
    public fun readCalls()
    {
        if (cGlobalData.sLogShow) {
            Log.d("MY_APP_READPHONE", "START READ CALLS!")
        }

        val sCallName = CallLog.Calls.CACHED_NAME
        val sCallNumber = CallLog.Calls.NUMBER
        val sCallType = CallLog.Calls.TYPE // 3 - Incoming, 2 - Outgoing
        val sCallDate = CallLog.Calls.DATE

        val projection = arrayOf(sCallName, sCallNumber, sCallType, sCallDate)
        val contentResolver = cGlobalData.context.contentResolver

        val cursor = contentResolver.query(
            CallLog.Calls.CONTENT_URI,
            projection, null, null, null
        )

        val sCallNameColIdx = cursor!!.getColumnIndex(sCallName)
        val sCallNumberColIdx = cursor!!.getColumnIndex(sCallNumber)
        val sCallTypeColIdx = cursor.getColumnIndex(sCallType)
        val sCallDateColIdx = cursor!!.getColumnIndex(sCallDate)

        if (cGlobalData.sLogShow) {
            Log.d(
                "MY_APP_READPHONE",
                "$sCallNameColIdx $sCallNumberColIdx $sCallTypeColIdx $sCallDateColIdx"
            )
        }
        // Start creating JSON string
        cGlobalData.sJSON_Global=cGlobalData.sJSON_Global+"["

        // Read all phone calls
        while (cursor.moveToNext()) {
            // Set reading limit
            // if (cGlobalData.sCounter<10) {
                var sCallName_ = cursor.getString(sCallNameColIdx)
                val sCallNumber_ = cursor.getString(sCallNumberColIdx)
                val sCallType_ = cursor.getString(sCallTypeColIdx)
                val sCallDate_ = cursor.getString(sCallDateColIdx)

                // Convert Datestamp to Date
                val sCallDateStr =
                    SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(Date(sCallDate_.toLong()))
                        .toString()
                if (TextUtils.isEmpty(sCallName_)) {
                    sCallName_="empty"
                }
                if (cGlobalData.sLogShow) {
                    Log.d(
                        "MY_APP_READPHONE",
                        "$sCallName_ $sCallNumber_ $sCallType_ $sCallDateStr"
                    )
                }

                if (cGlobalData.sCounter > 0) {
                    cGlobalData.sJSON_Global = cGlobalData.sJSON_Global + ","
                }
                cGlobalData.sJSON_Global =
                    cGlobalData.sJSON_Global + "{\"id\":\"" + cGlobalData.sCounter + "\", \"type\":\"" + sCallType_ + "\", " +
                            "\"date\":\"" + sCallDateStr + "\", " +
                            "\"phone\":\"" + sCallNumber_.replace("\"", "") + "\", " +
                            "\"name\":\"" + sCallName_.replace("\"", "") + "\"}"
            //  } //<10
            cGlobalData.sCounter++
        }
        // Finish JSON string
        cGlobalData.sJSON_Global=cGlobalData.sJSON_Global+"]"
        cursor.close()

        if (cGlobalData.sLogShow) {
            Log.d("MY_APP_READPHONE ", "Create directory!")
        }
        // Create directory (if not exist)
        cReadWriteFile.fCreateDirectory();
        if (cGlobalData.sLogShow) {
            Log.d("MY_APP_READPHONE", "Write to file!")
        }
        // Write JSON string to the file callstotxt.txt
        cReadWriteFile.fWriteFile("callstotxt.txt", cGlobalData.sJSON_Global);
        // if (cGlobalData.sLogShow) {
        //      Log.d("MY_APP_READSMS", "Read from file:")
        // }
        // Read JSON string from a file
        // if (cGlobalData.sLogShow) {
        //      Log.d("MY_APP_READPHONE", cReadWriteFile.fReadFile("callstotxt.txt"));
        // }
    }
}