package com.aip.commerce_e;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.aip.commerce_e.databinding.FragmentFirstBinding;
import com.aip.commerce_e.models.User;
import com.aip.commerce_e.models.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private FirebaseAuth mAuth;
    private UserViewModel userViewModel;
    public ProgressDialog loadingBar;
    public ProgressDialog loginprogress;

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
        loginprogress=new ProgressDialog(binding.getRoot().getContext());
        binding.btnLogin.setOnClickListener(view12 -> {
            if (validate()){
                loginprogress.setTitle("Logging In");
                loginprogress.setMessage("Please Wait ");
                loginprogress.setCanceledOnTouchOutside(false);
                loginprogress.show();
                login();
            }else {
                Toast.makeText(binding.getRoot().getContext(), "Must fill all fields", Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnRegister.setOnClickListener(view1 -> {
            NavHostFragment.findNavController(FirstFragment.this)
                    .navigate(R.id.registerUserFragment2);
        });
        binding.btnForgot.setOnClickListener(view13 -> showRecoverPasswordDialog());
    }
    public Boolean validate(){
        if (binding.username.getText().toString().isEmpty() || binding.password.getText().toString().isEmpty()){
            return false;
        }
        return  true;
    }
    private void showRecoverPasswordDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(binding.getRoot().getContext());
        builder.setTitle("Recover Password");
        LinearLayout linearLayout=new LinearLayout(binding.getRoot().getContext());
        final EditText emailet= new EditText(binding.getRoot().getContext());

        // write the email using which you registered
        emailet.setHint("Enter email");
        emailet.setMinEms(16);
        emailet.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(emailet);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);

        // Click on Recover and a email will be sent to your registered email id
        builder.setPositiveButton("Recover", (dialog, which) -> {
            String email=emailet.getText().toString().trim();
            beginRecovery(email);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
    private void beginRecovery(String email) {
        loadingBar = new ProgressDialog(binding.getRoot().getContext());
        loadingBar.setMessage("Sending Email....");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        // calling sendPasswordResetEmail
        // open your email and write the new
        // password and then you can login
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            loadingBar.dismiss();
            if(task.isSuccessful())
            {
                // if isSuccessful then done message will be shown
                // and you can change the password
                Toast.makeText(binding.getRoot().getContext(),"Email sent",Toast.LENGTH_LONG).show();
            }
            else {
                Toast.makeText(binding.getRoot().getContext(), task.getException().getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(e -> {
            loadingBar.dismiss();
            Toast.makeText(binding.getRoot().getContext(),"Error Failed",Toast.LENGTH_LONG).show();
        });
    }

    public void login(){
        mAuth.signInWithEmailAndPassword(binding.username.getText().toString(), binding.password.getText().toString())
                .addOnCompleteListener((Activity) binding.getRoot().getContext(), task -> {
                    if (task.isSuccessful()) {
                        loginprogress.dismiss();
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                    } else {
                        // If sign in fails, display a message to the user.
                        loginprogress.hide();
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