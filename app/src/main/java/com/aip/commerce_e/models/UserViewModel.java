package com.aip.commerce_e.models;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.aip.commerce_e.repository.UserRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;

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
    public void deleteAll() {
        userRepository.deleteAll();
    }

    public LiveData<List<User>> findAll() {
        return allUsers;
    }

    public void deleteById(Integer id){
        userRepository.deleteById(id);
    }
}
