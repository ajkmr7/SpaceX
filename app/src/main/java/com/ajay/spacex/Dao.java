package com.ajay.spacex;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import java.util.List;

@androidx.room.Dao
public interface Dao {

    // below method is use to
    // add data to database.
    @Insert
    void insert(MemberModel model);


    // below line is use to delete a
    // specific course in our database.
    @Delete
    void delete(MemberModel model);

    // on below line we are making query to
    // delete all courses from our database.
    @Query("DELETE FROM member_table")
    void deleteAllMembers();

    // below line is to read all the courses from our database.
    // in this we are ordering our courses in ascending order
    // with our course name.
    @Query("SELECT * FROM member_table")
    LiveData<List<MemberModel>> getAllMembers();
}
