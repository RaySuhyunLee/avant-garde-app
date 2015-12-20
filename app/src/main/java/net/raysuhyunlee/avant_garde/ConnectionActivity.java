package net.raysuhyunlee.avant_garde;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class ConnectionActivity extends AppCompatActivity {
    BluetoothService bluetoothService;
    ServiceConnection connection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);

        // attach button click listener
        final Button buttonConnect = (Button) findViewById(R.id.buttonConnect);
        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open bluetooth device list
                Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                //Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //startActivity(intent);
            }
        });

        final TextView textViewBluetooth = (TextView)findViewById(R.id.textViewBluetooth);

        // bind BluetoothService
        Intent intent = new Intent(this, BluetoothService.class);
        //startService(intent);
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                bluetoothService = ((BluetoothService.BluetoothBinder) service).getService();
                buttonConnect.setEnabled(true);
                textViewBluetooth.setText("블루투스 서비스가 연결되었습니다.");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                bluetoothService = null;
                textViewBluetooth.setText("블루투스 서비스가 연결되지 않았습니다.");
            }
        };
        bindService(intent, connection, BIND_AUTO_CREATE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if(resultCode == Activity.RESULT_OK) {
                bluetoothService.connect(data);
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        } /*else if(requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if(resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
            } else {
                // Do something if user doesn't choose any device (Pressed back)
            }
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
