package com.aip.commerce_e.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import com.aip.commerce_e.LoginActivity;
import com.aip.commerce_e.MainActivity;
import com.aip.commerce_e.R;
import com.aip.commerce_e.databinding.FragmentRegisterUserBinding;
import com.aip.commerce_e.databinding.FragmentUserViewBinding;
import com.aip.commerce_e.drawerFragments.product.actions.CreateProductFragment;
import com.aip.commerce_e.models.Product;
import com.aip.commerce_e.models.User;
import com.aip.commerce_e.models.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class UserViewFragment extends Fragment {

    private FragmentUserViewBinding binding;
    private UserViewModel userViewModel;
    private ProgressDialog progress;
    public UserViewFragment() {
        // Required empty public constructor
    }

    public static UserViewFragment newInstance(String param1, String param2) {
        UserViewFragment fragment = new UserViewFragment();
        Bundle args = new Bundle();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentUserViewBinding.inflate(inflater, container, false);
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        User userlogged = MainActivity.userLogged;
        progress = new ProgressDialog(binding.getRoot().getContext());

        binding.emailtxtView.setText(userlogged.getEmail());
        binding.emailtxtView.setEnabled(false);
        Picasso.get().load(userlogged.getImageUrl())
                .into(binding.imageView3);
        binding.lastNametxtView.setText(userlogged.getLastName());
        binding.userNametxtView.setText(userlogged.getName());
        binding.phoneNumberview.setText(userlogged.getPhone());
        List<String> list = new ArrayList<>();
        list.add(userlogged.getRole());
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                (binding.getRoot().getContext(), android.R.layout.simple_spinner_item,
                        list);
        arrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);

        binding.spnRolesView.setAdapter(arrayAdapter);
        binding.spnRolesView.setEnabled(false);
        binding.phoneNumberview.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        binding.btnEditUser.setOnClickListener(view -> {
            progress.setTitle("Updating");
            progress.setMessage("Please Wait ");
            progress.setCanceledOnTouchOutside(false);
            progress.show();
            Handler handler = new Handler();

            if (binding.swPassword.isChecked()){
                handler.postDelayed(() ->{
                    progress.dismiss();
                    if (validatePass()){
                        updateUser(userlogged);
                        updatePassword(binding.passConfirm2View.getText().toString());

                        NavHostFragment.findNavController(UserViewFragment.this)
                                .navigate(R.id.homeFragment3);
                    }else {
                        Toast.makeText(binding.getRoot().getContext(), "Password does not match", Toast.LENGTH_SHORT).show();
                    }
                },3000);
            }else {
                handler.postDelayed(() -> {
                    progress.dismiss();
                    updateUser(userlogged);
                        //progress.dismiss();
                        Toast.makeText(binding.getRoot().getContext(), "User updated", Toast.LENGTH_SHORT).show();
                        NavHostFragment.findNavController(UserViewFragment.this)
                                .navigate(R.id.homeFragment3);
                }, 2000);

            }
        });





        return binding.getRoot();
    }

    public void updateUser(User userEdit){
        userEdit.setPhone(binding.phoneNumberview.getText().toString());
        userEdit.setName(binding.userNametxtView.getText().toString());
        userEdit.setLastName(binding.lastNametxtView.getText().toString());
        userViewModel.update(userEdit);
        MainActivity.userLogged.setPhone(userEdit.getPhone());
        MainActivity.userLogged.setName(userEdit.getName());
        MainActivity.userLogged.setLastName(userEdit.getLastName());
        NavigationView navigationView = getActivity().findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.username);
        username.setText(userEdit.getName()+" "+userEdit.getLastName());

    }
    public void updatePassword(String password){
        FirebaseAuth.getInstance().getCurrentUser().updatePassword(password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                progress.dismiss();
                Toast.makeText(binding.getRoot().getContext(), "Password and user updated", Toast.LENGTH_SHORT).show();

            }else {
                progress.hide();
                Toast.makeText(binding.getRoot().getContext(), task.getException().getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    public Boolean validatePass(){
        if (!binding.passConfirm2View.getText().toString().equals(binding.passConfirm1View.getText().toString())){
            Toast.makeText(binding.getRoot().getContext(), "Password must be the same", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.passConfirm2View.getText().toString().length() <= 5){
            Toast.makeText(binding.getRoot().getContext(), "Password must have 6 or more words", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}