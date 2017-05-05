package com.hch.qewqs.project_army;
public class Specialist{

    private static double height;
    private static double weight;
    private static double sight_L;
    private static double sight_R;
    private static int degree;
    private static double bmi;
    private static String major;
    private String license1;
    private String license2;
    private String license3;
    private String myunheo;


    public Specialist(){

        this.height=0;
        this.weight=0;
        this.sight_L=0;
        this.sight_R=0;
        this.degree=0;
        this.bmi=0;
        this.major="NONE";
        this.license1="NONE";
        this.license2="NONE";
        this.license3="NONE";
        this.myunheo="NONE";
    }
    public void setMajor(String major){
        this.major=major;
    }

    public void setLicense1(String license){
        this.license1=license;
    }

    public void setLicense2(String license){
        this.license2=license;
    }

    public void setLicense3(String license){
        this.license3=license;
    }

    public void setMyunheo(String myunheo) {this.myunheo = myunheo;}

    public void setHeight(double heigh){
        this.height=heigh;
    }

    public void setWeight(double weigh){
        this.weight=weigh;
    }

    public void setSight_L(double sightL){
        this.sight_L=sightL;
    }

    public void setSight_R(double sightR){
        this.sight_R=sightR;
    }

    public double getHeight(){
        return this.height;
    }

    public String getMyunheo() {return this.myunheo;}

    public double getWeight(){
        return this.weight;
    }

    public double getSight_L(){
        return this.sight_L;
    }

    public double getSight_R(){
        return this.sight_R;
    }

    public int getDegree(){
        return this.degree;
    }

    public void setDegree(int degree){ this.degree = degree; }

    public double getBmi(){
        return this.bmi;
    }

    public String getMajor(){
        return this.major;
    }

    public String getLicense1(){
        return this.license1;
    }

    public String getLicense2(){
        return this.license2;
    }

    public String getLicense3(){
        return this.license3;
    }


    public void compareBmi() {
        int siDegree=0;
        this.bmi = (this.weight*10000)/(this.height * this.height);     //BMI 값 계산

        if(this.sight_L<=0.2&&this.sight_R<=0.2){
            siDegree=5;
        }
        else if(this.sight_R<=0.1||this.sight_L<=0.1){
            siDegree=5;
        }
        else if(this.sight_L<=0.6||this.sight_R<=0.6){
            siDegree=4;
        }

        if (this.height <= 140) {
            this.degree = 6;
        } else if (this.height >= 204) {
            this.degree = 4;
        } else if (this.height >= 141 && height <= 145) {
            this.degree = 5;
        } else if (this.height >= 146 && height <= 158) {
            this.degree = 4;
        } else if (this.height >= 159 && height < 161) {
            if (bmi >= 17 && bmi < 33) {
                this.degree = 3;
            } else if (bmi < 17 && bmi >= 33) {
                this.degree = 4;
            }
        } else if (this.height >= 161 && height < 204) {
            if (bmi >= 20 && bmi < 25) {
                this.degree = 1;
            } else if (bmi >= 18.5 && bmi < 20) {
                this.degree = 2;
            } else if (bmi >= 25 && bmi < 30) {
                this.degree = 2;
            } else if (bmi >= 17 && bmi < 18.5) {
                this.degree = 3;
            } else if (bmi >= 30 && bmi < 33) {
                this.degree = 3;
            } else if (bmi < 17 || bmi >= 33) {
                this.degree = 4;
            }
        }

        if(siDegree>this.degree){
            this.degree=siDegree;
        }

    }


}