package com.aip.commerce_e.repository;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.aip.commerce_e.database.AppDatabase;
import com.aip.commerce_e.database.UserDao;
import com.aip.commerce_e.models.User;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserRepository {
    private UserDao userDao;
    private LiveData<List<User>> allUsers;
    private LiveData<List<User>> activeAllUsers;


    public UserRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        userDao = db.userDao();
        allUsers = userDao.getAllUsers();
        activeAllUsers = userDao.getAllUserActive();

    }

    public void insert(User user) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                userDao.insert(user));
    }
    public void update(User user) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                userDao.update(user));
    }
    public User findUserByEmail(String email) throws ExecutionException, InterruptedException {
       return new findUserAsyncTask(userDao).execute(email).get();
    }

    public void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(() ->
                userDao.deleteAll());
    }

    public LiveData<List<User>> findAll() {
        return allUsers;
    }

    public LiveData<List<User>> findAllActive(){
        return activeAllUsers;
    }

    public void deleteById(Integer id){
        AppDatabase.databaseWriteExecutor.execute(() ->
                userDao.deleteById(false, id));
    }
    private static class findUserAsyncTask extends AsyncTask<String, Void, User>{
        private UserDao asyncProdDao;

        findUserAsyncTask(UserDao asyncProdDao){ this.asyncProdDao = asyncProdDao; }
        @Override
        protected User doInBackground(String... emails) {
            User user = asyncProdDao.findByEmail(emails[0]);
            return user;
        }
    }


}
