package com.example.lostandfound

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class ItemListActivity : AppCompatActivity() {
    private lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_list)

        val listView = findViewById<ListView>(R.id.listViewItems)
        adapter = ItemAdapter(this, ItemStorage.getItems().toMutableList())
        listView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}
