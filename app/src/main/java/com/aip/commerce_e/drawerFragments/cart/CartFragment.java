package com.aip.commerce_e.drawerFragments.cart;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;
import androidx.annotation.NonNull;
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
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class CartFragment extends Fragment implements CartRCVInterface {
    FragmentCartBinding binding;
    CartListAdapter cartListAdapter;
    ProductViewModel productViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        cartListAdapter = new CartListAdapter(null, this);
        cartListAdapter.setCart(MainActivity.cart);

        binding.totalText.setText("Sub Total ("+MainActivity.cartSize()+" items): $ "+MainActivity.subTotal());

        binding.cartRcv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.cartRcv.setAdapter(cartListAdapter);

        return binding.getRoot();
    }

    @SuppressLint("SetTextI18n")
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.checkoutBtn.setOnClickListener(view1 -> {
            for (CartProduct product: MainActivity.cart) {
                Product product1 = product.getProduct();
                //product.setIsUSed(true);
                //productViewModel.update(product1);
            }
            MainActivity.cart = new ArrayList<>();
            Toast.makeText(this.getContext(), "Gracias por su compra", Toast.LENGTH_LONG).show();
            NavHostFragment.findNavController(CartFragment.this)
                    .navigate(R.id.homeFragment3);
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
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void increaseQuantity(Integer pos) {
        Integer quant = MainActivity.cart.get(pos).getQuantity();
        MainActivity.cart.get(pos).setQuantity(quant+1);
        binding.totalText.setText("Sub Total ("+MainActivity.cartSize()+" items): $ "+MainActivity.subTotal());
        Objects.requireNonNull(binding.cartRcv.getAdapter()).notifyItemChanged(pos);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void deleteFromCart(Integer pos) {
        MainActivity.cart.remove((int) pos);
        binding.totalText.setText("Sub Total ("+MainActivity.cartSize()+" items): $ "+MainActivity.subTotal());
        Objects.requireNonNull(binding.cartRcv.getAdapter()).notifyItemRemoved(pos);
    }

    @Override
    public void navigateOnClick(Integer pos) {

    }
}