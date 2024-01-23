package com.example.activist_app;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    NavigationBarView bottomNavigationView;

    ChatFragment chatFragment = new ChatFragment();
    MapFragment mapFragment = new MapFragment();
    TalkieFragment talkieFragment = new TalkieFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        setUpBottomNav();
    }

    // Inspired by this tutorial and the comment section:
    // https://gist.github.com/codinginflow/8a728a27a78e92876ca1c71b3dce28f6?permalink_comment_id=4452667#gistcomment-4452667
    private void setUpBottomNav(){
        bottomNavigationView.setSelectedItemId(R.id.map);
        loadFragment(new MapFragment());

        // Clean up this code later :-)
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.map){
                    loadFragment(new MapFragment());
                } else if (id == R.id.talkie) {
                    loadFragment(new TalkieFragment());
                } else if (id == R.id.chat) {
                    loadFragment(new ChatFragment());
                } else {
                    return false;
                }
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
