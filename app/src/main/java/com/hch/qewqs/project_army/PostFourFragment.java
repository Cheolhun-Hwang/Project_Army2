package com.hch.qewqs.project_army;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostFourFragment extends Fragment {
    View v;
    private ListView listView;
    private ArrayList<PostListView> Aplv;
    public ProgressDialog progressDialog;
    private Thread t1;
    DatabaseReference mdef;
    private int postiongID;
    int viewcount;

    public PostFourFragment() {
        // Required empty public constructor
        mdef= FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_main, container, false);


        Aplv = new ArrayList<PostListView>();
        listView = (ListView) v.findViewById(R.id.postGallery_listview);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getActivity(), "Position : " + position+"/ID : " + id, Toast.LENGTH_SHORT).show();

                //Firebase_info finfo = new Firebase_info(mFirebaseAuth);
                  /*arg = new Bundle();
                 arg.putParcelable("acc", finfo);
                 fragments.setArguments(arg);*/
                postiongID = position;
                mdef.child("Post").child("ETC").child("postnumber"+position).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        viewcount = dataSnapshot.child("viewcount").getValue(Integer.class);
                        Log.d("ETC", "***************************************************viewcount before : " + viewcount);
                        viewcount = viewcount + 1;
                        Log.d("ETC", "***************************************************viewcount after : " + viewcount);
                        mdef.child("Post").child("ETC").child("postnumber"+postiongID).child("viewcount").setValue(viewcount);
                        Log.d("ETC", "***************************************************viewcount after2 : " + viewcount);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                Fragment fragment = new Post_ShowFragment();
                Bundle arg = new Bundle();
                arg.putInt("postnumber", position);
                arg.putString("category", "ETC");
                fragment.setArguments(arg);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, fragment).commit();
            }
        });

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("게시물 로딩");
        progressDialog.setMessage("게시물을 가져오고 있습니다.");

        return v;
    }

    @Override
    public void onStart(){
        super.onStart();

        progressDialog.show();

        t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                PostLoadDate();

            }
        });

        t1.start();
    }

    public void onAttach(Context context){
        super.onAttach(context);
    }

    public void PostLoadDate(){
        Log.d("PostFour", "***************************************************************PostLoadDate() Start");

        DatabaseReference mdef = FirebaseDatabase.getInstance().getReference();


        mdef.child("Post").child("ETC").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long child = dataSnapshot.getChildrenCount();
                if(child <= 0L){
                    Log.d("PostFour", "***************************************************************No Post");
                }else{
                    PostContents PC;
                    for(int i = 0 ; i < child ; i++){
                        PC = dataSnapshot.child("postnumber"+i).getValue(PostContents.class);
                        PostListView plv = new PostListView("Post" + i, PC.getTitle(), PC.getWhen(), PC.getViewcount(), PC.getReply());
                        Aplv.add(plv);
                        listView.setAdapter(new PostListView_Adapter(getContext(), R.layout.post_listview, Aplv));
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        Log.d("PostFour", "***************************************************************PostLoadDate() End");
    }

}
