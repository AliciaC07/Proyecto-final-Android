package com.aip.commerce_e.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "category_table")
public class Category {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "category_id")
    private Integer id;
    private String name;
    @ColumnInfo(defaultValue = "true")
    private Boolean active = true;

}
