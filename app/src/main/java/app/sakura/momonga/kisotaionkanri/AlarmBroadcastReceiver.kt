package app.sakura.momonga.kisotaionkanri

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.widget.Toast
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi


class AlarmBroadcastReceiver() : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d("Receive", "ok")
        Toast.makeText(context, "Received", Toast.LENGTH_LONG).show()

    }


}