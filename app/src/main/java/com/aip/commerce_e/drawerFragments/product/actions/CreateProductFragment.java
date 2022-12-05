package com.aip.commerce_e.drawerFragments.product.actions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.Toast;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.aip.commerce_e.R;
import com.aip.commerce_e.databinding.FragmentCreateProductBinding;
import com.aip.commerce_e.databinding.FragmentEditProductBinding;
import com.aip.commerce_e.models.Category;
import com.aip.commerce_e.models.CategoryViewModel;
import com.aip.commerce_e.models.Product;
import com.aip.commerce_e.models.ProductViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.*;
import com.squareup.picasso.Picasso;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static android.app.Activity.RESULT_OK;


public class CreateProductFragment extends Fragment {
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    FragmentCreateProductBinding bindingCreate;
    FragmentEditProductBinding bindingEdit;
    ProductViewModel productViewModel;
    CategoryViewModel categoryViewModel;
    private StorageTask<com.google.firebase.storage.UploadTask.TaskSnapshot> UploadTask;
    private List<StorageTask<com.google.firebase.storage.UploadTask.TaskSnapshot>> tasks;
    private String thumbnail = "";
    List<Task<Uri>> myImgs;
    Task<Uri> img;
    private Product aux;
    ArrayList<Uri> imageUris  = new ArrayList<>();
    boolean selectedImage = false;
    FirebaseStorage storage;
    StorageReference storageReference;
    StorageReference ref;
    private boolean isEdit = false;
    private ProgressDialog progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private View createView(LayoutInflater inflater, ViewGroup container){
        bindingCreate = FragmentCreateProductBinding.inflate(inflater,container,false);
        progress = new ProgressDialog(bindingCreate.getRoot().getContext());
        categoryViewModel.findAllActive(true).observe(getViewLifecycleOwner(),
                categories -> {
                    ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<Category>(getContext(), android.R.layout.simple_spinner_item, categories);
                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    bindingCreate.productCategorySpn.setAdapter(categoryAdapter);
                    if(getArguments() != null) {
                        Category myCat = null;
                        try {
                            myCat = categoryViewModel.findById(aux.getCategoryId());
                            int i = categories.indexOf(myCat);
                            bindingCreate.productCategorySpn.setSelection(i);
                        } catch (ExecutionException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
        bindingCreate.productImagePicker.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(bindingCreate.getRoot().getContext(), view);
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

        bindingCreate.btnRegisterProduct.setOnClickListener(view -> {
            progress.setTitle("Registering Product");
            progress.setMessage("Please Wait ");
            progress.setCanceledOnTouchOutside(false);
            progress.show();
            // for each image upload file
            if(validForm() && validateImg()){
                // Toast.makeText(getContext(), "Registering Product", Toast.LENGTH_SHORT).show();
                String uuid = UUID.randomUUID().toString();
                for(int i = 0; i < imageUris.size(); i ++){
                    uploadFile(uuid, imageUris.get(i),i == imageUris.size()-1, i == 0);
                }
            } else{
                if(!validateImg())
                    Toast.makeText(getContext(), "At least 1 image must be selected", Toast.LENGTH_SHORT).show();
                else if(!validForm())
                    Toast.makeText(getContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
            }
        });

        return bindingCreate.getRoot();
    }
    private View editView(LayoutInflater inflater, ViewGroup container){
        bindingEdit = FragmentEditProductBinding.inflate(inflater, container, false);
        progress = new ProgressDialog(bindingEdit.getRoot().getContext());
        categoryViewModel.findAllActive(true).observe(getViewLifecycleOwner(),
                categories -> {
                    ArrayAdapter<Category> categoryAdapter = new ArrayAdapter<Category>(getContext(), android.R.layout.simple_spinner_item, categories);
                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    bindingEdit.productCategorySpn.setAdapter(categoryAdapter);
                    if(getArguments() != null) {
                        Category myCat = null;
                        try {
                            myCat = categoryViewModel.findById(aux.getCategoryId());
                            int i = categories.indexOf(myCat);
                            bindingEdit.productCategorySpn.setSelection(i);
                        } catch (ExecutionException | InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
        bindingEdit.btnRegisterProduct.setText("Update");
        aux = (Product) getArguments().getSerializable("product");
        bindingEdit.productDescriptionTxt.setText(aux.getDescription());
        bindingEdit.productNameTxt.setText(aux.getName());
        bindingEdit.productPriceTxt.setText(aux.getPrice().toString());
        //  replace with carousel
        downloadImgs();
        /*if(aux.getThumbnailUrl() != null) {
            Picasso.get().load(Uri.parse(aux.getThumbnailUrl())).into(bindingCreate.productImagePicker);
        }*/
        bindingEdit.imageWarning.setVisibility(View.INVISIBLE);
        bindingEdit.btnRegisterProduct.setOnClickListener(view -> {
            Handler handler = new Handler();
            progress.setTitle("Updating");
            progress.setMessage("Please Wait ");
            progress.setCanceledOnTouchOutside(false);
            progress.show();
            handler.postDelayed(()-> {
                progress.dismiss();
                if (validForm()){
                    //Toast.makeText(getContext(), "Updating Product", Toast.LENGTH_SHORT).show();
                    updateProduct();
                }
                else
                    Toast.makeText(getContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
            },2000);
        });

        return bindingEdit.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        productViewModel = new ViewModelProvider(requireActivity()).get(ProductViewModel.class);
        if(getArguments() != null){
            isEdit = true;
            return editView(inflater, container);
        }else {
            return createView(inflater, container);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bindingCreate = null;
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
                            bindingCreate.productImagePicker.setImageURI(imageUris.get(0));
                        }else{
                            Uri imageUri = result.getData().getData();
                            if (null != imageUri) {
                                // update the preview image in the layout
                                // select first image from bunch
                                bindingCreate.productImagePicker.setImageURI(imageUri);
                            }
                            imageUris.add(imageUri);
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
                        Uri imageUri = getImageUri(bindingCreate.getRoot().getContext(), photo);
                        imageUris.add(imageUri);
                        // Set the image in imageview for display
                        bindingCreate.productImagePicker.setImageBitmap(photo);
                    }
                }
            }
    );

    void imageChooser(String type) {
        if (type.equals("image/*")){
            Intent i = new Intent();
            i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            i.setType(type);
            i.setAction(Intent.ACTION_GET_CONTENT);
            activityResultLauncher.launch(i);
            selectedImage = true;
        }else {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            activityResultLauncher.launch(intent);
            selectedImage = true;
        }
    }
    void takeImage(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (bindingCreate.getRoot().getContext().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                selectedImage = true;
            }else {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                camera_intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                // Start the activity with camera_intent, and request pic id
                activityResultLauncherCamera.launch(camera_intent);
                selectedImage = true;
            }
        }
    }

    private void downloadImgs() {
        StorageReference myRef = storageReference.child("products/"+aux.getPhotosId());
        myRef.listAll().addOnSuccessListener(listResult -> {
            List<StorageReference> list = listResult.getItems();
            for (StorageReference ref: list) {
                ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    //images.add(new CarouselItem(uri.toString()));
                    bindingEdit.productImagePicker.addData(new CarouselItem(uri.toString()));
                });
            }
        });
        // make call to viewpager adapter here
        /*adapter.setImgUrls(imageUrls);
        Log.i("Cantidad de fotos", String.valueOf(imageUrls.size()));*/

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void uploadFile(String uuid, Uri imageUri, boolean isLast, boolean isFirst) {
        if (imageUri != null) {
            String imgUuid = UUID.randomUUID().toString();
            // Defining the child of storageReference
            StorageReference myref = storageReference.child("products/"+uuid+"/"+ imgUuid);
            if(isFirst)
                thumbnail = imgUuid;
            StorageTask<com.google.firebase.storage.UploadTask.TaskSnapshot> uploadTask = myref.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                progress.dismiss();
//                        Handler handler = new Handler();
//                        handler.postDelayed(() -> binding.categoryProgressBar.setProgress(0), 500);
                        if(isLast)
                            insertProduct(uuid, thumbnail);
                            //ref.getDownloadUrl().addOnSuccessListener(uri -> insertCategory(uri.toString(), uuid));
                        ///Toast.makeText(binding.getRoot().getContext(), "Upload successful", Toast.LENGTH_LONG).show();
                    })
                    .addOnFailureListener(e -> {Toast.makeText(bindingCreate.getRoot().getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        progress.hide();
                    });
//                    .addOnProgressListener(taskSnapshot -> {
//                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                        binding.categoryProgressBar.setProgress((int) progress);
//                    });
        } else {
            Toast.makeText(bindingCreate.getRoot().getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
    public void updateProduct(){
        aux.setName(bindingCreate.productNameTxt.getText().toString());
        aux.setPrice(Float.parseFloat(bindingCreate.productPriceTxt.getText().toString()));
        aux.setDescription(bindingCreate.productDescriptionTxt.getText().toString());
        Category category = (Category) bindingCreate.productCategorySpn.getSelectedItem();
        aux.setCategoryId(category.getId());
        productViewModel.update(aux);
        clear();
    }
    public void insertProduct(String uuid, String thumbnailUrl){
        Product product = new Product();
        Category category = (Category) bindingCreate.productCategorySpn.getSelectedItem();
        // make category used true
        category.setUsed(true);
        product.setName(bindingCreate.productNameTxt.getText().toString());
        product.setPhotosId(uuid);
        product.setActive(true);
        product.setCategoryId(category.getId());
        product.setThumbnailUrl(thumbnailUrl);
        product.setDescription(bindingCreate.productDescriptionTxt.getText().toString());
        product.setPrice(Float.parseFloat(bindingCreate.productPriceTxt.getText().toString()));
        productViewModel.insert(product);
        categoryViewModel.update(category);
        clear();
    }
    public void clear(){
        //binding.categoryProgressBar.setProgress(0);
        NavHostFragment.findNavController(CreateProductFragment.this)
                .navigate(R.id.productFragment);
    }

    private Boolean validateImg(){
        return imageUris.size() > 0;
    }

    private Boolean validForm(){
        return !bindingCreate.productNameTxt.getText().toString().equalsIgnoreCase("") &&
                !bindingCreate.productPriceTxt.getText().toString().equalsIgnoreCase("") &&
                !bindingCreate.productDescriptionTxt.getText().toString().equalsIgnoreCase("");
    }
}