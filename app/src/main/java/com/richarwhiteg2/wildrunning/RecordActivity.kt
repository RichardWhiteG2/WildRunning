    package com.richarwhiteg2.wildrunning

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.content.ContextCompat

    class RecordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(R.id.toolbar_record)
        setSupportActionBar(toolbar)

        //Establecer titulo desde codigo,
        toolbar.title = getString(R.string.bar_title_record)
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.gray_dark))  // cambiar color del titulo
        toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.gray_light)) //cambiar background

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

    }
        //desde el toolbar de historial cuando das en atras manda al home
        override fun onSupportNavigateUp(): Boolean {
            onBackPressed()
            return true // super.onSupportNavigateUp()
        }

        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.order_records_by, menu)
        return true // super.onCreateOptionsMenu(menu)
    }
        //Al seleccionar una opcion la cambia por la contraria
        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId){
                R.id.orderby_date ->{
                    if (item.title ==getString(R.string.orderby_dateZA)){
                        item.title =getString(R.string.orderby_dateAZ)
                    }
                    else{
                        item.title =getString(R.string.orderby_dateZA)
                    }
                    return true
                }
                R.id.orderby_duration ->{
                    if (item.title ==getString(R.string.orderby_durationZA)){
                        item.title =getString(R.string.orderby_durationAZ)
                    }
                    else{
                        item.title =getString(R.string.orderby_durationZA)
                    }
                    return true
                }
                R.id.orderby_distance ->{
                    if (item.title ==getString(R.string.orderby_distanceZA)){
                        item.title =getString(R.string.orderby_distanceAZ)
                    }
                    else{
                        item.title =getString(R.string.orderby_distanceZA)
                    }
                    return true
                }
                R.id.orderby_avgspeed ->{
                    if (item.title ==getString(R.string.orderby_avgspeedZA)){
                        item.title =getString(R.string.orderby_avgspeedAZ)
                    }
                    else{
                        item.title =getString(R.string.orderby_avgspeedZA)
                    }
                    return true
                }
                R.id.orderby_maxspeed ->{
                    if (item.title ==getString(R.string.orderby_maxspeedZA)){
                        item.title =getString(R.string.orderby_maxspeedAZ)
                    }
                    else{
                        item.title =getString(R.string.orderby_maxspeedZA)
                    }
                    return true
                }
            }
            return super.onOptionsItemSelected(item)
        }
        //Con el boton flotante manda al home
        fun callHome(v: View){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
}