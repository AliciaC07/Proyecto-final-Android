package com.aip.commerce_e;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.aip.commerce_e.databinding.FragmentFirstBinding;
import com.aip.commerce_e.databinding.FragmentRegisterUserBinding;
import com.aip.commerce_e.models.User;
import com.aip.commerce_e.models.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterUserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentRegisterUserBinding binding;
    private FirebaseAuth mAuth;
    private UserViewModel userViewModel;
    private String USER = "USER";
    private Boolean selectedImage = false;
    Integer SELECT_PICTURE = 200;
    Integer PICTURE_ID = 300;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE = 200;
    private Uri imageUri;
    private StorageTask UploadTask;
    private StorageReference ref;
    private StorageReference storageReference;


    public RegisterUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterUserFragment newInstance(String param1, String param2) {
        RegisterUserFragment fragment = new RegisterUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentRegisterUserBinding.inflate(inflater, container, false);
        storageReference = FirebaseStorage.getInstance().getReference();
        userViewModel =new ViewModelProvider(this).get(UserViewModel.class);
        mAuth = FirebaseAuth.getInstance();

        binding.imageView2.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(binding.getRoot().getContext(), view);
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.pop_up_menu, popup.getMenu());

            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(item -> {
                if (item.getTitle().equals("Choose From Gallery")){
                    imageChooser("Gallery");
                } else if (item.getTitle().equals("Choose From Camera")) {
                    takeImage();
                } else if (item.getTitle().equals("Choose From Storage")) {
                    imageChooser("image/*");
                }
                return true;
            });

            popup.show();
        });

        binding.btnRegisterUser.setOnClickListener(view -> {
            if (validate() && validatePass()){
                registerFirebase();
            }

        });

        return binding.getRoot();

    }
    public void registerFirebase(){
        mAuth.createUserWithEmailAndPassword(binding.emailtxt.getText().toString(), binding.passConfirm2.getText().toString())
                .addOnCompleteListener((Activity) binding.getRoot().getContext(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(binding.getRoot().getContext(), "Registered.",
                                Toast.LENGTH_SHORT).show();
                        uploadFile(user.getUid());

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("TAG", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(binding.getRoot().getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void insertUser( String url, String uuid){
        User user = new User();
        user.setName(binding.userNametxt.getText().toString());
        user.setLastName(binding.lastNametxt.getText().toString());
        user.setEmail(binding.emailtxt.getText().toString());
        Log.i("Role", binding.spnRoles.getSelectedItem().toString());
        user.setRole(binding.spnRoles.getSelectedItem().toString());
        user.setImageUrl(url);
        user.setUIdFirebase(uuid);
        userViewModel.insert(user);
        clear();
    }
    void imageChooser(String type) {

        // create an instance of the
        // intent of the type image

        if (type.equals("image/*")){

            Intent i = new Intent();
            i.setType(type);
            i.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncher.launch(i);
            selectedImage = true;

        }else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intent);
            selectedImage = true;
        }
    }
    void takeImage(){
        if (binding.getRoot().getContext().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
            selectedImage = true;
        }else {
            Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Start the activity with camera_intent, and request pic id
            activityResultLauncherCamera.launch(camera_intent);
            selectedImage = true;
        }

    }

    public Boolean validate(){
        if (binding.emailtxt.getText().toString().isEmpty() || binding.userNametxt.getText().toString().isEmpty() || binding.lastNametxt.getText().toString().isEmpty()
        || binding.passConfirm1.getText().toString().isEmpty() || binding.passConfirm2.getText().toString().isEmpty()){
            Toast.makeText(binding.getRoot().getContext(), "Must fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (imageUri == null){
            Toast.makeText(binding.getRoot().getContext(), "Must select an image", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }
    public Boolean validatePass(){
        if (!binding.passConfirm2.getText().toString().equals(binding.passConfirm1.getText().toString())){
            Toast.makeText(binding.getRoot().getContext(), "Password must be the same", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (binding.passConfirm2.getText().toString().length() <= 5){
            Toast.makeText(binding.getRoot().getContext(), "Password must have 6 or more words", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    public void clear(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        /*NavHostFragment.findNavController(RegisterUserFragment.this)
                .navigate(R.id.homeFragment);*/
    }
    private void uploadFile(String uuid) {
        Log.i("Here", "Bobo");
        if (imageUri != null) {
            // Defining the child of storageReference
            ref = storageReference.child("users/" + UUID.randomUUID().toString());

            UploadTask = ref.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                        Handler handler = new Handler();
                        handler.postDelayed(() -> binding.progressBar2.setProgress(0), 500);
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                insertUser(uri.toString(), uuid);
                            }
                        });
                        ///Toast.makeText(binding.getRoot().getContext(), "Upload successful", Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(binding.getRoot().getContext(), e.getMessage(), Toast.LENGTH_SHORT).show())
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        binding.progressBar2.setProgress((int) progress);
                    });
        } else {
            Toast.makeText(binding.getRoot().getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK){

                        // Get the url of the image from data
                        //Uri selectedImageUri = result.getData().getData();
                        imageUri = result.getData().getData();
                        if (null != imageUri) {
                            // update the preview image in the layout
                            binding.imageView2.setImageURI(imageUri);

                        }
                    }
                }
            }


    );
    private ActivityResultLauncher<Intent> activityResultLauncherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK){

                        Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                        imageUri = getImageUri(binding.getRoot().getContext(), photo);
                        // Set the image in imageview for display
                        binding.imageView2.setImageBitmap(photo);
                    }
                }
            }


    );

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}