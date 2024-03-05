package com.example.activist_app

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.OnMapsSdkInitializedCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

// Some of this code is based on: https://github.com/JSDumbuya/Voyager

class MapFragment : Fragment(), OnMapsSdkInitializedCallback {

    var googleApiKey = BuildConfig.GOOGLE_API_KEY
    var firebaseURL = BuildConfig.FIREBASE_REALTIME_URL

    var defaultPos = LatLng(55.658619, 12.589548) // ITU's location
    val DEFAULT_ZOOM: Float = 15F

    private lateinit var googleMap: GoogleMap

    private lateinit var topMenu: MaterialToolbar
    private lateinit var locationManager: LocationManager

    private lateinit var okButton: MaterialButton

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

        //val database = Firebase.database(firebaseURL)

        // add a pin no. 1 as a test (it works)
        /*pinsRef.child("1").child("message").setValue("testing for pin no. 1")
        positionsRef.child("1").child("lat").setValue(55.658619)
        positionsRef.child("1").child("lng").setValue(12.589548)*/

        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        locationManager = requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        topMenu = requireView().findViewById(R.id.top_menu)
        topMenu.setOnMenuItemClickListener(Toolbar.OnMenuItemClickListener { item ->
            if (item.itemId == R.id.placePin) {
                //Toast.makeText(context, "placePin clicked", Toast.LENGTH_LONG).show()
                handlePlacePinClick()
                return@OnMenuItemClickListener true
            }
            false
        })
    }

    @SuppressLint("MissingPermission")
    private fun handlePlacePinClick() {

        var currentLatLng: LatLng = LatLng(55.658619,12.589548)
        googleMap.addMarker(
            MarkerOptions()
                .position(currentLatLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))
                .draggable(true)
        )

        okButton = requireView().findViewById(R.id.ok_button)
        okButton.visibility = View.VISIBLE
        okButton.setOnClickListener{
            Toast.makeText(context, "ok button clicked!", Toast.LENGTH_LONG) .show()
        }

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
        googleMap.clear()

        if (ActivityCompat.checkSelfPermission(requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        } else {
            // Get the user's current location
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

        Firebase.database(firebaseURL).reference.apply {
            keepSynced(true)
        }.child("infopins2").addValueEventListener(object : ValueEventListener  {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var i = 1
                for (pin in dataSnapshot.children) {
                    var message = dataSnapshot.child(i.toString()).child("message").getValue<String>()
                    var lat = dataSnapshot.child(i.toString()).child("position").child("lat").getValue<Double>()
                    var lng = dataSnapshot.child(i.toString()).child("position").child("lng").getValue<Double>()
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(lat!!,lng!!))
                            .title("Info from a friend: ")
                            .snippet(message)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                            .alpha(0.4f)
                    )
                    i++ // there must be a way to use the key instead of doing this silly little counter
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Loadpost: onCancelled")
            }
        })
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

    }



}