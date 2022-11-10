package com.aip.commerce_e.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.aip.commerce_e.models.Product;

import java.util.List;

@Dao
public interface ProductDao {

    @Insert
    void insert(Product product);

    @Query("DELETE FROM product_table")
    void deleteAll();

    @Query("UPDATE product_table SET active = :status WHERE product_id = :id ")
    void deleteById(Boolean status, Integer id);


    @Query("SELECT * from product_table ORDER BY price ASC")
    LiveData<List<Product>> getAllProducts();

    @Query("SELECT * from product_table WHERE active = 'true' ORDER BY price ASC")
    LiveData<List<Product>> getAllActiveProducts();

}
