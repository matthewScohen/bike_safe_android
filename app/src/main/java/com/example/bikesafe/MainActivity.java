package com.example.bikesafe;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity<LeDeviceListAdapter> extends AppCompatActivity {

    TextView textView;
    EditText phoneNum;

    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

//    private LeDeviceListAdapter leDeviceListAdapter = new LeDeviceListAdapter();

    // Device scan callback.
//    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
//        @Override
//        public void onLeScan(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
//            System.out.println(bluetoothDevice);
//        }
//    };

//    private LeDeviceListAdapter leDeviceListAdapter = new LeDeviceListAdapter();

    //     Device scan callback.
    private ScanCallback leScanCallback =
            new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
//                    leDeviceListAdapter.addDevice(result.getDevice());
//                    leDeviceListAdapter.notifyDataSetChanged();
                    Log.d("TEST", "MESSAGE2");
                    Log.d("IDENTIFIER",result.toString());
                }
            };

    private BluetoothLeScanner bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
    private boolean scanning;
    private Handler handler = new Handler();

    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    private void scanLeDevice() {
        if (!scanning) {
            // Stops scanning after a predefined scan period.
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    scanning = false;
                    Log.d("TEST", "STOP");
                    bluetoothLeScanner.stopScan(leScanCallback);
                }
            }, SCAN_PERIOD);

            scanning = true;
            Log.d("TEST", "START");
            bluetoothLeScanner.startScan(leScanCallback);
        } else {
            scanning = false;
            Log.d("TEST", "STOP2");
            bluetoothLeScanner.stopScan(leScanCallback);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        phoneNum = (EditText) findViewById(R.id.inputPhone);

//        scanLeDevice();   //This currently breaks the emulator, I think because of permissions
    }

    public void setPhone(View view){
        Snackbar.make(view, "Button Pressed, Sending phone number \"" + phoneNum.getText() + "\"", Snackbar.LENGTH_LONG).show();

    }
}