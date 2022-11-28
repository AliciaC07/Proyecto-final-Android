package com.aip.commerce_e.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.aip.commerce_e.database.AppDatabase;
import com.aip.commerce_e.database.ProductDao;
import com.aip.commerce_e.models.Product;

import java.util.List;

public class ProductRepository {

    private ProductDao productDao;
    private LiveData<List<Product>> mAllProduct;

    public ProductRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        productDao = db.productDao();
        mAllProduct = productDao.getAllProducts();
    }

    public void insert(Product product) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                productDao.insert(product));
    }
    public void update(Product product) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                productDao.update(product));
    }

    public void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(() ->
                productDao.deleteAll());
    }

    public LiveData<List<Product>> findAll() {
        return mAllProduct;
    }

    public LiveData<List<Product>> findAllActive(boolean status){
        return productDao.getAllActiveProducts(status);
    }
    public void deleteById(Integer id){
        AppDatabase.databaseWriteExecutor.execute(() ->
                productDao.deleteById(false, id));
    }

}
