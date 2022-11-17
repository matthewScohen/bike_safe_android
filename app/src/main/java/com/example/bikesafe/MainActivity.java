package com.example.bikesafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

public class MainActivity<LeDeviceListAdapter> extends AppCompatActivity {

    TextView textView;
    EditText phoneNum;


    private BluetoothGatt ble_gatt = null;
    private final BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    // We successfully connected, proceed with service discovery
                    Log.d("bike_safe_only", "device connected");
                    gatt.discoverServices();
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    // We successfully disconnected on our own request
                    gatt.close();
                } else {
                    // We're CONNECTING or DISCONNECTING, ignore for now
                }
            } else {
                // An error happened...figure out what happened!
                gatt.close();
            }
        }
        @Override
        public void onServicesDiscovered (BluetoothGatt gatt, int status)
        {
            ble_gatt = gatt;
            // the 3rd service in the list is the UART service
            UUID UART_UUID = gatt.getServices().get(2).getUuid();
            BluetoothGattCharacteristic RX_CHAR = gatt.getServices().get(2).getCharacteristics().get(1);
            UUID UART_WRITE_CHARACTERISTIC = RX_CHAR.getUuid();

            Log.d("bike_safe_only", "found service " + UART_UUID);
            Log.d("bike_safe_only", "found characteristic " + UART_WRITE_CHARACTERISTIC);

//            byte[] message = phoneNum.getText().toString().getBytes(StandardCharsets.UTF_8);
//            RX_CHAR.setValue(message);
//            RX_CHAR.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
//            gatt.writeCharacteristic(RX_CHAR);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        phoneNum = (EditText) findViewById(R.id.inputPhone);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Get device object for a mac address

        BluetoothDevice device = bluetoothAdapter.getRemoteDevice("58:BF:25:E7:14:5E");
        // Check if the peripheral is cached or not
        int deviceType = device.getType();
        if(deviceType == BluetoothDevice.DEVICE_TYPE_UNKNOWN) {
            Log.d("bike_safe_only", "device not cached");
        } else {
            Log.d("bike_safe_only", "device cached");
        }

        BluetoothGatt gatt = device.connectGatt(textView.getContext(), false, bluetoothGattCallback, BluetoothDevice.TRANSPORT_LE);
    }

    public void setPhone(View view){

        //verify input first
        if(!validPhone()){
            Snackbar.make(view, "INVALID PHONE NUMBER: Please enter number in the form XXX-XXX-XXXX or XXXXXXXXXX", Snackbar.LENGTH_LONG).show();
            return;
        }
        Snackbar.make(view, "Button Pressed, Sending phone number \"" + phoneNum.getText() + "\"", Snackbar.LENGTH_LONG).show();

        UUID UART_UUID = ble_gatt.getServices().get(2).getUuid();
        BluetoothGattCharacteristic RX_CHAR = ble_gatt.getServices().get(2).getCharacteristics().get(1);
        UUID UART_WRITE_CHARACTERISTIC = RX_CHAR.getUuid();

        byte[] message = getMessage();
        Log.d("Phone", new String(message, StandardCharsets.UTF_8));
        RX_CHAR.setValue(message);
        RX_CHAR.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT);
        ble_gatt.writeCharacteristic(RX_CHAR);
    }

    public void toMap(View view){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    public byte[] getMessage(){
        String number = phoneNum.getText().toString();
        if(number.length() == 12){
            number = number.substring(0,3) + number.substring(4,7) + number.substring(8);
        }
        return number.getBytes(StandardCharsets.UTF_8);
    }

    public boolean validPhone(){
        String number = phoneNum.getText().toString();
        Log.d("PHONE", "starting check with number " + number);
        Log.d("PHONE", "Length: " + number.length());
        if(number.length() == 12){
            Log.d("PHONE", "trying to remove hyphens");
            // XXX-XXX-XXXX
            //remove hyphens
            number = number.substring(0,3) + number.substring(4,7) + number.substring(8);
        }
        if(number.length() == 10){
            try{
                long test = Long.parseLong(number);
                if(test < 0){
                    return false;
                }
            }
            catch (Exception e){
                return false;
            }
            return true;
        }
        return false;
    }
}
