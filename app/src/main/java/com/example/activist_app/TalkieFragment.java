package com.example.activist_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TalkieFragment extends Fragment {

    private Button sendButton;
    private Button cancelButton;
    private Button recordButton;
    private TextView recordingText;

    private boolean recording = false;

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

        recordingText = v.findViewById(R.id.recordingText);
        sendButton = v.findViewById(R.id.sendButton);
        recordButton = v.findViewById(R.id.recordButton);
        cancelButton = v.findViewById(R.id.cancelButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Your message was sent to everyone.", Toast.LENGTH_LONG).show();
                switchButtons();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Your message was deleted. Want to start over?", Toast.LENGTH_LONG).show();
                switchButtons();
            }
        });

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchButtons();
            }
        });

        return v;
    }

    private void switchButtons() {
        if (recording == false) {
            recordingText.setVisibility(View.VISIBLE);
            sendButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
            recordButton.setVisibility(View.GONE);
            recording = true;
        } else {
            recordingText.setVisibility(View.INVISIBLE);
            recordButton.setVisibility(View.VISIBLE);
            sendButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
            recording = false;
        }
    }

}