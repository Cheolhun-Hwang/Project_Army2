package com.hch.qewqs.project_army;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hch on 2017-05-03.
 */

public class PostListView_Adapter extends BaseAdapter {

    Context ctx;
    int layout;
    ArrayList<PostListView> list;
    LayoutInflater inf;

    public PostListView_Adapter(){
        this.ctx = null;
        this.list=null;
        this.layout = 0;
    }

    public PostListView_Adapter(Context ctx, int Layout, ArrayList<PostListView> list){
        this.ctx = ctx;
        this.layout = Layout;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            this.inf = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inf.inflate(layout, parent, false);
        }

        TextView postTitle = (TextView) convertView.findViewById(R.id.post_listview_title);
        TextView postDate = (TextView) convertView.findViewById(R.id.post_listview_date);
        TextView postViewcount = (TextView) convertView.findViewById(R.id.post_listview_viewcount);
        TextView postComments = (TextView) convertView.findViewById(R.id.post_listview_comment);

        PostListView PLV = list.get(position);

        Log.d("ListView Adapter", "******************************************************"+PLV.getPostnumber());
        Log.d("ListView Adapter", "******************************************************"+PLV.getTitle());
        Log.d("ListView Adapter", "******************************************************"+PLV.getDate());
        Log.d("ListView Adapter", "******************************************************"+PLV.getViewcount());
        Log.d("ListView Adapter", "******************************************************"+PLV.getComments());

        postTitle.setText(PLV.getTitle());
        postDate.setText(PLV.getDate());
        postViewcount.setText("조회수 : " + PLV.getViewcount());
        postComments.setText("댓글수 : " + PLV.getComments());

        return convertView;
    }
}
