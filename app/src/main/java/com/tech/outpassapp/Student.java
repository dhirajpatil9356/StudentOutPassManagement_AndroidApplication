package com.tech.outpassapp;

public class Student {
    private String name;
    private String rollNumber;
    private String branch;
    private String roomNumber;
    private String studentId;


    public Student() {
    }

    public Student(String name, String rollNumber, String branch, String roomNumber, String studentId) {
        this.name = name;
        this.rollNumber = rollNumber;
        this.branch = branch;
        this.roomNumber = roomNumber;
        this.studentId = studentId;
    }


    public String getName() {
        return name;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public String getBranch() {
        return branch;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getStudentId() {
        return studentId;
    }
}
