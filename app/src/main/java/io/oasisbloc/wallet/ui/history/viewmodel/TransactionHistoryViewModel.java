package io.oasisbloc.wallet.ui.history.viewmodel;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.oasisbloc.wallet.base.BaseViewModel;
import io.oasisbloc.wallet.base.livedata.SingleLiveData;
import io.oasisbloc.wallet.data.TransactionHistory;
import io.oasisbloc.wallet.model.WalletModel;

public class TransactionHistoryViewModel extends BaseViewModel {

    private SingleLiveData<TransactionHistory> historyResult = new SingleLiveData<>();
    private String mSymbol;

    public static TransactionHistoryViewModel get(FragmentActivity activity) {
        TransactionHistoryViewModel vm = new ViewModelProvider(activity, new ViewModelProvider.Factory() {
            @SuppressWarnings("unchecked")
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new TransactionHistoryViewModel();
            }
        }).get(TransactionHistoryViewModel.class);
        activity.getLifecycle().addObserver(vm);
        return vm;
    }

    private TransactionHistoryViewModel() {
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchHistory();
    }


    /*
        getter
     */

    public SingleLiveData<TransactionHistory> getHistoryResult() {
        return historyResult;
    }

    public void setSymbol(String symbol){
        mSymbol = symbol;
    }


    /*
        action
     */

    private void fetchHistory() {
        historyResult.setLoading(true);
        disposable = WalletModel.getTransactionHistory(mSymbol).subscribe(
                result -> {
                    historyResult.setResult(result, null);
                    historyResult.setLoading(false);
                },
                throwable -> {
                    historyResult.setResult(null, throwable);
                    historyResult.setLoading(false);
                }
        );
    }

    public void refresh() {
        fetchHistory();
    }
}
