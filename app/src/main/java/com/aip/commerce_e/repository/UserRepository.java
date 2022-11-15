package com.aip.commerce_e.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.aip.commerce_e.database.AppDatabase;
import com.aip.commerce_e.database.ProductDao;
import com.aip.commerce_e.database.UserDao;
import com.aip.commerce_e.models.Product;
import com.aip.commerce_e.models.User;

import java.util.List;

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
}
