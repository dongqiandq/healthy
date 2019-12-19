package com.example.dell.expelliarmus;

import java.util.ArrayList;

public class CommunicateQuestions {
    private int id;
    public int userId;
    public String userName;
    public String question;
    public String headImg;
    private ArrayList<Comment> listResponse = new ArrayList<>();

    public static class Comment{
        private int id;
        private int messageId;
        private String content;
        private int sendPersonId;
        private int likeNumber;
        private String sendPersonName;

        public String getSendPersonName() {
            return sendPersonName;
        }
        public void setSendPersonName(String sendPersonName) {
            this.sendPersonName = sendPersonName;
        }
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public int getMessageId() {
            return messageId;
        }
        public void setMessageId(int messageId) {
            this.messageId = messageId;
        }
        public String getContent() {
            return content;
        }
        public void setContent(String content) {
            this.content = content;
        }
        public int getSendPersonId() {
            return sendPersonId;
        }
        public void setSendPersonId(int sendPersonId) {
            this.sendPersonId = sendPersonId;
        }
        public int getLikeNumber() {
            return likeNumber;
        }
        public void setLikeNumber(int likeNumber) {
            this.likeNumber = likeNumber;
        }
        @Override
        public String toString() {
            return "Comment [id=" + id + ", messageId=" + messageId + ", content=" + content + ", sendPersonId="
                    + sendPersonId + ", likeNumber=" + likeNumber + "]";
        }
        public Comment(int id, int messageId, String content, int sendPersonId, int likeNumber) {
            super();
            this.id = id;
            this.messageId = messageId;
            this.content = content;
            this.sendPersonId = sendPersonId;
            this.likeNumber = likeNumber;
        }

        public Comment(){}

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

//    public void initListResponseContent(){
//        for(Comment c:listResponse){
//            listResponseContent.add(c.getContent());
//        }
//    }

//    public CommunicateQuestions(){initListResponseContent();}

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

//    public ArrayList<String> getListResponseContent() {
//        return listResponseContent;
//    }
//
//    public void setListResponseContent(ArrayList<String> listResponseContent) {
//        this.listResponseContent = listResponseContent;
//    }

    public ArrayList<Comment> getListResponse() {
        return listResponse;
    }

    public void setListResponse(ArrayList<Comment> listResponse) {
        this.listResponse = listResponse;
    }
}
