package com.aip.commerce_e.drawerFragments.product.actions;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import com.aip.commerce_e.R;
import com.aip.commerce_e.databinding.FragmentCreateProductBinding;
import com.aip.commerce_e.models.Category;
import com.aip.commerce_e.models.CategoryViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CreateProductFragment extends Fragment {
    FragmentCreateProductBinding binding;

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
}