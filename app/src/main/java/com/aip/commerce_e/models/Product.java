package com.aip.commerce_e.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Entity(tableName = "product_table")
public class Product implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "product_id")
    private Integer id;
    private String name;
    private Float price;

    private Integer categoryId;

    @ColumnInfo(defaultValue = "true")
    private Boolean active = true;

}
