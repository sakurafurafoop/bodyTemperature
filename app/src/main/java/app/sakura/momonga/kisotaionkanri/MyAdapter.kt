package app.sakura.momonga.kisotaionkanri

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.text.ParsePosition

open class MyAdapter(private val listDataSet:List<ListData>,private var listener: CustomOnClickLister):RecyclerView.Adapter<MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val listView = LayoutInflater.from(parent.context).inflate(R.layout.list,parent,false)
        return MyViewHolder(listView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.dateView.text = listDataSet[position].day.toString()
        holder.weekView.text = listDataSet[position].weekDay
        holder.temperatureView.text = listDataSet[position].temperature.toString()
        holder.plusminusView.text = listDataSet[position].difference.toString()
        holder.itemView.setOnClickListener{
            listener.onClickLister(position)
        }
    }

    override fun getItemCount() = listDataSet.size


    interface CustomOnClickLister {
        fun onClickLister(position: Int)
    }


}
