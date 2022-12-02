package com.aip.commerce_e.drawerFragments.product.actions;


import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.aip.commerce_e.MainActivity;
import com.aip.commerce_e.databinding.FragmentProductViewBinding;
import com.aip.commerce_e.models.CartProduct;
import com.aip.commerce_e.models.Product;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.jetbrains.annotations.NotNull;
import java.util.List;


public class ProductViewFragment extends Fragment {
    FragmentProductViewBinding binding;
    FirebaseStorage storage;
    StorageReference storageReference;
    Product product;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        binding = FragmentProductViewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable Bundle savedInstanceState) {
        if(getArguments()!=null){
            product = (Product) getArguments().getSerializable("product");
            downloadImgs();
            binding.idTag.setText("# " + product.getId());
            binding.descriptionTag.setText(product.getDescription());
            binding.nameTag.setText(product.getName());
            binding.priceTag.setText("$ " + product.getPrice());
            binding.addToCart.setOnClickListener(view1 -> {
                Integer pos = MainActivity.hasProduct(product);
                if (pos >= 0){
                    Integer quant = MainActivity.cart.get(pos).getQuantity();
                    MainActivity.cart.get(pos).setQuantity(quant+1);
                }else{
                    MainActivity.cart.add(new CartProduct(product, 1));
                }
            });
        }
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        binding = null;
    }

    private void downloadImgs() {
        StorageReference myRef = storageReference.child("products/"+product.getPhotosId());
        myRef.listAll().addOnSuccessListener(listResult -> {
            List<StorageReference> list = listResult.getItems();
            for (StorageReference ref: list) {
                ref.getDownloadUrl().addOnSuccessListener(uri -> {
                    //images.add(new CarouselItem(uri.toString()));
                    binding.carousel.addData(new CarouselItem(uri.toString()));
                });
            }
        });
        // make call to viewpager adapter here
        /*adapter.setImgUrls(imageUrls);
        Log.i("Cantidad de fotos", String.valueOf(imageUrls.size()));*/

    }
}