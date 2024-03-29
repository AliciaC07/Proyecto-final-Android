package com.aip.commerce_e.models;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CategoryProduct {
    @Embedded
    public Category category;
    @Relation(
            parentColumn = "category_id",
            entityColumn = "categoryId"
    )
    public List<Product> products;
}
