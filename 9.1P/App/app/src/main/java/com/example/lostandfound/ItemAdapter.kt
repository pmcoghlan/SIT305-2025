package com.example.lostandfound

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog

class ItemAdapter(private val context: Context, private val items: MutableList<LostFoundItem>) :
    BaseAdapter() {

    override fun getCount(): Int = items.size

    override fun getItem(position: Int): Any = items[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val item = items[position]
        val view = convertView ?: LayoutInflater.from(context).inflate(
            android.R.layout.simple_list_item_2, parent, false)

        val title = view.findViewById<TextView>(android.R.id.text1)
        val subtitle = view.findViewById<TextView>(android.R.id.text2)

        title.text = "${item.type}: ${item.description}"
        subtitle.text = "At ${item.location} on ${item.date}"

        view.setOnClickListener {
            // Show full details and offer to delete
            AlertDialog.Builder(context)
                .setTitle("${item.type} Item Details")
                .setMessage("""
                    Name: ${item.name}
                    Phone: ${item.phone}
                    Description: ${item.description}
                    Date: ${item.date}
                    Location: ${item.location}
                """.trimIndent())
                .setPositiveButton("Delete") { _, _ ->
                    ItemStorage.removeItem(item)
                    notifyDataSetChanged()
                    Toast.makeText(context, "Item deleted", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Close", null)
                .show()
        }

        return view
    }
}
