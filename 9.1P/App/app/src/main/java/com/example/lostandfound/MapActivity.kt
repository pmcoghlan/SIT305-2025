package com.example.lostandfound

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val items = ItemStorage.getItems()

        if (items.isEmpty()) {
            // No items, zoom to Australia as default
            val australia = LatLng(-25.2744, 133.7751)
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(australia, 4f))
        } else {
            var firstLocation: LatLng? = null

            for (item in items) {
                val itemLocation = LatLng(item.latitude, item.longitude)

                mMap.addMarker(
                    MarkerOptions()
                        .position(itemLocation)
                        .title("${item.type}: ${item.description}")
                        .snippet("Owner: ${item.name}\nPhone: ${item.phone}\nLocation: ${item.location}")
                        .icon(
                            if (item.type == "Lost") BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
                            else BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)
                        )
                )

                if (firstLocation == null) {
                    firstLocation = itemLocation
                }
            }

            // Focus camera on the first item
            firstLocation?.let {
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(it, 12f))
            }
        }
    }
}
