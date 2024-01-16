package com.example.activist_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MenuFragment extends Fragment {

    private TextView testText;
    private Button toTalkie;

      public MenuFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
           }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
                View v = inflater.inflate(R.layout.fragment_menu, container, false);

                testText = v.findViewById(R.id.testText);
                toTalkie = v.findViewById(R.id.toTalkieButton);

                toTalkie.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Navigation.findNavController(view).navigate(R.id.action_menuFragment_to_talkieFragment);
                    }
                });

                return v;
    }
}