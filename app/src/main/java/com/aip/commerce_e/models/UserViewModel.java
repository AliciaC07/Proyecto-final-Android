package com.aip.commerce_e.models;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.aip.commerce_e.repository.UserRepository;
import com.google.common.util.concurrent.ListenableFuture;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserViewModel extends AndroidViewModel {
    private UserRepository userRepository;
    private LiveData<List<User>> allUsers;

    public UserViewModel(@NonNull @NotNull Application application) {
        super(application);
        userRepository = new UserRepository(application);
        allUsers = userRepository.findAll();


    }

    public void insert(User user){
        userRepository.insert(user);
    }
    public User findUserByEmail(String email) throws ExecutionException, InterruptedException {
        return userRepository.findUserByEmail(email);
    }
    public void deleteAll() {
        userRepository.deleteAll();
    }

    public LiveData<List<User>> findAll() {
        return allUsers;
    }
    public void deleteById(Integer id){
        userRepository.deleteById(id);
    }
    public void update(User user){
        userRepository.update(user);
    }
}
