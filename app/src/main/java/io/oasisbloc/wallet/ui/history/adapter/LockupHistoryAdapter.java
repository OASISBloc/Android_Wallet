package io.oasisbloc.wallet.ui.history.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseViewHolder;
import io.oasisbloc.wallet.data.Lockup;
import io.oasisbloc.wallet.databinding.RowLockupBinding;

public class LockupHistoryAdapter extends ListAdapter<Lockup, BaseViewHolder> {

    private Callback mCallback;

    public LockupHistoryAdapter() {
        super(new DiffUtil.ItemCallback<Lockup>() {
            @Override
            public boolean areItemsTheSame(@NonNull Lockup oldItem,
                                           @NonNull Lockup newItem) {
                return oldItem.areItemsTheSame(newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull Lockup oldItem,
                                              @NonNull Lockup newItem) {
                return oldItem.areContentsTheSame(newItem);
            }
        });
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BaseViewHolder(parent, R.layout.row_lockup);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        RowLockupBinding binding = holder.getViewDataBinding();
        Lockup item = getItem(position);

        binding.root.setOnClickListener(v -> mCallback.onItemClicked(item));
        binding.account.setText(item.getAccount());
        binding.datetime.setText(item.getDatetime());
        binding.amount.setText(item.getBalance(item.getSymbol()));
        binding.type.setText(item.getType());
    }

    public interface Callback {

        void onItemClicked(Lockup item);

    }

}
