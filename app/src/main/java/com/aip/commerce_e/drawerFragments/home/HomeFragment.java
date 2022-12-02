package com.aip.commerce_e.drawerFragments.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleCoroutineScope;
import androidx.lifecycle.ViewModelProvider;
import com.aip.commerce_e.MainActivity;
import com.aip.commerce_e.R;
import com.aip.commerce_e.databinding.ActivityMainBinding;
import com.aip.commerce_e.databinding.FragmentHomeBinding;
import com.aip.commerce_e.models.User;
import com.aip.commerce_e.models.UserViewModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private FragmentHomeBinding binding;
    private UserViewModel userViewModel;
    private List<User> usersList = new ArrayList<>();
    private User userLogged;
    private  String email = "12345@gmail.com";


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        try {
            userLogged = userViewModel.findUserByEmail(email);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }




        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }


    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




    }

}