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

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.OnMapsSdkInitializedCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.Firebase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

// Some of this code is based on: https://github.com/JSDumbuya/Voyager

class MapFragment : Fragment(), OnMapsSdkInitializedCallback {

    var googleApiKey = BuildConfig.GOOGLE_API_KEY
    var firebaseURL = BuildConfig.FIREBASE_REALTIME_URL

    var defaultPos = LatLng(55.658619, 12.589548) // ITU's location
    val DEFAULT_ZOOM: Float = 15F

    private lateinit var googleMap: GoogleMap

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

        val database = Firebase.database(firebaseURL)
        val pinsRef = database.getReference("infopins")
        val positionsRef = database.getReference("positions")

        // add a pin no. 1 as a test (it works)
        pinsRef.child("1").child("message").setValue("testing for pin no. 1")
        positionsRef.child("1").child("lat").setValue(55.658619)
        positionsRef.child("1").child("lng").setValue(55.658619)

        /*pinsReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = dataSnapshot.getValue<String>()
                setAllMarkers(googleMap)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })*/

        /*val pinListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val pin = dataSnapshot.getValue<InfoPin>()
                setAllMarkers(googleMap)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "loadPin:onCancelled", databaseError.toException())
            }
        }
        pinReference.addValueEventListener(pinListener)*/

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
        val pins: ArrayList<InfoPin> = ArrayList()
        // here: somehow add from database to list ?

        for (pin in pins) {
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