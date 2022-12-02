package com.aip.commerce_e.drawerFragments.cart;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.aip.commerce_e.R;
import com.aip.commerce_e.models.CartProduct;
import com.aip.commerce_e.models.Product;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.CartViewHolder> {

    private final CartRCVInterface cartRCVInterface;
    List<CartProduct> cart;
    public CartListAdapter(List<CartProduct> cart, CartRCVInterface cartRCVInterface){
        this.cart = cart;
        this.cartRCVInterface = cartRCVInterface;
    }

    @NonNull
    @NotNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new CartViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_card, parent, false), cartRCVInterface);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull CartViewHolder holder, int position) {
        final long IMG_SIZE = 3840*2160;
        CartProduct cartProduct = cart.get(position);
        Product product = cartProduct.getProduct();
        holder.productId.setText("ID: "+product.getId());
        holder.productName.setText(product.getName());
        holder.productPrice.setText("$ "+product.getPrice());
        holder.productQuantity.setText(cartProduct.getQuantity().toString());
        if(product.getThumbnailUrl() != null){
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("products/"+product.getPhotosId()+"/"+product.getThumbnailUrl());
            storageReference.getBytes(IMG_SIZE).addOnSuccessListener(bytes -> {
                Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.productImg.setImageBitmap(bm);
            });
        }
    }

    @Override
    public int getItemCount() {
        if(cart != null)
            return cart.size();
        return 0;
    }

    public void setCart(List<CartProduct> cart) {
        this.cart = cart;
        notifyDataSetChanged();
    }

    public List<CartProduct> getCart(){ return cart; }
    static class CartViewHolder extends RecyclerView.ViewHolder{

        ImageView productImg;
        LinearLayoutCompat card;
        TextView productName, productQuantity, productId, productPrice;
        ImageButton deleteBtn, quantityUp, quantityDown;
        public CartViewHolder(@NonNull @NotNull View itemView, CartRCVInterface cartRCVInterface) {
            super(itemView);
            productImg = itemView.findViewById(R.id.product_cart_img);
            card = itemView.findViewById(R.id.product_cart_card);
            productName = itemView.findViewById(R.id.product_cart_name);
            productQuantity = itemView.findViewById(R.id.quantity);
            productId = itemView.findViewById(R.id.product_cart_id);
            productPrice = itemView.findViewById(R.id.product_cart_price);
            deleteBtn = itemView.findViewById(R.id.product_cart_delete);
            quantityUp = itemView.findViewById(R.id.quantity_up);
            quantityDown = itemView.findViewById(R.id.quantity_down);

            if(cartRCVInterface != null){
                card.setOnClickListener(view ->{
                    Integer pos = getBindingAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION)
                        cartRCVInterface.navigateOnClick(pos);
                });

                quantityUp.setOnClickListener(view -> {
                    Integer pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)
                        cartRCVInterface.increaseQuantity(pos);
                });

                quantityDown.setOnClickListener(view -> {
                    Integer pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION)
                        cartRCVInterface.decreaseQuantity(pos);
                });

                deleteBtn.setOnClickListener(view ->{
                    Integer pos = getBindingAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION)
                        cartRCVInterface.deleteFromCart(pos);
                });
            }
        }
    }
}
