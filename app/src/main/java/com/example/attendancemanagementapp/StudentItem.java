package com.example.attendancemanagementapp;

public class StudentItem {
    private long sid;
    private int roll;
    private String name;
    private String status;


    public StudentItem(long sid,int roll, String name) {
        this.roll = roll;
        this.sid = sid;
        this.name = name;
        status="";
    }

    public int getId() {
        return roll;
    }

    public void setId(int roll) {
        this.roll = roll;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }
}
