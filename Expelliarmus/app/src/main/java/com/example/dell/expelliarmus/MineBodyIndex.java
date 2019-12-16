package com.example.dell.expelliarmus;

public class MineBodyIndex {
    private Integer id;
    //    private double userHeight;
//    private double userWeight;
    private  int userHeart;
    private int condition;

    public int getCondition() {
        return condition;
    }

    public void setCondition(int condition) {
        this.condition = condition;
    }

    private Integer huserBloodPressure;
    private Integer duserBloodPressure;
    //    private double userBIM;
    public MineBodyIndex(){}

    public MineBodyIndex(Integer id, double userHeight, double userWeight, int userHeart, Integer huserBloodPressure, Integer duserBloodPressure, double userBIM) {
        this.id = id;
//        this.userHeight = userHeight;
//        this.userWeight = userWeight;
        this.userHeart = userHeart;
        this.huserBloodPressure = huserBloodPressure;
        this.duserBloodPressure = duserBloodPressure;
//        this.userBIM = userBIM;
    }

    @Override
    public String toString() {
        return "BodyIndex{" +
                "id=" + id +
//                ", userHeight=" + userHeight +
//                ", userWeight=" + userWeight +
                ", userHeart=" + userHeart +
                ", huserBloodPressure=" + huserBloodPressure +
                ", duserBloodPressure=" + duserBloodPressure +
//                ", userBIM=" + userBIM +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

//    public double getUserHeight() {
//        return userHeight;
//    }
//
//    public void setUserHeight(double userHeight) {
//        this.userHeight = userHeight;
//    }
//
//    public double getUserWeight() {
//        return userWeight;
//    }
//
//    public void setUserWeight(double userWeight) {
//        this.userWeight = userWeight;
//    }

    public int getUserHeart() {
        return userHeart;
    }

    public void setUserHeart(int userHeart) {
        this.userHeart = userHeart;
    }

    public Integer getHuserBloodPressure() {
        return huserBloodPressure;
    }

    public void setHuserBloodPressure(Integer huserBloodPressure) {
        this.huserBloodPressure = huserBloodPressure;
    }

    public Integer getDuserBloodPressure() {
        return duserBloodPressure;
    }

    public void setDuserBloodPressure(Integer duserBloodPressure) {
        this.duserBloodPressure = duserBloodPressure;
    }


}
