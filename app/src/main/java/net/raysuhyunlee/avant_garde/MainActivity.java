package net.raysuhyunlee.avant_garde;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.activeandroid.query.Select;

import net.raysuhyunlee.avant_garde.DB.FingerMap;
import net.raysuhyunlee.avant_garde.DB.FontHelper;
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

    ViewPager viewPager;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("새 모드");
                builder.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog d = (AlertDialog) dialog;
                        EditText editTextName = (EditText) d.findViewById(R.id.editTextName);
                        addSituation(editTextName.getText().toString());
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // set dialog layout
                View dialogView = getLayoutInflater()
                        .inflate(R.layout.dialog_new_situation, null);
                builder.setView(dialogView);
                builder.show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    private void loadFragments(List<Situation> situations) {
        FragmentManager fm = getSupportFragmentManager();
        adapter = new SimpleFragmentPagerAdapter(fm);

        for (Situation situation : situations) {
            FingersFragment f = new FingersFragment();
            f.situation = situation;
            adapter.addFragment(f, situation.name);
        }

        viewPager = (ViewPager)findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                FingersFragment item = (FingersFragment)adapter.getItem(position);
                Situation situation = item.situation;
                sendSituation(situation);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private List<Situation> getSituations() {
        List<Situation> situations = new Select().from(Situation.class).execute();
        if (situations == null) {
            situations = new ArrayList<>();
        }
        return situations;
    }

    private void addSituation(String situationName) {
        Situation situation = new Situation();
        situation.name = situationName;
        situation.save();
        for(int i=1; i<(int)Math.pow(2, 4); i++) {
            boolean[] booleans = new boolean[4];
            for(int j=0; j<booleans.length; j++)
                booleans[j] = ((i >> j) % 2) == 1;
            FingerMap fingerMap = new FingerMap(booleans, "상용구를 지정하세요!", situation.getId());
            fingerMap.save();
        }

        FingersFragment f = new FingersFragment();
        f.situation = situation;
        adapter.addFragment(f, "");
        adapter.notifyDataSetChanged();
        viewPager.setCurrentItem(adapter.getCount()-1);
    }

    private void sendSituation(Situation situation) {
        List<FingerMap> fingerMaps = situation.getFingerMaps();
        Bitmap b = FontHelper.textAsBitmap(fingerMaps.get(0).sentence, 5, Color.argb(255, 255, 0, 0));
        boolean[][] bytes = new boolean[b.getHeight()][b.getWidth()];
        for(int i=0; i<b.getHeight(); i++) {
            for(int j=0; j<b.getWidth(); j++) {
                int pixel = b.getPixel(j, i);
                bytes[i][j] = (b.getPixel(j, i) == 0);
            }
        }
        //Toast.makeText(this, "width: " + b.getWidth() + ", height: " + b.getHeight(), Toast.LENGTH_SHORT).show();
        ImageView imageViewTest = (ImageView)findViewById(R.id.imageViewTest);
        imageViewTest.setImageBitmap(b);
    }
}
