package app.sakura.momonga.kisotaionkanri

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {

                R.id.graph -> {
                    val transaction = supportFragmentManager.beginTransaction()
                    val graphFragment = GraphFragment()
                    transaction.replace(R.id.container, graphFragment)
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
    }
}
