package com.aip.commerce_e.drawerFragments.cart;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.aip.commerce_e.R;
import com.aip.commerce_e.databinding.FragmentCartBinding;
import org.jetbrains.annotations.NotNull;

public class CartFragment extends Fragment {
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
}