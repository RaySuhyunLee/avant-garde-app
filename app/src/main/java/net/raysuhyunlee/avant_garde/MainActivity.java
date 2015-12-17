package net.raysuhyunlee.avant_garde;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.TextView;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

/**
 * Created by SuhyunLee on 2015. 12. 18..
 */
public class MainActivity extends Activity {
    BluetoothService bluetoothService;
    ServiceConnection connection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView textViewTest = (TextView)findViewById(R.id.textViewTest);

        Intent intent = new Intent(this, BluetoothService.class);
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                bluetoothService = ((BluetoothService.BluetoothBinder)service).getService();
                bluetoothService.setOnDataReceivedListener(
                        new BluetoothSPP.OnDataReceivedListener() {
                            @Override
                            public void onDataReceived(byte[] data, String message) {
                                textViewTest.setText(message);
                            }
                        });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                bluetoothService = null;
            }
        };
        bindService(intent, connection, BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }
}
