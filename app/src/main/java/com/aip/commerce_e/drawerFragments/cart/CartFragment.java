package com.aip.commerce_e.drawerFragments.cart;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.aip.commerce_e.R;
import com.aip.commerce_e.databinding.FragmentCartBinding;
import org.jetbrains.annotations.NotNull;

public class CartFragment extends Fragment implements CartRCVInterface {
    FragmentCartBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void decreaseQuantity(Integer pos) {

    }

    @Override
    public void increaseQuantity(Integer pos) {

    }

    @Override
    public void deleteFromCart(Integer pos) {

    }

    @Override
    public void navigateOnClick(Integer pos) {

    }
}