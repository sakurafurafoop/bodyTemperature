package app.sakura.momonga.kisotaionkanri

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val listDataSet:List<ListData>):RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val listView = LayoutInflater.from(parent.context).inflate(R.layout.list,parent,false)
        return MyViewHolder(listView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.dateView.text = listDataSet[position].date.toString()
        holder.temperatureView.text = listDataSet[position].temperature.toString()
    }

    override fun getItemCount() = listDataSet.size

}