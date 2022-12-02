package com.aip.commerce_e.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.aip.commerce_e.models.Category;
import com.aip.commerce_e.models.Product;
import com.aip.commerce_e.models.User;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Product.class, Category.class, User.class}, version = 6, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static String NAME = "commerce";
    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getInstance(Context context){
        if(INSTANCE == null){
            synchronized (AppDatabase.class) {
                INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }

        return INSTANCE;
    }


    public abstract ProductDao productDao();
    public abstract CategoryDao categoryDao();
    public abstract UserDao userDao();
}
