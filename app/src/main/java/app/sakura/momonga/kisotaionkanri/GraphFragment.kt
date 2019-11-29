package app.sakura.momonga.kisotaionkanri

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_graph.*

class GraphFragment : Fragment() {
    var graphYear: Int = 0
    var graphMonth: Int = 0
    var graphLastDay: Int = 0

    private var mTypeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_graph, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            graphYear = bundle.getInt("KEY_YEAR")
            graphMonth = bundle.getInt("KEY_MONTH")
            graphLastDay = bundle.getInt("KEY_LASTDAY")
        }

        setupLineChart()
        lineChart.data = lineDataWithCount(30, 100f)

    }

    private fun setupLineChart() {
        lineChart.apply {

            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            isScaleXEnabled = true
            setPinchZoom(false)
            setDrawGridBackground(true)

            //データラベルの表示
            legend.apply {
                form = Legend.LegendForm.LINE
                typeface = mTypeface
                textSize = 11f
                textColor = Color.BLACK
                verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
                horizontalAlignment = Legend.LegendHorizontalAlignment.LEFT
                orientation = Legend.LegendOrientation.HORIZONTAL
                setDrawInside(false)
            }

            //y軸右側の表示
            axisRight.isEnabled = false

            //X軸表示
            xAxis.apply {
                typeface = mTypeface
                setDrawLabels(true)
                setDrawGridLines(false)
            }

            //y軸左側の表示
            axisLeft.apply {
                typeface = mTypeface
                textColor = Color.BLACK
                setDrawGridLines(true)
            }
        }
    }

    private fun lineDataWithCount(count: Int, range: Float):LineData {

        //val values = mutableListOf<Entry>()
        Realm.init(context)
        val mRealm = Realm.getDefaultInstance()
        val values = mRealm.where(SaveModel::class.java).equalTo("year", graphYear)
            .equalTo("month", graphMonth).findAll()

        val entryArray = mutableListOf<Entry>()


        for (result in values) {
            val entry = Entry(result.day.toFloat(),result.temperature.toFloat())
            entryArray.add(entry)
        }

        for (dayIndex in 1..graphLastDay) {
            val entry = Entry(dayIndex.toFloat(),0.toFloat())
            entryArray.add(entry)
        }


//        for ((index, element) in values.withIndex()) {
//            val entry = Entry(index.toFloat(),element.temperature.toFloat())
//            entryArray.add(entry) 
//        }
        Log.d("entry",entryArray.toString())

        // create a dataset and give it a type
        val yVals = LineDataSet(entryArray,"").apply {
            axisDependency =  YAxis.AxisDependency.LEFT
            color = Color.BLACK
            highLightColor = Color.YELLOW
            setDrawCircles(false)
            setDrawCircleHole(true)
            setDrawValues(false)
            lineWidth = 0.5f
        }
        val data = LineData(yVals)
        data.setValueTextColor(Color.BLACK)
        data.setValueTextSize(9f)
        return data
    }


}
