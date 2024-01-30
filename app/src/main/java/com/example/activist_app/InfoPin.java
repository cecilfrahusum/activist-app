package com.example.activist_app;

import com.google.android.gms.maps.model.LatLng;

public class InfoPin {

    private LatLng position;
    private String message;

    public InfoPin(LatLng position, String message) {
        this.position = position;
        this.message = message;
    }

    public LatLng getPosition() {
        return position;
    }

    public String getMessage() {
        return message;
    }
}
