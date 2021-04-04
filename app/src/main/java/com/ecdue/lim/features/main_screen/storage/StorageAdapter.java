package com.ecdue.lim.features.main_screen.storage;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ecdue.lim.R;
import com.ecdue.lim.data.Product;
import com.ecdue.lim.databinding.FoodRowItemBinding;
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
