package com.hch.qewqs.project_army;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class GameActivity extends AppCompatActivity{

    TextView Qustion;
    TextView countTimer;
    RadioGroup RG;
    RadioButton Q_one;
    RadioButton Q_two;
    RadioButton Q_three;
    RadioButton Q_four;

    private DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    ProgressDialog progressDialog;

    ArrayList<storeQuestion> ars_main =  new ArrayList<storeQuestion>();

    FirebaseAuth mfirebaseAuth;

    ArrayList<storeQuestion> questions;
    storeQuestion question;

    Thread timer;
    Thread roadData;
    String pcode;
    Intent intent;

    private boolean isroad;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.what == 1001){
                int time =  (int)msg.obj;
                Log.d("Timer2", "**************************************time Count : "+time);
                countTimer.setText("타이머 : " + time);
            }else if(msg.what == 2001){
                ars_main = (ArrayList<storeQuestion>)msg.obj;
                Log.d("Complete data3", "**************************************"+ars_main.size());
                isroad = true;

                setQuestion(ars_main);
            }else if(msg.what == 3001){
                RG.clearCheck();
                int next = (int)msg.obj;
                if(next < 10){
                    setQuestionMethod(ars_main, next);
                }else{
                    Log.e("Road Error", "**************************************Overflour");
                }

            }else if(msg.what == 5001){
                Log.d("Intent Rank Activity", "**************************************" + (int)msg.obj);

                Intent intent = new Intent(getApplicationContext(), RankResultActivity.class);
                intent.putExtra("result", (int)msg.obj);
                intent.putExtra("pcode", pcode);
                startActivity(intent);
                finish();
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        isroad =false;

        mfirebaseAuth = FirebaseAuth.getInstance();

        Log.d("Login state", "**************************************" + mfirebaseAuth.getCurrentUser().getDisplayName().toString());

        Qustion = (TextView) findViewById(R.id.QuestionTextview);
        countTimer = (TextView)findViewById(R.id.countShowTextview);
        RG = (RadioGroup)findViewById(R.id.answerRadioGroup);
        Q_one = (RadioButton)findViewById(R.id.answerOneRadioBTN);
        Q_two = (RadioButton)findViewById(R.id.answerTwoRadioBTN);
        Q_three = (RadioButton)findViewById(R.id.answerThreeRadioBTN);
        Q_four = (RadioButton)findViewById(R.id.answerFourRadioBTN);

        question = new storeQuestion();
        questions = new ArrayList<storeQuestion>();


        progressDialog = new ProgressDialog(GameActivity.this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("네트워크 요청");
        progressDialog.setMessage("문제를 불러오는 중입니다.");

        progressDialog.show();

        roadData = new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<storeQuestion> ars = new ArrayList<storeQuestion>();
                storeQuestion sqs;
                try{
                    for(int i = 1 ; i <= 10 ; i++){
                        sqs = loadingQuestion(i);
                        ars.add(sqs);
                    }

                    while(true){
                        if(ars.size() == 10 && !(ars.get(1).getAnswer().equals(""))
                                && !(ars.get(2).getAnswer().equals("")) && !(ars.get(3).getAnswer().equals("")) && !(ars.get(4).getAnswer().equals(""))
                                && !(ars.get(5).getAnswer().equals("")) && !(ars.get(6).getAnswer().equals("")) && !(ars.get(7).getAnswer().equals(""))
                                && !(ars.get(8).getAnswer().equals("")) && !(ars.get(9).getAnswer().equals("")) && !(ars.get(10).getAnswer().equals(""))){
                            break;
                        }else{
                            Thread.sleep(1000);
                        }
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }
                progressDialog.dismiss();
                Log.d("Complete data", "**************************************"+ars.size());

                Message msg = handler.obtainMessage();
                msg.what = 2001;
                msg.obj = ars;

                handler.sendMessage(msg);
            }
        });

        timer = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    boolean isfinish = false;
                    int j = 1;
                    int count = 0;
                    boolean[] isRightArray = new boolean[10];

                    while(true && !(isfinish)){
                        if(isroad){
                            for(int i = 15 ; i >= 0 ; i--){
                                Message msg = handler.obtainMessage();
                                msg.what = 1001;
                                msg.obj = i;
                                handler.sendMessage(msg);
                                Log.d("Timer", "**************************************time Count : "+i);
                                if (i < 1) {
                                    Log.d("J is", "************************************** : "+ j);
                                    if(j == 10){
                                        isroad = false;
                                        isfinish = true;
                                    }else{
                                        String check;
                                        boolean isRight;

                                        if(Q_one.isChecked()){
                                            check = "보기1";
                                        }else if(Q_two.isChecked()){
                                            check = "보기2";
                                        }else if(Q_three.isChecked()){
                                            check = "보기3";
                                        }else if(Q_four.isChecked()){
                                            check = "보기4";
                                        }else{
                                            check = "보기0";
                                        }

                                        if(ars_main.get(j-1).getAnswer().equals(check)){
                                            isRight = true;
                                            isRightArray[j-1] = isRight;
                                        }else{
                                            isRight = false;
                                            isRightArray[j-1] = isRight;
                                        }

                                        Log.d("isRight", "*******************************************************"+ isRight);
                                        Log.d("isRightArray", "*******************************************************"+ isRightArray[j-1] + " / "+ (j-1));
                                        Log.d("before J", "*******************************************************"+ j);
                                        Message msg2 = handler.obtainMessage();
                                        msg2.what = 3001;
                                        msg2.obj = j++;
                                        handler.sendMessage(msg2);

                                        Log.d("after J", "*******************************************************"+ j);
                                    }
                                }
                                Thread.sleep(1000);
                            }
                        }
                    }
                    Log.d("while", "**************************************isFinish: "+!(isfinish));

                    for(int i = 0 ; i<10 ;i++){
                        if(isRightArray[i]==true){
                            count++;
                            Log.d("isRightArray : True", count+"");
                        }else{
                            Log.d("isRightArray : False", count+"");
                        }
                    }
                    Log.d("isRightArray", count+"");

                    Message msg3 = handler.obtainMessage();
                    msg3.what = 5001;
                    msg3.obj = count;
                    handler.sendMessage(msg3);

                }catch (InterruptedException e){
                    e.printStackTrace();
                }

            }
        });

        roadData.start();
        timer.start();





    }

    @Override
    public void onStart(){
        super.onStart();
        intent = getIntent();
        pcode = intent.getStringExtra("pcode");
        Log.d("GameActivity", "**************************************" + pcode );
    }




    public storeQuestion loadingQuestion(int i){
        final storeQuestion ars_sub = new storeQuestion();
        DatabaseReference childRef;

            childRef = rootRef.child("GAME").child("Question" + i).child("답");
            childRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String st = dataSnapshot.getValue(String.class);
                    ars_sub.setAnswer(st);
                    Log.d("Road data", "**************************************" + ars_sub.getAnswer() );
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            childRef = rootRef.child("GAME").child("Question" + i).child("문제");
            childRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String st = dataSnapshot.getValue(String.class);
                    ars_sub.setQuestion(st);
                    Log.d("Road data", "**************************************" + ars_sub.getQuestion());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            childRef = rootRef.child("GAME").child("Question" + i).child("보기1");
            childRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String st = dataSnapshot.getValue(String.class);
                    ars_sub.setQ1(st);
                    Log.d("Road data", "**************************************" +ars_sub.getQ1());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            childRef = rootRef.child("GAME").child("Question" + i).child("보기2");
            childRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String st = dataSnapshot.getValue(String.class);
                    ars_sub.setQ2(st);
                    Log.d("Road data", "**************************************" + ars_sub.getQ2());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            childRef = rootRef.child("GAME").child("Question" + i).child("보기3");
            childRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String st = dataSnapshot.getValue(String.class);
                    ars_sub.setQ3(st);
                    Log.d("Road data", "**************************************" + ars_sub.getQ3());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            childRef = rootRef.child("GAME").child("Question" + i).child("보기4");
            childRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String st = dataSnapshot.getValue(String.class);
                    ars_sub.setQ4(st);
                    Log.d("Road data", "**************************************" + ars_sub.getQ4());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return  ars_sub;
    }

    public void setQuestion(ArrayList<storeQuestion> ars){

        for(int i = 0 ; i < 10 ; i++){

            Log.d("Show Question List", "*******************************************************************"+ars.get(i).getAnswer());
            Log.d("Show Question List", "*******************************************************************"+ars.get(i).getQuestion());
            Log.d("Show Question List", "*******************************************************************"+ars.get(i).getQ1());
            Log.d("Show Question List", "*******************************************************************"+ars.get(i).getQ2());
            Log.d("Show Question List", "*******************************************************************"+ars.get(i).getQ3());
            Log.d("Show Question List", "*******************************************************************"+ars.get(i).getQ4());
        }
        setQuestionMethod(ars, 0);
    }

    public void setQuestionMethod(ArrayList<storeQuestion> ars, int i){
        Qustion.setText(ars.get(i).getQuestion());
        Q_one.setText(ars.get(i).getQ1());
        Q_two.setText(ars.get(i).getQ2());
        Q_three.setText(ars.get(i).getQ3());
        Q_four.setText(ars.get(i).getQ4());
    }


}
