package com.example.activist_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapFragment extends Fragment {

    String googleApiKey = BuildConfig.GOOGLE_API_KEY;

    private PermissionsHandler permissionsHandler = new PermissionsHandler();
    private static final int REQUEST_PERMISSION = 1;
    private FusedLocationProviderClient locationClient;


    LatLng defaultPos = new LatLng(55.658619, 12.589548); // ITU's location
    private final static int DEFAULT_ZOOM = 15;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {

            // Handle if user has not given location permission after being asked
            if (permissionsHandler.hasLocationPermission(getActivity()) == false) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultPos, DEFAULT_ZOOM));
                Toast.makeText(getActivity(), "You will not be able to use the features of the map if you do not allow access to location data.", Toast.LENGTH_LONG).show();
            }


        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        locationClient = LocationServices.getFusedLocationProviderClient(getActivity()); // is this line actually necessary?

        permissionsHandler.requestLocationPermission(getActivity(), REQUEST_PERMISSION);
        if (permissionsHandler.hasLocationPermission(getActivity()) == false) {
            Toast.makeText(getActivity(), "You will not be able to use the features of the map if you do not allow access to location data.", Toast.LENGTH_LONG).show();
        }

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

}