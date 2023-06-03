package com.riski.mystorylife.ui.maps

import android.content.Context
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.riski.mystorylife.R
import com.riski.mystorylife.ViewModelFactory
import com.riski.mystorylife.data.model.Preference
import com.riski.mystorylife.databinding.ActivityMapsBinding
import com.riski.mystorylife.ui.main.MainViewModel
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mapsViewModel: MainViewModel

    private val mapCallback = OnMapReadyCallback { Map ->

        lifecycleScope.launch {
            mapsViewModel.getUser().observe(this@MapsActivity) {
                mapsViewModel.getLocation(it.token)
            }

            mapsViewModel.storyMain.observe(this@MapsActivity) { list ->
                list.forEach {
                    val location = LatLng(it.lat, it.lon)
                    Map.uiSettings.isZoomControlsEnabled = true
                    try {
                            val success = Map.setMapStyle(MapStyleOptions.loadRawResourceStyle(applicationContext, R.raw.map_style))
                            if (!success) {
                                Log.e(TAG,getString(R.string.maps_error))
                            }
                        } catch (exception: Resources.NotFoundException) {
                            Log.e(TAG,getString(R.string.maps_error), exception)
                        }
                        Map.addMarker(MarkerOptions().position(location).title(it.name).snippet(it.description).snippet(it.createdAt))
                        Map.animateCamera(CameraUpdateFactory.newLatLng(location))
                        Map.animateCamera(CameraUpdateFactory.newLatLngZoom(location,10f))
                }
            }
        }}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = getString(R.string.mapsactivity)
            setDisplayHomeAsUpEnabled(true)
        }

        mapsViewModel = ViewModelProvider(this, ViewModelFactory(Preference.getInstance(dataStore),this))[MainViewModel::class.java]

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(mapCallback)
    }

    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val TAG = "MapsActivity"
    }
}