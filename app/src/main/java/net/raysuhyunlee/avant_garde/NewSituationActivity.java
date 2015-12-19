package net.raysuhyunlee.avant_garde;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import net.raysuhyunlee.avant_garde.DB.FingerMap;
import net.raysuhyunlee.avant_garde.DB.Situation;

import java.util.ArrayList;

/**
 * Created by SuhyunLee on 2015. 12. 19..
 */
public class NewSituationActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_fingers);

        final Situation situation = new Situation();
        situation.name = "";
        situation.save();
        for(int i=1; i<(int)Math.pow(2, 4); i++) {
            boolean[] booleans = new boolean[4];
            for(int j=0; j<booleans.length; j++)
                booleans[j] = ((i >> j) % 2) == 1;
            FingerMap fingerMap = new FingerMap(booleans, "blabla", situation.getId());
            fingerMap.save();
        }

        // set Listview
        ListView listViewFingers = (ListView) findViewById(R.id.listViewFingers);
        final InjectionArrayAdapter<FingerMap> adapter = new InjectionArrayAdapter<>(
                this, R.layout.fingermap, situation.getFingerMaps(),
                new InjectionArrayAdapter.DrawViewInterface<FingerMap>() {
                    @Override
                    public View drawView(int position, View view, FingerMap data) {
                        final FingerMap fingerMap = data;
                        TextView textViewSentence = (TextView) view.findViewById(R.id.textViewSentence);
                        TextView textViewFingers = (TextView) view.findViewById(R.id.textViewFingers);
                        textViewSentence.setText(fingerMap.sentence);
                        textViewFingers.setText(fingerMap.fingers);
                        return view;
                    }
                });
        listViewFingers.setAdapter(adapter);

        listViewFingers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder dialogBuilder =
                        new AlertDialog.Builder(NewSituationActivity.this);
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
                dialogBuilder.show();
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
                    Toast.makeText(NewSituationActivity.this,
                            "상황 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                situation.save();

                Intent intent = new Intent();
                intent.putExtra(Api.EXTRA_SITUATION_NAME, situation.name);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }
}
