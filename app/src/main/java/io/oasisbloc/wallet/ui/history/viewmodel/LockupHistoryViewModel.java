package io.oasisbloc.wallet.ui.history.viewmodel;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import io.oasisbloc.wallet.base.BaseViewModel;
import io.oasisbloc.wallet.base.livedata.SingleLiveData;
import io.oasisbloc.wallet.data.LockupHistory;
import io.oasisbloc.wallet.model.WalletModel;

public class LockupHistoryViewModel extends BaseViewModel {

    private SingleLiveData<LockupHistory> historyResult = new SingleLiveData<>();

    public static LockupHistoryViewModel get(FragmentActivity activity) {
        LockupHistoryViewModel vm = new ViewModelProvider(activity, new ViewModelProvider.Factory() {
            @SuppressWarnings("unchecked")
            @NonNull
            @Override
            public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
                return (T) new LockupHistoryViewModel();
            }
        }).get(LockupHistoryViewModel.class);
        activity.getLifecycle().addObserver(vm);
        return vm;
    }

    private LockupHistoryViewModel() {
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchHistory();
    }


    /*
        getter
     */

    public SingleLiveData<LockupHistory> getHistoryResult() {
        return historyResult;
    }


    /*
        action
     */

    private void fetchHistory() {
        historyResult.setLoading(true);
        disposable = WalletModel.getLockupHistory().subscribe(
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
