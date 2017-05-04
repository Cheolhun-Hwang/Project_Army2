package com.hch.qewqs.project_army;

/**
 * Created by hch on 2017-05-02.
 */

public class Account {
    private String who;
    private String when;

    public Account(){
        this.who = "";
        this.when = "";
    }

    public void setWho(String r){
        this.who = r;
    }

    public void setWhen(String s){
        this.when = s;
    }

    public String getWho(){
        return this.who;
    }

    public String getWhen(){
        return this.when;
    }
}
