package com.ecdue.lim.features.main_screen.storage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ecdue.lim.R;
import com.ecdue.lim.data.Product;
import com.ecdue.lim.databinding.FoodRowItemBinding;
import com.ecdue.lim.databinding.ProductDetailBinding;
import com.ecdue.lim.databinding.ProductRowItemBinding;
import com.ecdue.lim.features.foods_storage.FoodCategoryViewModel;

import java.util.ArrayList;

public class StorageAdapter extends RecyclerView.Adapter<StorageAdapter.StorageViewHolder> {
    private StorageViewModel viewModel;
    private ArrayList<Product> products;

    public StorageAdapter(){

    }

    @NonNull
    @Override
    public StorageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProductRowItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.product_row_item,
                parent,
                false
        );
        return new StorageViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StorageViewHolder holder, int position) {
        holder.binding.setProduct(products.get(position));
        holder.binding.layoutProductRow.setOnClickListener(new View.OnClickListener() {
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
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public void setViewModel(StorageViewModel viewModel) {
        this.viewModel = viewModel;
    }

    class StorageViewHolder extends RecyclerView.ViewHolder {
        private final ProductRowItemBinding binding;
        public StorageViewHolder(ProductRowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
