package com.aip.commerce_e.drawerFragments.category.actions;

import android.Manifest;
import android.annotation.SuppressLint;
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
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.aip.commerce_e.MainActivity;
import com.aip.commerce_e.R;
import com.aip.commerce_e.databinding.FragmentCreateCategoryBinding;
import com.aip.commerce_e.models.Category;
import com.aip.commerce_e.models.CategoryViewModel;
import com.aip.commerce_e.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.*;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class CreateCategoryFragment extends Fragment {

    private FragmentCreateCategoryBinding binding;
    private CategoryViewModel categoryViewModel;
    private Category aux;
    private Uri imageUri;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private Boolean selectedImage = false;
    private StorageTask UploadTask;
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference ref;
    private boolean isEdit = false;
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        binding = FragmentCreateCategoryBinding.inflate(inflater, container, false);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        if(getArguments() != null){
            isEdit = true;
            binding.btnRegisterCategory.setText("Update");
            aux = (Category) getArguments().getSerializable("editCategory");
            binding.categoryNameTxt.setText(aux.getName());
            if(aux.getImageUrl() != null) {
                Uri categoryUri = Uri.parse(aux.getImageUrl());
                //FileDownloadTask path = storageReference.getFile(categoryUri);
                Picasso.get().load(categoryUri).into(binding.categoryImageView);
            }
        }
        binding.categoryImageView.setOnClickListener(view -> {
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

        binding.btnRegisterCategory.setOnClickListener(view -> {
            if(!isEdit){
                if(validate())
                    uploadFile(UUID.randomUUID().toString());
            }else{
                if(validate() && aux.getImageUrl()!=null){
                    updateFile(aux.getUIdFirebase());
                }
            }
        });
        return binding.getRoot();
    }

    public Boolean validate(){
        if (binding.categoryNameTxt.getText().toString().isEmpty()){
            Toast.makeText(binding.getRoot().getContext(), "Must fill all fields", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }
    public Boolean validateImage(){
        if (imageUri == null){
            Toast.makeText(binding.getRoot().getContext(), "Must select an image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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

    }

    private void uploadFile(String uuid) {
        if (imageUri != null) {
            // Defining the child of storageReference
            ref = storageReference.child("categories/" + uuid);

            UploadTask = ref.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
//                        Handler handler = new Handler();
//                        handler.postDelayed(() -> binding.categoryProgressBar.setProgress(0), 500);
                        ref.getDownloadUrl().addOnSuccessListener(uri -> insertCategory(uri.toString(), uuid));
                        ///Toast.makeText(binding.getRoot().getContext(), "Upload successful", Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(binding.getRoot().getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
//                    .addOnProgressListener(taskSnapshot -> {
//                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                        binding.categoryProgressBar.setProgress((int) progress);
//                    });
        } else {
            Toast.makeText(binding.getRoot().getContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateFile(String uuid){
        if (imageUri != null) {
            // Defining the child of storageReference
            ref = storageReference.child("categories/" + uuid);

            UploadTask = ref.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
//                        Handler handler = new Handler();
//                        handler.postDelayed(() -> binding.categoryProgressBar.setProgress(0), 500);
                        ref.getDownloadUrl().addOnSuccessListener(uri -> updateCategory(uri.toString(), uuid));
                        ///Toast.makeText(binding.getRoot().getContext(), "Upload successful", Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> Toast.makeText(binding.getRoot().getContext(), e.getMessage(), Toast.LENGTH_SHORT).show());
//                    .addOnProgressListener(taskSnapshot -> {
//                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                        binding.categoryProgressBar.setProgress((int) progress);
//                    });
        } else {
            //Toast.makeText(binding.getRoot().getContext(), "No file selected", Toast.LENGTH_SHORT).show();
            updateCategory(aux.getImageUrl(), aux.getUIdFirebase());
        }
    }
    public void updateCategory(String uri, String uuid){
        aux.setName(binding.categoryNameTxt.getText().toString());
        if(uuid != null){
            aux.setUIdFirebase(uuid);
            aux.setImageUrl(uri);
        }
        categoryViewModel.update(aux);
        clear();
    }
    public void insertCategory( String url, String uuid){
        Category category = new Category();
        category.setName(binding.categoryNameTxt.getText().toString());
        category.setImageUrl(url);
        category.setUIdFirebase(uuid);
        category.setActive(true);
        categoryViewModel.insert(category);
        clear();
    }
    public void clear(){
        //binding.categoryProgressBar.setProgress(0);
        NavHostFragment.findNavController(CreateCategoryFragment.this)
                .navigate(R.id.categoryFragment);
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
                            binding.categoryImageView.setImageURI(imageUri);

                        }
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
    private ActivityResultLauncher<Intent> activityResultLauncherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
            , new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK){

                        Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                        imageUri = getImageUri(binding.getRoot().getContext(), photo);
                        // Set the image in imageview for display
                        binding.categoryImageView.setImageBitmap(photo);
                    }
                }
            }


    );



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}