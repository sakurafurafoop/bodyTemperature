package app.sakura.momonga.kisotaionkanri

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import io.realm.Realm
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mRealm:Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //plusボタンを押す
        plusButton.setOnClickListener {

            val plusFragment = PlusFragment()
            plusFragment.show(supportFragmentManager,"tag")
        }



        //NavigationViewを選択した時
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.graph -> {
                    //Fragmentの追加や削除などの変更を行う時はTransactionを利用する
                    val transaction = supportFragmentManager.beginTransaction()
                    //Fragmentの作成
                    val graphFragment = GraphFragment()
                    //Fragmentを組み込む(Fragmentの入るID,挿入するFragment)
                    transaction.replace(R.id.container, graphFragment)
                    //変更を保存する
                    transaction.commit()
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.list -> {
                    val transaction = supportFragmentManager.beginTransaction()
                    val listFragment = ListFragment()
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
}
