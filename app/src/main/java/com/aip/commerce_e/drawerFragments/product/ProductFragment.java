package com.aip.commerce_e.drawerFragments.product;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.aip.commerce_e.R;
import com.aip.commerce_e.databinding.FragmentCategoryBinding;
import com.aip.commerce_e.databinding.FragmentProductBinding;
import com.aip.commerce_e.models.Product;
import com.aip.commerce_e.models.ProductViewModel;

public class ProductFragment extends Fragment {

    private ProductViewModel mViewModel;
    private FragmentProductBinding binding;

    public static ProductFragment newInstance() {
        return new ProductFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentProductBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
//        Product p = new Product();
//        p.setPrice(50.78f);
//        p.setName("T-shirt Hombre Under Armor");
//        p.setCategoryId(1);
//        mViewModel.insert(p);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO: Use the ViewModel
    }

}