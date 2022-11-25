package com.aip.commerce_e.models;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.aip.commerce_e.repository.ProductRepository;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProductViewModel extends AndroidViewModel {
    private ProductRepository productRepository;

    private LiveData<List<Product>> productAll;

    public ProductViewModel(@NonNull @NotNull Application application) {
        super(application);

        productRepository = new ProductRepository(application);

        productAll = productRepository.findAll();
    }

    public void insert(Product product) {
        productRepository.insert(product);
    }
    public void update(Product product){
        productRepository.update(product);
    }

    public void deleteAll() {
        productRepository.deleteAll();
    }

    public LiveData<List<Product>> findAll() {
        return productAll;
    }
    public LiveData<List<Product>> findAllActive(boolean status) {return productRepository.findAllActive(status);}

    public void deleteById(Integer id){
        productRepository.deleteById(id);
    }
}
