package com.aip.commerce_e.drawerFragments.product;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.aip.commerce_e.MainActivity;
import com.aip.commerce_e.R;
import com.aip.commerce_e.RecyclerViewInterface;
import com.aip.commerce_e.models.Product;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>{
    private final RecyclerViewInterface recyclerViewInterface;
    List<Product> products;
    public ProductListAdapter(List<Product> products,RecyclerViewInterface recyclerViewInterface) {
        this.products = products;
        this.recyclerViewInterface = recyclerViewInterface;
    }


    @NonNull
    @NotNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card,parent,false), recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductViewHolder holder, int position) {
        final long IMG_SIZE = 3024*3024;
        Product product = products.get(position);
        if(MainActivity.userLogged.getRole().equalsIgnoreCase("User")){
            holder.deleteBtn.setVisibility(View.INVISIBLE);
            holder.editBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if(products != null)
            return products.size();
        return 0;
    }

    public void setProducts(List<Product> products){
        this.products = products;
        notifyDataSetChanged();
    }
    static class ProductViewHolder extends RecyclerView.ViewHolder{
        ImageView productImg;
        TextView productPrice, productName;
        ImageButton editBtn, deleteBtn;

        public ProductViewHolder(@NonNull @NotNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            productImg = itemView.findViewById(R.id.product_img);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            editBtn = itemView.findViewById(R.id.product_edit);
            deleteBtn = itemView.findViewById(R.id.product_delete);

            //set edit and delete actions

        }
    }
}
