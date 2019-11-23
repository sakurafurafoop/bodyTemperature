package app.sakura.momonga.kisotaionkanri

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.widget.Toast
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat


class AlarmBroadcastReceiver() : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d("Receive", "ok")
        sendNotification(context,"hoge","fuga")
        Toast.makeText(context, "Received", Toast.LENGTH_LONG).show()

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun sendNotification(context: Context?,contentTitle: String,contentText:String) {
        val notificationManager =
            context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val name = "通知の情報を設定"
        val id = "casareal_chanel"
        val notifyDescription = "この通知の詳細情報を設定します"


        if (notificationManager.getNotificationChannel(id) == null) {
            val mChannel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
            mChannel.apply {
                description = notifyDescription
            }
            notificationManager.createNotificationChannel(mChannel)
        }

        val notification = NotificationCompat
            .Builder(context, id)
            .apply {
                setSmallIcon(R.drawable.ic_launcher_background)
                setContentTitle(contentTitle)
                setContentText(contentText)
            }.build()
        notificationManager.notify(1, notification)
    }

}