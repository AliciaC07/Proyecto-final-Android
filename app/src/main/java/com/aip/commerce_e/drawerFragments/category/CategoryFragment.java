package com.aip.commerce_e.drawerFragments.category;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.aip.commerce_e.MainActivity;
import com.aip.commerce_e.R;
import com.aip.commerce_e.RecyclerViewInterface;
import com.aip.commerce_e.databinding.FragmentCategoryBinding;
import com.aip.commerce_e.drawerFragments.product.ProductFragment;
import com.aip.commerce_e.models.*;
import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class CategoryFragment extends Fragment implements RecyclerViewInterface {

    private CategoryViewModel categoryViewModel;
    CategoryListAdapter categoryListAdapter;
    List<Category> categories;
    private FragmentCategoryBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater,container,false);

        categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        categoryListAdapter = new CategoryListAdapter(null, this);
        categoryViewModel.findAllActive(true).observe(getViewLifecycleOwner(),
                categories -> categoryListAdapter.setCategories(categories));

        binding.categoryRcv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.categoryRcv.setAdapter(categoryListAdapter);
        if (MainActivity.userLogged.getRole().equalsIgnoreCase("User")){
            binding.addCategoryFab.setVisibility(View.INVISIBLE);
        }
//        try {
//            List<CategoryProduct> categoryProducts = categoryViewModel.getAllProductsWithCategory();
//            Log.i("Tam ", String.valueOf(categoryProducts.size()));
//            for (CategoryProduct c : categoryProducts) {
//                for (Product p: c.products
//                     ) {
//                    Log.i("Producto", p.getName());
//                }
//            }
//
//        } catch (ExecutionException | InterruptedException e) {
//            throw new RuntimeException(e);
//        }

        return binding.getRoot();
    }
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // create edit/create view
        binding.addCategoryFab.setOnClickListener(view1 ->
                NavHostFragment.findNavController(CategoryFragment.this)
                        .navigate(R.id.createCategoryFragment));
    }

    @Override
    public void editOnClick(int pos) {
        if(categories == null){
            categories = categoryListAdapter.getCategories();
        }
        Category category = categories.get(pos);
        Bundle bundle = new Bundle();
        bundle.putSerializable("editCategory", category);
        NavHostFragment.findNavController(CategoryFragment.this)
                .navigate(R.id.createCategoryFragment, bundle);
    }

    @Override
    public void deleteOnclick(int pos) {
        if (categoryViewModel == null)
            categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        Category category = categoryListAdapter.getCategories().get(pos);
        if (!category.getUsed())
            categoryViewModel.deleteById(category.getId());
        else
            Toast.makeText(getContext(), "This category cannot be deleted", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void navigateOnClick(int pos){
        if(categoryViewModel == null){
            categoryViewModel = new ViewModelProvider(requireActivity()).get(CategoryViewModel.class);
        }
        Category category = categoryListAdapter.getCategories().get(pos);
        Bundle bundle = new Bundle();
        bundle.putSerializable("productsCategory", category);
        NavHostFragment.findNavController(CategoryFragment.this)
                .navigate(R.id.productFragment, bundle);

    }
}