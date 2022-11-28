package com.aip.commerce_e.drawerFragments.category;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.aip.commerce_e.MainActivity;
import com.aip.commerce_e.R;
import com.aip.commerce_e.RecyclerViewInterface;
import com.aip.commerce_e.models.Category;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.List;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    List<Category> categories;

    public CategoryListAdapter(List<Category> categories, RecyclerViewInterface recyclerViewInterface) {
        this.categories = categories;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @NotNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        //firebase getInstance here and reference
        return new CategoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card,parent,false), recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull CategoryViewHolder holder, int position) {
        final long IMG_SIZE = 3024*3024;
        Category category = categories.get(position);
        if (MainActivity.userLogged.getRole().equalsIgnoreCase("User")){
            holder.deleteBtn.setVisibility(View.INVISIBLE);
            holder.editBtn.setVisibility(View.INVISIBLE);
        }
        if(categories != null){
            // call category img
            holder.categoryName.setText(category.getName());
            //Log.i("productos", category.getImageUrl());
            Picasso.get().load(Uri.parse(category.getImageUrl()))
                    .into(holder.categoryImg);
            //holder.categoryImg.setImageBitmap();
        }
    }

    @Override
    public int getItemCount() {
        if(categories != null)
            return categories.size();
        return 0;
    }
    public void setCategories(List<Category> categories){
        this.categories = categories;
        notifyDataSetChanged();
    }

    public List<Category> getCategories(){
        return categories;
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImg;
        TextView categoryName;
        ImageButton editBtn, deleteBtn;
        LinearLayoutCompat card;
        public CategoryViewHolder (@NonNull View itemView, RecyclerViewInterface recyclerViewInterface){
            super(itemView);

            categoryImg = itemView.findViewById(R.id.category_img);
            categoryName = itemView.findViewById(R.id.category_name);
            editBtn = itemView.findViewById(R.id.category_edit);
            deleteBtn = itemView.findViewById(R.id.category_delete);
            card = itemView.findViewById(R.id.category_card);

            card.setOnClickListener(view -> {
                if(recyclerViewInterface != null){
                    int pos = getBindingAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION){
                        recyclerViewInterface.navigateOnClick(pos);
                    }
                }
            });
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
