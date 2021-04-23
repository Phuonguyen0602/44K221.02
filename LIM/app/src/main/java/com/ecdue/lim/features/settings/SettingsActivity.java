package com.ecdue.lim.features.settings;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TimePicker;

import com.ecdue.lim.R;
import com.ecdue.lim.base.BaseActivity;
import com.ecdue.lim.databinding.ActivitySettingsBinding;
import com.ecdue.lim.events.BackButtonClickedEvent;
import com.ecdue.lim.events.ChangeCosmeticNotificationEvent;
import com.ecdue.lim.events.ChangeFoodNotificationEvent;
import com.ecdue.lim.events.ChangeMedicineNotificationEvent;
import com.ecdue.lim.events.ChangeNotificationTimeEvent;
import com.ecdue.lim.utils.SharedPreferenceUtil;

import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;

public class SettingsActivity extends BaseActivity {
    private SettingsViewModel viewModel;
    private ActivitySettingsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);
        viewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        viewModel.initialize();
        if (SharedPreferenceUtil.getAllowNotification(getContext())) {
            viewModel.getNotificationAllowed().setValue(true);
            binding.switchNotification.setChecked(true);
        }
        else {
            viewModel.getNotificationAllowed().setValue(false);
            binding.switchNotification.setChecked(false);
        }
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferenceUtil.setAllowNotification(getContext(), isChecked);
                if (isChecked)
                    viewModel.onNotificationAllowed();
                else
                    viewModel.onNotificationDenied();
            }
        });
    }

    @Subscribe
    public void onBackPressedEvent(BackButtonClickedEvent event){
        onBackPressed();
    }

    @Subscribe
    public void onChangeFoodNotificationEvent(ChangeFoodNotificationEvent event){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter number of days");
        View view = LayoutInflater.from(getContext()).inflate(R.layout.choose_notification_day, null, false);
        builder.setView(view);
        builder.setPositiveButton("Change", null);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ((AlertDialog)(dialog)).getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = view.findViewById(R.id.edt_choose_notification_day);
                        editText.setError(null);
                        //Log.d("SettingsActivity", editText.getText().toString());
                        if (viewModel.validateNewNumberOfDays(editText.getText().toString())){
                            viewModel.setNewFoodNotificationSetting(Integer.parseInt(editText.getText().toString()));
                            dialog.cancel();
                        }
                        else {
                            editText.setError("Wrong number format");
                        }
                    }
                });

            }
        });
        dialog.show();
    }

    @Subscribe
    public void onChangeCosmeticNotificationEvent(ChangeCosmeticNotificationEvent event){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter number of days");
        View view = LayoutInflater.from(getContext()).inflate(R.layout.choose_notification_day, null, false);
        builder.setView(view);
        builder.setPositiveButton("Change", null);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ((AlertDialog)(dialog)).getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = view.findViewById(R.id.edt_choose_notification_day);
                        editText.setError(null);
                        //Log.d("SettingsActivity", editText.getText().toString());
                        if (viewModel.validateNewNumberOfDays(editText.getText().toString())){
                            viewModel.setNewCosmeticNotificationSetting(Integer.parseInt(editText.getText().toString()));
                            dialog.cancel();
                        }
                        else {
                            editText.setError("Wrong number format");
                        }
                    }
                });

            }
        });
        dialog.show();
    }

    @Subscribe
    public void onChangeMedicineNotificationEvent(ChangeMedicineNotificationEvent event){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Enter number of days");
        View view = LayoutInflater.from(getContext()).inflate(R.layout.choose_notification_day, null, false);
        builder.setView(view);
        builder.setPositiveButton("Change", null);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ((AlertDialog)(dialog)).getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = view.findViewById(R.id.edt_choose_notification_day);
                        editText.setError(null);
                        //Log.d("SettingsActivity", editText.getText().toString());
                        if (viewModel.validateNewNumberOfDays(editText.getText().toString())){
                            viewModel.setNewMedicineNotificationSetting(Integer.parseInt(editText.getText().toString()));
                            dialog.cancel();
                        }
                        else {
                            editText.setError("Wrong number format");
                        }
                    }
                });

            }
        });
        dialog.show();
    }

    @Subscribe
    public void onChangeNotificationTimeEvent(ChangeNotificationTimeEvent event){
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        viewModel.setNewNotificationTime(hourOfDay, minute);
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        dialog.show();
    }
}