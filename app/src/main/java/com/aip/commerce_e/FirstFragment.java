package com.aip.commerce_e;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.aip.commerce_e.databinding.FragmentFirstBinding;
import com.aip.commerce_e.models.User;
import com.aip.commerce_e.models.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private FirebaseAuth mAuth;
    private UserViewModel userViewModel;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
//        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //SharedPreferences sharedPreferences = getContext().getSharedPreferences("e-commerce", Context.MODE_PRIVATE);
        binding.btnLogin.setOnClickListener(view12 -> {
            if (validate()){
                login();
            }else {
                Toast.makeText(binding.getRoot().getContext(), "Must fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnRegister.setOnClickListener(view1 -> {
            NavHostFragment.findNavController(FirstFragment.this)
                    .navigate(R.id.registerUserFragment2);
        });
    }
    public Boolean validate(){
        if (binding.username.getText().toString().isEmpty() || binding.password.getText().toString().isEmpty()){
            return false;
        }
        return  true;
    }

    public void login(){
        mAuth.signInWithEmailAndPassword(binding.username.getText().toString(), binding.password.getText().toString())
                .addOnCompleteListener((Activity) binding.getRoot().getContext(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        //Toast.makeText(binding.getRoot().getContext(), "Logueado "+ user.getEmail(), Toast.LENGTH_SHORT).show();
                        //SharedPreferences.Editor editor = sharedPreferences.edit();
                       // editor.putString("email", user.getEmail());
                        //editor.apply();
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "signInWithEmail:failure", task.getException());
                        Toast.makeText(binding.getRoot().getContext(), task.getException().getMessage(),
                                Toast.LENGTH_LONG).show();

                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
//            User userLogged = userViewModel.findUserByEmail(currentUser.getEmail());
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("user", userLogged);
            /*NavHostFragment.findNavController(FirstFragment.this)
                    .navigate(R.id.homeFragment);*/
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}