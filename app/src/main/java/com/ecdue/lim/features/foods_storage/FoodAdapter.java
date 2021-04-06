package com.ecdue.lim.features.foods_storage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ecdue.lim.R;
import com.ecdue.lim.data.Product;
import com.ecdue.lim.databinding.FoodRowItemBinding;
import com.ecdue.lim.databinding.ProductDetailBinding;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {
    private FoodCategoryViewModel viewModel;
    private ArrayList<Product> products;

    public FoodAdapter(ArrayList<Product> products, FoodCategoryViewModel viewModel) {
        this.products = products;
        this.viewModel = viewModel;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public void setViewModel(FoodCategoryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public FoodAdapter() {
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FoodRowItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.food_row_item,
                parent,
                false
        );
        return new FoodViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        holder.binding.setProduct(products.get(position));
        holder.position = position;
        holder.binding.layoutFoodRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                ProductDetailBinding binding = DataBindingUtil.inflate(
                        LayoutInflater.from(v.getContext()),
                        R.layout.product_detail,
                        null,
                        false
                );
                binding.setProduct(products.get(holder.getAdapterPosition()));
                builder.setView(binding.getRoot());
                builder.setPositiveButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        holder.binding.imgFoodRowDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.onDeleteClicked(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class FoodViewHolder extends RecyclerView.ViewHolder {
        private final FoodRowItemBinding binding;
        private int position = 0;
        public FoodViewHolder(FoodRowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
