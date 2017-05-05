package com.hch.qewqs.project_army;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import static com.hch.qewqs.project_army.MainActivity.specialist;


/**
 * Created by food8 on 2017-05-03.
 */

public class CategorySpecialFragement extends Fragment {
    ViewGroup v;
    ConstraintLayout constraintLayout;
    LinearLayout linearLayout;
    EditText inputHeight;
    EditText inputWeight;
    EditText inputSightL;
    EditText inputSightR;
    Double height;
    Double weight;
    Double sightL;
    Double sightR;
    Button goNext;
    Button getResult;
    MainActivity mainActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = (ViewGroup) inflater.inflate(R.layout.fragment_category_one_2, container, false);
        constraintLayout = (ConstraintLayout) v.findViewById(R.id.inputBody);
        linearLayout = (LinearLayout) v.findViewById(R.id.bodyCheck);
        RadioGroup rg = (RadioGroup) v.findViewById(R.id.radioGroup1);
        RadioGroup body = (RadioGroup) v.findViewById(R.id.radioBody);
        inputHeight = (EditText) v.findViewById(R.id.inputHeight);
        inputWeight = (EditText) v.findViewById(R.id.inputWeight);
        inputSightL = (EditText) v.findViewById(R.id.inputSightL);
        inputSightR = (EditText) v.findViewById(R.id.inputSightR);

        goNext = (Button) v.findViewById(R.id.goNext);
        getResult = (Button) v.findViewById(R.id.getResult);
        mainActivity = (MainActivity) getActivity();

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.radio0){
                    linearLayout.setVisibility(View.VISIBLE);
                    constraintLayout.setVisibility(View.GONE);
                }
                else if(checkedId == R.id.radio1){
                    linearLayout.setVisibility(View.GONE);
                    constraintLayout.setVisibility(View.VISIBLE);
                }
            }
        });
        body.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId == R.id.body1){
                    specialist.setDegree(1);
                }
                else if(checkedId == R.id.body2){
                    specialist.setDegree(2);
                }
                else if(checkedId == R.id.body3){
                    specialist.setDegree(3);
                }
                else if(checkedId == R.id.body4){
                    specialist.setDegree(4);
                }
                else if(checkedId == R.id.body5){
                    specialist.setDegree(5);
                }
                else if(checkedId == R.id.body6){
                    specialist.setDegree(6);
                }
            }
        });
        getResult.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                height = Double.parseDouble(inputHeight.getText().toString());
                weight = Double.parseDouble(inputWeight.getText().toString());
                sightL = Double.parseDouble(inputSightL.getText().toString());
                sightR = Double.parseDouble(inputSightR.getText().toString());

                specialist.setHeight(height);
                specialist.setWeight(weight);
                specialist.setSight_L(sightL);
                specialist.setSight_R(sightR);

                specialist.compareBmi();
                int k = specialist.getDegree();
                String a = Integer.toString(k);
                if(k<=3) {
                    mainActivity.category_one_change(2);
                }
            }
        });
        goNext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int k = specialist.getDegree();
                String a = Integer.toString(k);
                if((k<=3)&&(k!=0)) {
                    mainActivity.category_one_change(2);
                }
            }
        });
        return v;
    }


}
