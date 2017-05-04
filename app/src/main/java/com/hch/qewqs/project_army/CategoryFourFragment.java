package com.hch.qewqs.project_army;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by hch on 2017-05-03.
 */

public class CategoryFourFragment extends Fragment {
    View v;
    public static final String TAG = "CategoryFour";
    public static final String ANONYMOUS = "anonymous";
    String username="";
    private FirebaseAuth finfo;
    private String Username;
    private String pcode;
    private String NowPage;

    private Fragment PostOne;
    private Fragment PostTwo;
    private Fragment PostThree;
    private Fragment PostFour;

    private TabLayout tabLayout;
    private ViewPager pager;
    private PagerAdapter adapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_category_four, container, false);
        pcode = "";
        NowPage = "";


        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("육군"));
        tabLayout.addTab(tabLayout.newTab().setText("해군"));
        tabLayout.addTab(tabLayout.newTab().setText("공군"));
        tabLayout.addTab(tabLayout.newTab().setText("ETC"));
        pager  = (ViewPager) v.findViewById(R.id.container);
        adapter = new PagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());


        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "************************************************************************Call PostingFragments");
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                manager.beginTransaction().replace(R.id.content_main, new PostingFragment()).commit();
            }
        });

        return v;
    }

    public void onStart(){
        super.onStart();
        finfo = ((MainActivity)getActivity()).getmFirebaseAuth();
        Username = ((MainActivity)getActivity()).getmUsername();
        pcode = ((MainActivity)getActivity()).getParsonalCode();
        Log.d(TAG, "************************************************************************Start Set OK");

        PostOne = new PostOneFragment();
        PostTwo = new PostTwoFragment();
        PostThree = new PostThreeFragment();
        PostFour = new PostFourFragment();

        pager.setAdapter(adapter);

        if(getArguments() != null){
            String cate = getArguments().getString("category");
            if(cate.equals("육군")){
                Log.d(TAG, "************************************************************************Argument : 육군");
                pager.setCurrentItem(0);
            }else if(cate.equals("해군")){
                Log.d(TAG, "************************************************************************Argument : 해군");
                pager.setCurrentItem(1);
            }else if(cate.equals("공군")){
                Log.d(TAG, "************************************************************************Argument : 공군");
                pager.setCurrentItem(2);
            }else{
                Log.d(TAG, "************************************************************************Argument : ETC");
                pager.setCurrentItem(3);
            }
        }

    }



    public void onAttach(Context context){
        super.onAttach(context);
    }


    private class PagerAdapter extends FragmentPagerAdapter{
        int num_tab;

        public PagerAdapter(FragmentManager fm, int numOfTabs){
            super(fm);
            this.num_tab = numOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 0){
                return PostOne;
            }else if(position == 1){
                return PostTwo;
            }else if(position == 2){
                return PostThree;
            }else{
                return PostFour;
            }
        }


        @Override
        public int getCount() {
            return num_tab;
        }
    }
}
