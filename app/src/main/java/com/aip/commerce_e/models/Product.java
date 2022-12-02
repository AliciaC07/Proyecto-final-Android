package com.aip.commerce_e.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

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
    // poner descripción
    private String description;
    @ColumnInfo(name = "categoryId")
    private Integer categoryId;
    private String photosId;
    private String thumbnailUrl;
    private boolean isUsed = false;

    @ColumnInfo(defaultValue = "true")
    private Boolean active = true;

    @Override
    public String toString() {
        return name;
    }
}
