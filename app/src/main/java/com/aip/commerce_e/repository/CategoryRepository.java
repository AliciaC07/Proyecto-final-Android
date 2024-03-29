package com.aip.commerce_e.repository;

import android.app.Application;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import com.aip.commerce_e.database.AppDatabase;
import com.aip.commerce_e.database.CategoryDao;
import com.aip.commerce_e.database.ProductDao;
import com.aip.commerce_e.database.UserDao;
import com.aip.commerce_e.models.Category;
import com.aip.commerce_e.models.CategoryProduct;
import com.aip.commerce_e.models.Product;
import com.aip.commerce_e.models.User;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
    public List<CategoryProduct> getCategoryWithProducts() throws ExecutionException, InterruptedException {
        return new getAllCategoryProducts(categoryDao).execute().get();
    }
    public List<Category> getActiveAllCategories() throws ExecutionException, InterruptedException {
        return new getActiveAllCategoriesAsync(categoryDao).execute().get();
    }
    public Category findById(Integer id) throws ExecutionException, InterruptedException {
        return new findById(categoryDao).execute(id).get();
    }

    private static class findById extends AsyncTask<Integer, Void, Category>{

        private CategoryDao asyncCategoryDao;
        findById(CategoryDao asyncCategoryDao){this.asyncCategoryDao = asyncCategoryDao;}
        @Override
        protected Category doInBackground(Integer... ids) {
            return asyncCategoryDao.findById(ids[0]);
        }
    }

    private static class getAllCategoryProducts extends AsyncTask<Void, Void, List<CategoryProduct>> {
        private CategoryDao asyncCategoryDao;

        getAllCategoryProducts(CategoryDao asyncCategoryDao){ this.asyncCategoryDao = asyncCategoryDao; }
        @Override
        protected List<CategoryProduct> doInBackground(Void... emails) {
            List<CategoryProduct> categoryProducts = asyncCategoryDao.getCategoryProducts();
            return categoryProducts;
        }
    }
    private static class getActiveAllCategoriesAsync extends AsyncTask<Void, Void, List<Category>> {
        private CategoryDao asyncCategoryDao;

        getActiveAllCategoriesAsync(CategoryDao asyncCategoryDao){ this.asyncCategoryDao = asyncCategoryDao; }
        @Override
        protected List<Category> doInBackground(Void... emails) {
            List<Category> categoryProducts = asyncCategoryDao.getActiveAllCategories(true);
            return categoryProducts;
        }
    }
}
