package app.sakura.momonga.kisotaionkanri

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var mRealm:Realm
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //plusボタンを押す
        plusButton.setOnClickListener {

            val plusFragment = PlusFragment()
            plusFragment.show(supportFragmentManager,"tag")
            sendAlerm()
            //sendNotification("体温を計りましょう！","おはようございます！")
        }



        //NavigationViewを選択した時
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.graph -> {
                    //Fragmentの追加や削除などの変更を行う時はTransactionを利用する
                    val transaction = supportFragmentManager.beginTransaction()
                    //Fragmentの作成
                    val graphFragment = GraphFragment()
                    //Fragmentを組み込む(Fragmentの入るID,挿入するFragment)
                    transaction.replace(R.id.container, graphFragment)
                    //変更を保存する
                    transaction.commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.list -> {
                    val transaction = supportFragmentManager.beginTransaction()
                    val listFragment = ListFragment()
                    transaction.replace(R.id.container, listFragment)
                    transaction.commit()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {

                    return@setOnNavigationItemSelectedListener true
                }
            }
        }

        Realm.init(this)
        mRealm = Realm.getDefaultInstance()
    }

    override fun onDestroy() {
        super.onDestroy()
        mRealm.close()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun sendNotification(contentTitle: String,contentText:String) {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

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
            .Builder(this, id)
            .apply {
                setSmallIcon(R.drawable.ic_launcher_background)
                setContentTitle(contentTitle)
                setContentText(contentText)
            }.build()
        notificationManager.notify(1, notification)
    }



    @RequiresApi(Build.VERSION_CODES.O)
    fun sendAlerm(){
        var calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(Calendar.SECOND,3)

        val intent = Intent(this,AlarmBroadcastReceiver::class.java)
        val pending = PendingIntent.getBroadcast(this,0,intent,0)


        var am : AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        am.setExact(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pending)

    }
}
