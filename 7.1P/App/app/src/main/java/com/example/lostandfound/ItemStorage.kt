package com.example.lostandfound

object ItemStorage {
    private val items = mutableListOf<LostFoundItem>()

    fun addItem(item: LostFoundItem) {
        items.add(item)
    }

    fun getItems(): List<LostFoundItem> {
        return items
    }

    fun removeItem(item: LostFoundItem) {
        items.remove(item)
    }
}
