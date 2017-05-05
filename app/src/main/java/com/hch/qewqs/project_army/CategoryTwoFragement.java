package com.hch.qewqs.project_army;

import android.app.Dialog;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by food8 on 2017-05-05.
 */

public class CategoryTwoFragement extends Fragment {
    TextView noticeView;
    Button searchAll;
    Button searchMatch;
    ViewGroup v;
    String base_URL;
    CheckEmpty checkEmpty;
    ArrayList<CheckEmpty> checkEmpties;
    ArrayList<CheckEmpty> resultEmpties;
    ArrayList<Storage> storages;
    String showAll = "";
    String result = "";
    ProgressDialog progressDialog;

    private boolean isSaveData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = (ViewGroup) inflater.inflate(R.layout.fragment_category_two,container,false);
        noticeView = (TextView) v.findViewById(R.id.noticeView);
        searchAll = (Button) v.findViewById(R.id.searchAllBtn);
        searchMatch = (Button) v.findViewById(R.id.searchMatchBtn);
        checkEmpties = new ArrayList<CheckEmpty>();

        isSaveData = false;

        if (getArguments() != null){
            isSaveData = true;
            storages = (ArrayList<Storage>)getArguments().get("Storages");
        }



        noticeView.setLines(1000);
        noticeView.setVerticalScrollBarEnabled(true);
        noticeView.setMovementMethod(new ScrollingMovementMethod());



        getApiData();

        searchAll.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showAll ="";
                result = "";
                for(CheckEmpty num : checkEmpties){
                    showAll += num.getGunName() + "\n" + "특기 번호 : " + num.getTkCode() + "\n" + "특기 명 : " + num.getTkName() + "\n" + "입영 장소 : " + num.getWhereName()
                            + "\n" + "입대 일 : " + num.getIpDate() + "\n" + "공석 수 : " + num.getNumOfEmpty() +"\n\n";
                }
                noticeView.setText(showAll);
            }
        });

        searchMatch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(isSaveData){
                    resultEmpties = new ArrayList<CheckEmpty>();
                    result ="";
                    showAll ="";
                    for(CheckEmpty num : checkEmpties){
                        for(Storage soso : storages) {
                            if(num.getTkCode().equals(soso.getTkCode())){
                                resultEmpties.add(num);
                            }
                        }
                    }
                    HashSet hashSet = new HashSet(resultEmpties);
                    ArrayList<CheckEmpty> kkk = new ArrayList<CheckEmpty>(hashSet);
                    for(CheckEmpty num : kkk){
                        result += num.getGunName() + "\n" + "특기 번호 : " + num.getTkCode() + "\n" + "특기 명 : " + num.getTkName() + "\n" + "입영 장소 : " + num.getWhereName()
                                + "\n" + "입대 일 : " + num.getIpDate() + "\n" + "공석 수 : " + num.getNumOfEmpty() +"\n\n";
                    }
                    noticeView.setText(result);
                }else{
                    Toast.makeText(getActivity(), "저장된 정보가 없습니다.", Toast.LENGTH_SHORT).show();;
                }
            }
        });

        return v;
    }

    public void getApiData(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setTitle("찾는 중");
        progressDialog.setMessage("알맞는 특기 검색 중입니다..");

        progressDialog.show();

        //작업스레드
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    base_URL = "http://apis.data.go.kr/1300000/MachumTG/list?serviceKey=6tLZdNCgFtUUkC1aMEPPSDH5EqZB09HbJ9vEwO1DeRGItkpZQzyAxdTw2npenOfhIQdsklstTNt9qrj2RODhkQ%3D%3D&numOfRows=677&pageSize=10&pageNo=1&startPage=1";
                    getXmlData(base_URL);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                progressDialog.dismiss();
            }
        });
        t.start();
    }

    void getXmlData(String url_base) {

        try {
            URL url = new URL(url_base); //문자열로 된 요청 url을 URL 객체로 생성.
            InputStream is = url.openStream();  //url위치로 입력스트림 연결
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));  //inputstream 으로부터 xml 입력받기

            String tag;

            xpp.next();
            int eventType = xpp.getEventType();
            checkEmpty = new CheckEmpty();

            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {

                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();
                        //테그 이름 얻어오기
                        if(tag.equals("gsteukgiCd")){
                            xpp.next();
                            checkEmpty.setTkCode(xpp.getText());
                        }else if(tag.equals("gsteukgiNm")){
                            xpp.next();
                            checkEmpty.setTkName(xpp.getText());
                        }else if(tag.equals("gtcdNm1")){
                            xpp.next();
                            checkEmpty.setGunName(xpp.getText());
                        }else if(tag.equals("gtcdNm2")){
                            xpp.next();
                            checkEmpty.setWhereName(xpp.getText());
                        }else if(tag.equals("ipyeongDt")){
                            xpp.next();
                            int a;
                            String date;
                            a = xpp.getText().indexOf("T");
                            date = xpp.getText().substring(0,a);
                            checkEmpty.setIpDate(date);
                        }else if(tag.equals("jygs")){
                            xpp.next();
                            checkEmpty.setNumOfEmpty(xpp.getText());
                            checkEmpties.add(checkEmpty);
                            checkEmpty = new CheckEmpty();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        break;

                    case XmlPullParser.END_TAG: // </> End Tag
                        tag = xpp.getName();    //테그 이름 얻어오기

                        if (tag.equals("item")){

                        }
                        break;
                }

                eventType = xpp.next();
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
