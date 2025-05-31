package com.example.exploration_services;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.exploration_services.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "DEV_MainActivity";

    private ActivityMainBinding binding;
    private DataService dataService;
    private boolean isBound = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DataService.DataServiceBinder binder = (DataService.DataServiceBinder) service;
            dataService = binder.getService();
            isBound = true;
            Log.i(TAG, "Service connected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            dataService = null;
            isBound = false;
            Log.i(TAG, "Service disconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent serviceIntent = new Intent(this, DataService.class);
        if (!isBound) {
            startService(serviceIntent);
            bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
        }

    }

    public void onButtonPushClick(View view) {
        Log.i(TAG, "onButtonPushClick");
        // Push data to the service
        dataService.setData(binding.editTextTextDataToPush.getText().toString()); // Push data to the service
        binding.editTextTextDataToPush.setText(""); // Clear the EditText
    }
    public void onButtonPullClick(View view) {
        Log.i(TAG, "onButtonPullClick");
        // Pull data from the service
        if (isBound) {
            Log.i(TAG, "Data from service: " + dataService.getData());
            binding.textViewDataPulled.setText(dataService.getData());
        } else {
           if (dataService != null) {
               String data = dataService.getData();
               if (data == "null") {
                   Log.e(TAG, "No data to pull");
                   binding.textViewDataPulled.setText("No data to pull");
               } else {
                   Log.i(TAG, "Data from service: " + data);
                   binding.textViewDataPulled.setText(data);
               }
           } else {
               Log.e(TAG, "Service is not bound");
               binding.textViewDataPulled.setText("Service is not bound");
           }Intent serviceIntent = new Intent(this, DataService.class);
           startService(serviceIntent);
           bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);

        }
    }

    public void onButtonKillClick(View view) {
        // stop and destroy the service
        Log.i(TAG, "onButtonKillClick");
        Intent serviceIntent = new Intent(this, DataService.class);
        stopService(serviceIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i(TAG, "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
        Log.i(TAG, "onStop");
    }
}