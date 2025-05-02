package com.example.lostandfound

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class CreateAdvertActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_advert)

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupType)
        val editName = findViewById<EditText>(R.id.editName)
        val editPhone = findViewById<EditText>(R.id.editPhone)
        val editDescription = findViewById<EditText>(R.id.editDescription)
        val editDate = findViewById<EditText>(R.id.editDate)
        val editLocation = findViewById<EditText>(R.id.editLocation)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)

        btnSubmit.setOnClickListener {
            val postType = when (radioGroup.checkedRadioButtonId) {
                R.id.radioLost -> "Lost"
                R.id.radioFound -> "Found"
                else -> ""
            }

            val name = editName.text.toString()
            val phone = editPhone.text.toString()
            val description = editDescription.text.toString()
            val date = editDate.text.toString()
            val location = editLocation.text.toString()

            if (name.isBlank() || phone.isBlank() || description.isBlank() ||
                date.isBlank() || location.isBlank() || postType.isBlank()
            ) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Store the item (we’ll create this function and class next)
            val item = LostFoundItem(postType, name, phone, description, date, location)
            ItemStorage.addItem(item)

            Toast.makeText(this, "Advert created!", Toast.LENGTH_SHORT).show()
            finish() // Go back to the home screen
        }
    }
}
