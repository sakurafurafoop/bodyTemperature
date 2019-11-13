package app.sakura.momonga.kisotaionkanri

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_list.*
import java.util.*

class ListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    val listDataSet = listOf<ListData>(ListData(date = Date(),temperature = 36.5F),ListData(date = Date(),temperature = 36.3F),ListData(date = Date(),temperature = 36.2F))

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

        viewManager = LinearLayoutManager(context)
        viewAdapter = MyAdapter(listDataSet)

        recyclerView = view.findViewById<RecyclerView>(R.id.my_recycler_view).apply{
            setHasFixedSize(false)
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

}
