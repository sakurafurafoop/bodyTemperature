package app.sakura.momonga.kisotaionkanri

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.realm.Realm
import java.util.*

class ListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: MyAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    var listYear: Int = 0
    var listMonth: Int = 0
    var listLastDay: Int = 0
    var listDay: Int = 0
    var listWeekDay:Int = 0
    val calendar = Calendar.getInstance()
    val listDataSet = mutableListOf<ListData>()

    //フラグメントのviewを生成するところ
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }


    //viewが生成された
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Realm.init(context)
        val mRealm = Realm.getDefaultInstance()
        val bundle = arguments
        if (bundle != null) {
            listYear = bundle.getInt("KEY_YEAR")
            listMonth = bundle.getInt("KEY_MONTH")
            listLastDay = bundle.getInt("KEY_LASTDAY")
            listDay = bundle.getInt(("KEY_DAY"))
            listWeekDay = bundle.getInt("KEY_WEEKDAY")
        }

        val resultResultArray = mRealm.where(SaveModel::class.java).equalTo("year", listYear)
            .equalTo("month", listMonth).findAll()



        for (dayIndex in 1..listLastDay) {
            calendar.set(listYear,listMonth,dayIndex)
            var week:String = ""
            when (calendar.get(Calendar.DAY_OF_WEEK)){
                Calendar.SUNDAY -> week = "Sun"
                Calendar.MONDAY -> week = "Mon"
                Calendar.TUESDAY -> week = "Tue"
                Calendar.WEDNESDAY -> week = "Wed"
                Calendar.THURSDAY -> week = "Thu"
                Calendar.FRIDAY -> week = "Fri"
                Calendar.SATURDAY -> week ="Sat"
            }
            listDataSet.add(ListData(day = dayIndex, weekDay = week))
        }


        for (result in resultResultArray) {
            calendar.set(listYear,listMonth,result.day)
            var week:String = ""
            when (calendar.get(Calendar.DAY_OF_WEEK)){
                Calendar.SUNDAY -> week = "Sun"
                Calendar.MONDAY -> week = "Mon"
                Calendar.TUESDAY -> week = "Tue"
                Calendar.WEDNESDAY -> week = "Wed"
                Calendar.THURSDAY -> week = "Thu"
                Calendar.FRIDAY -> week = "Fri"
                Calendar.SATURDAY -> week ="Sat"
            }

            listDataSet[result.day - 1] =
                ListData(
                    day = result.day,
                    weekDay = week,
                    temperature = result.temperature.toFloat()
                )
        }


        viewManager = LinearLayoutManager(context)
        viewAdapter = MyAdapter(listDataSet,object :MyAdapter.CustomOnClickLister{
            override fun onClickLister(position: Int) {
                var bundle = Bundle()
                bundle.putInt("KEY_YEAR",listYear)
                bundle.putInt("KEY_MONTH",listMonth)
                bundle.putInt("KEY_DAY",position + 1)

                val plusFragment = PlusFragment()
                plusFragment.arguments = bundle
                plusFragment.show(fragmentManager,"tag")
            }
        })


        recyclerView = view.findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            setHasFixedSize(false)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }






}
