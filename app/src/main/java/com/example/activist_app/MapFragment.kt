package com.example.activist_app

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.OnMapsSdkInitializedCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

// Some of this code is based on: https://github.com/JSDumbuya/Voyager

class MapFragment : Fragment(), OnMapsSdkInitializedCallback {

    var googleApiKey = BuildConfig.GOOGLE_API_KEY

    var defaultPos = LatLng(55.658619, 12.589548) // ITU's location
    val DEFAULT_ZOOM: Float = 15F

    private lateinit var googleMap: GoogleMap

    private lateinit var pinDB: PinDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapsInitializer.initialize(
            requireContext(),
            MapsInitializer.Renderer.LATEST, this
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        pinDB = ViewModelProvider(requireActivity())[PinDatabase::class.java]
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun checkPermission() =
        ActivityCompat.checkSelfPermission(
            requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        &&
        ActivityCompat.checkSelfPermission(
            requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap  ->
        this.googleMap = googleMap // why?????
        setAllMarkers(googleMap)

        if (ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            // Get the user's current location
            val locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0L, 0f, object :
                LocationListener {
                override fun onLocationChanged(location: Location) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, DEFAULT_ZOOM))
                    // Remove the location listener to conserve battery
                    locationManager.removeUpdates(this)
                }
                override fun onProviderDisabled(provider: String) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
            })
        }

        // refactor the checkPermission() later, it should not be negated I think
        if (!checkPermission()) {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
        }
    }

    companion object {
        private val TAG = MapFragment::class.qualifiedName
    }

    // Can this function be deleted?
    override fun onMapsSdkInitialized(renderer: MapsInitializer.Renderer) {
        when (renderer) {
            MapsInitializer.Renderer.LATEST ->
                Log.d(
                    TAG,
                    "The latest version of the renderer is used.")
            MapsInitializer.Renderer.LEGACY ->
                Log.d(
                    TAG,
                    "The legacy version of the renderer is used.")
        }
    }

    private fun setAllMarkers(googleMap: GoogleMap) {
        for (pin in pinDB.pins) {
            googleMap.addMarker(
                MarkerOptions()
                    .position(pin.position)
                    .title("Info from a friend: ")
                    .snippet(pin.message)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                    .alpha(0.4f)
            )
        }
    }

}