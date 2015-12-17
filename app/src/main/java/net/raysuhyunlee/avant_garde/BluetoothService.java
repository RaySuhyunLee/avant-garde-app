package net.raysuhyunlee.avant_garde;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

/**
 * Created by SuhyunLee on 2015. 12. 18..
 */
public class BluetoothService extends Service {
    private BluetoothSPP bt;
    private final IBinder mBinder = new BluetoothBinder();

    public class BluetoothBinder extends Binder {
        public BluetoothService getService() {
            return BluetoothService.this;
        }
    }

    @Override
    public void onCreate() {
        bt = new BluetoothSPP(this);
        if (!bt.isBluetoothAvailable()) {
            Toast.makeText(this, "이 기기엔 블루투스 기능이 엄서요 :(", Toast.LENGTH_SHORT);
            stopSelf();
        }

        if (bt.isBluetoothEnabled()) {
            bt.setupService();
            bt.startService(false);
        } else {
            Toast.makeText(this, "블루투스부터 켜시죠~", Toast.LENGTH_SHORT);
            stopSelf();
        }
    }

    /*
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

    }*/

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        bt.stopService();
        bt = null;
    }


    // attach listeners to bt
    private void setupBluetoothListeners() {
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() {
            public void onDataReceived(byte[] data, String message) {
                String str = "";
                for (byte d : data) {
                    str += (char)d;
                }
            }
        });
    }

    public void connect(Intent data) {
        bt.connect(data);
    }

    public void setOnDataReceivedListener(BluetoothSPP.OnDataReceivedListener listener) {
        bt.setOnDataReceivedListener(listener);
    }
}
