/**
 * Created by Grigoriy Golovanov (Alchemist) 2023
 * magentrum@gmail.com
 */

package com.example.smstotxt

import android.icu.text.SimpleDateFormat
import android.provider.Telephony
import android.util.Log
import java.sql.Date

/*
 * Example class how to read SMS from phone
 */
class cManageSMS {
    /*
     * Read SMS and write JSON data to a file
     */
    public fun readSms()
    {
        if (cGlobalData.sLogShow) {
            Log.d("MY_APP_READSMS", "START READ SMS!")
        }

        val sDate = Telephony.TextBasedSmsColumns.DATE
        val sDateSent = Telephony.TextBasedSmsColumns.DATE_SENT
        val sPhone = Telephony.TextBasedSmsColumns.ADDRESS
        val sSmsText = Telephony.TextBasedSmsColumns.BODY
        val sType = Telephony.TextBasedSmsColumns.TYPE // 1 - Inbox, 2 - Sent

        val projection = arrayOf(sPhone, sSmsText, sType, sDate, sDateSent)
        val contentResolver = cGlobalData.context.contentResolver
        val cursor = contentResolver.query(
            Telephony.Sms.CONTENT_URI,
            projection, null, null, null
        )

        val sDateSentColIdx = cursor!!.getColumnIndex(sDateSent)
        val sDateColIdx = cursor!!.getColumnIndex(sDate)
        val sPhoneColIdx = cursor!!.getColumnIndex(sPhone)
        val sSmsTextColIdx = cursor.getColumnIndex(sSmsText)
        val sTypeColIdx = cursor.getColumnIndex(sType)

        if (cGlobalData.sLogShow) {
            Log.d(
                "MY_APP_READSMS",
                "$sDateColIdx $sDateSentColIdx $sPhoneColIdx $sSmsTextColIdx $sTypeColIdx"
            )
        }

        // Start creating JSON string
        cGlobalData.sJSON_Global=cGlobalData.sJSON_Global+"["

        // Read all sms
        while (cursor.moveToNext()) {
            // Set reading limit
            // if (cGlobalData.sCounter<10) {
                val sDateSent_ = cursor.getString(sDateSentColIdx)
                val sDate_ = cursor.getString(sDateColIdx)
                val sPhone_ = cursor.getString(sPhoneColIdx)
                val sSmsText_ = cursor.getString(sSmsTextColIdx)
                val sType_ = cursor.getString(sTypeColIdx)

                // Convert Datestamp to Date
                val sDateSentStr =
                    SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(Date(sDateSent_.toLong()))
                        .toString()
                val sDateStr =
                    SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(Date(sDate_.toLong()))
                        .toString()
                if (cGlobalData.sLogShow) {
                    Log.d(
                        "MY_APP_READSMS",
                        "$sDateSentStr $sDateStr $sPhone_ $sSmsText_ $sType_"
                    )
                }
                if (cGlobalData.sCounter > 0) {
                    cGlobalData.sJSON_Global = cGlobalData.sJSON_Global + ","
                }
                cGlobalData.sJSON_Global =
                    cGlobalData.sJSON_Global + "{\"id\":\"" + cGlobalData.sCounter + "\", \"type\":\"" + sType_ + "\", " +
                            "\"sent_stamp\":\"" + sDateSent_ + "\", " +
                            "\"receive_stamp\":\"" + sDate_ + "\", " +
                            "\"phone\":\"" + sPhone_.replace("\"", "") + "\", " +
                            "\"text\":\"" + sSmsText_.replace("\"", "") + "\"}"
            // } //<10
            cGlobalData.sCounter++
        }
        // Finish JSON string
        cGlobalData.sJSON_Global=cGlobalData.sJSON_Global+"]"
        cursor.close()

        if (cGlobalData.sLogShow) {
            Log.d("MY_APP_READSMS", "Create directory!")
        }
        // Create directory (if not exist)
        cReadWriteFile.fCreateDirectory();
        if (cGlobalData.sLogShow) {
            Log.d("MY_APP_READSMS", "Write to file!")
        }
        // Write JSON string to the file smstotxt.txt
        cReadWriteFile.fWriteFile("smstotxt.txt", cGlobalData.sJSON_Global);
        // if (cGlobalData.sLogShow) {
        //      Log.d("MY_APP_READSMS", "Read from file:")
        // }
        // Read JSON string from a file
        // if (cGlobalData.sLogShow) {
        //      Log.d("MY_APP_READSMS", cReadWriteFile.fReadFile("smstotxt.txt"));
        // }
    }
}