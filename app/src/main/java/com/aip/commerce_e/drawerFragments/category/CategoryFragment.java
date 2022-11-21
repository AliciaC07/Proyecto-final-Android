package com.aip.commerce_e.drawerFragments.category;

import android.annotation.SuppressLint;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.aip.commerce_e.models.Category;
import com.aip.commerce_e.models.CategoryViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.List;
import java.util.Objects;

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
        categoryViewModel.findAllActive(true).observe(getViewLifecycleOwner(), categories -> categoryListAdapter.setCategories(categories));

        binding.categoryRcv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.categoryRcv.setAdapter(categoryListAdapter);
        if (MainActivity.userLogged.getRole().equalsIgnoreCase("User")){
            binding.addCategoryFab.setVisibility(View.INVISIBLE);
        }

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
        categoryViewModel.deleteById(category.getId());

    }
}