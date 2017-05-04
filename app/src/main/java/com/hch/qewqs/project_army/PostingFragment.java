package com.hch.qewqs.project_army;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostingFragment extends Fragment {
    View v;

    private FirebaseAuth finfo;
    private String Username;
    String pcode;

    Button OK;
    Button NO;

    EditText posting_context;
    EditText posting_title;
    Spinner limmit_rank;

    TextView countText;

    RadioGroup RG;
    RadioButton b1;
    RadioButton b2;
    RadioButton b3;
    RadioButton b4;

    public boolean isOverText;
    public boolean isRBcheck;

    public ProgressDialog progressDialog;


    public PostingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        isOverText = false;
        isRBcheck = false;

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("게시물 로딩 저장");
        progressDialog.setMessage("게시물을 저장하고 있습니다.");

        v = inflater.inflate(R.layout.fragment_posting, container, false);
        OK = (Button) v.findViewById(R.id.postingBTN_ok);
        NO=(Button) v.findViewById(R.id.postingBTN_no);
        posting_title = (EditText) v.findViewById(R.id.Posting_title_edittext);
        posting_context = (EditText) v.findViewById(R.id.posting_context_edittext);
        countText = (TextView) v.findViewById(R.id.countEditText);
        limmit_rank =(Spinner) v.findViewById(R.id.limmit_reply_rank);

        RG = (RadioGroup) v.findViewById(R.id.posting_category_rg);
        b1 = (RadioButton) v.findViewById(R.id.posting_category_b1);
        b2 = (RadioButton) v.findViewById(R.id.posting_category_b2);
        b3 = (RadioButton) v.findViewById(R.id.posting_category_b3);
        b4 = (RadioButton) v.findViewById(R.id.posting_category_b4);

        NO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                manager.beginTransaction().replace(R.id.content_main, new CategoryFourFragment()).commit();
            }
        });

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isOverText){
                    Toast.makeText(getActivity(), "본문 내용의 최대 길이를 확인해 주세요.", Toast.LENGTH_LONG).show();
                }else{
                    String Radiobuttoncheck = "";
                    if(b1.isChecked()){
                        Radiobuttoncheck = "육군";
                        isRBcheck = true;
                    }else if(b2.isChecked()){
                        Radiobuttoncheck = "해군";
                        isRBcheck = true;
                    }else if(b3.isChecked()){
                        Radiobuttoncheck = "공군";
                        isRBcheck = true;
                    }else if(b4.isChecked()){
                        Radiobuttoncheck = "ETC";
                        isRBcheck = true;
                    }
                    if(isRBcheck){
                        final String title = posting_title.getText().toString();
                        final String context = posting_context.getText().toString();
                        final String lim = limmit_rank.getSelectedItem().toString();
                        final String category = Radiobuttoncheck;
                        if((title.length() > 1) && (context.length() > 1)){
                            try{
                                final DatabaseReference mdf = FirebaseDatabase.getInstance().getReference();
                                mdf.child("Post").child(category).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Long i = dataSnapshot.getChildrenCount();

                                        long now = System.currentTimeMillis();
                                        Date date = new Date(now);
                                        SimpleDateFormat sdfnow = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                                        String stNow = sdfnow.format(date);

                                        PostContents PC = new PostContents(stNow, 0, 0, title, context, lim);

                                        mdf.child("Post").child(category).child("postnumber"+i).setValue(PC);

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                                try{
                                    progressDialog.show();
                                    Thread.sleep(1000);
                                }catch (InterruptedException e){
                                    e.printStackTrace();
                                }
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "전송이 완료되었습니다.", Toast.LENGTH_LONG).show();

                                FragmentManager manager = getActivity().getSupportFragmentManager();
                                manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                manager.beginTransaction().replace(R.id.content_main, new CategoryFourFragment()).commit();

                            }catch (Exception e){
                                e.printStackTrace();
                                Toast.makeText(getActivity(), "죄송합니다. 오류로 인하여 전달하지 못했습니다.", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            Toast.makeText(getActivity(), "제목과 본문은 한 자 이상 작성하여야 합니다.", Toast.LENGTH_LONG).show();
                        }
                    }else{
                        Toast.makeText(getActivity(), "카테고리를 선택해주세요.", Toast.LENGTH_LONG).show();
                    }

                }

            }
        });

        posting_context.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 1000){
                    countText.setText("최대 길이 1000자 / " + s.length());
                    countText.setTextColor(Color.RED);
                    isOverText = true;
                }else{
                    countText.setText("최대 길이 1000자 / " + s.length());
                    countText.setTextColor(Color.BLACK);
                    isOverText = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
