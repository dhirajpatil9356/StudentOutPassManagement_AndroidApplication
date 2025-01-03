package com.tech.outpassapp;
public class OutpassRequest {
    private String studentName;
    private String rollNumber;
    private String branch;
    private String studentID;
    private String roomNumber;


    public OutpassRequest() {

    }

    public OutpassRequest(String studentName, String rollNumber, String branch, String studentID, String roomNumber) {
        this.studentName = studentName;
        this.rollNumber = rollNumber;
        this.branch = branch;
        this.studentID = studentID;
        this.roomNumber = roomNumber;
    }


    public String getStudentName() { return studentName; }
    public String getRollNumber() { return rollNumber; }
    public String getBranch() { return branch; }
    public String getStudentID() { return studentID; }
    public String getRoomNumber() { return roomNumber; }
}
