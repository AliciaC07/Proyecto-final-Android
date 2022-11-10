package com.aip.commerce_e.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "product_table")
public class Product  {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "product_id")
    private Integer id;
    private String name;
    private Float price;
    private Category category;

    @ColumnInfo(defaultValue = "true")
    private Boolean active = true;

}
