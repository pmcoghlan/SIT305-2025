package com.example.lostandfound

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCreateAdvert = findViewById<Button>(R.id.btnCreateAdvert)
        val btnShowItems = findViewById<Button>(R.id.btnShowItems)
        val btnShowOnMap = findViewById<Button>(R.id.btnShowOnMap)

        btnCreateAdvert.setOnClickListener {
            val intent = Intent(this, CreateAdvertActivity::class.java)
            startActivity(intent)
        }

        btnShowItems.setOnClickListener {
            val intent = Intent(this, ItemListActivity::class.java)
            startActivity(intent)
        }

        btnShowOnMap.setOnClickListener {
            val intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
    }
}
