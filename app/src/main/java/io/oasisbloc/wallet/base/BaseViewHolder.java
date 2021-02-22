package io.oasisbloc.wallet.base;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    public BaseViewHolder(@NonNull ViewGroup parent, @LayoutRes int layoutId) {
        super(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
        try {
            itemView.setTag(DataBindingUtil.bind(itemView));
        } catch (Exception ignored) {
        }
    }

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        try {
            itemView.setTag(DataBindingUtil.bind(itemView));
        } catch (Exception ignored) {
        }
    }

    public <T> T getViewDataBinding() {
        try {
            //noinspection unchecked
            return (T) itemView.getTag();
        } catch (Exception ignored) {
            return null;
        }
    }
}
