package com.hch.qewqs.project_army;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,CategoryMajorFragement.onMyListener {

    private String parsonalCode;



    Fragment fragments;
    FragmentManager manager;
    //
    private static final String TAG = "MainActivity";
    private static final int RC_SIGN_IN = 9001;
    public static final String Pref = "login_ID";
    public static final int actMod = Activity.MODE_PRIVATE;
    public ProgressDialog progressDialog;

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;
    private String mUsername;
    public static final String ANONYMOUS = "anonymous";
    private boolean islogin = false;

    Bundle arg = null;
    ///////////////////////////////////////////
    static Specialist specialist;
    CategorySpecialFragement  categorySepcial;
    CategoryMajorFragement categoryMajor;
    ArrayList<Storage> storages;
    Bundle bundle;

    ///////////////////////////////////////// 생명주기에 맞는 실행을 위해 여기에 정의 /////////////////////////////////////////////////////////
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar action = getSupportActionBar();
        action.setDisplayHomeAsUpEnabled(true);
        action.setHomeButtonEnabled(true);

        Log.d(TAG, "******************************************************************start onCreate()");

        manager = getSupportFragmentManager();

        //onCreate는 가장 처음 초기화 단계만 설정해줄것. 이후 초기화때에만 같은 코드를 사용할 것.
        manager.beginTransaction().replace(R.id.content_main, new MainViewActivity()).commit();

        mUsername = ANONYMOUS;
        parsonalCode = "";


        ////////////////////////
        categoryMajor = new CategoryMajorFragement();
        categorySepcial = new CategorySpecialFragement();
        specialist = new Specialist();
        ////////////////////////

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences myshared = getSharedPreferences("PCode", Activity.MODE_PRIVATE);
        if (!(myshared == null) && myshared.contains("code")) {
            parsonalCode = myshared.getString("code", "");
            SharedPreferences.Editor editor = myshared.edit();
            editor.clear();
            editor.commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        isCode();
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences myshared = getSharedPreferences("PCode", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = myshared.edit();
        editor.putString("code", parsonalCode);
        editor.commit();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////  이부분은 카테고리 이동 & 메뉴바 이동 정의 한곳//////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public boolean onSupportNavigateUp() {
        fragments = new MainViewActivity();
        manager.beginTransaction().replace(R.id.content_main, fragments).commit();
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sign_in_menu:
                Log.d(TAG, "**************************call login()");
                if (mUsername.equals(ANONYMOUS)) {
                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setTitle("구글 로그인 요청");
                    progressDialog.setMessage("로그인하는 중입니다.");

                    progressDialog.show();

                    final Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            signIn();
                            islogin = true;
                        }
                    });
                    t.start();
                    item.setTitle("로그아웃");
                } else {
                    mFirebaseAuth.signOut();
                    //Auth.GoogleSignInApi.signOut(mGoogleApiClient); //(로그인 아이디 매번 다시 선택하기);
                    mUsername = ANONYMOUS;
                    islogin = false;
                    item.setTitle("로그인");
                    Toast.makeText(getApplicationContext(), "로그아웃 완료", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.qa_menu:
                fragments = new QA();
                manager.beginTransaction().replace(R.id.content_main, fragments).commit();
                Toast.makeText(this, "QA", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "**************************call QA()");
                return true;
            case R.id.settings_menu:
                fragments = new SettingFragment();
                manager.beginTransaction().replace(R.id.content_main, fragments).commit();
                Log.d(TAG, "**************************call Setting()");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClick_Category_One() {
        //Toast.makeText(MainActivity.this, "Category_One.", Toast.LENGTH_SHORT).show();
        fragments = new CategoryOneFragement();
        manager.beginTransaction().replace(R.id.content_main, fragments).commit();
    }

    public void onClick_Category_Two() {
        //Toast.makeText(MainActivity.this, "Category_Two.", Toast.LENGTH_SHORT).show();
        fragments = new CategoryTwoFragement();
        bundle = new Bundle();
        bundle.putSerializable("Storages",storages);
        fragments.setArguments(bundle);
        manager.beginTransaction().replace(R.id.content_main, fragments).commit();
    }

    public void onClick_Category_Three() {
        Log.d(TAG, "**************************call Category_three()");
        fragments = new CategoryTreeFragment();
        //Firebase_info finfo = new Firebase_info(mFirebaseAuth);
        /*arg = new Bundle();
        arg.putParcelable("acc", finfo);
        fragments.setArguments(arg);*/
        manager.beginTransaction().replace(R.id.content_main, fragments).commit();
    }

    public void onClick_Category_Four() {
        Log.d(TAG, "**************************call Category_four()");
        fragments = new CategoryFourFragment();
        manager.beginTransaction().replace(R.id.content_main, fragments).commit();

    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////// 이하 코드는 구글 자동 로그인 관련 코드임/////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void handleFirebaseAuthResult(AuthResult authResult) {
        if (authResult != null) {
            // Welcome the user
            FirebaseUser user = authResult.getUser();
            Toast.makeText(this, "Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();

        }
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed
                Log.e(TAG, "Google Sign In failed.");
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else {
                            mUsername = mFirebaseAuth.getCurrentUser().getDisplayName().toString();
                            Toast.makeText(getApplicationContext(), "Authentication Success. : " + mUsername, Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    }
                });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }


    /////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////// 프레그먼트와 액티비티의 데이터 통신을 위한 메소드 부분 삭제 하지 마시길 //////////////////////////////////////////
    /// /////////////////////////////////////

    public FirebaseAuth getmFirebaseAuth() {
        return mFirebaseAuth;
    }

    public String getmUsername() {
        return mUsername;
    }

    public boolean getislogin() {
        return islogin;
    }

    public String getParsonalCode(){
        return this.parsonalCode;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    ///// 유저 구분 코드 /////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////내부 데이터에 저장합니다.//////////////////////////


    public void isCode() {
        Log.d(TAG, "******************************************************************start isCode() : " + parsonalCode);
        if (parsonalCode.equals("")) {
            parsonalCode = roadPcode();
            Log.d(TAG, "****************************************************************** RESTORE PCODE : " + parsonalCode);
            if(parsonalCode.equals("not")){
                Log.d("PCODE", "*******************************************************NOT Defined PCODE");
                final DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                mDatabaseReference.child("Acc").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long num = dataSnapshot.getChildrenCount();
                        if (num == null) {
                            num = 0L;
                        }
                        Log.d("PCODE", "*******************************************************" + num);
                        parsonalCode = num + "NODE7418520963";
                        Account acc = new Account();
                        mDatabaseReference.child("Acc").child(parsonalCode).setValue(acc);
                        savePcode(parsonalCode);

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        }

        Log.d("PCODE", "*******************************************************is Defined PCODE : " + parsonalCode);
    }


    public void savePcode(String pcode){
        try{
            FileOutputStream fos = openFileOutput("data.txt", Context.MODE_PRIVATE);

            PrintWriter writer = new PrintWriter(fos);
            writer.println(pcode);
            writer.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public String roadPcode(){
        try{
            FileInputStream fis = openFileInput("data.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String str = reader.readLine();
            if(str == null){
                return "not";
            }else{
                return str;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "not";
    }

    public void modifyPcode(String pcode){
        try{
            FileOutputStream fos = openFileOutput("data.txt", Context.MODE_PRIVATE);

            PrintWriter writer = new PrintWriter(fos);
            writer.println(pcode);
            writer.close();

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
    }

    public void category_one_change(int index){
        if(index == 1){
            manager.beginTransaction().replace(R.id.content_main, categorySepcial).commit();
        }
        if(index==2){
            manager.beginTransaction().replace(R.id.content_main, categoryMajor).commit();
        }

    }

    @Override
    public void onRecieveData(ArrayList<Storage> data) {
        this.storages = data;
    }

}

