package com.aip.commerce_e;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import com.aip.commerce_e.models.CartProduct;
import com.aip.commerce_e.models.Product;
import com.aip.commerce_e.models.User;
import com.aip.commerce_e.models.UserViewModel;
import com.google.android.material.navigation.NavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.aip.commerce_e.databinding.ActivityMainBinding;

import android.view.MenuItem;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import lombok.val;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static androidx.navigation.fragment.FragmentKt.findNavController;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    public ActionBarDrawerToggle drawerToggle;
    private UserViewModel userViewModel;
    private  String email = "12345@gmail.com";
    public static User userLogged;
    public static List<CartProduct> cart;
    private FirebaseAuth mAuth;

    @SuppressLint({"ResourceType", "NonConstantResourceId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        cart = new ArrayList<>();
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        mAuth = FirebaseAuth.getInstance();
        email = mAuth.getCurrentUser().getEmail();
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        appBarConfiguration = new AppBarConfiguration.Builder(R.id.homeFragment3,R.id.categoryFragment,R.id.productFragment)
                .setOpenableLayout(drawer).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
//        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        try {
            userLogged = userViewModel.findUserByEmail(email);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        binding.navView.setNavigationItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.nav_home:
                    navController.navigate(R.id.homeFragment3);
                    break;
                case R.id.nav_category:
                    navController.navigate(R.id.categoryFragment);
                    break;
                case R.id.nav_product:
                    navController.navigate(R.id.productFragment);
                    break;
                case R.id.nav_cart:
                    navController.navigate(R.id.cartFragment);
                    break;
                case R.id.nav_logout:
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    break;
            }
            if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
                binding.drawerLayout.closeDrawer(GravityCompat.START);
            }
            return false;
        });


        View headerView = navigationView.getHeaderView(0);
        ImageView imageView = headerView.findViewById(R.id.userImg);
        TextView username = headerView.findViewById(R.id.username);
        TextView emailUser = headerView.findViewById(R.id.emailview);
        Picasso.get().load(userLogged.getImageUrl())
                .into(imageView);
        username.setText(userLogged.getName()+" "+userLogged.getLastName());
        emailUser.setText(userLogged.getEmail());
    }

    /////Cambiar a 3 digitos despues del punto el total Ruben
    public static Double subTotal(){
        Double sum = 0d;
        DecimalFormat df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        for (CartProduct product : cart) {
            sum += product.getProduct().getPrice()* product.getQuantity();
        }
        return Double.parseDouble(df.format(sum).toString());
    }
    public static Integer cartSize(){
        Integer sum = 0;
        for (CartProduct product : cart) {
            sum += product.getQuantity();
        }
        return sum;
    }

    public static Integer hasProduct(Product product){

        for(int i= 0; i < cart.size(); i ++){
            if(cart.get(i).getProduct().getId().equals(product.getId()))
                return i;
        }
        return -1;
    }

//    @Override
//    protected void onPostCreate(Bundle savedInstanceState) {
//        super.onPostCreate(savedInstanceState);
//        // Sync the toggle state after onRestoreInstanceState has occurred.
//        drawerToggle.syncState();
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        drawerToggle.onConfigurationChanged(newConfig);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        if (FirebaseAuth.getInstance().getCurrentUser() != null){
//            getMenuInflater().inflate(R.menu.menu_main, menu);
//        }
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            case R.id.search:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Select option for the search");
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
                alertDialogBuilder.setPositiveButton("Product",
                        (arg0, arg1) -> {
                            navController.navigate(R.id.searchProductFragment);

                        }
                );

                alertDialogBuilder.setNegativeButton("Category", (dialog, which) -> {
                            navController.navigate(R.id.searchCategoryFragment);

                        }
                        );

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case R.id.notifications:
                Toast.makeText(binding.getRoot().getContext(), "Notifications touched", Toast.LENGTH_SHORT).show();
                break;
            case R.id.cart:
                navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
                navController.navigate(R.id.cartFragment);
                break;

        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.END);
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}