package com.aip.commerce_e.drawerFragments.product;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.aip.commerce_e.MainActivity;
import com.aip.commerce_e.R;
import com.aip.commerce_e.RecyclerViewInterface;
import com.aip.commerce_e.models.Product;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ProductViewHolder holder, int position) {
        final long IMG_SIZE = 3024*3024;
        Product product = products.get(position);
        if(MainActivity.userLogged.getRole().equalsIgnoreCase("User")){
            holder.deleteBtn.setVisibility(View.INVISIBLE);
            holder.editBtn.setVisibility(View.INVISIBLE);
        }
        if(products != null){
            holder.productId.setText("ID: " + product.getId());
            holder.productName.setText(product.getName());
            holder.productPrice.setText("$"+product.getPrice().toString());
            if (product.getThumbnailUrl()!= null){

                StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                        .child("products/"+product.getPhotosId()+"/"+product.getThumbnailUrl());
                storageReference.getBytes(IMG_SIZE).addOnSuccessListener(bytes -> {
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    holder.productImg.setImageBitmap(bm);
                });
            }
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
        TextView productPrice, productName, productId;
        ImageButton editBtn, deleteBtn;
        LinearLayoutCompat card;

        public ProductViewHolder(@NonNull @NotNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            productId = itemView.findViewById(R.id.product_id);
            productImg = itemView.findViewById(R.id.product_img);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            editBtn = itemView.findViewById(R.id.product_edit);
            deleteBtn = itemView.findViewById(R.id.product_delete);
            card = itemView.findViewById(R.id.product_card);

            // set card on click method
            card.setOnClickListener(view -> {
                if(recyclerViewInterface != null){
                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        recyclerViewInterface.navigateOnClick(pos);
                    }
                }
            });
            //set edit and delete actions
            editBtn.setOnClickListener(view -> {
                if(recyclerViewInterface != null){
                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        recyclerViewInterface.editOnClick(pos);
                    }
                }
            });

            deleteBtn.setOnClickListener(view -> {
                if(recyclerViewInterface != null){
                    int pos = getBindingAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION)
                        recyclerViewInterface.deleteOnclick(pos);
                }
            });

        }
    }
}
