package it.kyaw.bleapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_FINE_LOCATION = 2;
    private static final long SCAN_PERIOD = 5000;
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Log.i("MainActivity", "Connected to GATT server.");
                gatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.i("MainActivity", "Disconnected from GATT server.");
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i("MainActivity", "Services discovered.");
            } else {
                Log.w("MainActivity", "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i("MainActivity", "Characteristic read.");
            }
        }
    };
    Button stoptbtn, startbtn;
    ImageView bt_search;
    TextView bt_text, text_date_time;
    ProgressBar progress_bar;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothLeScanner bluetoothLeScanner;
    private boolean isScanning = false;
    private Handler handler;
    private DeviceAdapter deviceAdapter;
    private final ScanCallback leScanCallback = new ScanCallback() {
        @SuppressLint("MissingPermission")
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            BluetoothDevice bluetoothDevice = result.getDevice();
            @SuppressLint("MissingPermission") Device device = new Device(
                    bluetoothDevice.getName() != null ? bluetoothDevice.getName() : "Unknown Device",
                    bluetoothDevice.getAddress());
            Log.i("Kyaw", "Discovered divice name :" + bluetoothDevice.getName() + "\n" + "Device address " + bluetoothDevice.getAddress());
            deviceAdapter.addDevice(device);
        }
    };
    private List<Device> deviceList = new ArrayList<>();

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initListener();
        // Display the current date and time
        displayCurrentDateTime();

        // Initialize Bluetooth
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        bluetoothAdapter = bluetoothManager.getAdapter();

        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            // Bluetooth is not enabled
            startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT);
        }

        handler = new Handler();
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
    }

    private void displayCurrentDateTime() {
        // Get the current date and time
        Date currentDate = new Date();

        // Format the date and time
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        String formattedDate = dateFormat.format(currentDate);

        // Display the formatted date and time in the TextView
        text_date_time.setText(formattedDate);

    }

    private void initListener() {

        startbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                startScan();
                startbtn.setText("Scanning");
                bt_text.setText("BLE Device Searching......");
            }
        });

        stoptbtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                stopScan();
                startbtn.setText("Start");
                deviceList.clear();

            }
        });


    }

    private void initUI() {

        startbtn = findViewById(R.id.startbtn);
        stoptbtn = findViewById(R.id.stoptbtn);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        bt_search = findViewById(R.id.bt_search);
        bt_text = findViewById(R.id.bt_text);
        progress_bar = findViewById(R.id.progress_bar);
        text_date_time = findViewById(R.id.text_date_time);

        // Initialize RecyclerView
        deviceAdapter = new DeviceAdapter(deviceList, this::connectToDevice);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(deviceAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_FINE_LOCATION);
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                startScan();
            } else {
                // Permission denied
                Toast.makeText(this, "Location permission is required for BLE scanning", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint({"SetTextI18n", "MissingPermission", "NotifyDataSetChanged"})
    private void startScan() {
        Log.i("Kyaw", "StartScanning.......");
        if (isScanning) return;

        // Stops scanning after  5 seconds scan period.
        handler.postDelayed(() -> {
            isScanning = false;
            bluetoothLeScanner.stopScan(leScanCallback);
            startbtn.setText("Start");
            bt_text.setText("BLE Disable......");
            progress_bar.setVisibility(View.GONE);
            bt_search.setImageResource(R.drawable.ic_bt_disabled);
            Toast.makeText(MainActivity.this, "Scan stopped", Toast.LENGTH_SHORT).show();
        }, SCAN_PERIOD);


        //Scanning 5 seconds
        isScanning = true;
        deviceList.clear();
        deviceAdapter.notifyDataSetChanged();
        bluetoothLeScanner.startScan(leScanCallback);
        progress_bar.setVisibility(View.VISIBLE);
        Toast.makeText(this, "Scanning 5seconds...", Toast.LENGTH_SHORT).show();
        bt_search.setImageResource(R.drawable.ic_bt_searching);
    }

    @SuppressLint("MissingPermission")
    private void stopScan() {
        Log.i("Kyaw", "Stoped Scanning.......");
        if (isScanning) {
            isScanning = false;
            deviceList.clear();
            bluetoothLeScanner.stopScan(leScanCallback);
            Toast.makeText(this, "Scan stopped", Toast.LENGTH_SHORT).show();
        }

        bt_search.setImageResource(R.drawable.ic_bt_disabled);
//        Log.i("Kyaw", "Stop Scanning...........");
    }

    private void connectToDevice(Device device) {
        BluetoothDevice bluetoothDevice = bluetoothAdapter.getRemoteDevice(device.getAddress());
        @SuppressLint("MissingPermission") BluetoothGatt bluetoothGatt = bluetoothDevice.connectGatt(this, false, gattCallback);
    }
}
