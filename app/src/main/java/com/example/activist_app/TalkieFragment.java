package com.example.activist_app;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class TalkieFragment extends Fragment {


    private Button sendButton;
    private Button cancelButton;
    private Button recordButton;
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

        sendButton = v.findViewById(R.id.sendButton);
        cancelButton = v.findViewById(R.id.cancelButton);
        recordButton = v.findViewById(R.id.recordButton);

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

        // TODO: Fix bug: When opening the TalkieFragment, switchButtons() only gets
        //  executed after clicking twice on recordButton. After initial execution
        //  it it will execute on a single click.
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchButtons();
            }
        });

        return v;
    }

    private void switchButtons() {
        if (recording == true) {
            sendButton.setVisibility(View.VISIBLE);
            cancelButton.setVisibility(View.VISIBLE);
            recordButton.setVisibility(View.GONE);
            recording = false;
        } else {
            recordButton.setVisibility(View.VISIBLE);
            sendButton.setVisibility(View.GONE);
            cancelButton.setVisibility(View.GONE);
            recording = true;
        }
    }
}