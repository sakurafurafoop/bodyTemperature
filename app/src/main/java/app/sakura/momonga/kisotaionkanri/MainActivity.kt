package app.sakura.momonga.kisotaionkanri

import android.app.*
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.RequiresApi
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity() {

    lateinit var mRealm: Realm

    //現在の年,月,日,時間,今月の最終日を取得
    var year: Int = 0
    var month: Int = 0
    var day: Int = 0
    var hour: Int = 0
    var lastDay: Int = 0
    val calendar = Calendar.getInstance()


    val dataStore: SharedPreferences by lazy {
        getSharedPreferences("DataStore", MODE_PRIVATE)
    }
    val editor by lazy {
        dataStore.edit()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        year = calendar.get(Calendar.YEAR)
        month = calendar.get(Calendar.MONTH)
        day = calendar.get(Calendar.DATE)
        hour = calendar.get(Calendar.HOUR_OF_DAY)
        lastDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)//今月のラストを取得

        toolbar.setSubtitle(year.toString())
        toolbar.setTitle(changeSetTitle(month))


        readList()

//        var spinnerItems = arrayOf("${month + 1}", "$month", "${month - 1}")
//
//        val adapter = ArrayAdapter(
//            this,
//            android.R.layout.simple_spinner_item,
//            spinnerItems
//        )
//
//        spinner.setAdapter(adapter)
//
//        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//
//            }
//
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                val spinner = parent as Spinner
//
//                val item = spinner.selectedItem as String
//
//
//                year = calendar.get(Calendar.YEAR)
//                month = calendar.get(Calendar.MONTH)
//                day = calendar.get(Calendar.DATE)
//                hour = calendar.get(Calendar.HOUR_OF_DAY)
//
//                val diff = month - item.toInt()
//                calendar.add(Calendar.MONTH, diff)
//
//                year = calendar.get(Calendar.YEAR)
//                month = calendar.get(Calendar.MONTH)
//                day = calendar.get(Calendar.DATE)
//                hour = calendar.get(Calendar.HOUR_OF_DAY)
//
//                lastDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
//                toolbar.setTitle(changeSetTitle(month))
//            }
//
//
//        }

        if(!dataStore.getBoolean("AlermSet",false)){
            sendMoriningAlerm(8)
            sendEvningAlerm(22)
        }


        //plusボタンを押す
        plusButton.setOnClickListener {
            //bundleを使用して値の受け渡し
            var bundle = Bundle()
            bundle.putInt("KEY_YEAR", year)
            bundle.putInt("KEY_MONTH", month)
            bundle.putInt("KEY_DAY", day)
            bundle.putInt("KEY_LASTDAY", lastDay)
            val plusFragment = PlusFragment()

            //??下2文の処理が分からない
            plusFragment.arguments = bundle
            plusFragment.show(supportFragmentManager, "tag")
        }


        //NavigationViewを選択した時
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.graph -> {

                    readGraph()
                    //??何で返り値がこれ
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.list -> {
                    readList()
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {

                    return@setOnNavigationItemSelectedListener true
                }
            }
        }

        //??何でここで初期化する？
        Realm.init(this)
        //??意味が理解できない
        mRealm = Realm.getDefaultInstance()
    }

    fun readGraph(){
        var bundle = Bundle()
        bundle.putInt("KEY_YEAR", year)
        bundle.putInt("KEY_MONTH", month)
        bundle.putInt("KEY_LASTDAY", lastDay)
        //Fragmentの追加や削除などの変更を行う時はTransactionを利用する
        val transaction = supportFragmentManager.beginTransaction()
        //Fragmentの作成
        val graphFragment = GraphFragment()
        graphFragment.arguments = bundle
        //Fragmentを組み込む(Fragmentの入るID,挿入するFragment)
        transaction.replace(R.id.container, graphFragment)
        //変更を保存する
        transaction.commit()
    }

    fun readList(){
        var bundle = Bundle()
        bundle.putInt("KEY_YEAR", year)
        bundle.putInt("KEY_MONTH", month)
        bundle.putInt("KEY_LASTDAY", lastDay)
        bundle.putInt("KEY_DAY", day)
        val transaction = supportFragmentManager.beginTransaction()
        val listFragment = ListFragment()
        listFragment.arguments = bundle
        transaction.replace(R.id.container, listFragment)
        transaction.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        mRealm.close()
    }

    //??ここはうまくいかない
    @RequiresApi(Build.VERSION_CODES.O)
    fun sendMoriningAlerm(hour: Int) {
        var alarmCalendar = Calendar.getInstance()
        alarmCalendar.set(
            alarmCalendar.get(Calendar.YEAR),
            alarmCalendar.get(Calendar.MONTH),
            alarmCalendar.get(Calendar.DAY_OF_MONTH),
            hour,
            0,
            0
        )
        alarmCalendar.add(Calendar.DAY_OF_MONTH,1)

        var nowCalendar = Calendar.getInstance()

        val intent = Intent(this, AlarmBroadcastReceiver::class.java)
        val pending = PendingIntent.getBroadcast(this, 0, intent, 0)

        var am: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        am.setRepeating(AlarmManager.RTC_WAKEUP, alarmCalendar.timeInMillis,AlarmManager.INTERVAL_DAY,pending)
        Log.d("calender", (alarmCalendar.timeInMillis - nowCalendar.timeInMillis).toString())
        editor.putBoolean("AlermSet", true)
        editor.apply()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sendEvningAlerm(hour: Int) {
        var alarmCalendar = Calendar.getInstance()
        alarmCalendar.set(
            alarmCalendar.get(Calendar.YEAR),
            alarmCalendar.get(Calendar.MONTH),
            alarmCalendar.get(Calendar.DAY_OF_MONTH),
            hour,
            0,
            0
        )
        alarmCalendar.add(Calendar.DAY_OF_MONTH,1)

        var nowCalendar = Calendar.getInstance()

        val intent = Intent(this, AlarmBroadEvning::class.java)
        val pending = PendingIntent.getBroadcast(this, 0, intent, 0)

        var am: AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        am.setRepeating(AlarmManager.RTC_WAKEUP, alarmCalendar.timeInMillis,AlarmManager.INTERVAL_DAY,pending)
        Log.d("calender", (alarmCalendar.timeInMillis - nowCalendar.timeInMillis).toString())
        editor.putBoolean("AlermSet", true)
        editor.apply()
    }



    fun changeSetTitle(month: Int): String {
        var title = ""
        when (month) {
            0 -> title = "January"
            1 -> title = "February"
            2 -> title = "March"
            3 -> title = "April"
            4 -> title = "May"
            5 -> title = "June"
            6 -> title = "July"
            7 -> title = "August"
            8 -> title = "September"
            9 -> title = "October"
            10 -> title = "November"
            11 -> title = "December"
        }
        return title

    }


}
