package com.aip.commerce_e.drawerFragments.product;

import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import com.aip.commerce_e.databinding.FragmentProductBinding;
import com.aip.commerce_e.drawerFragments.category.CategoryFragment;
import com.aip.commerce_e.models.*;
import com.aip.commerce_e.notification.NotificationCreate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

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
        if (getArguments() != null){
            Category category = (Category) getArguments().get("productsCategory");
            try {

                mViewModel.getProductsByCategory(category.getId()).observe(getViewLifecycleOwner(),products1 -> {
                    productListAdapter.setProducts(products1);
                });
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            }


        }else {
            mViewModel.findAllActive(true).observe(getViewLifecycleOwner(),
                    products1 -> productListAdapter.setProducts(products1));
        }
        binding.productRcv.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.productRcv.setAdapter(productListAdapter);
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
        Product product = productListAdapter.products.get(pos);
        Bundle bundle = new Bundle();
        bundle.putSerializable("product", product);
        NavHostFragment.findNavController(ProductFragment.this)
                .navigate(R.id.createProductFragment, bundle);
    }

    @Override
    public void deleteOnclick(int pos) {
        Product product = productListAdapter.products.get(pos);
        if(!product.isUsed())
            mViewModel.deleteById(product.getId());
        else
            Toast.makeText(getContext(), "This product cannot be deleted", Toast.LENGTH_SHORT).show();
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