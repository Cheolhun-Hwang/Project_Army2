package com.hch.qewqs.project_army;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class CategoryOneFragement extends Fragment {
    View v;
    Activity root = getActivity();
    Button btnCheck;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_category_one, container, false);
        btnCheck = (Button) v.findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.category_one_change(1);
            }
        });
        return v;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
