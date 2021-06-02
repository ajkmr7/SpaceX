package com.ajay.spacex;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

// below line is for setting table name.
@Entity(tableName = "member_table")
public class MemberModel {

    // below line is to auto increment
    // id for each course.
    @PrimaryKey(autoGenerate = true)

    // variable for our id.
    private int id;

    private String memberName, memberAgency, memberStatus, memberLink, memberID;


    // below line we are creating constructor class.
    // inside constructor class we are not passing
    // our id because it is incrementing automatically
    public MemberModel(String memberName, String memberAgency, String memberStatus, String memberLink, String memberID) {
        this.memberName = memberName;
        this.memberAgency = memberAgency;
        this.memberStatus = memberStatus;
        this.memberLink = memberLink;
        this.memberID = memberID;
    }

    // on below line we are creating
    // getter and setter methods.
    public String getMemberName() {
        return memberName;
    }

    public String getMemberAgency() {
        return memberAgency;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public String getMemberLink() {
        return memberLink;
    }

    public String getMemberID() {
        return memberID;
    }

    public int getId() {
        return id;
    }

}