package com.example.activist_app;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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

            if (ContextCompat.checkSelfPermission(
                    getContext()
                    , Manifest.permission.ACCESS_FINE_LOCATION
            ) ==
                    PackageManager.PERMISSION_GRANTED) {
                // This is where we will get the current location and zoom to it.
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultPos, DEFAULT_ZOOM));
                Toast.makeText(getActivity(), "You have allowed location tracking and something might happen now!!", Toast.LENGTH_LONG).show();

            } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                    getActivity(), Manifest.permission.ACCESS_FINE_LOCATION

            )) {
                Toast.makeText(getActivity(), "Here you will be informed that you cannot use the map :-(", Toast.LENGTH_LONG).show();
            } else {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                Toast.makeText(getActivity(), "You will probably be asked if you will allow us to track your location in 3.. 2.. 1..", Toast.LENGTH_LONG).show();
                requestPermissionLauncher.launch(
                        Manifest.permission.ACCESS_FINE_LOCATION
                );
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

        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);

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

    private ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    Toast.makeText(getActivity(), "isGranted() is true", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getActivity(), "isGranted() is false", Toast.LENGTH_LONG).show();
                }
            });


}