package com.example.activist_app;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class PinDatabase extends ViewModel {

    private ArrayList<InfoPin> pins = new ArrayList<>();

    public PinDatabase() {
        // adding a hardcoded pin at ITU for testing purposes
        LatLng testLocation1 = new LatLng(55.660505, 12.591268);
        addPin(testLocation1, "testing 1 2.. ");
    }

    public void addPin(LatLng latLng, String message) {
        InfoPin infoPin = new InfoPin(latLng, message);
        pins.add(infoPin);
    }

    public ArrayList<InfoPin> getPins() {
        return pins;
    }

    public int getSize(){
        return pins.size();
    }

}
