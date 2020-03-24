package com.example.todolist;

public class ExampleItem {

    private int mId;
    private String mTitle;
    private String mContent;
    private int mStatus;

    public ExampleItem(int id, String title,String content,int status){
        mId = id;
        mTitle = title;
        mStatus = status;
        mContent = content;
    }

    public void changeImage(int img){
        mStatus = img;
    }

//  Getters
    public String getTitleText(){
        return mTitle;
    }
    public int getStatusImage(){
        return mStatus;
    }
    public int getId(){
        return mId;
    }
    public String getContentText(){
        return mContent;
    }
}

