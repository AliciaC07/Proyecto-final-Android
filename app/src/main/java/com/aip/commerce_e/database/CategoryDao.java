package com.aip.commerce_e.database;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.aip.commerce_e.models.Category;
import com.aip.commerce_e.models.CategoryProduct;
import com.aip.commerce_e.models.Product;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert
    void insert(Category category);

    @Update
    void update(Category category);

    @Query("DELETE FROM category_table")
    void deleteAll();

    @Query("UPDATE category_table SET active = :status WHERE category_id = :id ")
    void deleteById(Boolean status, Integer id);


    @Query("SELECT * from category_table ORDER BY name ASC")
    LiveData<List<Category>> getAllCategory();

    @Query("SELECT * from category_table WHERE active = :status ORDER BY name ASC")
    LiveData<List<Category>> getAllActiveCategory(Boolean status);

    @Transaction
    @Query("SELECT * FROM category_table")
    public List<CategoryProduct> getCategoryProducts();

}
