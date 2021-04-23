package com.ecdue.lim.events;

import android.view.View;

import com.ecdue.lim.features.main_screen.storage.StorageViewModel;

public class PopupMenuClickedEvent {
    private StorageViewModel viewModel;
    private View clickedView;

    public PopupMenuClickedEvent(View clickedView, StorageViewModel viewModel) {
        this.viewModel = viewModel;
        this.clickedView = clickedView;
    }

    public View getClickedView() {
        return clickedView;
    }

    public void setClickedView(View clickedView) {
        this.clickedView = clickedView;
    }

    public StorageViewModel getViewModel() {
        return viewModel;
    }

    public void setViewModel(StorageViewModel viewModel) {
        this.viewModel = viewModel;
    }
}
