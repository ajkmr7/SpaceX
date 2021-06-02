package com.ajay.spacex;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class MemberRepository {

    // below line is the create a variable
    // for dao and list for all courses.
    private Dao dao;
    private LiveData<List<MemberModel>> allMembers;

    // creating a constructor for our variables
    // and passing the variables to it.
    public MemberRepository(Application application) {
        MemberDatabase database = MemberDatabase.getInstance(application);
        dao = database.Dao();
        allMembers = dao.getAllMembers();
    }

    // creating a method to insert the data to our database.
    public void insert(MemberModel model) {
        new InsertCourseAsyncTask(dao).execute(model);
    }



    // below is the method to delete all the courses.
    public void deleteAllMembers() {
        new DeleteAllMembersAsyncTask(dao).execute();
    }

    // below method is to read all the courses.
    public LiveData<List<MemberModel>> getAllMembers() {
        return allMembers;
    }


    // we are creating a async task method to insert new course.
    private static class InsertCourseAsyncTask extends AsyncTask<MemberModel, Void, Void> {
        private Dao dao;

        private InsertCourseAsyncTask(Dao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(MemberModel... model) {
            // below line is use to insert our modal in dao.
            dao.insert(model[0]);
            return null;
        }
    }


    // we are creating a async task method to delete all courses.
    private static class DeleteAllMembersAsyncTask extends AsyncTask<Void, Void, Void> {
        private Dao dao;
        private DeleteAllMembersAsyncTask(Dao dao) {
            this.dao = dao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            // on below line calling method
            // to delete all courses.
            dao.deleteAllMembers();
            return null;
        }
    }
}
