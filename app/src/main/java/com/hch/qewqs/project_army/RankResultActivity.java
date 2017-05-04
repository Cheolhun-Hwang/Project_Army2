package com.hch.qewqs.project_army;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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

public class RankResultActivity extends AppCompatActivity {

    int result;
    Intent intent;

    String result_rank;

    Button backtohome;
    ImageView RankImageView;
    TextView MessageCong;
    TextView MessageisRight;

    public static final String MESSAGES_CHILD_1 = "Acc";
    public String MESSAGES_CHILD_2;
    DatabaseReference mFirebaseDatabaseReference;

    FirebaseAuth mfirebaseAuth;
    String PCODE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_result);

        intent = getIntent();
        result = intent.getIntExtra("result", 0);
        PCODE = intent.getStringExtra("pcode");

        backtohome = (Button) findViewById(R.id.BacktoHome);
        RankImageView = (ImageView) findViewById(R.id.RankImageView);
        MessageCong = (TextView) findViewById(R.id.MessageCong);
        MessageisRight = (TextView)findViewById(R.id.MessageIsRight);

        mfirebaseAuth = FirebaseAuth.getInstance();
        String username = mfirebaseAuth.getCurrentUser().getDisplayName().toString();
        Log.d("Login state", "**************************************" + username);

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        MESSAGES_CHILD_2 = username;

        Log.d("Login state", "**************************************PCODE : " + PCODE);

        if(result == 0 ){
            RankImageView.setImageResource(R.drawable.rank0);
            result_rank = "훈련병";
        }else if(result == 1){
            RankImageView.setImageResource(R.drawable.rank1);
            result_rank = "이등병";
        }else if(result == 2){
            RankImageView.setImageResource(R.drawable.rank2);
            result_rank = "일병";
        }else if(result == 3){
            RankImageView.setImageResource(R.drawable.rank3);
            result_rank = "상병";
        }else if(result == 4){
            RankImageView.setImageResource(R.drawable.rank4);
            result_rank = "병장";
        }else if(result == 5){
            RankImageView.setImageResource(R.drawable.rank5);
            result_rank = "하사";
        }else if(result == 6){
            RankImageView.setImageResource(R.drawable.rank6);
            result_rank = "중사";
        }else if(result == 7){
            RankImageView.setImageResource(R.drawable.rank7);
            result_rank = "상사";
        }else if(result == 8){
            RankImageView.setImageResource(R.drawable.rank8);
            result_rank = "소위";
        }else if(result == 9){
            RankImageView.setImageResource(R.drawable.rank9);
            result_rank = "중위";
        }else if(result == 10){
            RankImageView.setImageResource(R.drawable.rank10);
            result_rank = "대위";
        }

        MessageCong.setText("축하합니다. "+username+"님께서\n "+result_rank+" 랭크를 얻으셨습니다.");
        MessageisRight.setText(result+"/10");

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat sdfnow = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String stNow = sdfnow.format(date);

        mFirebaseDatabaseReference.child(MESSAGES_CHILD_1).child(PCODE).child("who").setValue(username);
        mFirebaseDatabaseReference.child(MESSAGES_CHILD_1).child(PCODE).child("when").setValue(stNow);
        mFirebaseDatabaseReference.child(MESSAGES_CHILD_1).child(PCODE).child("isRank").setValue(result_rank);
        mFirebaseDatabaseReference.child(MESSAGES_CHILD_1).child(PCODE).child("Result").setValue(result);

        backtohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
