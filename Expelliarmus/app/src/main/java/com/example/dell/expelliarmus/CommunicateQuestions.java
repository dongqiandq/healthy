package com.example.dell.expelliarmus;

import java.util.ArrayList;

public class CommunicateQuestions {
    private int id;
    public int userId;
    public String userName;
    public String question;
    public String headImg;
    public ArrayList<String> listResponseContent = new ArrayList<>();
    private ArrayList<Comment> listResponse = new ArrayList<>();

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void initListResponseContent(){
        for(Comment c:listResponse){
            listResponseContent.add(c.getContent());
        }
    }

    public CommunicateQuestions(){initListResponseContent();}

    public int getId(){
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public ArrayList<String> getListResponseContent() {
        return listResponseContent;
    }

    public void setListResponseContent(ArrayList<String> listResponseContent) {
        this.listResponseContent = listResponseContent;
    }

    public ArrayList<Comment> getListResponse() {
        return listResponse;
    }

    public void setListResponse(ArrayList<Comment> listResponse) {
        this.listResponse = listResponse;
    }
}
