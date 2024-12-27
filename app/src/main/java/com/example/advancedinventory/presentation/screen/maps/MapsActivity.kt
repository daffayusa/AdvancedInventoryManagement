package com.example.advancedinventory.presentation.screen.maps

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.advancedinventory.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.GeoPoint

import com.google.android.gms.maps.SupportMapFragment

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var selectedLocation: LatLng? = null

    companion object {
        const val LOCATION_REQUEST_CODE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Memeriksa izin lokasi
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initializeMap()
        } else {
            // Meminta izin lokasi jika belum diberikan
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
        }

        val button = findViewById<Button>(R.id.btnSaveLocation)
        button.setOnClickListener {
            selectedLocation?.let { latLng ->
                val resultIntent = Intent().apply {
                    putExtra("latitude", latLng.latitude)
                    putExtra("longitude", latLng.longitude)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } ?: run {
                Toast.makeText(this, "Lokasi belum dipilih", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun initializeMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as? SupportMapFragment

        mapFragment?.getMapAsync(this) ?: run {
            Toast.makeText(this, "Map Fragment not found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        Log.d("MapsActivity", "onMapReady: Google Map initialized")
        // Menambahkan listener untuk memilih lokasi
        mMap.setOnMapClickListener { latLng ->
            selectedLocation = latLng
            mMap.clear() // Menghapus marker lama
            mMap.addMarker(MarkerOptions().position(latLng).title("Lokasi Terpilih"))
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f)) // Menyesuaikan zoom
        }

        // Memeriksa izin lokasi dan menampilkan lokasi pengguna
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
        } else {
            // Menampilkan pesan jika izin tidak diberikan
            Toast.makeText(this, "Izin lokasi diperlukan", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Memanggil kembali initializeMap setelah izin diberikan
                initializeMap()
            } else {
                // Jika izin ditolak, beri pesan bahwa izin diperlukan
                Toast.makeText(this, "Izin lokasi diperlukan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}