package com.aip.commerce_e.search;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import com.aip.commerce_e.R;
import com.aip.commerce_e.databinding.FragmentSearchCategoryBinding;
import com.aip.commerce_e.databinding.FragmentSearchProductBinding;
import com.aip.commerce_e.drawerFragments.category.CategoryFragment;
import com.aip.commerce_e.models.Category;
import com.aip.commerce_e.models.CategoryViewModel;
import com.aip.commerce_e.models.Product;
import com.aip.commerce_e.models.ProductViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class SearchCategoryFragment extends Fragment {

    private FragmentSearchCategoryBinding binding;
    private CategoryViewModel categoryViewModel;
    private List<Category> list;
    private ArrayAdapter<Category> adapter;

    public SearchCategoryFragment() {
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
        binding = FragmentSearchCategoryBinding.inflate(inflater, container, false);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);

        list = new ArrayList<>();
        try {
            list.addAll(categoryViewModel.getActiveCategories());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        adapter = new ArrayAdapter<Category>(binding.getRoot().getContext(), android.R.layout.simple_list_item_1,list);
        binding.listCategory.setAdapter(adapter);

        binding.searchViewCategory.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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
        binding.listCategory.setOnItemClickListener((adapterView, view, i, l) -> {
            Category category = adapter.getItem(i);
            Bundle bundle = new Bundle();
            bundle.putSerializable("productsCategory", category);
            NavHostFragment.findNavController(SearchCategoryFragment.this)
                    .navigate(R.id.productFragment, bundle);
        });



        return binding.getRoot();
    }
}