package com.aip.commerce_e.repository;

import android.app.Application;
import androidx.lifecycle.LiveData;
import com.aip.commerce_e.database.AppDatabase;
import com.aip.commerce_e.database.CategoryDao;
import com.aip.commerce_e.database.ProductDao;
import com.aip.commerce_e.models.Category;
import com.aip.commerce_e.models.Product;

import java.util.List;

public class CategoryRepository {

    private CategoryDao categoryDao;
    private LiveData<List<Category>> mAllCategory;
    private LiveData<List<Category>> activeAllCategory;

    public CategoryRepository(Application application) {
        AppDatabase db = AppDatabase.getInstance(application);
        categoryDao = db.categoryDao();
        mAllCategory = categoryDao.getAllCategory();
        //activeAllCategory = categoryDao.getAllActiveCategory();
    }

    public void insert(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                categoryDao.insert(category));
    }
    public void update(Category category) {
        AppDatabase.databaseWriteExecutor.execute(() ->
                categoryDao.update(category));
    }

    public void deleteAll() {
        AppDatabase.databaseWriteExecutor.execute(() ->
                categoryDao.deleteAll());
    }

    public LiveData<List<Category>> findAll() {
        return mAllCategory;
    }
    public LiveData<List<Category>> findAllActive(Boolean status) {
        return categoryDao.getAllActiveCategory(status);
    }

    public void deleteById(Integer id){
        AppDatabase.databaseWriteExecutor.execute(() ->
                categoryDao.deleteById(false, id));
    }
}
