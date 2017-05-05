package com.hch.qewqs.project_army;

/**
 * Created by food8 on 2017-05-03.
 */
public class Storage {
    private String armyName;      //육해공 cdNm1
    private String myMajor;         //전공 cdNm2
    private String tkCode;
    private String gubun;         //전공,자격증 구분   gubun
    private String speciality;       //주특기      특기Name

    public Storage(){
        this.armyName="NONE";
        this.myMajor="NONE";
        this.gubun="NONE";
        this.speciality="NONE";
        this.tkCode = "NONE";
    }

    public void setArmyName(String armyName){
        this.armyName=armyName;
    }

    public void setMyMajor(String myMajor){
        this.myMajor=myMajor;
    }

    public void setGubun(String gubun){
        this.gubun=gubun;
    }

    public void setTkCode(String tkCode){this.tkCode = tkCode;}

    public String getTkCode(){return this.tkCode;}

    public void setSpeciality(String speciality){
        this.speciality=speciality;
    }

    public String getArmyName(){
        return this.armyName;
    }

    public String getMyMajor(){
        return this.myMajor;
    }

    public String getGubun(){
        return this.gubun;
    }

    public String getSpeciality(){
        return this.speciality;
    }


}