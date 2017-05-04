package com.hch.qewqs.project_army;

/**
 * Created by hch on 2017-04-30.
 */

public class storeQuestion {
    private String question;
    private String answer;
    private String q1;
    private String q2;
    private String q3;
    private String q4;

    public storeQuestion(){
        this.question = "";
        this.answer = "";
        this.q1 = "";
        this.q2 = "";
        this.q3 = "";
        this.q4 = "";
    }

    private storeQuestion(String q, String a, String one, String two, String three, String four){
        this.question = q;
        this.answer = a;
        this.q1 = one;
        this.q2 = two;
        this.q3 = three;
        this.q4 = four;
    }

    public void setQuestion(String q){
        this.question = q;
    }
    public String getQuestion(){
        return this.question;
    }

    public void setAnswer(String a){
        this.answer = a;
    }
    public String getAnswer(){
        return this.answer;
    }

    public void setQ1(String q){
        this.q1 = q;
    }
    public String getQ1(){
        return this.q1;
    }

    public void setQ2(String q){
        this.q2 = q;
    }
    public String getQ2(){
        return this.q2;
    }

    public void setQ3(String q){
        this.q3 = q;
    }
    public String getQ3(){
        return this.q3;
    }

    public void setQ4(String q){
        this.q4 = q;
    }
    public String getQ4(){
        return this.q4;
    }

    public void clear(){
        this.question = "";
        this.answer = "";
        this.q1 = "";
        this.q2 = "";
        this.q3 = "";
        this.q4 = "";
    }
}
