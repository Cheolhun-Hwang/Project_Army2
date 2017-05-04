package com.hch.qewqs.project_army;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Adapter;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

public class MainViewActivity  extends Fragment {
    View v;

    private Button category1;
    private Button category2;
    private Button category3;
    private Button category4;

    private MainActivity mCallback;

    Animation flowani;
    TextView animationTextview;

    FragmentManager manager = getFragmentManager();

    Fragment fragments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_main_view, container, false);

        category1 = (Button) v.findViewById(R.id.category_one);
        category2 = (Button) v.findViewById(R.id.category_two);
        category3 = (Button) v.findViewById(R.id.category_three);
        category4 = (Button) v.findViewById(R.id.category_four);


        category1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClick_Category_One();
            }
        });
        category2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClick_Category_Two();
            }
        });
        category3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {mCallback.onClick_Category_Three();
            }});
        category4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.onClick_Category_Four();
            }
        });

        animationTextview = (TextView) v.findViewById(R.id.flowAnimationTextview);
        flowani = AnimationUtils.loadAnimation(getContext(), R.anim.flow);

        flowani.setAnimationListener(new FlowAnimationListener());
        animationTextview.setAnimation(flowani);

        return v;
    }

    private class FlowAnimationListener implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        // This makes sure that the container activity has implemented

        // the callback interface. If not, it throws an exception

        try {

            mCallback = (MainActivity) activity;

        } catch (ClassCastException e) {

            throw new ClassCastException(activity.toString()

                    + " must implement OnHeadlineSelectedListener");
        }

    }
}
