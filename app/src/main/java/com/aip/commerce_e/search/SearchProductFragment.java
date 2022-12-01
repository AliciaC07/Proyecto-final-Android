package com.aip.commerce_e.search;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.aip.commerce_e.R;
import com.aip.commerce_e.databinding.FragmentSearchProductBinding;
import com.aip.commerce_e.drawerFragments.product.ProductFragment;
import com.aip.commerce_e.models.Product;
import com.aip.commerce_e.models.ProductViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchProductFragment extends Fragment {

    private FragmentSearchProductBinding binding;
    private List<Product> list;
    private ArrayAdapter<Product > adapter;

    private ProductViewModel productViewModel;




    public SearchProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchProductBinding.inflate(inflater, container, false);
        productViewModel = new ViewModelProvider(this).get(ProductViewModel.class);

        list = new ArrayList<>();
        try {
            list.addAll(productViewModel.getAllActiveProductsAsync());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        adapter = new ArrayAdapter<Product>(binding.getRoot().getContext(), android.R.layout.simple_list_item_1,list);
        binding.listProducts.setAdapter(adapter);

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if(list.contains(s)){
                    adapter.getFilter().filter(s);
                }
//                else{
//                    Toast.makeText(binding.getRoot().getContext(), "No Match found",Toast.LENGTH_LONG).show();
//                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return false;
            }
        });
        binding.listProducts.setOnItemClickListener((adapterView, view, i, l) -> {
            Product p = adapter.getItem(i);
            Bundle bundle = new Bundle();
            bundle.putSerializable("product", p);
            NavHostFragment.findNavController(SearchProductFragment.this)
                    .navigate(R.id.productViewFragment, bundle);
        });

        return binding.getRoot();
    }


}