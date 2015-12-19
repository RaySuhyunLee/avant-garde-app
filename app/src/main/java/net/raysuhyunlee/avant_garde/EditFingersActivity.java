package net.raysuhyunlee.avant_garde;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;

import net.raysuhyunlee.avant_garde.DB.FingerMap;
import net.raysuhyunlee.avant_garde.DB.Situation;

/**
 * Created by SuhyunLee on 2015. 12. 18..
 */
public class EditFingersActivity extends Activity {
    String situation_name;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_fingers);

        Intent intent = getIntent();
        situation_name = intent.getStringExtra(Api.EXTRA_SITUATION_NAME);

        final Situation situation = new Select().from(Situation.class)
                        .where("Name = ?", situation_name).executeSingle();


        // set Listview
        ListView listViewFingers = (ListView) findViewById(R.id.listViewFingers);
        final InjectionArrayAdapter<FingerMap> adapter = new InjectionArrayAdapter<>(
                this, R.layout.fingermap, situation.getFingerMaps(),
                new InjectionArrayAdapter.DrawViewInterface<FingerMap>() {
            @Override
            public View drawView(int position, View view, FingerMap data) {
                final FingerMap fingerMap = data;
                TextView textViewSentence = (TextView) view.findViewById(R.id.textViewSentence);
                textViewSentence.setText(fingerMap.sentence);
                return view;
            }
        });
        listViewFingers.setAdapter(adapter);
        listViewFingers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder dialogBuilder =
                        new AlertDialog.Builder(EditFingersActivity.this);
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_edit_sentence, null);
                final int index = position;
                dialogBuilder
                        .setTitle("상용구 설정")
                        .setView(dialogView);
                dialogBuilder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editTextSentence = (EditText)
                                ((AlertDialog) dialog).findViewById(R.id.editTextSentence);
                        FingerMap fingerMap = situation.getFingerMaps().get(index);
                        fingerMap.sentence
                                = editTextSentence.getText().toString();
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                dialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                dialogBuilder.create();
            }
        });

        // set EditText
        final EditText editTextName = (EditText)findViewById(R.id.editTextName);

        // set button
        Button buttonSave = (Button)findViewById(R.id.buttonSave);
        Button buttonCancel = (Button)findViewById(R.id.buttonCancel);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                situation.name = editTextName.getText().toString();
                if (situation.name.length() == 0) {
                    Toast.makeText(EditFingersActivity.this,
                            "상황 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                situation.save();
                finish();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
