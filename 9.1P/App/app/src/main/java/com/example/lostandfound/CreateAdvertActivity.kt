package com.example.lostandfound

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import java.util.*

class CreateAdvertActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    private var selectedAddress: String = ""
    private var selectedLatLng: LatLng? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_advert)

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupType)
        val editName = findViewById<EditText>(R.id.editName)
        val editPhone = findViewById<EditText>(R.id.editPhone)
        val editDescription = findViewById<EditText>(R.id.editDescription)
        val editDate = findViewById<EditText>(R.id.editDate)
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        val btnGetCurrentLocation = findViewById<Button>(R.id.btnGetCurrentLocation)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // ✅ Initialize Places with your new API key
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "API_KEY")
        }

        // Set up AutocompleteSupportFragment
        val autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocompleteFragment) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG))

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                selectedAddress = place.address ?: ""
                selectedLatLng = place.latLng
            }

            override fun onError(status: Status) {
                Toast.makeText(this@CreateAdvertActivity, "Error: ${status.statusMessage}", Toast.LENGTH_SHORT).show()
            }
        })

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

            if (name.isBlank() || phone.isBlank() || description.isBlank() ||
                date.isBlank() || selectedAddress.isBlank() || selectedLatLng == null || postType.isBlank()
            ) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val item = LostFoundItem(
                postType,
                name,
                phone,
                description,
                date,
                selectedAddress,
                selectedLatLng!!.latitude,
                selectedLatLng!!.longitude
            )

            ItemStorage.addItem(item)

            Toast.makeText(this, "Advert created!", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnGetCurrentLocation.setOnClickListener {
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                val geocoder = Geocoder(this)
                val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    selectedAddress = addresses[0].getAddressLine(0)
                    selectedLatLng = LatLng(location.latitude, location.longitude)

                    val autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocompleteFragment) as AutocompleteSupportFragment
                    autocompleteFragment.setText(selectedAddress)
                }
            } else {
                Toast.makeText(this, "Location not available. Try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
