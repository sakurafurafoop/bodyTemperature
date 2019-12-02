package app.sakura.momonga.kisotaionkanri

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.intArrayOf as intArrayOf1
import android.widget.ArrayAdapter
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    lateinit var mRealm:Realm
    var year: Int = 0
    var month: Int = 0
    var day:Int = 0
    var lastDay: Int = 0
    var hour: Int = 0
    val calendar = Calendar.getInstance()
    val dataStore:SharedPreferences by lazy {
        getSharedPreferences("DataStore", MODE_PRIVATE)
    }
    val editor by lazy{
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
        Log.d("hour",hour.toString())
        lastDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH)
        toolbar.setTitle(changeSetTitle(month))
        sendMoriningAlerm()
        var spinnerItems = arrayOf(month - 1,month,month + 1)

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            spinnerItems
        )

        spinner.setAdapter(adapter)

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

            }

            //　アイテムが選択された時
            fun onItemSelected(
                parent: AdapterView<*>,
                view: Context, position: Int, id: Long
            ) {
                val spinner = parent as Spinner
                val item = spinner.selectedItem as String
                val addNum:Int
                addNum = month - position
                calendar.add(month,addNum)
                toolbar.setTitle(changeSetTitle(month))
            }

            //　アイテムが選択されなかった
            override fun onNothingSelected(parent: AdapterView<*>) {
                //
            }
        }

//        if(!dataStore.getBoolean("AlermSet",false)){
//            sendMoriningAlerm()
//        }


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
            //sendMoriningAlerm()
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
                    bundle.putInt("KEY_DAY",day)
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
    fun sendMoriningAlerm(){
        var alarmCalendar = Calendar.getInstance()
        //alarmCalendar.add(Calendar.DAY_OF_MONTH,0)
        alarmCalendar.set(
            alarmCalendar.get(Calendar.YEAR),alarmCalendar.get(Calendar.MONTH),alarmCalendar.get(Calendar.DAY_OF_MONTH),1,25,0
        )
        Log.d("alerm",alarmCalendar.toString())
        Log.d("timeIn",alarmCalendar.timeInMillis.toString())
        var nowCalendar = Calendar.getInstance()
        Log.d("now",nowCalendar.timeInMillis.toString())


        val intent = Intent(this,AlarmBroadcastReceiver::class.java)
        val pending = PendingIntent.getBroadcast(this,0,intent,0)

        var am : AlarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,60000,pending)
        Log.d("calender",(alarmCalendar.timeInMillis - nowCalendar.timeInMillis).toString())
        editor.putBoolean("AlermSet",true)
        editor.apply()
    }

    fun sendEvningAlerm(){

    }

    fun changeSetTitle(month:Int):String{
        var title = ""
        when(month){
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
