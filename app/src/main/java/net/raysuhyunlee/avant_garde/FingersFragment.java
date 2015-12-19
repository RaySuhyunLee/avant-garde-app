package net.raysuhyunlee.avant_garde;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import net.raysuhyunlee.avant_garde.DB.FingerMap;
import net.raysuhyunlee.avant_garde.DB.Situation;

/**
 * Created by SuhyunLee on 2015. 12. 19..
 */
public class FingersFragment extends Fragment {

    public Situation situation;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_fingers, parent, false);

        if (situation != null) {
            // set list
            setList((ListView) v.findViewById(R.id.listViewFingers));

            TextView textViewName = (TextView)v.findViewById(R.id.textViewName);
            textViewName.setText(situation.name);
        }

        return v;
    }

    private void setList(ListView v) {
        final InjectionArrayAdapter<FingerMap> adapter
                = new InjectionArrayAdapter<>(getContext(), R.layout.fingermap,
                situation.getFingerMaps(), new InjectionArrayAdapter.DrawViewInterface<FingerMap>() {
            @Override
            public View drawView(int position, View view, FingerMap data) {
                TextView textViewSentence = (TextView) view.findViewById(R.id.textViewSentence);
                TextView textViewFingers = (TextView) view.findViewById(R.id.textViewFingers);
                textViewSentence.setText(data.sentence);
                textViewFingers.setText(data.fingers);
                return view;
            }
        });
        v.setAdapter(adapter);


        v.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int index = position;

                AlertDialog.Builder dialogBuilder =
                        new AlertDialog.Builder(getActivity());
                View dialogView = getActivity().getLayoutInflater()
                        .inflate(R.layout.dialog_edit_sentence, null);
                FingerMap fingerMap = situation.getFingerMaps().get(index);
                EditText editTextSentence =
                        (EditText)dialogView.findViewById(R.id.editTextSentence);
                editTextSentence.setText(fingerMap.sentence);

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
                        fingerMap.save();
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
    }
}
