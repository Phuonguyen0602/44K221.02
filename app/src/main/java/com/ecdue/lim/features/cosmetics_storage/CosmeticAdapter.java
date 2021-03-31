package com.ecdue.lim.features.cosmetics_storage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ecdue.lim.R;
import com.ecdue.lim.data.Product;
import com.ecdue.lim.databinding.CosmeticRowItemBinding;

import java.util.ArrayList;

public class CosmeticAdapter extends RecyclerView.Adapter<CosmeticAdapter.CosmeticViewHolder> {
    private ArrayList<Product> products;

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public CosmeticViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CosmeticRowItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.cosmetic_row_item,
                parent,
                false
        );
        return new CosmeticViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CosmeticViewHolder holder, int position) {
        holder.binding.setProduct(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
    class CosmeticViewHolder extends RecyclerView.ViewHolder {
        private CosmeticRowItemBinding binding;
        public CosmeticViewHolder(CosmeticRowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
