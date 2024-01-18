package com.example.activist_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class TalkieFragment extends Fragment {

    private Button recordButton;
    private Button sendButton;

    public TalkieFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_talkie, container, false);

        recordButton = v.findViewById(R.id.recordButton);
        sendButton = v.findViewById(R.id.sendButton);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendButton.bringToFront();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Your message was sent to everyone.", Toast.LENGTH_LONG).show();
                recordButton.bringToFront();
            }
        });

        return v;
    }
}