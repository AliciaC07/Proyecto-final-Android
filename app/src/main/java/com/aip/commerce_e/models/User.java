package com.aip.commerce_e.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(tableName = "user_table")
public class User {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "user_id")
    private Integer id;
    private String email;
    private String name;
    private String lastName;
//    private String password;
    private String uIdFirebase;
    private String role;
    private String imageUrl;
    private Boolean active = true;
}
