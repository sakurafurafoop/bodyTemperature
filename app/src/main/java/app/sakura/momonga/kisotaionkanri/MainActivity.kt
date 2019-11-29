package app.sakura.momonga.kisotaionkanri

import android.app.*
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import java.time.Month
import java.time.Year
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var mRealm:Realm
    var toolbarTitle:String = ""
    var year: Int = 0
    var month: Int = 0
    var day:Int = 0
    var lastDay: Int = 0
    val calendar = Calendar.getInstance()


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DATE)
        lastDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
        toolbar.setTitle(month.toString())


        //plusボタンを押す
        plusButton.setOnClickListener {
            var bundle = Bundle()
            bundle.putInt("KEY_YEAR",year)
            bundle.putInt("KEY_MONTH",month)
            bundle.putInt("KEY_DAY",day)
            bundle.putInt("KEY_LASTDAY",lastDay)
            val plusFragment = PlusFragment()
            plusFragment.arguments = bundle
            plusFragment.show(supportFragmentManager,"tag")
            sendAlerm()
        }



        //NavigationViewを選択した時
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.graph -> {
                    var bundle = Bundle()
                    bundle.putInt("KEY_YEAR",year)
                    bundle.putInt("KEY_MONTH",month)
                    bundle.putInt("KEY_LASTDAY",lastDay)
                    //Fragmentの追加や削除などの変更を行う時はTransactionを利用する
                    val transaction = supportFragmentManager.beginTransaction()
                    //Fragmentの作成
                    val graphFragment = GraphFragment()
                    graphFragment.arguments = bundle
                    //Fragmentを組み込む(Fragmentの入るID,挿入するFragment)
                    transaction.replace(R.id.container, graphFragment)
                    //変更を保存する
                    transaction.commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.list -> {
                    var bundle = Bundle()
                    bundle.putInt("KEY_YEAR",year)
                    bundle.putInt("KEY_MONTH",month)
                    bundle.putInt("KEY_LASTDAY",lastDay)
                    val transaction = supportFragmentManager.beginTransaction()
                    val listFragment = ListFragment()
                    listFragment.arguments = bundle
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
    fun sendAlerm(){
        var calendar = Calendar.getInstance()
        //calendar.timeInMillis = System.currentTimeMillis()
        //calendar.add(Calendar.SECOND,3)
        //var hour = calendar.get(Calendar.HOUR_OF_DAY)

        val intent = Intent(this,AlarmBroadcastReceiver::class.java)
        val pending = PendingIntent.getBroadcast(this,0,intent,0)

        var am : AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        am.setExact(AlarmManager.RTC_WAKEUP,calendar.timeInMillis,pending)

    }
}
