package app.sakura.momonga.kisotaionkanri

import android.content.BroadcastReceiver
import android.content.Context
import android.widget.Toast
import android.content.Intent
import android.util.Log



class AlarmBroadcastReceiver() : BroadcastReceiver()  {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("Receive","ok")
        Toast.makeText(context,"Received",Toast.LENGTH_LONG).show()
    }
}