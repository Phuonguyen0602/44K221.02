package com.ecdue.lim.features.add_item;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.ecdue.lim.R;
import com.ecdue.lim.databinding.FragmentAddItemBinding;

import java.text.ParseException;
import java.util.Calendar;

public class AddItemFragment extends DialogFragment {
    //reference: https://github.com/yatatsu/android-data-binding/blob/master/app/src/main/java/com/github/yatatsu/android/trydatabinding/view/EditItemFragment.java
    private FragmentAddItemBinding binding;
    private AddItemFragmentViewModel viewModel;
    private int defaultCategoryIndex = 0;
    private final Calendar cldr = Calendar.getInstance();
    public static AddItemFragment newInstance() {
        AddItemFragment fragment = new AddItemFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onStart() {
        super.onStart();
    }
    @Override
    public void onStop(){
        super.onStop();
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_add_item, null, false);
        binding = FragmentAddItemBinding.bind(view);
        viewModel = new ViewModelProvider(getActivity()).get(AddItemFragmentViewModel.class);
        binding.setViewModel(viewModel);
        binding.setImageSource(viewModel.getProductImage().getValue());
        binding.setLifecycleOwner(this);

        binding.edtAddExp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String userInputDate = s.toString();
                // Only update the remain days when input date is correct
                if (viewModel.dateValidation(userInputDate)){
                    int cDay = cldr.get(Calendar.DAY_OF_MONTH);
                    int cMonth = cldr.get(Calendar.MONTH);
                    int cYear = cldr.get(Calendar.YEAR);
                    viewModel.calculateDaysLeft(cDay, cMonth, cYear, userInputDate);
                }
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog dialog = builder.create();
        // Set custom button listener so that the dialog can only be closed under certain condition
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ((AlertDialog)(dialog)).getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("AddItemFragment", "This won't close the dialog");
                        if (submitProductInfo())
                            dialog.cancel();
                    }
                });
                binding.spnAddCategory.setSelection(defaultCategoryIndex);
            }
        });

        return dialog;

    }

    public void showDatePickerDialog(){
        int cDay = cldr.get(Calendar.DAY_OF_MONTH);
        int cMonth = cldr.get(Calendar.MONTH);
        int cYear = cldr.get(Calendar.YEAR);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Log.d("Date", "" + dayOfMonth + "-" + month + "-" + year);
                // month index starts at 0 so it must be plus 1 before showing to user
                viewModel.getExpirationDate().postValue("" + dayOfMonth + "/" + (month+1) + "/" + year);
                try {
                    viewModel.calculateDaysLeft(cDay, cMonth, cYear, dayOfMonth, month, year);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }, cYear, cMonth, cDay);
        dialog.show();
    }
    private boolean submitProductInfo(){
        String productName = binding.edtAddName.getText().toString();
        String quantity = binding.edtAddQuantity.getText().toString();
        String unit = "";
        if (binding.rdoAddUnitChoice.getCheckedRadioButtonId() == binding.rdoAddChooseUnit.getId())
            unit = binding.spnAddUnit.getSelectedItem().toString();
        else if (binding.rdoAddUnitChoice.getCheckedRadioButtonId() == binding.rdoAddNewUnit.getId())
            unit = binding.edtAddUnit.getText().toString();
        String category = binding.spnAddCategory.getSelectedItem().toString();
        String expDate = binding.edtAddExp.getText().toString();
        String barcode = binding.edtAddBarcode.getText().toString();
        // Image goes here
        binding.edtAddName.setError(null);
        binding.edtAddQuantity.setError(null);
        binding.edtAddExp.setError(null);
        // Validate user inputs before submitting them
        if (!viewModel.productNameValidation(productName))
            binding.edtAddName.setError("This field cannot be empty");
        else if (!viewModel.quantityValidation(quantity))
            binding.edtAddQuantity.setError("Quantity must be a number");
        else if (!viewModel.dateValidation(expDate))
            binding.edtAddExp.setError("Date format must be dd/M/yyyy");
        else {
            viewModel.addNewItem(productName,
                    quantity,
                    unit,
                    category,
                    expDate,
                    barcode);
            return true;
        }
        return false;
    }
    public void setDefaultCategory(int position){
        defaultCategoryIndex = position;
    }
}
