package com.hch.qewqs.project_army;

/**
 * Created by food8 on 2017-05-05.
 */

public class CheckEmpty {
    private String tkCode;
    private String tkName;
    private String gunName;
    private String whereName;
    private String ipDate;
    private String numOfEmpty;

    public CheckEmpty(){
        this.tkCode = "NONE";
        this.tkName = "NONE";
        this.gunName = "NONE";
        this.whereName = "NONE";
        this.ipDate = "NONE";
        this.numOfEmpty = "NONE";
    }

    public String getTkCode(){
        return this.tkCode;
    }
    public String getTkName(){
        return this.tkName;
    }
    public String getGunName(){
        return this.gunName;
    }
    public String getWhereName(){
        return this.whereName;
    }
    public String getIpDate(){
        return this.ipDate;
    }
    public String getNumOfEmpty(){
        return this.numOfEmpty;
    }

    public void setTkCode(String tkCode){
        this.tkCode = tkCode;
    }
    public void setGunName(String gunName){
        this.gunName = gunName;
    }
    public void setWhereName(String whereName){
        this.whereName = whereName;
    }
    public void setTkName(String tkName){
        this.tkName = tkName;
    }
    public void setIpDate(String ipDate){
        this.ipDate = ipDate;
    }
    public void setNumOfEmpty(String numOfEmpty){
        this.numOfEmpty = numOfEmpty;
    }

}
