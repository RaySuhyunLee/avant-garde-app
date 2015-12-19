package net.raysuhyunlee.avant_garde;

import android.app.ActionBar;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.activeandroid.query.Select;

import net.raysuhyunlee.avant_garde.DB.FingerMap;
import net.raysuhyunlee.avant_garde.DB.Situation;

import java.util.ArrayList;
import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

/**
 * Created by SuhyunLee on 2015. 12. 18..
 */
public class MainActivity extends AppCompatActivity {
    BluetoothService bluetoothService;
    ServiceConnection connection;

    SimpleFragmentPagerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, BluetoothService.class);
        connection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                bluetoothService = ((BluetoothService.BluetoothBinder)service).getService();
                bluetoothService.setOnDataReceivedListener(
                        new BluetoothSPP.OnDataReceivedListener() {
                            @Override
                            public void onDataReceived(byte[] data, String message) {
                                //textViewTest.setText(message);
                            }
                        });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                bluetoothService = null;
            }
        };
        bindService(intent, connection, BIND_AUTO_CREATE);

        List<Situation> situations = getSituations();
        loadFragments(situations);

        Button buttonAdd = (Button)findViewById(R.id.buttonAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewSituationActivity.class);
                startActivityForResult(intent, Api.REQUEST_NEW_FINGERS);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    @Override
    public void onActivityResult(int requestCode, int respondCode, Intent data) {
        if (requestCode == Api.REQUEST_NEW_FINGERS) {
            if (respondCode == RESULT_OK) {
                String situationName = data.getStringExtra(Api.EXTRA_SITUATION_NAME);

                FingersFragment f = new FingersFragment();
                f.situation = new Select().from(Situation.class)
                        .where("Name = ?", situationName).executeSingle();
                adapter.addFragment(f, "");
                adapter.notifyDataSetChanged();
            }
        }
    }

    private void loadFragments(List<Situation> situations) {
        FragmentManager fm = getSupportFragmentManager();
        adapter = new SimpleFragmentPagerAdapter(fm);

        for (Situation situation : situations) {
            FingersFragment f = new FingersFragment();
            f.situation = situation;
            adapter.addFragment(f, situation.name);
        }

        ViewPager viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);
    }

    private List<Situation> getSituations() {
        List<Situation> situations = new Select().from(Situation.class).execute();
        if (situations == null) {
            situations = new ArrayList<>();
        }
        return situations;
    }
}
