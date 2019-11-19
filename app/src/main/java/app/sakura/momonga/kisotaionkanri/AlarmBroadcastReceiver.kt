package app.sakura.momonga.kisotaionkanri

import android.content.Context
import android.widget.Toast
import android.content.Intent



class AlarmBroadcastReceiver {
    fun onReceive(context: Context, intent: Intent) {
        // toast で受け取りを確認
        Toast.makeText(context, "Received ", Toast.LENGTH_LONG).show()
    }
}