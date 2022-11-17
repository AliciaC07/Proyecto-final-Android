package com.aip.commerce_e.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Dao;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity(tableName = "category_table")
public class Category implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "category_id")
    private Integer id;
    private String name;
    private String uIdFirebase;
    private String imageUrl;
    @ColumnInfo(defaultValue = "true")
    private Boolean active = true;

}
