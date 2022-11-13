package com.aip.commerce_e.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.aip.commerce_e.models.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);
    @Update
    void update(User user);

    @Query("DELETE FROM user_table")
    void deleteAll();

    @Query("UPDATE user_table SET active = :status WHERE user_id = :id ")
    void deleteById(Boolean status, Integer id);


    @Query("SELECT * from user_table ORDER BY email ASC")
    LiveData<List<User>> getAllUsers();

    @Query("SELECT * from user_table WHERE active = 'true' ORDER BY email ASC")
    LiveData<List<User>> getAllUserActive();
}
