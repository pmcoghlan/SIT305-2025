package com.example.lostandfound

data class LostFoundItem(
    val type: String,         // "Lost" or "Found"
    val name: String,
    val phone: String,
    val description: String,
    val date: String,
    val location: String
)
