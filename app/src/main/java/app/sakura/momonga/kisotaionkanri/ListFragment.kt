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

class ListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: MyAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    var listYear: Int = 0
    var listMonth: Int = 0
    var listLastDay: Int = 0
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
        }
        val resultResultArray = mRealm.where(SaveModel::class.java).equalTo("year", listYear)
            .equalTo("month", listMonth).findAll()


        for (dayIndex in 1..listLastDay) {
            listDataSet.add(ListData(day = dayIndex, weekDay = "Mon"))
        }

        for (result in resultResultArray) {
            listDataSet[result.day - 1] =
                ListData(
                    day = result.day,
                    weekDay = "Mon",
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
