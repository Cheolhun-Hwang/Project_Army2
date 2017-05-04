package com.hch.qewqs.project_army;

/**
 * Created by hch on 2017-05-03.
 */

public class PostListView {
    public String postnumber;
    public String title;
    public String date;
    public int viewcount;
    public int comments;

    public PostListView(){
        this.postnumber="";
        this.title = "";
        this.date = "";
        this.viewcount=0;
        this.comments = 0;
    }

    public PostListView(String p, String t, String d, int v, int c){
        this.postnumber=p;
        this.title = t;
        this.date = d;
        this.viewcount=v;
        this.comments = c;
    }

    public String getPostnumber() {
        return postnumber;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public int getViewcount() {
        return viewcount;
    }

    public int getComments() {
        return comments;
    }

    public void setPostnumber(String postnumber) {
        this.postnumber = postnumber;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setViewcount(int viewcount) {
        this.viewcount = viewcount;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }
}
