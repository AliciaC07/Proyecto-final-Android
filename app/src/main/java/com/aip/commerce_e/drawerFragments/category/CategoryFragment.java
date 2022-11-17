package com.aip.commerce_e.drawerFragments.category;

import android.annotation.SuppressLint;
import android.widget.ImageView;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.aip.commerce_e.R;
import com.aip.commerce_e.RecyclerViewInterface;
import com.aip.commerce_e.databinding.FragmentCategoryBinding;
import com.aip.commerce_e.models.CategoryViewModel;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class CategoryFragment extends Fragment implements RecyclerViewInterface {

    private CategoryViewModel mViewModel;
    private FragmentCategoryBinding binding;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCategoryBinding.inflate(inflater,container,false);

        return binding.getRoot();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            /*NavigationView navigationView = getActivity().findViewById(R.id.drawer_nav);
            View headerView = navigationView.getHeaderView(0);
            ImageView userImg = headerView.requireViewById(R.id.userImg);
            userImg.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_shopping_basket_24));
        }*/
        // TODO: Use the ViewModel
    }

    @Override
    public void editOnClick(int pos) {

    }
}