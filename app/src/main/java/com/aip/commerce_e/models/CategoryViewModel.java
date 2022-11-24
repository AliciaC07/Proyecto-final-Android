package com.aip.commerce_e.models;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.aip.commerce_e.repository.CategoryRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class CategoryViewModel extends AndroidViewModel {
    private CategoryRepository categoryRepository;

    private LiveData<List<Category>> allCategory;
    public CategoryViewModel(@NonNull @NotNull Application application) {
        super(application);

        categoryRepository = new CategoryRepository(application);

        allCategory = categoryRepository.findAll();
    }
    public void insert(Category category) {
        categoryRepository.insert(category);
    }
    public void update(Category category){
        categoryRepository.update(category);
    }

    public void deleteAll() {
        categoryRepository.deleteAll();
    }

    public LiveData<List<Category>> findAll() {
        return allCategory;
    }
    public LiveData<List<Category>> findAllActive(Boolean status){ return categoryRepository.findAllActive(status); }

    public void deleteById(Integer id){
        categoryRepository.deleteById(id);
    }
    public List<CategoryProduct> getAllProductsWithCategory() throws ExecutionException, InterruptedException {
        return categoryRepository.getCategoryWithProducts();
    }
    public Category findById(Integer id) { return categoryRepository.findById(id);}
}
