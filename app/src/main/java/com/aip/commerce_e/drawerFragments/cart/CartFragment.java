package com.aip.commerce_e.drawerFragments.cart;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationChannelCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.aip.commerce_e.MainActivity;
import com.aip.commerce_e.R;
import com.aip.commerce_e.databinding.FragmentCartBinding;
import com.aip.commerce_e.models.CartProduct;
import com.aip.commerce_e.models.Product;
import com.aip.commerce_e.models.ProductViewModel;
import com.aip.commerce_e.notification.NotificationCreate;
import com.aip.commerce_e.notification.Notifications;
import com.owlike.genson.GenericType;
import com.owlike.genson.Genson;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartFragment extends Fragment implements CartRCVInterface {
    FragmentCartBinding binding;
    CartListAdapter cartListAdapter;
    Genson genson;
    ProductViewModel productViewModel;

    String CHANNEL_ID = "Notification.cart";
    private ProgressDialog progress;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        progress = new ProgressDialog(binding.getRoot().getContext());
        genson = new Genson();

        cartListAdapter = new CartListAdapter(null, this);
        cartListAdapter.setCart(MainActivity.cart);

        binding.totalText.setText("Sub Total ("+MainActivity.cartSize()+" items): "+MainActivity.subTotal()+" $");

        binding.cartRcv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.cartRcv.setAdapter(cartListAdapter);
       notificationChannel();

        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.checkoutBtn.setOnClickListener(view1 -> {
            progress.setTitle("Processing purchase");
            progress.setMessage("Please Wait ");
            progress.setCanceledOnTouchOutside(false);
            progress.show();
            Handler handler = new Handler();
            handler.postDelayed(()->{
                progress.dismiss();
                for (CartProduct product: MainActivity.cart) {
                    Product product1 = product.getProduct();
                    product1.setUsed(true);
                    productViewModel.update(product1);
                }
                notificationAdd();
                MainActivity.cart = new ArrayList<>();
                Genson genson = new Genson();
                SharedPreferences sharedPreferences = requireContext().getSharedPreferences("ecommerce", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                String jsonAux = genson.serialize(MainActivity.cart, GenericType.of(List.class));
                editor.putString("ecommerce", jsonAux).apply();

                NavHostFragment.findNavController(CartFragment.this)
                        .navigate(R.id.homeFragment3);
            }, 3000);
            Toast.makeText(this.getContext(), "Thank you for your purchase", Toast.LENGTH_LONG).show();


        });
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void decreaseQuantity(Integer pos) {
        Integer quant = MainActivity.cart.get(pos).getQuantity();
        if(quant > 1) {
            MainActivity.cart.get(pos).setQuantity(quant - 1);
            binding.totalText.setText("Sub Total (" + MainActivity.cartSize() + " items): $ " + MainActivity.subTotal());
            Objects.requireNonNull(binding.cartRcv.getAdapter()).notifyItemChanged(pos);

            SharedPreferences sharedPreferences = requireContext().getSharedPreferences("ecommerce", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            String jsonAux = genson.serialize(MainActivity.cart, GenericType.of(List.class));
            editor.putString("ecommerce", jsonAux).apply();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void increaseQuantity(Integer pos) {
        Integer quant = MainActivity.cart.get(pos).getQuantity();
        MainActivity.cart.get(pos).setQuantity(quant+1);
        binding.totalText.setText("Sub Total ("+MainActivity.cartSize()+" items): $ "+MainActivity.subTotal());
        Objects.requireNonNull(binding.cartRcv.getAdapter()).notifyItemChanged(pos);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("ecommerce", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonAux = genson.serialize(MainActivity.cart, GenericType.of(List.class));
        editor.putString("ecommerce", jsonAux).apply();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void deleteFromCart(Integer pos) {
        MainActivity.cart.remove((int) pos);
        binding.totalText.setText("Sub Total ("+MainActivity.cartSize()+" items): $ "+MainActivity.subTotal());
        Objects.requireNonNull(binding.cartRcv.getAdapter()).notifyItemRemoved(pos);

        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("ecommerce", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonAux = genson.serialize(MainActivity.cart, GenericType.of(List.class));
        editor.putString("ecommerce", jsonAux).apply();
    }

    @Override
    public void navigateOnClick(Integer pos) {

    }

    public void notificationChannel(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channelCompat = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channelCompat);
        }
    }

    public void notificationAdd(){
        NotificationCreate notificationCreate = new NotificationCreate("Purchase made", "The purchase was completed. Your total: "+MainActivity.subTotal() +" $");
        notificationCreate.notificationAdd(binding.getRoot().getContext(), CHANNEL_ID);
        Notifications notifications = new Notifications("Purchase made", "The purchase was completed. Your total: "+MainActivity.subTotal() +" $",
                "commerce-e", "Cart");
        MainActivity.notifications.add(notifications);
    }
}