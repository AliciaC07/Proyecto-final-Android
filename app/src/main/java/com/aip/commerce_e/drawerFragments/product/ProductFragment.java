package com.aip.commerce_e.drawerFragments.product;

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
import com.aip.commerce_e.databinding.FragmentProductBinding;
import com.aip.commerce_e.drawerFragments.category.CategoryFragment;
import com.aip.commerce_e.models.Product;
import com.aip.commerce_e.models.ProductViewModel;

import java.util.List;

public class ProductFragment extends Fragment implements RecyclerViewInterface {


    private ProductViewModel mViewModel;
    private FragmentProductBinding binding;
    List<Product> products;
    ProductListAdapter productListAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = FragmentProductBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(ProductViewModel.class);
        productListAdapter = new ProductListAdapter(null, this);
        mViewModel.findAllActive(true).observe(getViewLifecycleOwner(),
                products1 -> productListAdapter.setProducts(products1));
        binding.productRcv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.productRcv.setAdapter(productListAdapter);
//        Product p = new Product();
//        p.setPrice(50.78f);
//        p.setName("T-shirt Hombre Under Armor");
//        p.setCategoryId(1);
//        mViewModel.insert(p);
        if (MainActivity.userLogged.getRole().equalsIgnoreCase("User")){
            binding.addProductFab.setVisibility(View.INVISIBLE);
        }

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // create edit/create view
        binding.addProductFab.setOnClickListener(view1 ->
                NavHostFragment.findNavController(ProductFragment.this)
                        .navigate(R.id.createProductFragment));
    }

    @Override
    public void editOnClick(int pos) {

    }

    @Override
    public void deleteOnclick(int pos) {

    }

    @Override
    public void navigateOnClick(int pos) {
        Product product = productListAdapter.products.get(pos);
        Bundle bundle = new Bundle();

        bundle.putSerializable("product", product);
        NavHostFragment.findNavController(ProductFragment.this)
                .navigate(R.id.productViewFragment, bundle);
    }
}