package com.hch.qewqs.project_army;

/**
 * Created by hch on 2017-05-03.
 */

public class PostContents {
    private String when;
    private int reply;
    private int viewcount;
    private String title;
    private String context;
    private String lim;

    public PostContents(){
        this.when =  "";
        this.reply = 0;
        this.viewcount=0;
        this.title = "";
        this.context = "";
        this.lim = "";
    }

    public PostContents(String w, int r, int v, String t, String c, String l){
        this.when =  w;
        this.reply = r;
        this.viewcount=v;
        this.title = t;
        this.context = c;
        this.lim = l;
    }

    public String getWhen() {
        return when;
    }

    public int getReply() {
        return reply;
    }

    public int getViewcount() {
        return viewcount;
    }

    public String getTitle() {
        return title;
    }

    public String getContext() {
        return context;
    }

    public String getLim() {
        return lim;
    }

    public void setWhen(String when) {
        this.when = when;
    }

    public void setReply(int reply) {
        this.reply = reply;
    }

    public void setViewcount(int viewcount) {
        this.viewcount = viewcount;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public void setLim(String lim) {
        this.lim = lim;
    }
}
