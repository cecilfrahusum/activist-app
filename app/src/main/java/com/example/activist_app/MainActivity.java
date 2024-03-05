package com.example.activist_app;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    MaterialToolbar topMenu;
    NavigationBarView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topMenu = findViewById(R.id.top_menu);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        topMenu.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.placePin) {
                    Toast.makeText(getApplicationContext(), "placePin clicked", Toast.LENGTH_LONG).show();
                    return true;
                }
                return false;
            }
        });

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        // Thanks to this video: https://www.youtube.com/watch?v=pT_4rV3gO78
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
    }



}
