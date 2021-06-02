package com.ajay.spacex;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class ViewModel extends AndroidViewModel {

    // creating a new variable for course repository.
    private MemberRepository repository;

    // below line is to create a variable for live
    // data where all the courses are present.
    private LiveData<List<MemberModel>> allCourses;

    // constructor for our view modal.
    public ViewModel(@NonNull Application application) {
        super(application);
        repository = new MemberRepository(application);
        allCourses = repository.getAllMembers();
    }

    // below method is use to insert the data to our repository.
    public void insert(MemberModel model) {
        repository.insert(model);
    }

    // below method is to delete all the courses in our list.
    public void deleteAllMembers() {
        repository.deleteAllMembers();
    }

    // below method is to get all the courses in our list.
    public LiveData<List<MemberModel>> getAllMembers() {
        return allCourses;
    }
}