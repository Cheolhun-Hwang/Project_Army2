package com.hch.qewqs.project_army;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;


public class CategoryTreeFragment extends Fragment {
    View v;

    public static final String ANONYMOUS = "anonymous";
    private FirebaseAuth finfo;
    private String Username;
    String pcode;

    Button start;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_category_tree, container, false);


        start = (Button) v.findViewById(R.id.categoryThreeStartGame);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((MainActivity)getActivity()).getislogin()){
                    Intent intent = new Intent(getActivity(), GameActivity.class);
                    intent.putExtra("pcode", pcode);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                }else{
                    Toast.makeText(getActivity(), "로그인 이후 가능한 서비스 입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return v;
    }

    public void onStart(){
        super.onStart();
        //이부분이 현 프래그먼트의 상위의 액티비티 중 MainActivity를 참조하여 그 안의 메소드를 호출하는 코드.
        finfo = ((MainActivity)getActivity()).getmFirebaseAuth();
        Username = ((MainActivity)getActivity()).getmUsername();
        pcode = ((MainActivity)getActivity()).getParsonalCode();

    }


    public void onAttach(Context context){
        super.onAttach(context);
    }

}
