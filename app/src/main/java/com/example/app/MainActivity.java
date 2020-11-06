package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


ListView listView;
TextView statusTextView;
Button searchButton;
BluetoothAdapter bluetoothAdapter;



    //  for receiver

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i("Action", action);

//            now code for if action finished

            if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){

                statusTextView.setText("Finished Search.");
                searchButton.setEnabled(true);

            }
//            to rather than working on bluetooth adapter , we now work with bluetooth device
            else if(BluetoothDevice.ACTION_FOUND.equals(action)){

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //device.getName(); // to get the device name -> then address -> then signal ...
                String name = device.getName();
                String address = device.getAddress();
                String rssi = Integer.toString(intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE));  // gives rss id
                // more negative number ~ stringer connection ...
Log.i("Device found!", "Device Name: " + name + "Address: " + address + "RSSI "+ rssi);


            }

        }
    };

    public void searchClicked(View view){

        statusTextView.setText("Searching devices...");
//        not to lauch the process everytime ...
        searchButton.setEnabled(false);

        bluetoothAdapter.startDiscovery();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
        statusTextView = findViewById(R.id.statusTextView);
        searchButton = findViewById(R.id.searchButton);

// now we set up the bluetooth...

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter(); // allow us to work with bluetoothadapter

// intent filter which would give us the required message
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
// now register

        registerReceiver(broadcastReceiver, intentFilter);


//        bluetoothAdapter.startDiscovery();


    }
}
