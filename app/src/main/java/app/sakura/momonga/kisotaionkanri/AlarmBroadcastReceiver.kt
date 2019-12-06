package app.sakura.momonga.kisotaionkanri

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.widget.Toast
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import java.util.*


class AlarmBroadcastReceiver() : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        sendNotification(context,"体温を測りましょう","")
        sendMoriningAlerm(context)
    }

    fun sendMoriningAlerm(context: Context?){
        var alarmCalendar = Calendar.getInstance()
        alarmCalendar.add(Calendar.DAY_OF_MONTH,1)
        alarmCalendar.set(
            alarmCalendar.get(Calendar.YEAR),alarmCalendar.get(Calendar.MONTH),alarmCalendar.get(
                Calendar.DAY_OF_MONTH),8,0,0
        )

        var nowCalendar = Calendar.getInstance()


        val intent = Intent(context,AlarmBroadcastReceiver::class.java)
        val pending = PendingIntent.getBroadcast(context,0,intent,0)

        var am : AlarmManager = context?.getSystemService(AppCompatActivity.ALARM_SERVICE) as AlarmManager
        am.setExact(AlarmManager.RTC_WAKEUP,alarmCalendar.timeInMillis - nowCalendar.timeInMillis,pending)
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