package app.sakura.momonga.kisotaionkanri

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import io.realm.Realm
import kotlinx.android.synthetic.main.fragment_plus.*
import kotlinx.android.synthetic.main.fragment_plus.view.*
import java.util.*

class PlusFragment : DialogFragment() {
    var plusYear: Int = 0
    var plusMonth: Int = 0
    var plusDay: Int = 0
    var plusLastDay: Int = 0
    var saveWater: Int = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Realm.init(context)
        val mRealm = Realm.getDefaultInstance()
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val bundle = arguments
            if (bundle != null) {
                plusYear = bundle.getInt("KEY_YEAR")
                plusMonth = bundle.getInt("KEY_MONTH")
                plusDay = bundle.getInt("KEY_DAY")
                plusLastDay = bundle.getInt("KEY_LASTDAY")
                Log.d("plusDay",plusDay.toString())
            }

            val inflater = requireActivity().layoutInflater;
            val view = inflater.inflate(R.layout.fragment_plus, null)
//
//            water1.setOnClickListener {
//                saveWater = 1
//            }
//
//            water2.setOnClickListener {
//                saveWater = 2
//            }
//
//            water3.setOnClickListener {
//                saveWater = 3
//            }

            builder.setView(view)

            builder.setPositiveButton("Yes",object :DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {
                    mRealm.executeTransaction { realm ->
                        var saveModel = mRealm.createObject(SaveModel::class.java , UUID.randomUUID().toString())
                        val resultArray = mRealm.where(SaveModel::class.java).equalTo("year",plusYear).equalTo("month",plusMonth).equalTo("day",plusDay).findAll()

                        for(result in resultArray){
                            result.deleteFromRealm()
                        }

                        saveModel.day = plusDay
                        saveModel.year = plusYear
                        saveModel.month = plusMonth
                        saveModel.temperature = view?.editText?.text.toString().toFloat()
                        //saveModel.water = saveWater

                        realm.copyToRealm(saveModel)
                    }
                }
            })
            builder.setNegativeButton("Cancel",object :DialogInterface.OnClickListener{
                override fun onClick(dialog: DialogInterface?, which: Int) {

                }
            })
            builder.create()

        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)


    }

}
