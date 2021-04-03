package com.ecdue.lim.features.medicines_storage;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.ecdue.lim.R;
import com.ecdue.lim.data.Product;
import com.ecdue.lim.databinding.MedicineRowItemBinding;

import java.util.ArrayList;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.MedicineViewHolder> {
    private ArrayList<Product> products;
    private MedicineCategoryViewModel viewModel;
    @NonNull
    @Override
    public MedicineAdapter.MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MedicineRowItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.medicine_row_item,
                parent,
                false
        );
        return new MedicineViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineAdapter.MedicineViewHolder holder, int position) {
        holder.binding.setProduct(products.get(position));

        holder.binding.imgMedicineRowDelete.setOnClickListener(new View.OnClickListener() {
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

    public void setProducts(ArrayList<Product> data) {
        this.products = data;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setViewModel(MedicineCategoryViewModel viewModel) {
        this.viewModel = viewModel;
    }

    class MedicineViewHolder extends RecyclerView.ViewHolder {
        private MedicineRowItemBinding binding;
        public MedicineViewHolder(MedicineRowItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
