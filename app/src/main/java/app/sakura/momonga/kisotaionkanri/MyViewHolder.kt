package app.sakura.momonga.kisotaionkanri

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
    var dateView: TextView = itemView.findViewById(R.id.dateView)
    var weekView: TextView = itemView.findViewById(R.id.weekView)
    var temperatureView: TextView = itemView.findViewById(R.id.temperatureView)
    var plusminusView:TextView = itemView.findViewById(R.id.plusminusView)
}