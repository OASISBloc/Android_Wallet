package io.oasisbloc.wallet.ui.history.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import io.oasisbloc.wallet.App;
import io.oasisbloc.wallet.R;
import io.oasisbloc.wallet.base.BaseViewHolder;
import io.oasisbloc.wallet.data.Transaction;
import io.oasisbloc.wallet.databinding.RowTransactionBinding;

public class TransactionHistoryAdapter extends ListAdapter<Transaction, BaseViewHolder> {

    private static int COLOR_DEFAULT = App.getInstance().getColor(R.color.text_default);
    private static int COLOR_SEND = App.getInstance().getColor(R.color.text_send);
    private static int COLOR_RECEIVE = App.getInstance().getColor(R.color.text_receive);
    private static int COLOR_LOCK = App.getInstance().getColor(R.color.gray_middle);

    private Callback mCallback;

    public TransactionHistoryAdapter() {
        super(new DiffUtil.ItemCallback<Transaction>() {
            @Override
            public boolean areItemsTheSame(@NonNull Transaction oldItem,
                                           @NonNull Transaction newItem) {
                return oldItem.areItemsTheSame(newItem);
            }

            @Override
            public boolean areContentsTheSame(@NonNull Transaction oldItem,
                                              @NonNull Transaction newItem) {
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
        return new BaseViewHolder(parent, R.layout.row_transaction);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        RowTransactionBinding binding = holder.getViewDataBinding();
        Transaction item = getItem(position);

        binding.root.setOnClickListener(v -> mCallback.onItemClicked(item));
        binding.account.setText(item.getAccount());
        binding.amount.setText(item.getBalance(item.getSymbol()));
        binding.datetime.setText(item.getDatetime());
        binding.type.setText(item.getType());

        if (item.isSend()) {
            binding.icon.setImageResource(R.drawable.ic_transaction_send);
            binding.account.setTextColor(COLOR_DEFAULT);
            binding.amount.setTextColor(COLOR_SEND);
            binding.datetime.setTextColor(COLOR_DEFAULT);
            binding.type.setTextColor(COLOR_SEND);
        }
        if (item.isReceive()) {
            binding.icon.setImageResource(R.drawable.ic_transaction_receive);
            binding.account.setTextColor(COLOR_DEFAULT);
            binding.amount.setTextColor(COLOR_RECEIVE);
            binding.datetime.setTextColor(COLOR_DEFAULT);
            binding.type.setTextColor(COLOR_RECEIVE);
        }
        if (item.isLockup()) {
            binding.icon.setImageResource(R.drawable.ic_history_lockup);
            binding.account.setTextColor(COLOR_LOCK);
            binding.amount.setTextColor(COLOR_LOCK);
            binding.datetime.setTextColor(COLOR_LOCK);
            binding.type.setTextColor(COLOR_LOCK);
        }
    }


    public interface Callback {

        void onItemClicked(Transaction item);

    }
}
