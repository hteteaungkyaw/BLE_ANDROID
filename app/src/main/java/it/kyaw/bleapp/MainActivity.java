package it.kyaw.bleapp;

//package com.example.bleapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_FINE_LOCATION = 2;

    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private DeviceAdapter deviceAdapter;
    private List<String> deviceList = new ArrayList<>();
    private boolean isScanning = false;

    Button stoptbtn,startbtn;
    ImageView bt_search;
    TextView bt_text;

//    private static final String DEVICE_UUID = "00001101-0000-1000-8000-00805F9B34FB";

    private static final long SCAN_PERIOD = 3000;

    private Handler handler;




    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Request permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
            }
        }

        // Initialize Bluetooth
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

        // Initialize RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        deviceAdapter = new DeviceAdapter(this,deviceList);
        recyclerView.setAdapter(deviceAdapter);
        initUI();
        initListener();

    }

    @SuppressLint("SetTextI18n")
    private void initListener() {
        startbtn.setOnClickListener(view -> {
                    if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    }else{

                startbtn.setText("Scanning");
                bt_text.setText("Bluetooth Device Searching...");

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startScan();
                    }
                },SCAN_PERIOD);

                Log.i("kyaw","scanning timeout 3s");


        }});
        stoptbtn.setOnClickListener(view -> {
            startbtn.setText("Start");
            bt_text.setText("Bluetooth Disable");
            stopScan();

        });
    }

    private void initUI() {

        // Initialize buttons
        startbtn = findViewById(R.id.startbtn);
        stoptbtn = findViewById(R.id.stoptbtn);
        bt_search = findViewById(R.id.bt_search);
        bt_text= findViewById(R.id.bt_text);

        handler = new Handler();

    }

    @SuppressLint({"MissingPermission", "NotifyDataSetChanged"})
    private void startScan() {
        if (!isScanning) {
            isScanning = true;
            deviceList.clear();
            deviceAdapter.notifyDataSetChanged();
            bluetoothLeScanner.startScan(scanCallback);
            Toast.makeText(this, "Scanning started...", Toast.LENGTH_SHORT).show();
        }
        bt_search.setImageResource(R.drawable.ic_bt_searching);
        Log.i("Kyaw", "Start Scanning..........");
    }

    @SuppressLint("MissingPermission")
    private void stopScan() {
        if (isScanning) {
            isScanning = false;
            deviceList.clear();
            bluetoothLeScanner.stopScan(scanCallback);
            Toast.makeText(this, "Scanning stopped.", Toast.LENGTH_SHORT).show();
        }
        bt_search.setImageResource(R.drawable.ic_bt_disabled);
        Log.i("Kyaw", "Stop Scanning...........");
    }

    private final ScanCallback scanCallback = new ScanCallback() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice device = result.getDevice();
            @SuppressLint("MissingPermission") String deviceName = device.getName();
            if (deviceName != null && !deviceList.contains(deviceName)) {
                deviceList.add(deviceName);
                deviceAdapter.notifyDataSetChanged();
                Log.i("Kyaw","Discovered divice name :" + deviceName);

            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Location permission is required for BLE scanning.", Toast.LENGTH_SHORT).show();
//                finish();
            }
        }
    }
}
