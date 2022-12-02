package com.aip.commerce_e.drawerFragments.product.actions;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;
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
import com.aip.commerce_e.notification.NotificationCreate;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Objects;


public class ProductViewFragment extends Fragment {
    FragmentProductViewBinding binding;
    FirebaseStorage storage;
    StorageReference storageReference;
    Product product;
    String CHANNEL_ID = "Notification.Add";


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        binding = FragmentProductViewBinding.inflate(inflater, container, false);
        notificationChannel();
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
                    notificationAdd(product);
                }else{
                    MainActivity.cart.add(new CartProduct(product, 1));
                    notificationAdd(product);
                }
                Genson genson = new Genson();
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("ecommerce", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String jsonAux = genson.serialize(MainActivity.cart, GenericType.of(List.class));
                editor.putString("ecommerce", jsonAux).apply();
                Toast.makeText(getContext(),"Product added to cart", Toast.LENGTH_SHORT).show();
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
    public void notificationChannel(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channelCompat = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            channelCompat.setShowBadge(true);
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channelCompat);
        }
    }

    public void notificationAdd(Product product){
        NotificationCreate notificationCreate = new NotificationCreate("Product added to cart", "Product "+ product.getName()+" added to cart");
        notificationCreate.notificationAdd(binding.getRoot().getContext(), CHANNEL_ID);
    }
}