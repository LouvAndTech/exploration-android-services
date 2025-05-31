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

        /**
         * This part create a new service Intent at the start of the app and bind it to this activity.
         * If the service is already running, it will bind to the existing service.
         * Binding the service allows the activity to communicate with the service.
         */
        Intent serviceIntent = new Intent(this, DataService.class);
        if (!isBound) {
            startService(serviceIntent);
            bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
        }

    }

    public void onButtonPushClick(View view) {
        Log.i(TAG, "onButtonPushClick");
        
        /**
         * This part retrieves the text from the EditText and pushes it to the service.
         */
        dataService.setData(binding.editTextTextDataToPush.getText().toString()); // Push data to the service
        
        binding.editTextTextDataToPush.setText(""); // Clear the EditText
    }
    public void onButtonPullClick(View view) {
        Log.i(TAG, "onButtonPullClick");

        /**
         * This part pulls the data from the service and displays it in the TextView.
         * If the service is bound, it retrieves the data directly from the service.
         * If not bound, it checks if the service is null or if there is no data to pull.
         * If the service is not bound, it starts the service and binds to it.
         */
        if (isBound) {
            Log.i(TAG, "Data from service: " + dataService.getData());
            binding.textViewDataPulled.setText(dataService.getData());
        } else {
           if (dataService != null) {
               String data = dataService.getData();
               if ("null".equals(data) || data == null || data.trim().isEmpty()) { 
                    // For some reason, obscure to me I can never enter in this condition even after testing every type of null return.
                    // This does not break the process but it's far from ideal.
                   Log.e(TAG, "No data to pull");
                   binding.textViewDataPulled.setText("No data to pull");
               } else {
                   Log.i(TAG, "Data from service: " + data);
                   binding.textViewDataPulled.setText(data);
               }
           }else {
               Log.e(TAG, "Service is not bound");
               binding.textViewDataPulled.setText("Service is not bound");
           }

           Intent serviceIntent = new Intent(this, DataService.class);
           startService(serviceIntent);
           bindService(serviceIntent, serviceConnection, BIND_AUTO_CREATE);
        }
    }

    public void onButtonKillClick(View view) {
        // stop and destroy the service
        Log.i(TAG, "onButtonKillClick");
        
        /**
         * This part stops the service and unbinds it from the activity.
         * It ensures that the service is no longer running and cleans up resources.
         */
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