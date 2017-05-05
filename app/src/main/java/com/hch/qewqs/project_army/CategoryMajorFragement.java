package com.hch.qewqs.project_army;

import android.app.Dialog;
import android.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import static com.hch.qewqs.project_army.MainActivity.specialist;
import static com.hch.qewqs.project_army.R.id.spinner;

/**
 * Created by food8 on 2017-05-03.
 */

public class CategoryMajorFragement extends Fragment {
    ViewGroup v;
    Button btnSearch;
    Button saveBtn;
    String base_URL;
    TextView resultText;
    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;
    Spinner spinner4;
    Spinner spinner5;
    String result ="";
    ArrayList<Storage> storages;
    ProgressDialog progressDialog;

    public interface onMyListener{
        void onRecieveData(ArrayList<Storage> data);
    }

    private onMyListener monMyListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof onMyListener){
            monMyListener = (onMyListener) context;
        }else {
            throw new RuntimeException(context.toString() + "failed");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        monMyListener = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = (ViewGroup) inflater.inflate(R.layout.fragment_category_one_3, container, false);
        btnSearch = (Button) v.findViewById(R.id.btnSearch);
        saveBtn = (Button) v.findViewById(R.id.saveBtn);
        resultText = (TextView) v.findViewById(R.id.resultText);
        spinner1 = (Spinner) v.findViewById(spinner);
        spinner2 = (Spinner) v.findViewById(R.id.spinner2);
        spinner3 = (Spinner) v.findViewById(R.id.spinner3);
        spinner4 = (Spinner) v.findViewById(R.id.spinner4);
        spinner5 = (Spinner) v.findViewById(R.id.spinner5);
        localSpinner1(R.array.spinnerArray1);
        localSpinner2(R.array.spinnerArray2);
        localSpinner3(R.array.spinnerArray2);
        localSpinner4(R.array.spinnerArray2);
        localSpinner5(R.array.spinnerArray3);
        resultText.setLines(1000);
        resultText.setVerticalScrollBarEnabled(true);
        resultText.setMovementMethod(new ScrollingMovementMethod());
        spinner1.setOnItemSelectedListener(setSpinner1);
        spinner2.setOnItemSelectedListener(setSpinner2);
        spinner3.setOnItemSelectedListener(setSpinner3);
        spinner4.setOnItemSelectedListener(setSpinner4);
        spinner5.setOnItemSelectedListener(setSpinner5);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //다이알로그
                result = "";
                storages = new ArrayList<Storage>();
                //final MyDialogFragment frag = MyDialogFragment.newInstance();
                //frag.show( );
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

                            base_URL = "http://apis.data.go.kr/1300000/mjbJiWon/list?serviceKey=6tLZdNCgFtUUkC1aMEPPSDH5EqZB09HbJ9vEwO1DeRGItkpZQzyAxdTw2npenOfhIQdsklstTNt9qrj2RODhkQ%3D%3D&numOfRows=1000&pageSize=10&pageNo=835&startPage=835";
                            getXmlData(base_URL);

                            for(Storage num : storages){
                                result += num.getSpeciality() + "\n" + num.getArmyName() + "\n" + "(" + num.getGubun() + ") " + num.getMyMajor() + "\n\n";
                            }
                            resultText.setText(result);


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        progressDialog.dismiss();
                    }
                });
                t.start();
            }

        });

        saveBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                monMyListener.onRecieveData(storages);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new MainViewActivity()).commit();
            }
        });

        return v;
    }

    /////////////////////////////////////

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
            Storage storage;
            storage = new Storage();
            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tag = xpp.getName();
                        if(tag.equals("gsteukgiCd")){
                            xpp.next();
                            storage.setTkCode(xpp.getText());
                        }
                        else if(tag.equals("gsteukgiNm")){
                            xpp.next();
                            storage.setSpeciality(xpp.getText());
                        }
                        else if(tag.equals("gtcdNm1")){
                            xpp.next();
                            storage.setArmyName(xpp.getText());
                        }
                        else if(tag.equals("gtcdNm2")){
                            xpp.next();
                            storage.setMyMajor(xpp.getText());
                        }
                        else if(tag.equals("gubun")){
                            xpp.next();
                            storage.setGubun(xpp.getText());
                            if(storage.getGubun().equals("전공")){
                                if(storage.getMyMajor().contains(specialist.getMajor())){
                                    storages.add(0,storage);
                                    Log.d("1",storages.get(0).getSpeciality());
                                    Log.d("2",storages.get(0).getArmyName());
                                    Log.d("3",storages.get(0).getMyMajor());
                                    Log.d("4",storages.get(0).getGubun());
                                }
                                storage = new Storage();
                            }
                            else if(storage.getGubun().equals("자격")){
                                if(specialist.getLicense1().equals(storage.getMyMajor()) || specialist.getLicense2().equals(storage.getMyMajor()) ||
                                        specialist.getLicense3().equals(storage.getMyMajor())){ //speciailist에 있는 자격증 정보와 api에서 받아온 자격증 정보 일치 여부 비교
                                    storages.add(0,storage);
                                    Log.d("1",storages.get(0).getSpeciality());
                                    Log.d("2",storages.get(0).getArmyName());
                                    Log.d("3",storages.get(0).getMyMajor());
                                    Log.d("4",storages.get(0).getGubun());
                                }
                                storage = new Storage();
                            }
                            else if(storage.getGubun().equals("면허")){
                                if(specialist.getMyunheo().equals(storage.getMyMajor())){
                                    storages.add(0,storage);
                                }
                                storage = new Storage();
                            }

                        }

                        //테그 이름 얻어오기
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

    private void localSpinner1(int itemNum) {
        ArrayAdapter<CharSequence> fAdapter;
        fAdapter = ArrayAdapter.createFromResource(getActivity(), itemNum, android.R.layout.simple_spinner_item);
        fAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(fAdapter);
    }

    private void localSpinner2(int itemNum) {
        ArrayAdapter<CharSequence> fAdapter;
        fAdapter = ArrayAdapter.createFromResource(getActivity(), itemNum, android.R.layout.simple_spinner_item);
        fAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(fAdapter);
    }
    private void localSpinner3(int itemNum) {
        ArrayAdapter<CharSequence> fAdapter;
        fAdapter = ArrayAdapter.createFromResource(getActivity(), itemNum, android.R.layout.simple_spinner_item);
        fAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(fAdapter);
    }
    private void localSpinner4(int itemNum) {
        ArrayAdapter<CharSequence> fAdapter;
        fAdapter = ArrayAdapter.createFromResource(getActivity(), itemNum, android.R.layout.simple_spinner_item);
        fAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(fAdapter);
    }
    private void localSpinner5(int itemNum) {
        ArrayAdapter<CharSequence> fAdapter;
        fAdapter = ArrayAdapter.createFromResource(getActivity(), itemNum, android.R.layout.simple_spinner_item);
        fAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner5.setAdapter(fAdapter);
    }

    public AdapterView.OnItemSelectedListener setSpinner1 = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 0:
                    specialist.setMajor("NULL");
                    break;
                case 1:
                case 2:
                    specialist.setMajor("건축");
                    break;
                case 3:
                    specialist.setMajor("경영");
                    break;
                case 4:
                    specialist.setMajor("경제");
                    break;
                case 5:
                case 6:
                    specialist.setMajor("경찰");
                    break;
                case 7:
                    specialist.setMajor("관광");
                    break;
                case 8:
                    specialist.setMajor("국문");
                    break;
                case 9:
                case 10:
                    specialist.setMajor("기계");
                    break;
                case 11:
                case 12:
                    specialist.setMajor("기악");
                    break;
                case 13:
                    specialist.setMajor("물리");
                case 14:
                    specialist.setMajor("화학");
                    break;
                case 15:
                    specialist.setMajor("도시");
                    break;
                case 16:
                    specialist.setMajor("동양");
                    break;
                case 17:
                    specialist.setMajor("미디어");
                    break;
                case 18:
                    specialist.setMajor("바이오");
                    break;
                case 19:
                    specialist.setMajor("법");
                    break;
                case 20:
                    specialist.setMajor("사회");
                    break;
                case 21:
                    specialist.setMajor("산림");
                    break;
                case 22:
                case 23:
                    specialist.setMajor("산업");
                    break;
                case 24:
                case 25:
                    specialist.setMajor("생명");
                    break;
                case 26:
                    specialist.setMajor("성악");
                    break;
                case 27:
                    specialist.setMajor("소방");
                    break;
                case 28:
                    specialist.setMajor("아동");
                    break;
                case 29:
                    specialist.setMajor("소프트");
                    break;
                case 30:
                case 31:
                    specialist.setMajor("수의");
                    break;
                case 32:
                    specialist.setMajor("시각");
                    break;
                case 33:
                    specialist.setMajor("식품");
                    break;
                case 34:
                    specialist.setMajor("건축");
                    break;
                case 35:
                    specialist.setMajor("심리");
                    break;
                case 36:
                    specialist.setMajor("약");
                    break;
                case 37:
                    specialist.setMajor("연기");
                    break;
                case 38:
                    specialist.setMajor("영미");
                    break;
                case 39:
                    specialist.setMajor("영양");
                    break;
                case 40:
                    specialist.setMajor("원자");
                    break;
                case 41:
                    specialist.setMajor("유럽");
                    break;
                case 42:
                    specialist.setMajor("유아");
                    break;
                case 43:
                    specialist.setMajor("응용");
                    break;
                case 44:
                    specialist.setMajor("자율");
                    break;
                case 45:
                case 46:
                    specialist.setMajor("작곡");
                    break;
                case 47:
                    specialist.setMajor("재료");
                    break;
                case 48:
                    specialist.setMajor("전기");
                    break;
                case 49:
                    specialist.setMajor("전자");
                    break;
                case 50:
                    specialist.setMajor("제약");
                    break;
                case 51:
                case 52:
                    specialist.setMajor("조경");
                    break;
                case 53:
                    specialist.setMajor("조선");
                    break;
                case 54:
                    specialist.setMajor("종교");
                    break;
                case 55:
                    specialist.setMajor("철학");
                    break;
                case 56:
                    specialist.setMajor("체육");
                    break;
                case 57:
                    specialist.setMajor("컴퓨터");
                    break;
                case 58:
                    specialist.setMajor("태권도");
                    break;
                case 59:
                    specialist.setMajor("토목");
                    break;
                case 60:
                    specialist.setMajor("패션");
                    break;
                case 61:
                case 62:
                    specialist.setMajor("한의");
                    break;
                case 63:
                    specialist.setMajor("행정");
                    break;
                case 64:
                case 65:
                    specialist.setMajor("화학");
                    break;
                default:
                    specialist.setMajor("");
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public AdapterView.OnItemSelectedListener setSpinner2 = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(position==0){
                spinner3.setVisibility(View.INVISIBLE);
            }
            else {
                spinner3.setVisibility(View.VISIBLE);
                specialist.setLicense1(spinner2.getSelectedItem().toString());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    public AdapterView.OnItemSelectedListener setSpinner3 = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(position==0){
                spinner4.setVisibility(View.INVISIBLE);
            }
            else {
                spinner4.setVisibility(View.VISIBLE);
                specialist.setLicense2(spinner3.getSelectedItem().toString());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
    public AdapterView.OnItemSelectedListener setSpinner4 = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            specialist.setLicense3(spinner4.getSelectedItem().toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    public AdapterView.OnItemSelectedListener setSpinner5 = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (position==0){
                specialist.setMyunheo("NULL");
            }else {
            specialist.setMyunheo(spinner5.getSelectedItem().toString());
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };
}
