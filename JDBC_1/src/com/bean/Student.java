package com.bean;

public class Student {
    private int flowID;
    private int tpe;
    private String IDCard;
    private String examCard;
    private String name;
    private String location;
    private int grade;

    public Student(int flowID, int tpe, String IDCard, String examCard, String name, String location, int grade) {
        this.flowID = flowID;
        this.tpe = tpe;
        this.IDCard = IDCard;
        this.examCard = examCard;
        this.name = name;
        this.location = location;
        this.grade = grade;
    }

    public Student() {
    }

    public int getFlowID() {
        return flowID;
    }

    public void setFlowID(int flowID) {
        this.flowID = flowID;
    }

    public int getTpe() {
        return tpe;
    }

    public void setTpe(int tpe) {
        this.tpe = tpe;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getExamCard() {
        return examCard;
    }

    public void setExamCard(String examCard) {
        this.examCard = examCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        System.out.println("====================查询结果==================");
        return info();
    }

    private String info() {

        return "流水号"+flowID+"\n四级/六级"+tpe+"\n准考证号"+examCard+"\n学生姓名"+name+"\n城市"+location+"\n成绩"+grade;
    }
}
