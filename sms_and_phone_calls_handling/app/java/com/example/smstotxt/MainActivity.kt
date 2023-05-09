/**
 * Created by Grigoriy Golovanov (Alchemist) 2023
 * magentrum@gmail.com
 */

package com.example.smstotxt

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    // cMangeSMS class initialize
    // val cManageSMS=cManageSMS()

    // cManageCALLS class initialize
    val cManageCALLS=cManageCALLS()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Storage context
        cGlobalData.context = getApplicationContext();
        if (cGlobalData.sLogShow) {
            Log.d("MY_APP_ONCREATE", "---------------------------------------------------------")
            Log.d("MY_APP_ONCREATE", "APP START!")
        }

        // Storage context
        cGlobalData.context = getApplicationContext();
        // SMS/CALLS Counter
        cGlobalData.sCounter=0
        // JSON String
        cGlobalData.sJSON_Global=""

        // Setup permissions
        var myPermission: Array<String> = arrayOf(
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        ActivityCompat.requestPermissions(this, myPermission,1)

        // Read SMS method
        // cManageSMS.readSms()

        // Read PHONE CALLS method
        cManageCALLS.readCalls()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}