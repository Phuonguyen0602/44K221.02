package com.ecdue.lim.features.cosmetics_storage;

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
import com.ecdue.lim.databinding.CosmeticRowItemBinding;
import com.ecdue.lim.databinding.ProductDetailBinding;
import com.ecdue.lim.features.medicines_storage.MedicineAdapter;

import java.util.ArrayList;

public class CosmeticAdapter extends RecyclerView.Adapter<CosmeticAdapter.CosmeticViewHolder> {
    private ArrayList<Product> products;
    private CosmeticCategoryViewModel viewModel;

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setViewModel(CosmeticCategoryViewModel viewModel) {
        this.viewModel = viewModel;
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
        holder.binding.layoutCosmeticRow.setOnClickListener(new View.OnClickListener() {
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
        holder.binding.imgCosmeticRowDelete.setOnClickListener(new View.OnClickListener() {
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
    class CosmeticViewHolder extends RecyclerView.ViewHolder {
        private CosmeticRowItemBinding binding;
        public CosmeticViewHolder(CosmeticRowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
