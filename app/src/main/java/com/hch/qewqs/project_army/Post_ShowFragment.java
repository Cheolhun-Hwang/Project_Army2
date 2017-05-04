package com.hch.qewqs.project_army;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Post_ShowFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    View v;
    private TextView title;
    private TextView context;
    private TextView lim;
    private TextView date;
    private ListView listView;
    private EditText replyedittext;
    private TextView edittextLength;
    private Button replyBTN;
    private Button gotoCategory;
    private String category;
    int postnum;
    private boolean isOverText;
    DatabaseReference mdef;
    int comments;
    String commentrank;
    String userrank;

    public ProgressDialog progressDialog;

    private ArrayList<replyContents> Aplv;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Post_ShowFragment() {
        mdef = FirebaseDatabase.getInstance().getReference();
    }

    // TODO: Rename and change types and number of parameters
    public static Post_ShowFragment newInstance(String param1, String param2) {
        Post_ShowFragment fragment = new Post_ShowFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_post__show, container, false);

        isOverText = false;
        postnum = 0;
        category = "";

        title=(TextView) v.findViewById(R.id.PostShow_title_textview);
        context=(TextView)v.findViewById(R.id.PostShow_context);
        lim = (TextView) v.findViewById(R.id.PostShow_limmit);
        date = (TextView) v.findViewById(R.id.PostShow_Date);
        listView= (ListView) v.findViewById(R.id.PostShowListView);
        replyedittext = (EditText)v.findViewById(R.id.PostShow_reply_edittext);
        replyBTN = (Button)v.findViewById(R.id.PostShow_replyBTN);
        gotoCategory = (Button) v.findViewById(R.id.PostShow_gotoCategoryBTN);
        edittextLength = (TextView) v.findViewById(R.id.reply_edittext_length);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("게시물 로딩");
        progressDialog.setMessage("게시물을 가져오고 있습니다.");

        replyedittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 1000){
                    edittextLength.setText("최대 길이 1000자 / " + s.length());
                    edittextLength.setTextColor(Color.RED);
                    isOverText = true;
                }else{
                    edittextLength.setText("최대 길이 1000자 / " + s.length());
                    edittextLength.setTextColor(Color.BLACK);
                    isOverText = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        gotoCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                manager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                Fragment fragment = new CategoryFourFragment();
                Bundle arg = new Bundle();
                arg.putString("category", category);
                fragment.setArguments(arg);
                manager.beginTransaction().replace(R.id.content_main, fragment).commit();
            }
        });

        replyBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!(((MainActivity)getActivity()).getislogin())){
                    Log.d("Post_Show", "**********************************************************No Login");
                    Toast.makeText(getActivity(), "로그인 이후 댓글을 작성하실 수 있습니다.", Toast.LENGTH_SHORT).show();
                }else{
                    if(!checkRank(userrank)){
                        Log.d("Post_Show", "**********************************************************Limmit Rank");
                        Toast.makeText(getActivity(), "랭크가 낮아 댓글을 작성하실 수 없습니다.", Toast.LENGTH_SHORT).show();
                    }else{
                        if((isOverText) || (replyedittext.length() <= 0)){
                            Log.d("Post_Show", "**********************************************************String Length False");
                            Toast.makeText(getActivity(), "댓글은 최소 1자, 최대 1000자까지만 가능합니다..", Toast.LENGTH_SHORT).show();
                        }else{
                            mdef.child("Post").child(category).child("postnumber"+postnum).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Long commentcount = dataSnapshot.child("comment").getChildrenCount();
                                    if (commentcount == null){
                                        commentcount = 0L;
                                    }

                                    Log.d("PostShow" + category, "***********************************Last Set mdef Call");
                                    long now = System.currentTimeMillis();
                                    Date date = new Date(now);
                                    SimpleDateFormat sdfnow = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                                    String stNow = sdfnow.format(date);

                                    replyContents rc = new replyContents(
                                            ((MainActivity)getActivity()).getmUsername(), stNow, userrank, replyedittext.getText().toString());

                                    Log.d("PostShow" + category, "***********************************rc : " +  rc.getWho());
                                    Log.d("PostShow" + category, "***********************************rc : " +  rc.getWhen());
                                    Log.d("PostShow" + category, "***********************************rc : " +  rc.getRank());
                                    Log.d("PostShow" + category, "***********************************rc : " +  rc.getContext());

                                    replyedittext.setText("");
                                    Log.d("PostShow" + category, "***********************************rc : " + category);
                                    Log.d("PostShow" + category, "***********************************rc : " + postnum);

                                    mdef.child("Post").child(category).child("postnumber"+postnum).child("comment").child("commentnumber"+commentcount).child("who").setValue(rc.getWho());
                                    mdef.child("Post").child(category).child("postnumber"+postnum).child("comment").child("commentnumber"+commentcount).child("when").setValue(rc.getWhen());
                                    mdef.child("Post").child(category).child("postnumber"+postnum).child("comment").child("commentnumber"+commentcount).child("rank").setValue(rc.getRank());
                                    mdef.child("Post").child(category).child("postnumber"+postnum).child("comment").child("commentnumber"+commentcount).child("context").setValue(rc.getContext());


                                    comments = dataSnapshot.child("reply").getValue(Integer.class);
                                    Log.d("ShowPost" + category, "***************************************************viewcount before : " + comments);
                                    comments = comments + 1;
                                    Log.d("ShowPost" + category, "***************************************************viewcount after : " + comments);
                                    mdef.child("Post").child(category).child("postnumber"+postnum).child("reply").setValue(comments);
                                    Log.d("ShowPost" + category, "***************************************************viewcount after2 : " + comments);

                                    PostShowLoadDate();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }
                    }
                }
            }
        });

        return v;
    }

    @Override
    public void onStart(){
        super.onStart();
        if(getArguments() != null){
            category = getArguments().getString("category");
            postnum = getArguments().getInt("postnumber");
        }

        DatabaseReference mdef = FirebaseDatabase.getInstance().getReference();
        mdef.child("Post").child(category).child("postnumber"+postnum).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PostContents PC;
                PC = dataSnapshot.getValue(PostContents.class);
                Log.d("PostShow", " ***************************************** " + PC.getTitle() + " / " + PC.getContext());
                title.setText(PC.getTitle());
                context.setText(PC.getContext());
                lim.setText(PC.getLim());
                date.setText("작성날짜 : " + PC.getWhen());

                commentrank = dataSnapshot.child("lim").getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mdef.child("Acc").child(((MainActivity)getActivity()).getParsonalCode()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userrank = dataSnapshot.child("isRank").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        PostShowLoadDate();
    }


    public void PostShowLoadDate(){
        Aplv = new ArrayList<replyContents>();
        Log.d("PostOne", "***************************************************************PostShowLoadDate() Start");

        DatabaseReference mdef = FirebaseDatabase.getInstance().getReference();

        mdef.child("Post").child(category).child("postnumber"+postnum).child("comment").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Long child = dataSnapshot.getChildrenCount();
                Log.d("PostShow" + category + " ListView", "***********************************child : " +  child);
                if(child <= 0L){
                    Log.d("PostShow" + category, "***************************************************************No Post");
                }else{
                    String who;
                    String when;
                    String rank;
                    String context;
                    for(int i = 0 ; i < child ; i++){
                        who = dataSnapshot.child("commentnumber"+i).child("who").getValue(String.class);
                        when= dataSnapshot.child("commentnumber"+i).child("when").getValue(String.class);
                        rank= dataSnapshot.child("commentnumber"+i).child("rank").getValue(String.class);
                        context= dataSnapshot.child("commentnumber"+i).child("context").getValue(String.class);

                        Log.d("PostShow" + category + " ListView", "***********************************who : " +  who);
                        Log.d("PostShow" + category + " ListView", "***********************************when : " +  when);
                        Log.d("PostShow" + category + " ListView", "***********************************rank : " +  rank);
                        Log.d("PostShow" + category + " ListView", "***********************************context : " +  context);

                        replyContents plv = new replyContents(who, when, rank, context);
                        Aplv.add(plv);
                        listView.setAdapter(new PostShowListView_Adapter(getContext(), R.layout.replylistitem, Aplv));
                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

        Log.d("PosShowtOne", "***************************************************************PostLoadDate() End");
    }


    public class replyContents{
        private String who;
        private String when;
        private String rank;
        private String context;

        public replyContents(){
            this.who = "";
            this.when = "";
            this.rank = "";
            this.context = "";
        }

        public replyContents(String who, String when, String r, String c){
            this.who = who;
            this.when = when;
            this.rank = r;
            this.context = c;
        }

        public String getWho() {
            return who;
        }

        public String getWhen() {
            return when;
        }

        public String getRank() {
            return rank;
        }

        public String getContext() {
            return context;
        }

        public void setWho(String who) {
            this.who = who;
        }

        public void setWhen(String when) {
            this.when = when;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public void setContext(String context) {
            this.context = context;
        }
    }

    public boolean checkRank(String userRank){
        int user = changeRankInt(userRank);
        int commentlimmit = changeRankInt(commentrank);

        Log.d("ShowPost" + category, "***************************************************user integer : " + user);
        Log.d("ShowPost" + category, "***************************************************userRank : " + userRank);
        Log.d("ShowPost" + category, "***************************************************commentlimmit integer : " + commentlimmit);
        Log.d("ShowPost" + category, "***************************************************commnetlimmit " + commentrank);




        if (user == -1){
            Toast.makeText(getActivity(), "유저 랭크 정보 전달 에러", Toast.LENGTH_SHORT).show();
            Log.d("ShowPost" + category, "*************************************************** Call user == -1 ");
            return false;
        }else if(user >= commentlimmit){
            Log.d("ShowPost" + category, "*************************************************** user (" + user + ") >= commetlimmit (" + commentlimmit + ")");
            return true;
        }else{
            Log.d("ShowPost" + category, "*************************************************** Call else  ");
            return false;
        }
    }

    public int changeRankInt(String r){
        if(r.equals("이등병")){
            return 0;
        }else if(r.contains("일병")){
            return 1;
        }else if(r.contains("상병")){
            return 2;
        }else if(r.contains("병장")){
            return 3;
        }else if(r.contains("하사")){
            return 4;
        }else if(r.contains("중사")){
            return 5;
        }else if(r.contains("상사")){
            return 6;
        }else if(r.contains("소위")){
            return 7;
        }else if(r.contains("중위")){
            return 8;
        }else if(r.contains("대위")){
            return 9;
        }else{
            return -1;
        }
    }

    public class PostShowListView_Adapter extends BaseAdapter {

        Context ctx;
        int layout;
        ArrayList<replyContents> list;
        LayoutInflater inf;

        public PostShowListView_Adapter(){
            this.ctx = null;
            this.list=null;
            this.layout = 0;
        }

        public PostShowListView_Adapter(Context ctx, int Layout, ArrayList<replyContents> list){
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

            TextView postShowwho = (TextView) convertView.findViewById(R.id.reply_listviewitem_who);
            TextView postShowwhen = (TextView) convertView.findViewById(R.id.reply_listviewitem_date);
            TextView postShowrank = (TextView) convertView.findViewById(R.id.reply_listviewitem_rank);
            TextView postShowcontext = (TextView) convertView.findViewById(R.id.reply_listviewitem_context);

            replyContents PLV = list.get(position);

            Log.d("ListView Adapter", "******************************************************"+PLV.getWho());
            Log.d("ListView Adapter", "******************************************************"+PLV.getWhen());
            Log.d("ListView Adapter", "******************************************************"+PLV.getRank());
            Log.d("ListView Adapter", "******************************************************"+PLV.getContext());

            postShowwho.setText("작성자 : "+PLV.getWho());
            postShowwhen.setText("랭크 : " + PLV.getWhen());
            postShowrank.setText("작성날짜 : " + PLV.getRank());
            postShowcontext.setText(PLV.getContext());

            return convertView;
        }
    }

}
