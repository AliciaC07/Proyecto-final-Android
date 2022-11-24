package com.aip.commerce_e.drawerFragments.product.actions;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import com.aip.commerce_e.R;
import com.aip.commerce_e.databinding.FragmentCreateProductBinding;
import com.aip.commerce_e.models.Category;
import com.aip.commerce_e.models.CategoryViewModel;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class CreateProductFragment extends Fragment {
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    FragmentCreateProductBinding binding;
    ArrayList<Uri> imageUris  = new ArrayList<>();
    boolean selectedImage = false;
    private static final int IMAGE_PICKER = 1;
    private static final int READ_PERMISSION = 101;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateProductBinding.inflate(inflater,container,false);
        CategoryViewModel categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        categoryViewModel.findAllActive(true).observe(getViewLifecycleOwner(),
                categories -> {
                    ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<Category>(getContext(), android.R.layout.simple_spinner_item, categories);
                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.productCategorySpn.setAdapter(categoryAdapter);
                });


        binding.productImagePicker.setOnClickListener(view -> {
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
        binding.btnRegisterProduct.setOnClickListener(view -> {
            Category category = (Category) binding.productCategorySpn.getSelectedItem();
            Toast.makeText(getContext(),category.getId().toString(), Toast.LENGTH_SHORT).show();
        });
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK && result.getData()!= null){
                        if(result.getData().getClipData() != null){
                            int imgCount = result.getData().getClipData().getItemCount();
                            for(int i = 0; i < imgCount; i ++){
                                Uri imageUri = result.getData().getClipData().getItemAt(i).getUri();
                                imageUris.add(imageUri);
                            }
                            binding.productImagePicker.setImageURI(imageUris.get(0));
                        }else{
                            Uri imageUri = result.getData().getData();
                            if (null != imageUri) {
                                // update the preview image in the layout
                                // select first image from bunch
                                binding.productImagePicker.setImageURI(imageUri);
                            }
                        }
                    }
                }
            }
    );
    private ActivityResultLauncher<Intent> activityResultLauncherCamera = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK){

                        Bitmap photo = (Bitmap) result.getData().getExtras().get("data");
                        Uri imageUri = getImageUri(binding.getRoot().getContext(), photo);
                        // Set the image in imageview for display
                        binding.productImagePicker.setImageBitmap(photo);
                    }
                }
            }
    );

    void imageChooser(String type) {
        if (type.equals("image/*")){
            Intent i = new Intent();
            i.setType(type);
            i.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncher.launch(i);
            selectedImage = true;
        }else {
            Intent intent = new Intent(Intent.ACTION_PICK_ACTIVITY, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}