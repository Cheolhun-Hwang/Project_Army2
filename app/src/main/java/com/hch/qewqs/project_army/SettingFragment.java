package com.hch.qewqs.project_army;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SettingFragment extends Fragment {
    View v;

    Button RestoreBTN;
    TextView Pcode_textview;
    EditText Pcode_edittext;

    String pcode;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_setting, container, false);

        RestoreBTN = (Button) v.findViewById(R.id.restoreBTN);
        Pcode_edittext = (EditText) v.findViewById(R.id.pcode_edittext);
        Pcode_textview = (TextView) v.findViewById(R.id.Pcode_textview);


        RestoreBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String restorecode = Pcode_edittext.getText().toString();
                DatabaseReference mdf = FirebaseDatabase.getInstance().getReference();
                mdf.child("Acc").child(restorecode).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String s = dataSnapshot.child("who").getValue(String.class);
                        Log.d("SettingFragment", "*************************************************is EditText Code?" + s);

                        if(s == null){
                            Log.d("SettingFragment", "*************************************************is not Code");
                            Toast.makeText(getActivity(), "올바르지 않는 코드입니다. 코드를 다시 확인해주세요.", Toast.LENGTH_LONG).show();
                        }else{
                            Log.d("SettingFragment", "*************************************************Code is Right");
                            ((MainActivity)getActivity()).modifyPcode(Pcode_edittext.getText().toString());
                            Toast.makeText(getActivity(), "계정 복구 코드가 적용되었습니다.", Toast.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


        return v;
    }

    public void onStart(){
        super.onStart();
        //이부분이 현 프래그먼트의 상위의 액티비티 중 MainActivity를 참조하여 그 안의 메소드를 호출하는 코드.
        pcode = ((MainActivity)getActivity()).getParsonalCode();
        Log.d("SettingFragment", "*************************************************" + pcode);
        Pcode_textview.setText(pcode);

    }


    public void onAttach(Context context){
        super.onAttach(context);
    }

}
